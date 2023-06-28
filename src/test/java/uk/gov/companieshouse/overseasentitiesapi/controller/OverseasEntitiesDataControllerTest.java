package uk.gov.companieshouse.overseasentitiesapi.controller;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesDataControllerTest {

    private static final String ERIC_REQUEST_ID = "XaBcDeF12345";
    private static final String overseasEntityId = "123456778";
    private static final String email = "TEST@MAILINATOR.COM";

    private static final String COMPANY_NUMBER = "OE111129";

    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    private Transaction transaction = new Transaction();

    @Test
    void testGetOverseasEntityDetailsReturnsSuccessfully() throws ServiceException {
        OverseasEntityDataApi overseasEntityDataApi = new OverseasEntityDataApi();
        overseasEntityDataApi.setEmail(email);
        when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(overseasEntityDataApi);
        when(overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)).thenReturn(
                Optional.of(createOverseasEntitySubmissionMock()));

        OverseasEntitiesDataController overseasEntitiesDataController = new OverseasEntitiesDataController(privateDataRetrievalService, overseasEntitiesService);
        setUpdateEnabledFeatureFlag(overseasEntitiesDataController, true);

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transaction, overseasEntityId, ERIC_REQUEST_ID);

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

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transaction, overseasEntityId, ERIC_REQUEST_ID);

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

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transaction, overseasEntityId, ERIC_REQUEST_ID);

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
                        transaction,
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
                        transaction,
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
                transaction,
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

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transaction, overseasEntityId, ERIC_REQUEST_ID);

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

        var response = overseasEntitiesDataController.getOverseasEntityDetails(transaction, overseasEntityId, ERIC_REQUEST_ID);

        verify(privateDataRetrievalService, times(1)).getOverseasEntityData(COMPANY_NUMBER);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
