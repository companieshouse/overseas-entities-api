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
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

/**
 * Security tests verifying that the private data endpoints in {@link OverseasEntitiesDataController}
 * reject requests where the {@code overseas_entity_id} is not linked to the {@code transaction_id}.
 * These tests assert the EXPECTED secure behaviour: when a submission does not belong to the
 * transaction in the request path, the controller must return 400 BAD_REQUEST before any PII
 * is retrieved. The fix should call
 * {@code TransactionUtils.isTransactionLinkedToOverseasEntitySubmission} (or equivalent)
 * on these private endpoints, matching the pattern already used by
 * {@code OverseasEntitiesService.getSavedOverseasEntity}.
 * NOTE: These tests will FAIL until the transaction-linkage check is added to the controller.
 * A passing run confirms the IDOR vulnerability has been remediated.
 */
@ExtendWith(MockitoExtension.class)
class OverseasEntitiesDataControllerTransactionLinkingTest {

    private static final String ERIC_REQUEST_ID = "req-12345";

    // The attacker's own legitimate transaction (NOT linked to the victim's submission)
    private static final String ATTACKER_TRANSACTION_ID = "attacker-txn-999";

    // A victim's overseas entity submission ID (not linked to ATTACKER_TRANSACTION_ID)
    private static final String VICTIM_OVERSEAS_ENTITY_ID = "victim-submission-abc";

    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @InjectMocks
    private OverseasEntitiesDataController overseasEntitiesDataController;

    private Transaction attackerTransaction;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(overseasEntitiesDataController, "salt", "mockedSalt");

        attackerTransaction = new Transaction();
        attackerTransaction.setId(ATTACKER_TRANSACTION_ID);
    }

    @Nested
    @DisplayName("GET /details - must reject when submission not linked to transaction")
    class DetailsEndpointTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getDetails_rejectsMismatchedTransactionAndSubmission() throws ServiceException, SubmissionNotLinkedToTransactionException {
            // Arrange: getSavedOverseasEntity throws because the submission is not linked to this transaction
            when(overseasEntitiesService.getSavedOverseasEntity(attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                    .thenThrow(new SubmissionNotLinkedToTransactionException(
                            String.format("Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s",
                                    ATTACKER_TRANSACTION_ID, VICTIM_OVERSEAS_ENTITY_ID)));

            // Act: attacker calls with their own transaction but a victim's submission ID.
            var response = overseasEntitiesDataController.getOverseasEntityDetails(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            // Assert: request is rejected with BAD_REQUEST — no PII returned
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                    "Private data must not be served when overseas_entity_id is not linked to transaction_id");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim data for a mismatched transaction")
        void getDetails_mustNotReturnOkForMismatchedTransaction() throws ServiceException, SubmissionNotLinkedToTransactionException {
            // Arrange: getSavedOverseasEntity throws for mismatched transaction/submission
            when(overseasEntitiesService.getSavedOverseasEntity(attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                    .thenThrow(new SubmissionNotLinkedToTransactionException("Not linked"));

            // Act
            var response = overseasEntitiesDataController.getOverseasEntityDetails(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            // Assert: must NOT be 200 OK — if it is, PII is being leaked cross-tenant
            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's entity details returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /beneficial-owners - must reject when submission not linked to transaction")
    class BeneficialOwnersEndpointTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getBeneficialOwners_rejectsMismatchedTransactionAndSubmission() throws NoSuchAlgorithmException, SubmissionNotLinkedToTransactionException {
            // Arrange: getSavedOverseasEntity throws because the submission is not linked to this transaction
            when(overseasEntitiesService.getSavedOverseasEntity(attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                    .thenThrow(new SubmissionNotLinkedToTransactionException(
                            String.format("Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s",
                                    ATTACKER_TRANSACTION_ID, VICTIM_OVERSEAS_ENTITY_ID)));

            // Act: attacker calls with mismatched transaction/submission
            var response = overseasEntitiesDataController.getOverseasEntityBeneficialOwners(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            // Assert: beneficial owner PII is NOT returned
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                    "Beneficial owner PII must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim BO data for a mismatched transaction")
        void getBeneficialOwners_mustNotReturnOkForMismatchedTransaction() throws NoSuchAlgorithmException, SubmissionNotLinkedToTransactionException {
            // Arrange: getSavedOverseasEntity throws for mismatched transaction/submission
            when(overseasEntitiesService.getSavedOverseasEntity(attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                    .thenThrow(new SubmissionNotLinkedToTransactionException("Not linked"));

            // Act
            var response = overseasEntitiesDataController.getOverseasEntityBeneficialOwners(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            // Assert: must NOT be 200 OK
            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's beneficial owner PII returned to unrelated transaction caller");
        }
    }

    @Nested
    @DisplayName("GET /managing-officers - must reject when submission not linked to transaction")
    class ManagingOfficersEndpointTransactionLinking {

        @Test
        @DisplayName("Should return BAD_REQUEST when overseas_entity_id is not linked to the transaction_id")
        void getManagingOfficers_rejectsMismatchedTransactionAndSubmission() throws ServiceException, SubmissionNotLinkedToTransactionException {
            // Arrange: getSavedOverseasEntity throws because the submission is not linked to this transaction
            when(overseasEntitiesService.getSavedOverseasEntity(attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                    .thenThrow(new SubmissionNotLinkedToTransactionException(
                            String.format("Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s",
                                    ATTACKER_TRANSACTION_ID, VICTIM_OVERSEAS_ENTITY_ID)));

            // Act: attacker calls with mismatched transaction/submission
            var response = overseasEntitiesDataController.getManagingOfficers(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            // Assert: managing officer PII is NOT returned
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                    "Managing officer PII must not be served for unlinked submission");
        }

        @Test
        @DisplayName("Must not return 200 OK with victim MO data for a mismatched transaction")
        void getManagingOfficers_mustNotReturnOkForMismatchedTransaction() throws ServiceException, SubmissionNotLinkedToTransactionException {
            // Arrange: getSavedOverseasEntity throws for mismatched transaction/submission
            when(overseasEntitiesService.getSavedOverseasEntity(attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID))
                    .thenThrow(new SubmissionNotLinkedToTransactionException("Not linked"));

            // Act
            var response = overseasEntitiesDataController.getManagingOfficers(
                    attackerTransaction, VICTIM_OVERSEAS_ENTITY_ID, ERIC_REQUEST_ID);

            // Assert: must NOT be 200 OK
            assertNotEquals(HttpStatus.OK, response.getStatusCode(),
                    "IDOR VULNERABILITY: victim's managing officer PII returned to unrelated transaction caller");
        }
    }
}

