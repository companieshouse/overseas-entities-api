package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CostsServiceTest {

    @InjectMocks
    private CostsService costService;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(costService, "costAmount", "13.00");
    }

    @Test
    void getCosts() {
        var result = costService.getCosts();

        assertEquals("13.00", result.getAmount());
        assertEquals(Collections.singletonList("credit-card"), result.getAvailablePaymentMethods());
        assertEquals(Collections.singletonList("data-maintenance"), result.getClassOfPayment());
        assertEquals("Register Overseas Entity fee", result.getDescription());
        assertEquals("description-identifier", result.getDescriptionIdentifier());
        assertEquals("payment-session#payment-session", result.getKind());
        assertEquals("overseas-entity", result.getResourceKind());
        assertEquals("register-overseas-entity", result.getProductType());
    }
}
