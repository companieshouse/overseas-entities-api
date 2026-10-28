package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;
import uk.gov.companieshouse.api.model.payment.Cost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostsControllerTest {

    private final static String OVERSEAS_ENTITY_ID = "testId";

    private final static String REQUEST_ID = "7567675jgjhgh";

    private final Transaction transaction = new Transaction();

    @Mock
    private CostsService costsService;

    private CostsController costsController;

    @BeforeEach
    void init() {
        costsController = new CostsController(costsService);
    }

    @Test
    void testGetCostsReturnsCosts() throws SubmissionNotFoundException {
        final String amount = "13.00";
        Cost cost = new Cost();
        cost.setAmount(amount);
        when(costsService.getCosts(REQUEST_ID, OVERSEAS_ENTITY_ID)).thenReturn(cost);

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);

        assertEquals(amount, response.getBody().getFirst().getAmount());
        verify(costsService, times(1)).getCosts(REQUEST_ID, OVERSEAS_ENTITY_ID);
    }

    @Test
    void testGetCostsSubmissionException() throws SubmissionNotFoundException {
        when(costsService.getCosts(REQUEST_ID, OVERSEAS_ENTITY_ID)).thenThrow(
                new SubmissionNotFoundException("test"));

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);
        assertEquals(500, response.getStatusCode().value());
        verify(costsService, times(1)).getCosts(REQUEST_ID, OVERSEAS_ENTITY_ID);
    }
}
