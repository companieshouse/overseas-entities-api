package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.trustees.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.trustees.individualtrustee.PrivateIndividualTrusteeListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksListApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.lenient;

/**
 * Security tests verifying that the trusts private data endpoints in {@link TrustsDataController}
 * reject requests where the {@code overseas_entity_id} is not linked to the {@code transaction_id}.
 * These tests assert the EXPECTED secure behaviour: when a submission does not belong to the
 * transaction in the request path, the controller must return 400 BAD_REQUEST before any PII
 * is retrieved.
 * A passing run confirms the IDOR vulnerability has been remediated.
 */
@ExtendWith(MockitoExtension.class)
class TrustsDataControllerTransactionLinkingTest {

    private static final String ERIC_REQUEST_ID = "req-99999";
    private static final String ATTACKER_TRANSACTION_ID = "attacker-txn-888";
    private static final String VICTIM_OVERSEAS_ENTITY_ID = "victim-submission-xyz";
    private static final String TRUST_ID = "trust-id-001";

    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @InjectMocks
    private TrustsDataController trustsDataController;

    private Transaction attackerTransaction;

    @BeforeEach
    void setUp() throws SubmissionNotLinkedToTransactionException {
        ReflectionTestUtils.setField(trustsDataController, "salt", "mockedSalt");

        attackerTransaction = new Transaction();
        attackerTransaction.setId(ATTACKER_TRANSACTION_ID);

        // Service throws when submission is not linked to the attacker's transaction
        lenient().when(overseasEntitiesService.getSavedOverseasEntity(
                        attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                .thenThrow(new SubmissionNotLinkedToTransactionException(
                        "Transaction id: " + ATTACKER_TRANSACTION_ID +
                                " does not have a resource that matches Overseas Entity submission id: " +
                                VICTIM_OVERSEAS_ENTITY_ID));
    }

    @Nested
    @DisplayName("GET /trusts/details - must reject when submission not linked to transaction")
    class TrustDetailsTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getTrustDetails_rejectsMismatchedTransactionAndSubmission() throws ServiceException {
            ResponseEntity<PrivateTrustDetailsListApi> response = trustsDataController.getTrustDetails(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            assertEquals(400, response.getStatusCode().value(),
                    "Trust details must not be served when overseas_entity_id is not linked to transaction_id");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim trust details for a mismatched transaction")
        void getTrustDetails_mustNotReturnOkForMismatchedTransaction() throws ServiceException {
            ResponseEntity<PrivateTrustDetailsListApi> response = trustsDataController.getTrustDetails(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's trust details returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /trusts/beneficial-owners/links - must reject when submission not linked to transaction")
    class TrustLinksTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getTrustLinks_rejectsMismatchedTransactionAndSubmission() throws ServiceException {
            ResponseEntity<PrivateTrustLinksListApi> response = trustsDataController.getTrustLinks(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            assertEquals(400, response.getStatusCode().value(),
                    "Trust links must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim trust links for a mismatched transaction")
        void getTrustLinks_mustNotReturnOkForMismatchedTransaction() throws ServiceException {
            ResponseEntity<PrivateTrustLinksListApi> response = trustsDataController.getTrustLinks(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's trust links returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /trusts/{trust_id}/individual-trustees - must reject when submission not linked to transaction")
    class IndividualTrusteesTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getIndividualTrustees_rejectsMismatchedTransactionAndSubmission() throws ServiceException {
            ResponseEntity<PrivateIndividualTrusteeListApi> response = trustsDataController.getIndividualTrustees(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID);

            assertEquals(400, response.getStatusCode().value(),
                    "Individual trustee PII must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim individual trustees for a mismatched transaction")
        void getIndividualTrustees_mustNotReturnOkForMismatchedTransaction() throws ServiceException {
            ResponseEntity<PrivateIndividualTrusteeListApi> response = trustsDataController.getIndividualTrustees(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID);

            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's individual trustee PII returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /trusts/{trust_id}/corporate-trustees - must reject when submission not linked to transaction")
    class CorporateTrusteesTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getCorporateTrustees_rejectsMismatchedTransactionAndSubmission() throws ServiceException {
            ResponseEntity<PrivateCorporateTrusteeListApi> response = trustsDataController.getCorporateTrustees(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID);

            assertEquals(400, response.getStatusCode().value(),
                    "Corporate trustee PII must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim corporate trustees for a mismatched transaction")
        void getCorporateTrustees_mustNotReturnOkForMismatchedTransaction() throws ServiceException {
            ResponseEntity<PrivateCorporateTrusteeListApi> response = trustsDataController.getCorporateTrustees(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID);

            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's corporate trustee PII returned to unrelated transaction caller");
        }
    }
}
