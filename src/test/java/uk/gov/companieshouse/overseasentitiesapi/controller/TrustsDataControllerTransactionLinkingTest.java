package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

/**
 * Security tests verifying that the trusts private data endpoints in {@link TrustsDataController}
 * reject requests where the {@code overseas_entity_id} is not linked to the {@code transaction_id}.
 *
 * <p>When the submission is not linked to the transaction, {@code getCompanyNumber} throws
 * {@link SubmissionNotLinkedToTransactionException}. The controller-level
 * {@code @ExceptionHandler} maps this to 400 BAD_REQUEST at runtime.
 * These unit tests verify the exception is thrown, confirming the IDOR vulnerability
 * has been remediated.</p>
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
        @DisplayName("Should reject when overseas_entity_id is not linked to the transaction_id (mapped to 400 BAD_REQUEST by @ExceptionHandler)")
        void getTrustDetails_rejectsMismatchedTransactionAndSubmission() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getTrustDetails(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID),
                    "Trust details must not be served when overseas_entity_id is not linked to transaction_id");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim trust details for a mismatched transaction")
        void getTrustDetails_mustNotReturnOkForMismatchedTransaction() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getTrustDetails(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID),
                    "IDOR VULNERABILITY: victim's trust details returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /trusts/beneficial-owners/links - must reject when submission not linked to transaction")
    class TrustLinksTransactionLinking {

        @Test
        @DisplayName("Should reject when overseas_entity_id is not linked to the transaction_id (mapped to 400 BAD_REQUEST by @ExceptionHandler)")
        void getTrustLinks_rejectsMismatchedTransactionAndSubmission() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getTrustLinks(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID),
                    "Trust links must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim trust links for a mismatched transaction")
        void getTrustLinks_mustNotReturnOkForMismatchedTransaction() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getTrustLinks(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID),
                    "IDOR VULNERABILITY: victim's trust links returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /trusts/{trust_id}/individual-trustees - must reject when submission not linked to transaction")
    class IndividualTrusteesTransactionLinking {

        @Test
        @DisplayName("Should reject when overseas_entity_id is not linked to the transaction_id (mapped to 400 BAD_REQUEST by @ExceptionHandler)")
        void getIndividualTrustees_rejectsMismatchedTransactionAndSubmission() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getIndividualTrustees(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID),
                    "Individual trustee PII must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim individual trustees for a mismatched transaction")
        void getIndividualTrustees_mustNotReturnOkForMismatchedTransaction() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getIndividualTrustees(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID),
                    "IDOR VULNERABILITY: victim's individual trustee PII returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /trusts/{trust_id}/corporate-trustees - must reject when submission not linked to transaction")
    class CorporateTrusteesTransactionLinking {

        @Test
        @DisplayName("Should reject when overseas_entity_id is not linked to the transaction_id (mapped to 400 BAD_REQUEST by @ExceptionHandler)")
        void getCorporateTrustees_rejectsMismatchedTransactionAndSubmission() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getCorporateTrustees(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID),
                    "Corporate trustee PII must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim corporate trustees for a mismatched transaction")
        void getCorporateTrustees_mustNotReturnOkForMismatchedTransaction() {
            assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                    trustsDataController.getCorporateTrustees(
                            attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, TRUST_ID, ERIC_REQUEST_ID),
                    "IDOR VULNERABILITY: victim's corporate trustee PII returned to unrelated transaction caller");
        }
    }
}
