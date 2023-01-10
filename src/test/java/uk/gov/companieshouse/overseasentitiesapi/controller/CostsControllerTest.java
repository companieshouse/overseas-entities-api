package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.SubmissionType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostsControllerTest {

    private final static String OVERSEAS_ENTITY_ID = "testId";

    private final static String REQUEST_ID = "7567675jgjhgh";

    private final Transaction transaction = new Transaction();

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private CostsService costService = new CostsService(overseasEntitiesService);

    @InjectMocks
    private CostsController costsController;

    @Test
    void getCosts() {
        when(overseasEntitiesService.getSubmissionType(any())).thenReturn(SubmissionType.Registration);

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);

        verify(costService, times(1)).getCosts(OVERSEAS_ENTITY_ID);
    }
}
