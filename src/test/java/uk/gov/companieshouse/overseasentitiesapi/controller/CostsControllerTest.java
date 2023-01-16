package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
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

    @Test
    void getRegistrationCosts() throws ServiceException {
        when(overseasEntitiesService.getSubmissionType(any())).thenReturn(SubmissionType.REGISTRATION);

        final CostsService costService = new CostsService(overseasEntitiesService);
        costService.setRegistrationCostAmount("13.00");
        final CostsController costsController = new CostsController(costService);

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);
        assertEquals("13.00", response.getBody().get(0).getAmount());
    }

    @Test
    void getUpdateCosts() throws ServiceException {
        when(overseasEntitiesService.getSubmissionType(any())).thenReturn(SubmissionType.UPDATE);

        final CostsService costService = new CostsService(overseasEntitiesService);
        costService.setUpdateCostAmount("13.00");
        final CostsController costsController = new CostsController(costService);

        var response = costsController.getCosts(transaction, OVERSEAS_ENTITY_ID, REQUEST_ID);
        assertEquals("13.00", response.getBody().get(0).getAmount());
    }
}
