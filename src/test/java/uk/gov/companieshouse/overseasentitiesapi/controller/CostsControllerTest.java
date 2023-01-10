package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CostsControllerTest {

    private final static String OVERSEAS_ENTITY_ID = "testId";

    private final static String REQUEST_ID = "7567675jgjhgh";

    private final Transaction transaction = new Transaction();

    @Mock
    private CostsService costService;

    @InjectMocks
    private CostsController costsController;

    @Test
    void getCosts() {
        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);

        verify(costService, times(1)).getCosts(OVERSEAS_ENTITY_ID);
    }
}
