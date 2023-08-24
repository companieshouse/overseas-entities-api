package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesDataControllerTest {

    private static final String ERIC_REQUEST_ID = "XaBcDeF12345";
    private static final String overseasEntityId = "123456778";
    private static final String email = "TEST@MAILINATOR.COM";

    private static final String transactionId = "123456";

    private static final String COMPANY_NUMBER = "OE111129";

    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;


    @Test
    void testGetOverseasEntityDetailsReturnsSuccessfully() throws ServiceException {
        OverseasEntityDataApi overseasEntityDataApi = new OverseasEntityDataApi();
        overseasEntityDataApi.setEmail(email);
        when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(overseasEntityDataApi);
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transactionId, overseasEntityId, ERIC_REQUEST_ID);

        verify(privateDataRetrievalService, times(1)).getOverseasEntityData(COMPANY_NUMBER);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(overseasEntityDataApi, response.getBody());
    }

    @Test
    void testGetOverseasEntityDetailsReturnsSubmissionEmailSuccessfully() throws ServiceException {
        final var cachedEmail = "alice@test.com";
        OverseasEntityDataApi overseasEntityDataApi = new OverseasEntityDataApi();
        overseasEntityDataApi.setEmail(cachedEmail);

        OverseasEntitySubmissionDto submissionDto = createOverseasEntitySubmissionMock();
        submissionDto.getEntity().setEmail(cachedEmail);
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(submissionDto));

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transactionId, overseasEntityId, ERIC_REQUEST_ID);

        verify(privateDataRetrievalService, times(0)).getOverseasEntityData(COMPANY_NUMBER);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(overseasEntityDataApi, response.getBody());
    }

    @Test
    void testGetOverseasEntityDetailsReturnsInternalServerErrorWhenExceptionThrown() throws ServiceException {
        Mockito.doThrow(new ServiceException("Exception thrown")).when(privateDataRetrievalService).getOverseasEntityData(COMPANY_NUMBER);

        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transactionId, overseasEntityId, ERIC_REQUEST_ID);

        assertNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetOverseasEntityDetailsReturnsInternalServerErrorWhenUpdateDisabled() {
        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(
                privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, false);

        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));

        assertThrows(ServiceException.class,
                () -> overseasEntitiesDataController.getOverseasEntityDetails(
                        transactionId,
                        overseasEntityId,
                        ERIC_REQUEST_ID));
    }

    @Test
    void testGetOverseasEntityDetailsReturnsInternalServerErrorWhenRegistration() {
        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(
                privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        OverseasEntitySubmissionDto overseasEntitySubmissionDto = createOverseasEntitySubmissionMock();
        overseasEntitySubmissionDto.setEntityNumber(null);
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        assertThrows(ServiceException.class,
                () -> overseasEntitiesDataController.getOverseasEntityDetails(
                        transactionId,
                        overseasEntityId,
                        ERIC_REQUEST_ID));
    }

    @Test
    void testGetOverseasEntityDetailsReturnsNotFoundWhenNoSubmissionFound() throws ServiceException {
        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(
                privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.empty());

        var response = overseasEntitiesDataController.getOverseasEntityDetails(
                transactionId,
                overseasEntityId,
                ERIC_REQUEST_ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetOverseasEntityDetailsReturnsNotFoundForNoDetails() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));
        when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(null);

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transactionId, overseasEntityId, ERIC_REQUEST_ID);

        verify(privateDataRetrievalService, times(1)).getOverseasEntityData(COMPANY_NUMBER);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetOverseasEntityDetailsReturnsNotFoundForNoEmailInDetails() throws ServiceException {
        OverseasEntityDataApi overseasEntityDataApi = new OverseasEntityDataApi();
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));
        when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(overseasEntityDataApi);

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transactionId, overseasEntityId, ERIC_REQUEST_ID);

        verify(privateDataRetrievalService, times(1)).getOverseasEntityData(COMPANY_NUMBER);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetManagingOfficersReturnsSuccessfully() throws ServiceException {
        List<ManagingOfficerDataApi> managingOfficers = new ArrayList<>();
        ManagingOfficerDataApi officerDataApi = new ManagingOfficerDataApi();
        managingOfficers.add(officerDataApi);
        ManagingOfficerListDataApi managingOfficerListDataApi = new ManagingOfficerListDataApi(managingOfficers);

        OverseasEntitySubmissionDto submissionDtoMock = createOverseasEntitySubmissionMock();
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId))
                .thenReturn(Optional.of(submissionDtoMock));

        String entityNumber = submissionDtoMock.getEntityNumber();
        when(privateDataRetrievalService.getManagingOfficerData(entityNumber))
                .thenReturn(managingOfficerListDataApi);

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);
        ResponseEntity<ManagingOfficerListDataApi> response = overseasEntitiesDataController.getManagingOfficers("TransactionID", overseasEntityId, "requestId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(managingOfficerListDataApi, response.getBody());
    }

    @Test
    void testGetManagingOfficersReturnsNotFoundWhenNoOfficersFound() throws ServiceException {
        OverseasEntitySubmissionDto submissionDtoMock = createOverseasEntitySubmissionMock();
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId))
                .thenReturn(Optional.of(submissionDtoMock));

        String entityNumber = submissionDtoMock.getEntityNumber();
        when(privateDataRetrievalService.getManagingOfficerData(entityNumber))
                .thenReturn(null);

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);
        ResponseEntity<ManagingOfficerListDataApi> response = overseasEntitiesDataController.getManagingOfficers("TransactionID", overseasEntityId, "requestId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody()); // The body should be null for a NOT_FOUND response
    }

    @Test
    void testGetManagingOfficersReturnsInternalServerErrorWhenExceptionThrown() throws ServiceException {
        OverseasEntitySubmissionDto submissionDtoMock = createOverseasEntitySubmissionMock();
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId))
                .thenReturn(Optional.of(submissionDtoMock));

        String entityNumber = submissionDtoMock.getEntityNumber();
        when(privateDataRetrievalService.getManagingOfficerData(entityNumber))
                .thenThrow(new ServiceException("error occurred"));

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);
        ResponseEntity<ManagingOfficerListDataApi> response = overseasEntitiesDataController.getManagingOfficers("TransactionID", overseasEntityId, "requestId");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetManagingOfficersReturnsInternalServerErrorWhenUpdateDisabled() {
        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(
                privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, false);

        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));

        assertThrows(ServiceException.class,
                () -> overseasEntitiesDataController.getManagingOfficers(
                        transactionId,
                        overseasEntityId,
                        ERIC_REQUEST_ID));
    }

    private OverseasEntitySubmissionDto createOverseasEntitySubmissionMock() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber(COMPANY_NUMBER);
        EntityDto entityDto = new EntityDto();
        overseasEntitySubmissionDto.setEntity(entityDto);
        return overseasEntitySubmissionDto;
    }

    private void setUpdateEnabledFeatureFlag(OverseasEntitiesDataController overseasEntitiesDataController, boolean value) {
        ReflectionTestUtils.setField(overseasEntitiesDataController, "isRoeUpdateEnabled", value);
    }
}
