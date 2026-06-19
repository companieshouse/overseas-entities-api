package uk.gov.companieshouse.overseasentitiesapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.trustees.corporatetrustee.PrivateCorporateTrusteeApi;
import uk.gov.companieshouse.api.model.trustees.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.trustees.individualtrustee.PrivateIndividualTrusteeApi;
import uk.gov.companieshouse.api.model.trustees.individualtrustee.PrivateIndividualTrusteeListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksListApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

@ExtendWith(MockitoExtension.class)
class TrustsDataControllerTest {

    private static final String ERIC_REQUEST_ID = "XaBcDeF12345";
    private static final String TRANSACTION_ID = "12345-464634";

    private final String HASHED_ID = "WA0s6H5fbmpR0zXPGQe1o1sLMWc";
    @InjectMocks
    private TrustsDataController trustsDataController;
    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;
    @Mock
    private OverseasEntitiesService overseasEntitiesService;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    @Mock
    private Transaction transaction;

    private ByteArrayOutputStream outputStreamCaptor;

    private final String CORP_BODY_HASHED_ID = "7WjVLd1jRquoDgnNNTGq1j-k3gE";

    @BeforeEach
    void setUp() {
        lenient().when(overseasEntitySubmissionDto.isForUpdateOrRemove()).thenReturn(true);
        reset(privateDataRetrievalService, overseasEntitiesService);
        outputStreamCaptor = new ByteArrayOutputStream();
        ReflectionTestUtils.setField(trustsDataController, "salt", "mockedSalt");

        when(transaction.getId()).thenReturn(TRANSACTION_ID);
    }

    @AfterEach
    void tearDown() {
        outputStreamCaptor.reset();
        System.setOut(System.out);
    }

    @Test
    void getIndividualTrustees_success() throws ServiceException, SubmissionNotLinkedToTransactionException {
        PrivateIndividualTrusteeApi trusteeApi = createIndividualTrustApiMock();
        PrivateIndividualTrusteeListApi listApi = new PrivateIndividualTrusteeListApi(
                List.of(trusteeApi));
        when(privateDataRetrievalService.getIndividualTrustees(any(), any())).thenReturn(listApi);

        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateIndividualTrusteeListApi> responseEntity = trustsDataController.getIndividualTrustees(
                transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID);

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getIndividualTrustees_getIndividualTrusteesNull()
            throws ServiceException, SubmissionNotLinkedToTransactionException {

        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateIndividualTrusteeListApi> responseEntity = trustsDataController.getIndividualTrustees(
                transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),"Could not find any individual trustee for overseas entity overseasEntityId"));

    }

    @Test
    void getCorporateTrustees_success() throws ServiceException, SubmissionNotLinkedToTransactionException {
        PrivateCorporateTrusteeApi trusteeApi = createCorpTrustApiMock();
        PrivateCorporateTrusteeListApi listApi = new PrivateCorporateTrusteeListApi(
                List.of(trusteeApi));
        when(privateDataRetrievalService.getCorporateTrustees(any(), any())).thenReturn(listApi);

        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrustees(
                transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID);

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getCorporateTrustees_getCorporateTrusteesNull()
            throws ServiceException, SubmissionNotLinkedToTransactionException {

        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrustees(
                transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),"Could not find any corporate trustee for overseas entity overseasEntityId"));

    }

    @Test
    void getTrustDetails_success() throws ServiceException, SubmissionNotLinkedToTransactionException {
        PrivateTrustDetailsApi trustDetailsApi = createTrustDetailsApiMock();
        PrivateTrustDetailsListApi listApi = new PrivateTrustDetailsListApi(
                List.of(trustDetailsApi));
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(listApi);
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals(HASHED_ID, responseEntity.getBody().getData().getFirst().getHashedId());
    }


    @Test
    void getTrustLinks_success() throws ServiceException, SubmissionNotLinkedToTransactionException {
        PrivateTrustLinksApi trustLinksApi = createTrustLinksApiMock();
        PrivateTrustLinksListApi listApi = new PrivateTrustLinksListApi(
                List.of(trustLinksApi));
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(listApi);
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals(HASHED_ID, responseEntity.getBody().getData().getFirst().getHashedId());
        assertEquals(CORP_BODY_HASHED_ID, responseEntity.getBody().getData().getFirst().getHashedCorporateBodyAppointmentId());
    }

    @Test
    void getCorporateTrustees_noCompanyNumber() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateCorporateTrusteeListApi> responseEntity = trustsDataController.getCorporateTrustees(
                transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void getIndividualTrustees_noCompanyNumber() throws ServiceException, SubmissionNotLinkedToTransactionException {

        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateIndividualTrusteeListApi> responseEntity = trustsDataController.getIndividualTrustees(
                transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void getTrustLinks_getTrustLinksApiListNull() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust links for overseas entity overseasEntityId"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiListNull() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        System.setOut(new PrintStream(outputStreamCaptor));
        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);
        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "Could not find any trust details for overseas entity overseasEntityId"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_getTrustLinksApiNull() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(new PrivateTrustLinksListApi(null));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                transaction, "OE123456", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust links for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_getTrustDetailsApiEmpty() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustDetails(any())).thenReturn(
                new PrivateTrustDetailsListApi(Collections.emptyList()));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                transaction, "OE123456", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "Could not find any trust details for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_getTrustLinksApiEmpty() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");
        when(privateDataRetrievalService.getTrustLinks(any())).thenReturn(new PrivateTrustLinksListApi(Collections.emptyList()));

        System.setOut(new PrintStream(outputStreamCaptor));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                transaction, "OE123456", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Could not find any trust links for overseas entity OE123456"));
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustDetails_noCompanyNumber() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustLinks_noCompanyNumber() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));

        ResponseEntity<PrivateTrustLinksListApi> responseEntity = trustsDataController.getTrustLinks(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertNull(responseEntity.getBody());
    }

    @Test
    void getTrustData_noOverseasEntitySubmission() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.empty());

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        ResponseEntity<PrivateTrustLinksListApi> linksResponseEntity = trustsDataController.getTrustLinks(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(404, linksResponseEntity.getStatusCode().value());
        assertNull(responseEntity.getBody());
        assertNull(linksResponseEntity.getBody());
    }

    @Test
    void retrievePrivateTrustData_isForUpdateFalse() throws SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDto.isForUpdateOrRemove()).thenReturn(false);

        var detailsThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustDetails(transaction, "overseasEntityId", ERIC_REQUEST_ID));

        var linksThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getTrustDetails(transaction, "overseasEntityId", ERIC_REQUEST_ID));
        
        var corpTrusteeThrown = assertThrows(ServiceException.class,
                () -> trustsDataController.getCorporateTrustees(transaction, "overseasEntityId", "trustId", ERIC_REQUEST_ID));


        final String expectedExceptionMessage = "Submission for overseas entity details must be for update or remove";

        assertEquals(expectedExceptionMessage, detailsThrown.getMessage());
        assertEquals(expectedExceptionMessage, linksThrown.getMessage());
        assertEquals(expectedExceptionMessage, corpTrusteeThrown.getMessage());
    }

    @Test
    void retrievePrivateTrustData_responseEntityWith500StatusCode() throws ServiceException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(eq(transaction), any(), eq(ERIC_REQUEST_ID))).thenReturn(
                Optional.of(overseasEntitySubmissionDto));
        when(privateDataRetrievalService.getTrustDetails(any())).thenThrow(ServiceException.class);
        when(overseasEntitySubmissionDto.getEntityNumber()).thenReturn("OE123456");

        ResponseEntity<PrivateTrustDetailsListApi> responseEntity = trustsDataController.getTrustDetails(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        ResponseEntity<PrivateTrustDetailsListApi> linksResponseEntity = trustsDataController.getTrustDetails(
                transaction, "overseasEntityId", ERIC_REQUEST_ID);

        assertEquals(500, responseEntity.getStatusCode().value());
        assertEquals(500, linksResponseEntity.getStatusCode().value());
    }

    private PrivateIndividualTrusteeApi createIndividualTrustApiMock() {
        PrivateIndividualTrusteeApi trusteeApi = new PrivateIndividualTrusteeApi();
        trusteeApi.setId("1111");
        return trusteeApi;
    }

    private PrivateCorporateTrusteeApi createCorpTrustApiMock() {
        PrivateCorporateTrusteeApi trusteeApi = new PrivateCorporateTrusteeApi();
        trusteeApi.setId("1111");
        return trusteeApi;
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
}
