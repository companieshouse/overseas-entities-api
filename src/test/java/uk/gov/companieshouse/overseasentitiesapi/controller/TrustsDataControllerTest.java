package uk.gov.companieshouse.overseasentitiesapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    void getTrustDetails_success() throws ServiceException {
        PrivateTrustLinksApi trustDetailsApi = createTrustLinksApiMock();
        PrivateTrustLinksListApi listApi = new PrivateTrustLinksListApi(
                List.of(trustDetailsApi));
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(listApi);
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals(HASHED_ID, responseEntity.getBody().getData().get(0).getHashedId());
    }

    @Test
    void getTrustDetails_getTrustDetailsNull()
            throws ServiceException {

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
    void getTrustDetails_noCompanyNumber()
            throws ServiceException {

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

    @Test
    void checkSubmissionDto_updateEnabledFeatureFlagFalse() {
        setUpdateEnabledFeatureFlag(trustsDataController, false);

        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));

        var thrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustLinks("transactionId", "overseasEntityId", "requestId"));

        assertEquals("ROE Update feature must be enabled for get overseas entity links", thrown.getMessage());
    }

    @Test
    void checkSubmissionDto_isForUpdateFalse() {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.isForUpdate()).thenReturn(false);

        var thrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustLinks("transactionId", "overseasEntityId", "requestId"));

        assertEquals("Submission for overseas entity trust links must be for update",
                thrown.getMessage());
    }

    @Test
    void retrievePrivateTrustData_responseEntityWith500StatusCode() throws ServiceException {
        when(overseasEntitiesService.getOverseasEntitySubmission(any())).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(privateDataRetrievalService.getTrustLinks(any())).thenThrow(ServiceException.class);
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                "transactionId", "overseasEntityId", "requestId");

        assertEquals(500, responseEntity.getStatusCodeValue());
    }

    private PrivateTrustLinksApi createTrustLinksApiMock() {
        PrivateTrustLinksApi trustLinksApi = new PrivateTrustLinksApi();
        trustLinksApi.setId("1111");
        trustLinksApi.setCorporateBodyAppointmentId("afdffds");
        return trustLinksApi;
    }

    private void setUpdateEnabledFeatureFlag(TrustsDataController trustsDataController, boolean value) {
        ReflectionTestUtils.setField(trustsDataController, "isRoeUpdateEnabled", value);
    }
}
