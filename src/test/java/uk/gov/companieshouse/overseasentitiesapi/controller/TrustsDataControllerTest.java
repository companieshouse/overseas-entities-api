package uk.gov.companieshouse.overseasentitiesapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksListApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

@ExtendWith(MockitoExtension.class)
class TrustsDataControllerTest {
    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @InjectMocks
    private TrustsDataController trustsDataController;

    @Mock
    private HashHelper hashHelper;

    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    private ByteArrayOutputStream outputStreamCaptor;

    private final String HASHED_ID = "u2YuMp-BwsQ_GGMbYE2EExDBkoA";

    private final String CORP_BODY_HASHED_ID = "WhjCS3yQb908KXF-c1Z8sEJsJII";
    
    @BeforeEach
    void setUp() {
        setUpdateEnabledFeatureFlag(trustsDataController, true);
        lenient().when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(true);
        reset(privateDataRetrievalService, overseasEntitiesService, hashHelper);
        outputStreamCaptor = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
        outputStreamCaptor.reset();
        System.setOut(System.out);
    }

    @Test
    void getTrustDetails_success() throws ServiceException {
        PrivateTrustDetailsApi trustDetailsApi = createTrustDetailsApiMock();
        PrivateTrustDetailsListApi listApi = new PrivateTrustDetailsListApi(
                List.of(trustDetailsApi));
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(listApi);
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals(HASHED_ID, responseEntity.getBody().getData().get(0).getHashedId());
    }

    @Test
    void getTrustLinks_success() throws ServiceException {
        PrivateTrustLinksApi trustLinksApi = createTrustLinksApiMock();
        PrivateTrustLinksListApi listApi = new PrivateTrustLinksListApi(
                List.of(trustLinksApi));
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(listApi);
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals(HASHED_ID, responseEntity.getBody().getData().get(0).getHashedId());
        assertEquals(CORP_BODY_HASHED_ID, responseEntity.getBody().getData().get(0).getHashedCorporateBodyAppointmentId());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiListNull() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust details for overseas entity overseasEntityId"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_getTrustLinksApiListNull() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust links for overseas entity overseasEntityId"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiNull() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(new PrivateTrustDetailsListApi(null));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "OE123456", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust details for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_getTrustLinksApiNull() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(new PrivateTrustLinksListApi(null));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "OE123456", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust links for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiEmpty() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(new PrivateTrustDetailsListApi(Collections.emptyList()));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "OE123456", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust details for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_getTrustLinksApiEmpty() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(new PrivateTrustLinksListApi(Collections.emptyList()));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "OE123456", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust links for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_noCompanyNumber() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_noCompanyNumber() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustData_noOverseasEntitySubmission() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.empty());

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        ResponseEntity<PrivateTrustLinksListApi> linksResponseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(404, linksResponseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
        assertNull(linksResponseEntity.getBody());
    }

    @Test
    void retrievePrivateTrustData_updateEnabledFeatureFlagFalse() {
        setUpdateEnabledFeatureFlag(trustsDataController, false);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));

        var detailsThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustDetails("transactionId", "overseasEntityId", "requestId"));

        var linksThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustLinks("transactionId", "overseasEntityId", "requestId"));

        assertEquals("ROE Update feature must be enabled for get overseas entity details", detailsThrown.getMessage());
        assertEquals("ROE Update feature must be enabled for get overseas entity details", linksThrown.getMessage());
    }

    @Test
    void retrievePrivateTrustData_isForUpdateFalse() {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(false);

        var detailsThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustDetails("transactionId", "overseasEntityId", "requestId"));

        var linksThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustDetails("transactionId", "overseasEntityId", "requestId"));


        assertEquals("Submission for overseas entity details must be for update", detailsThrown.getMessage());
        assertEquals("Submission for overseas entity details must be for update", linksThrown.getMessage());
    }

    @Test
    void retrievePrivateTrustData_responseEntityWith500StatusCode() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(privateDataRetrievalService.getTrustDetails(any())).thenThrow(ServiceException.class);
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        ResponseEntity<PrivateTrustDetailsListApi> linksResponseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(500, responseEntity.getStatusCodeValue());
        assertEquals(500, linksResponseEntity.getStatusCodeValue());
    }

    private PrivateTrustDetailsApi createTrustDetailsApiMock() {
        PrivateTrustDetailsApi trustDetailsApi = new PrivateTrustDetailsApi();
        trustDetailsApi.setId("1111");
        return trustDetailsApi;
    }

    private PrivateTrustLinksApi createTrustLinksApiMock() {
        PrivateTrustLinksApi trustLinksApi = new PrivateTrustLinksApi();
        trustLinksApi.setId("1111");
        trustLinksApi.setCorporateBodyAppointmentId("8888");
        return trustLinksApi;
    }

    private void setUpdateEnabledFeatureFlag(TrustsDataController trustsDataController, boolean value) {
        ReflectionTestUtils.setField(trustsDataController, "isRoeUpdateEnabled", value);
    }
}
