package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.SubmissionType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostsControllerTest {

    private final static String OVERSEAS_ENTITY_ID = "testId";

    private final static String REQUEST_ID = "7567675jgjhgh";

    private final Transaction transaction = new Transaction();

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    private CostsService costsService;

    @BeforeEach
    void init() {
        costsService = new CostsService(overseasEntitiesService);
        ReflectionTestUtils.setField(costsService, "registerCostAmount", "13.00");
        ReflectionTestUtils.setField(costsService, "updateCostAmount", "26.00");
    }

    @Test
    void testGetCostsReturnsRegistionCosts() throws SubmissionNotFoundException {
        when(overseasEntitiesService.getSubmissionType(any(), any())).thenReturn(SubmissionType.REGISTRATION);

        final CostsController costsController = new CostsController(costsService);

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);
        assertEquals("13.00", response.getBody().get(0).getAmount());
    }

    @Test
    void testGetCostsReturnsUpdateCosts() throws SubmissionNotFoundException {
        when(overseasEntitiesService.getSubmissionType(any(), any())).thenReturn(SubmissionType.UPDATE);

        final CostsController costsController = new CostsController(costsService);

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);
        assertEquals("26.00", response.getBody().get(0).getAmount());
    }
;
    @Test
    void testGetCostsSubmissionException() throws SubmissionNotFoundException {
        when(overseasEntitiesService.getSubmissionType(any(), any())).thenThrow(
                new SubmissionNotFoundException("test"));

        final CostsController costsController = new CostsController(costsService);
        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);
        assertEquals(500, response.getStatusCodeValue());
    }
}
