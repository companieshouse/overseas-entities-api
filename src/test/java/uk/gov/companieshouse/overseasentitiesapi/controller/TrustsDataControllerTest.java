package uk.gov.companieshouse.overseasentitiesapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
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
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

@ExtendWith(MockitoExtension.class)
class TrustsDataControllerTest {

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
        when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(true);
        reset(privateDataRetrievalService, overseasEntitiesService, hashHelper);
        outputStreamCaptor = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
        outputStreamCaptor.reset();
        System.setOut(System.out);
    }

    @Test
    void getCorporateTrusts_success() throws ServiceException {
        PrivateCorporateTrusteeApi trusteeApi = createTrustApiMock();
        PrivateCorporateTrusteeListApi listApi = new PrivateCorporateTrusteeListApi(
                List.of(trusteeApi));
        when(privateDataRetrievalService.getCorporateTrustees(any(), any())).thenReturn(listApi);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrusts(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getCorporateTrusts_getCorporateTrusteesNull()
            throws ServiceException {

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrusts(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),"Could not find any corporate trustee for overseas entity overseasEntityId"));

    }


    @Test
    void getCorporateTrusts_noCompanyNumber()
            throws ServiceException {

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrusts(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
    }



    @Test
    void checkSubmissionDto_updateEnabledFeatureFlagFalse() {
        setUpdateEnabledFeatureFlag(trustsDataController, false);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        var thrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getCorporateTrusts("transactionId", "overseasEntityId",
                        "trustId", "requestId"));

        assertEquals("ROE Update feature must be enabled for get overseas entity details",
                thrown.getMessage());
    }
    @Test
    void checkSubmissionDto_isForUpdateFalse() {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(false);

        var thrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getCorporateTrusts("transactionId", "overseasEntityId",
                        "trustId", "requestId"));

        assertEquals("Submission for overseas entity details must be for update",
                thrown.getMessage());
    }


    @Test
    void retrievePrivateTrustData_responseEntityWith500StatusCode() throws ServiceException {
        PrivateCorporateTrusteeApi trusteeApi = createTrustApiMock();
        PrivateCorporateTrusteeListApi listApi = new PrivateCorporateTrusteeListApi(
                List.of(trusteeApi));

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        when(privateDataRetrievalService.getCorporateTrustees(any(), any())).thenThrow(
                ServiceException.class);

        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");


        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrusts(
                "transactionId", "overseasEntityId", "trustId", "requestId");

        assertEquals(500, responseEntity.getStatusCodeValue());
    }

    private PrivateCorporateTrusteeApi createTrustApiMock() {
        PrivateCorporateTrusteeApi trusteeApi = new PrivateCorporateTrusteeApi();
        trusteeApi.setId("1111");
        return trusteeApi;
    }

    private void setUpdateEnabledFeatureFlag(TrustsDataController trustsDataController,
            boolean value) {
        ReflectionTestUtils.setField(trustsDataController, "isRoeUpdateEnabled", value);
    }
}
