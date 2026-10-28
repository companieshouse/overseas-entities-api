package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostsServiceTest {

    private final static String TEST_REQUEST_ID = "testRequestId";
    private final static String TEST_OVERSEAS_ENTITY_ID = "testOverseasEntityId";

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    private CostsService costService;

    @BeforeEach
    void init() {
        costService = new CostsService(overseasEntitiesService);
        ReflectionTestUtils.setField(costService, "registerCostAmount", "13.00");
        ReflectionTestUtils.setField(costService, "updateCostAmount", "26.00");
        ReflectionTestUtils.setField(costService, "removeCostAmount", "39.00");
    }

    @Test
    void getRegistrationCosts() throws SubmissionNotFoundException {

        when(overseasEntitiesService.isSubmissionAnUpdate(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID)).thenReturn(false);

        var result = costService.getCosts(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID);

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
    void getUpdateCosts() throws SubmissionNotFoundException {

        when(overseasEntitiesService.isSubmissionAnUpdate(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID)).thenReturn(true);

        var result = costService.getCosts(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID);

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
    void getRemoveCosts() throws SubmissionNotFoundException {

        when(overseasEntitiesService.isSubmissionAnUpdate(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID)).thenReturn(false);
        when(overseasEntitiesService.isSubmissionARemove(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID)).thenReturn(true);

        var result = costService.getCosts(TEST_REQUEST_ID, TEST_OVERSEAS_ENTITY_ID);

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
