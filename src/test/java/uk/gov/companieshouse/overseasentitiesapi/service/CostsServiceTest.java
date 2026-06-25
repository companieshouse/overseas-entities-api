package uk.gov.companieshouse.overseasentitiesapi.service;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostsServiceTest {

    private final static String TEST_REQUEST_ID = "testRequestId";
    private final static String TEST_OVERSEAS_ENTITY_ID = "testOverseasEntityId";

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private Transaction transaction;

    private CostsService costService;

    @BeforeEach
    void init() {
        costService = new CostsService(overseasEntitiesService);
        ReflectionTestUtils.setField(costService, "registerCostAmount", "13.00");
        ReflectionTestUtils.setField(costService, "updateCostAmount", "26.00");
        ReflectionTestUtils.setField(costService, "removeCostAmount", "39.00");
    }

    @Test
    void getRegistrationCosts()
            throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {

        OverseasEntitySubmissionDto overseasEntitiesRegistrationSubmission = new OverseasEntitySubmissionDto();

        when(overseasEntitiesService.getSavedOverseasEntity(transaction, TEST_OVERSEAS_ENTITY_ID, TEST_REQUEST_ID)).thenReturn(
                Optional.of(overseasEntitiesRegistrationSubmission));

        var result = costService.getCosts(transaction,TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID);

        assertEquals("13.00", result.getAmount());
        assertEquals(Collections.singletonList("credit-card"), result.getAvailablePaymentMethods());
        assertEquals(Collections.singletonList("data-maintenance"), result.getClassOfPayment());
        assertEquals("Register Overseas Entity fee", result.getDescription());
        assertEquals("description-identifier", result.getDescriptionIdentifier());
        assertEquals("payment-session#payment-session", result.getKind());
        assertEquals("overseas-entity", result.getResourceKind());
        assertEquals("register-overseas-entity", result.getProductType());
    }

    @Test
    void getUpdateCosts()
            throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {

        OverseasEntitySubmissionDto overseasEntitiesUpdateSubmission = new OverseasEntitySubmissionDto();
        overseasEntitiesUpdateSubmission.setEntityNumber("OE123456");
        overseasEntitiesUpdateSubmission.setIsRemove(false);

        when(overseasEntitiesService.getSavedOverseasEntity(transaction, TEST_OVERSEAS_ENTITY_ID, TEST_REQUEST_ID)).thenReturn(
                Optional.of(overseasEntitiesUpdateSubmission));
        when(overseasEntitiesService.isSubmissionAnUpdate(TEST_REQUEST_ID, overseasEntitiesUpdateSubmission)).thenReturn(true);

        var result = costService.getCosts(transaction, TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID);

        assertEquals("26.00", result.getAmount());
        assertEquals(Collections.singletonList("credit-card"), result.getAvailablePaymentMethods());
        assertEquals(Collections.singletonList("data-maintenance"), result.getClassOfPayment());
        assertEquals("Update Overseas Entity fee", result.getDescription());
        assertEquals("description-identifier", result.getDescriptionIdentifier());
        assertEquals("payment-session#payment-session", result.getKind());
        assertEquals("overseas-entity", result.getResourceKind());
        assertEquals("update-overseas-entity", result.getProductType());
    }

    @Test
    void getRemoveCosts()
            throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {

        OverseasEntitySubmissionDto overseasEntitiesRemoveSubmission = new OverseasEntitySubmissionDto();
        overseasEntitiesRemoveSubmission.setEntityNumber("OE123456");
        overseasEntitiesRemoveSubmission.setIsRemove(true);

        when(overseasEntitiesService.getSavedOverseasEntity(transaction, TEST_OVERSEAS_ENTITY_ID, TEST_REQUEST_ID)).thenReturn(
                Optional.of(overseasEntitiesRemoveSubmission));
        when(overseasEntitiesService.isSubmissionAnUpdate(TEST_REQUEST_ID, overseasEntitiesRemoveSubmission)).thenReturn(false);
        when(overseasEntitiesService.isSubmissionARemove(TEST_REQUEST_ID, overseasEntitiesRemoveSubmission)).thenReturn(true);

        var result = costService.getCosts(transaction, TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID);

        assertEquals("39.00", result.getAmount());
        assertEquals(Collections.singletonList("credit-card"), result.getAvailablePaymentMethods());
        assertEquals(Collections.singletonList("data-maintenance"), result.getClassOfPayment());
        assertEquals("Remove Overseas Entity fee", result.getDescription());
        assertEquals("description-identifier", result.getDescriptionIdentifier());
        assertEquals("payment-session#payment-session", result.getKind());
        assertEquals("overseas-entity", result.getResourceKind());
        assertEquals("remove-overseas-entity", result.getProductType());
    }
}
