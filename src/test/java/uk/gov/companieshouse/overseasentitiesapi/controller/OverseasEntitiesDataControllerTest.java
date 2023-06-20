package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesDataControllerTest {

    private static final String ERIC_REQUEST_ID = "XaBcDeF12345";
    private static final String companyNumber = "OE000201";
    private static String email = "TEST@MAILINATOR.COM";

    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Test
    void testGetOverseasEntityDetailsReturnsSuccessfully () throws ServiceException {
        OverseasEntityDataApi overseasEntityDataApi = new OverseasEntityDataApi();
        overseasEntityDataApi.setEmail(email);
        when(privateDataRetrievalService.getOverseasEntityData(companyNumber)).thenReturn(overseasEntityDataApi);

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService);
        var response = overseasEntitiesDataController.getOverseasEntityDetails(companyNumber, ERIC_REQUEST_ID);

        verify(privateDataRetrievalService, times(1)).getOverseasEntityData(companyNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(overseasEntityDataApi, response.getBody());
    }

    @Test
    void testGetOverseasEntityDetailsReturnsInternalServerErrorWhenExceptionThrown () throws ServiceException {
        Mockito.doThrow(new ServiceException("Exception thrown")).when(privateDataRetrievalService).getOverseasEntityData(companyNumber);

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService);
        var response = overseasEntitiesDataController.getOverseasEntityDetails(companyNumber, ERIC_REQUEST_ID);

        assertNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
