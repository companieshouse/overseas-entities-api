package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostsServiceTest {
    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @BeforeEach
    void init() {
        // ReflectionTestUtils.setField(costService, "costRegisterAmount", "13.00");
    }

    @Test
    void getRegistrationCosts() throws ServiceException {

        when(overseasEntitiesService.getSubmissionType(any())).thenReturn(SubmissionType.Registration);

        final CostsService costService = new CostsService(overseasEntitiesService);

        costService.setRegistrationCostAmount("13.00");
        var result = costService.getCosts("testId");

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
    void getUpdateCosts() throws ServiceException {

        when(overseasEntitiesService.getSubmissionType(any())).thenReturn(SubmissionType.Update);

        final CostsService costService = new CostsService(overseasEntitiesService);

        costService.setUpdateCostAmount("13.00");
        var result = costService.getCosts("testId");

        assertEquals("13.00", result.getAmount());
        assertEquals(Collections.singletonList("credit-card"), result.getAvailablePaymentMethods());
        assertEquals(Collections.singletonList("data-maintenance"), result.getClassOfPayment());
        assertEquals("Update Overseas Entity fee", result.getDescription());
        assertEquals("description-identifier", result.getDescriptionIdentifier());
        assertEquals("payment-session#payment-session", result.getKind());
        assertEquals("overseas-entity", result.getResourceKind());
        assertEquals("update-overseas-entity", result.getProductType());
    }
}
