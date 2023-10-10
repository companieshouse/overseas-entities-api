package uk.gov.companieshouse.overseasentitiesapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.model.corporatetrustee.PrivateCorporateTrusteeApi;
import uk.gov.companieshouse.api.model.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

@ExtendWith(MockitoExtension.class)
class TrustsDataControllerTest {

    private final String HASHED_ID = "u2YuMp-BwsQ_GGMbYE2EExDBkoA";
    @InjectMocks
    private TrustsDataController trustsDataController;
    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;
    @Mock
    private OverseasEntitiesService overseasEntitiesService;
    @Mock
    private HashHelper hashHelper;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void setUp() {
        setUpdateEnabledFeatureFlag(trustsDataController, true);
        lenient().when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(true);
        reset(privateDataRetrievalService, overseasEntitiesService);
        outputStreamCaptor = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
        outputStreamCaptor.reset();
        System.setOut(System.out);
    }

    @Test
    void getCorporateTrustees_success() throws ServiceException {
        PrivateCorporateTrusteeApi trusteeApi = createTrustApiMock();
        PrivateCorporateTrusteeListApi listApi = new PrivateCorporateTrusteeListApi(
                List.of(trusteeApi));
        when(privateDataRetrievalService.getCorporateTrustees(any(), any())).thenReturn(listApi);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrustees(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getCorporateTrustees_getCorporateTrusteesNull()
            throws ServiceException {

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrustees(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),"Could not find any corporate trustee for overseas entity overseasEntityId"));

    }

    void getTrustDetails_success() throws ServiceException {
        PrivateTrustDetailsApi trustDetailsApi = createTrustDetailsApiMock();
        PrivateTrustDetailsListApi listApi = new PrivateTrustDetailsListApi(
                List.of(trustDetailsApi));
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(listApi);
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals(HASHED_ID, responseEntity.getBody().getData().get(0).getHashedId());
    }


    @Test
    void getCorporateTrustees_noCompanyNumber() throws ServiceException {

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrustees(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiListNull() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));
        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "Could not find any trust details for overseas entity overseasEntityId"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiEmpty() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(
                new PrivateTrustDetailsListApi(Collections.emptyList()));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "OE123456", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "Could not find any trust details for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_noCompanyNumber() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_noOverseasEntitySubmission() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.empty());

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

    @Test
    void retrievePrivateTrustData_updateEnabledFeatureFlagFalse() {

        setUpdateEnabledFeatureFlag(trustsDataController, false);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        var thrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getCorporateTrustees("transactionId", "overseasEntityId",
                        "trustId", "requestId"));

        assertEquals("ROE Update feature must be enabled for get overseas entity details",
                thrown.getMessage());
    }

    @Test
    void retrievePrivateTrustData_isForUpdateFalse() {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(false);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        var thrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustDetails("transactionId", "overseasEntityId",
                        "requestId"));

        assertEquals("Submission for overseas entity details must be for update",
                thrown.getMessage());
    }

    @Test
    void retrievePrivateTrustData_responseEntityWith500StatusCode() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(privateDataRetrievalService.getTrustDetails(any())).thenThrow(ServiceException.class);
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(500, responseEntity.getStatusCodeValue());
    }

    private PrivateCorporateTrusteeApi createTrustApiMock() {
        PrivateCorporateTrusteeApi trusteeApi = new PrivateCorporateTrusteeApi();
        trusteeApi.setId("1111");
        return trusteeApi;
    }

    private PrivateTrustDetailsApi createTrustDetailsApiMock() {
        PrivateTrustDetailsApi trustDetailsApi = new PrivateTrustDetailsApi();
        trustDetailsApi.setId("1111");
        return trustDetailsApi;
    }

    private void setUpdateEnabledFeatureFlag(TrustsDataController trustsDataController,
            boolean value) {
        ReflectionTestUtils.setField(trustsDataController, "isRoeUpdateEnabled", value);
    }
}
