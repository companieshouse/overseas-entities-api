package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.beneficialowner.PrivateBeneficialOwnerResourceHandler;
import uk.gov.companieshouse.api.handler.beneficialowner.request.PrivateBeneficialOwnerGet;
import uk.gov.companieshouse.api.handler.corporatetrustee.PrivateCorporateTrusteesResourceHandler;
import uk.gov.companieshouse.api.handler.corporatetrustee.request.PrivateCorporateTrusteesGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.managingofficerdata.PrivateManagingOfficerDataResourceHandler;
import uk.gov.companieshouse.api.handler.managingofficerdata.request.PrivateManagingOfficerDataGet;
import uk.gov.companieshouse.api.handler.trustees.individualtrustee.PrivateIndividualTrusteesResourceHandler;
import uk.gov.companieshouse.api.handler.trustees.individualtrustee.request.PrivateIndividualTrusteesGet;
import uk.gov.companieshouse.api.handler.trusts.PrivateTrustDetailsResourceHandler;
import uk.gov.companieshouse.api.handler.trusts.PrivateTrustLinksResourceHandler;
import uk.gov.companieshouse.api.handler.trusts.request.PrivateTrustDetailsGet;
import uk.gov.companieshouse.api.handler.trusts.request.PrivateTrustLinksGet;
import uk.gov.companieshouse.api.handler.update.PrivateOverseasEntityDataHandler;
import uk.gov.companieshouse.api.handler.update.request.PrivateOverseasEntityDataGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.trustees.corporatetrustee.PrivateCorporateTrusteeApi;
import uk.gov.companieshouse.api.model.trustees.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.trustees.individualtrustee.PrivateIndividualTrusteeApi;
import uk.gov.companieshouse.api.model.trustees.individualtrustee.PrivateIndividualTrusteeListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksListApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PrivateBeneficialOwnersMock;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

@ExtendWith(MockitoExtension.class)
class PrivateDataRetrievalServiceTest {

    private static final String COMPANY_NUMBER = "OE123456";
    private static final ApiErrorResponseException FOUR_HUNDRED_AND_FOUR_EXCEPTION = ApiErrorResponseException.fromHttpResponseException(
            new HttpResponseException.Builder(404, "ERROR", new HttpHeaders()).build());
    private final String jsonManagingOfficerString = "["
            + "{"
            + "\"managingOfficerAppointmentId\":\"9001808986\","
            + "\"residential_address\":{"
            + "\"address_line_1\":\"32 WHITE FIRST ROAD\","
            + "\"address_line_2\":\"QUOS ADIPISCI OFFICI\","
            + "\"care_of\":null,"
            + "\"country\":\"HUNGARY\","
            + "\"locality\":\"QUI QUIS EST MAIORE\","
            + "\"po_box\":null,"
            + "\"postal_code\":\"51291\","
            + "\"premises\":\"DARRYL VALENTINE\","
            + "\"region\":\"CILLUM EXERCITATION\""
            + "},"
            + "\"principal_address\":{"
            + "\"address_line_1\":null,"
            + "\"address_line_2\":null,"
            + "\"care_of\":null,"
            + "\"country\":null,"
            + "\"locality\":null,"
            + "\"po_box\":null,"
            + "\"postal_code\":null,"
            + "\"premises\":null,"
            + "\"region\":null"
            + "},"
            + "\"date_of_birth\":\"2023-01-01 00:00:00.0\","
            + "\"contact_name_full\":null,"
            + "\"contact_email_address\":null"
            + "},"
            + "{"
            + "\"managingOfficerAppointmentId\":\"9001808987\","
            + "\"residential_address\":{"
            + "\"address_line_1\":null,"
            + "\"address_line_2\":null,"
            + "\"care_of\":null,"
            + "\"country\":null,"
            + "\"locality\":null,"
            + "\"po_box\":null,"
            + "\"postal_code\":null,"
            + "\"premises\":null,"
            + "\"region\":null"
            + "},"
            + "\"principal_address\":{"
            + "\"address_line_1\":\"29 SOUTH MILTON ROAD\","
            + "\"address_line_2\":\"TENETUR QUI VOLUPTAT\","
            + "\"care_of\":null,"
            + "\"country\":\"ICELAND\","
            + "\"locality\":\"QUIA ELIGENDI DOLORE\","
            + "\"po_box\":null,"
            + "\"postal_code\":\"77761\","
            + "\"premises\":\"WYNNE GARCIA\","
            + "\"region\":\"VERO CORRUPTI IPSUM\""
            + "},"
            + "\"date_of_birth\":\"9999-12-31 00:00:00.0\","
            + "\"contact_name_full\":\"TYLER DELEON\","
            + "\"contact_email_address\":\"QEZUX@MAILINATOR.COM\""
            + "}"
            + "]";

    private ByteArrayOutputStream outputStreamCaptor;
    @InjectMocks
    private PrivateDataRetrievalService privateDataRetrievalService;
    @Mock
    private ApiClientService apiClientService;
    @Mock
    private InternalApiClient apiClient;
    @Mock
    private ApiResponse<OverseasEntityDataApi> overseasEntityDataResponse;
    @Mock
    private ApiResponse<ManagingOfficerListDataApi> managingOfficerDataResponse;
    @Mock
    private PrivateOverseasEntityDataHandler privateOverseasEntityDataHandler;
    @Mock
    private PrivateOverseasEntityDataGet privateOverseasEntityDataGet;
    @Mock
    private ApiResponse<PrivateBoDataListApi> apiBoDataListGetResponse;
    @Mock
    private PrivateBeneficialOwnerResourceHandler privateBeneficialOwnerResourceHandler;
    @Mock
    private PrivateBeneficialOwnerGet privateBeneficialOwnerGet;
    @Mock
    private PrivateManagingOfficerDataResourceHandler privateManagingOfficerDataResourceHandler;
    @Mock
    private PrivateManagingOfficerDataGet privateManagingOfficerDataGet;
    @Mock
    private PrivateCorporateTrusteesResourceHandler privateCorporateTrusteeDataResourceHandler;
    @Mock
    private PrivateCorporateTrusteesGet privateCorporateTrusteeGet;
    @Mock
    private ApiResponse<PrivateCorporateTrusteeListApi> privateCorporateTrusteeDataResponse;
    @Mock
    private PrivateIndividualTrusteesResourceHandler privateIndividualTrusteeDataResourceHandler;
    @Mock
    private PrivateIndividualTrusteesGet privateIndividualTrusteeGet;
    @Mock
    private ApiResponse<PrivateIndividualTrusteeListApi> privateIndividualTrusteeDataResponse;
    @Mock
    private PrivateTrustDetailsResourceHandler privateTrustDetailsResourceHandler;
    @Mock
    private PrivateTrustDetailsGet privateTrustDetailsGet;
    @Mock
    private HashHelper hashHelper;
    @Mock
    private ApiResponse<PrivateTrustDetailsListApi> privateTrustDetailsDataResponse;
    @Mock
    private PrivateTrustLinksResourceHandler privateTrustLinksResourceHandler;

    @Mock
    private PrivateTrustLinksGet privateTrustLinksGet;

    @Mock
    private ApiResponse<PrivateTrustLinksListApi> privateTrustLinksDataResponse;

    protected void retrievePrivateData() throws ServiceException {
        privateDataRetrievalService.getOverseasEntityData((COMPANY_NUMBER));
        privateDataRetrievalService.getBeneficialOwnersData((COMPANY_NUMBER));
        privateDataRetrievalService.getManagingOfficerData((COMPANY_NUMBER));
    }

    @Nested
    class BeneficialOwnerTests {

        @BeforeEach
        public void init() throws IOException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
            // Beneficial Owner Data Mocks
            when(apiClient.privateBeneficialOwnerResourceHandler()).thenReturn(
                    privateBeneficialOwnerResourceHandler);
            when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(
                    Mockito.anyString())).thenReturn(privateBeneficialOwnerGet);
            // Trust data Mocks
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
        }

        @Test
        void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsURIValidationException()
                throws IOException, URIValidationException {
            when(privateBeneficialOwnerGet.execute()).thenThrow(
                    new URIValidationException("ERROR"));

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getBeneficialOwnersData((COMPANY_NUMBER));
            });
        }

        @Test
        void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsIOException()
                throws IOException, URIValidationException {

            when(privateBeneficialOwnerGet.execute()).thenThrow(
                    ApiErrorResponseException.fromIOException(new IOException("ERROR")));

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getBeneficialOwnersData(COMPANY_NUMBER);
            });
        }
    }

    @Nested
    class ManagingOfficerTests {

        @BeforeEach
        public void init() throws IOException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            // Overseas Entity Data Mocks
            when(apiClient.privateOverseasEntityDataHandler()).thenReturn(
                    privateOverseasEntityDataHandler);
            when(privateOverseasEntityDataHandler.getOverseasEntityData(
                    Mockito.anyString())).thenReturn(privateOverseasEntityDataGet);

            // Beneficial Owner Data Mocks
            when(apiClient.privateBeneficialOwnerResourceHandler()).thenReturn(
                    privateBeneficialOwnerResourceHandler);
            when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(
                    Mockito.anyString())).thenReturn(privateBeneficialOwnerGet);

            // Managing Officer Data Mocks
            when(apiClient.privateManagingOfficerDataResourceHandler()).thenReturn(
                    privateManagingOfficerDataResourceHandler);
            when(privateManagingOfficerDataResourceHandler.getManagingOfficerData(
                    Mockito.anyString())).thenReturn(privateManagingOfficerDataGet);
        }

        @Test
        void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsURIValidationException()
                throws IOException, URIValidationException {
            var privateBoDataListApi = new PrivateBoDataListApi(Collections.emptyList());
            var overseasEntityApi = new OverseasEntityDataApi();

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(privateBoDataListApi);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateManagingOfficerDataGet.execute()).thenThrow(
                    new URIValidationException("ERROR"));

            assertThrows(ServiceException.class, () -> {
                retrievePrivateData();
            });
        }

        @Test
        void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsIOException()
                throws IOException, URIValidationException {
            var privateBoDataListApi = new PrivateBoDataListApi(Collections.emptyList());
            var overseasEntityApi = new OverseasEntityDataApi();

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(privateBoDataListApi);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateManagingOfficerDataGet.execute()).thenThrow(
                    ApiErrorResponseException.fromIOException(new IOException("ERROR")));

            assertThrows(ServiceException.class, () -> {
                retrievePrivateData();
            });
        }
    }

    @Nested
    class OverseasEntitiesTests {

        @BeforeEach
        public void init() throws IOException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            when(apiClient.privateOverseasEntityDataHandler()).thenReturn(
                    privateOverseasEntityDataHandler);
            when(privateOverseasEntityDataHandler.getOverseasEntityData(
                    Mockito.anyString())).thenReturn(privateOverseasEntityDataGet);
        }

        @Test
        void testServiceExceptionThrownWhenGetOverseasEntitiesOEPrivateDataThrowsURIValidationException()
                throws IOException, URIValidationException {

            when(privateOverseasEntityDataGet.execute()).thenThrow(
                    new URIValidationException("ERROR"));

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getOverseasEntityData((COMPANY_NUMBER));
            });
        }

        @Test
        void testServiceExceptionThrownWhenGetOverseasEntitiesOEPrivateDataThrowsNon404Error()
                throws IOException, URIValidationException {

            when(privateOverseasEntityDataGet.execute()).thenThrow(
                    ApiErrorResponseException.fromIOException(new IOException()));

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getOverseasEntityData((COMPANY_NUMBER));
            });
        }

        @Test
        void testNoEmailWhenGetOverseasEntitiesPrivateDataThrowsApiResponseError404Exception()
                throws ApiErrorResponseException, URIValidationException, ServiceException {

            when(privateOverseasEntityDataGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

            OverseasEntityDataApi result = privateDataRetrievalService.getOverseasEntityData(
                    COMPANY_NUMBER);

            assertNull(result.getEmail());
        }
    }

    @Nested
    class utilisingAllMocksTests {

        @BeforeEach
        public void init() throws IOException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            // Overseas Entity Data Mocks
            when(apiClient.privateOverseasEntityDataHandler()).thenReturn(
                    privateOverseasEntityDataHandler);
            when(privateOverseasEntityDataHandler.getOverseasEntityData(
                    Mockito.anyString())).thenReturn(privateOverseasEntityDataGet);

            // Beneficial Owner Data Mocks
            when(apiClient.privateBeneficialOwnerResourceHandler()).thenReturn(
                    privateBeneficialOwnerResourceHandler);
            when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(
                    Mockito.anyString())).thenReturn(privateBeneficialOwnerGet);

            // Managing Officer Data Mocks
            when(apiClient.privateManagingOfficerDataResourceHandler()).thenReturn(
                    privateManagingOfficerDataResourceHandler);
            when(privateManagingOfficerDataResourceHandler.getManagingOfficerData(
                    Mockito.anyString())).thenReturn(privateManagingOfficerDataGet);
        }

        @Test
        void testGetPrivateDataBeneficialOwnersDataNotFound()
                throws IOException, URIValidationException, ServiceException {
            var overseasEntityApi = new OverseasEntityDataApi();
            var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(
                    Collections.emptyList());
            var boDataListApiResponse = new ApiResponse<PrivateBoDataListApi>(404, null);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
            when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

            when(privateBeneficialOwnerGet.execute()).thenReturn(boDataListApiResponse);

            retrievePrivateData();
            verify(apiClientService, times(3)).getInternalApiClient();
        }

        @Test
        void testGetPrivateOverseasEntityDataNotFound()
                throws IOException, URIValidationException, ServiceException {
            var privateBoDataListApi = new PrivateBoDataListApi(Collections.emptyList());
            var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(
                    Collections.emptyList());

            var oeDataListApiResponse = new ApiResponse<OverseasEntityDataApi>(404, null);

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(privateBoDataListApi);

            when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
            when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

            when(privateOverseasEntityDataGet.execute()).thenReturn(oeDataListApiResponse);

            retrievePrivateData();
            verify(apiClientService, times(3)).getInternalApiClient();
        }

        @Test
        void testGetPrivateManagingOfficerDataNotFound()
                throws IOException, URIValidationException, ServiceException {

            var privateBoDataListApi = new PrivateBoDataListApi(Collections.emptyList());
            var overseasEntityApi = new OverseasEntityDataApi();

            var moDataListApiResponse = new ApiResponse<ManagingOfficerListDataApi>(404, null);

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(privateBoDataListApi);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateManagingOfficerDataGet.execute()).thenReturn(moDataListApiResponse);

            retrievePrivateData();
            verify(apiClientService, times(3)).getInternalApiClient();
        }

        @Test
        void testGetPrivateDataIsSuccessful()
                throws IOException, URIValidationException, ServiceException {

            var overseasEntityApi = new OverseasEntityDataApi();

            List<PrivateBoDataApi> privateBoDataApiList = Collections.emptyList();
            var boDataListApi = new PrivateBoDataListApi(privateBoDataApiList);

            List<ManagingOfficerDataApi> managingOfficerDataApiList = Collections.emptyList();
            var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(
                    managingOfficerDataApiList);

            when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
            when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

            retrievePrivateData();
            verify(apiClientService, times(3)).getInternalApiClient();
        }

        @Test
        void testGettersForBoMoEntityData()
                throws ServiceException, ApiErrorResponseException, URIValidationException, JsonProcessingException {

            var objectMapper = new ObjectMapper();

            var overseasEntityApi = new OverseasEntityDataApi();

            //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
            var boDataListApi = objectMapper.readValue(
                    PrivateBeneficialOwnersMock.jsonBeneficialOwnerString,
                    PrivateBoDataListApi.class);
            var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString,
                    ManagingOfficerListDataApi.class);

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
            when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

            assertNotNull(privateDataRetrievalService.getBeneficialOwnersData(COMPANY_NUMBER));
            assertNotNull(privateDataRetrievalService.getOverseasEntityData(COMPANY_NUMBER));
            assertNotNull(privateDataRetrievalService.getManagingOfficerData(COMPANY_NUMBER));
        }

        @Test
        void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsApiErrorResponseException()
                throws IOException, URIValidationException, ServiceException {
            var objectMapper = new ObjectMapper();

            var overseasEntityApi = new OverseasEntityDataApi();

            //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
            var boDataListApi = objectMapper.readValue(
                    PrivateBeneficialOwnersMock.jsonBeneficialOwnerString,
                    PrivateBoDataListApi.class);
            var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString,
                    ManagingOfficerListDataApi.class);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
            when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

            when(privateBeneficialOwnerGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

            assertNotNull(privateDataRetrievalService.getOverseasEntityData(COMPANY_NUMBER));
            assertEquals(privateDataRetrievalService.getBeneficialOwnersData(COMPANY_NUMBER),
                    new PrivateBoDataListApi(Collections.emptyList()));
            assertNotNull(privateDataRetrievalService.getManagingOfficerData(COMPANY_NUMBER));
        }

        @Test
        void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsApiErrorResponseException()
                throws IOException, URIValidationException, ServiceException {
            var objectMapper = new ObjectMapper();

            var overseasEntityApi = new OverseasEntityDataApi();

            //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
            var boDataListApi = objectMapper.readValue(
                    PrivateBeneficialOwnersMock.jsonBeneficialOwnerString,
                    PrivateBoDataListApi.class);
            var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString,
                    ManagingOfficerListDataApi.class);

            when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
            when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

            when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
            when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

            when(privateManagingOfficerDataGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

            assertNotNull(privateDataRetrievalService.getBeneficialOwnersData(COMPANY_NUMBER));
            assertNotNull(privateDataRetrievalService.getOverseasEntityData(COMPANY_NUMBER));
            assertEquals(privateDataRetrievalService.getManagingOfficerData(COMPANY_NUMBER),
                    new ManagingOfficerListDataApi(Collections.emptyList()));
        }
    }

    @Nested
    class TrustDataTests {

        @BeforeEach
        public void init() throws IOException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            when(apiClient.privateTrustDetailsResourceHandler()).thenReturn(
                    privateTrustDetailsResourceHandler);
            when(privateTrustDetailsResourceHandler.getTrustDetails(
                    Mockito.anyString())).thenReturn(privateTrustDetailsGet);
        }

        @Test
        void testGetTrustDetailsIsSuccessful()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            List<PrivateTrustDetailsApi> trustDetails = List.of(new PrivateTrustDetailsApi());
            var trustDetailsList = new PrivateTrustDetailsListApi(trustDetails);

            when(privateTrustDetailsGet.execute()).thenReturn(privateTrustDetailsDataResponse);
            when(privateTrustDetailsDataResponse.getData()).thenReturn(trustDetailsList);

            var result = privateDataRetrievalService.getTrustDetails((COMPANY_NUMBER));

            verify(apiClientService, times(1)).getInternalApiClient();
            assertEquals(1, result.getData().size());
        }

        @Test
        void testGetTrustDetailsReturnsNullTrustList()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            PrivateTrustDetailsListApi trustDetailsList = null;

            when(privateTrustDetailsGet.execute()).thenReturn(privateTrustDetailsDataResponse);
            when(privateTrustDetailsDataResponse.getData()).thenReturn(trustDetailsList);

            var result = privateDataRetrievalService.getTrustDetails((COMPANY_NUMBER));

            verify(apiClientService, times(1)).getInternalApiClient();
            assertNull(result);
        }

        @Test
        void testGetTrustDetailsApiErrorResponseExceptionThrownNotFoundReturnsEmptyList()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            var exception = new ApiErrorResponseException(
                    new HttpResponseException.Builder(404, "notFound", new HttpHeaders()));
            when(privateTrustDetailsGet.execute()).thenThrow(exception);

            var result = privateDataRetrievalService.getTrustDetails((COMPANY_NUMBER));

            verify(apiClientService, times(1)).getInternalApiClient();
            assertEquals(0, result.getData().size());
        }

        @Test
        void testGetTrustDetailsApiErrorResponseExceptionThrownCausesServiceException()
                throws ApiErrorResponseException, URIValidationException {
            var exception = new ApiErrorResponseException(
                    new HttpResponseException.Builder(401, "unauthorised", new HttpHeaders()));
            when(privateTrustDetailsGet.execute()).thenThrow(exception);

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getTrustDetails((COMPANY_NUMBER));
            });
        }

        @Test
        void testGetTrustDetailsURIValidationExceptionThrown()
                throws ApiErrorResponseException, URIValidationException {
            when(privateTrustDetailsGet.execute()).thenThrow(new URIValidationException("Error"));

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getTrustDetails((COMPANY_NUMBER));
            });
        }
    }


    @Nested
    class CorporateTrusteeDataTests {

        private static final String HASHED_TRUST_ID = "hashedTrustId";
        private static final String NON_HASHED_TRUST_ID = "trustId";
        private static final String COMPANY_NUMBER = "companyNumber";

        @BeforeEach
        public void init() throws IOException, NoSuchAlgorithmException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            privateDataRetrievalService.setHashHelper(hashHelper);

            lenient().when(hashHelper.encode(NON_HASHED_TRUST_ID)).thenReturn(HASHED_TRUST_ID);

            outputStreamCaptor = new ByteArrayOutputStream();
        }


        @AfterEach
        void tearDown() {
            outputStreamCaptor.reset();
            System.setOut(System.out);
        }

        private void trustDetailsStubbings()
                throws ApiErrorResponseException, URIValidationException {
            var details = new PrivateTrustDetailsApi();
            details.setId(NON_HASHED_TRUST_ID);

            List<PrivateTrustDetailsApi> trustDetails = List.of(details);
            var trustDetailsList = new PrivateTrustDetailsListApi(trustDetails);

            when(privateTrustDetailsGet.execute()).thenReturn(privateTrustDetailsDataResponse);
            when(privateTrustDetailsDataResponse.getData()).thenReturn(trustDetailsList);
            when(apiClient.privateTrustDetailsResourceHandler()).thenReturn(
                    privateTrustDetailsResourceHandler);
            when(privateTrustDetailsResourceHandler.getTrustDetails(
                    Mockito.anyString())).thenReturn(privateTrustDetailsGet);
        }


        private void corporateTrusteesStubbing(
                List<PrivateCorporateTrusteeApi> corporateTrusteesList)
                throws ApiErrorResponseException, URIValidationException {

            lenient().when(privateCorporateTrusteeGet.execute()).thenReturn(
                    privateCorporateTrusteeDataResponse);
            lenient().when(privateCorporateTrusteeDataResponse.getData())
                    .thenReturn(new PrivateCorporateTrusteeListApi(corporateTrusteesList));

            lenient().when(apiClient.privateCorporateTrusteeDataResourceHandler()).thenReturn(
                    privateCorporateTrusteeDataResourceHandler);
            lenient().when(privateCorporateTrusteeDataResourceHandler.getCorporateTrusteeData(
                    Mockito.anyString())).thenReturn(privateCorporateTrusteeGet);
        }


        @Test
        void testGetCorporateTrusteesIsSuccessful()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();

            var privateCorporateTrustee = new PrivateCorporateTrusteeApi();
            privateCorporateTrustee.setId("123");
            List<PrivateCorporateTrusteeApi> corporateTrustees = List.of(privateCorporateTrustee);
            corporateTrusteesStubbing(corporateTrustees);

            var result = privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID,
                    COMPANY_NUMBER);

            verify(apiClientService, times(2)).getInternalApiClient();
            assertEquals(1, result.getData().size());
        }


        @Test
        void testGetCorporateTrusteesReturnsNullTrusteeList()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();
            corporateTrusteesStubbing(null);

            var result = privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID,
                    COMPANY_NUMBER);

            verify(apiClientService, times(2)).getInternalApiClient();
            assertNull(result.getData());
        }

        @Test
        void testGetCorporateTrusteesApiErrorResponseExceptionThrownNotFoundReturnsEmptyList()
                throws ApiErrorResponseException, URIValidationException, ServiceException {

            trustDetailsStubbings();

            var privateCorporateTrustee = new PrivateCorporateTrusteeApi();
            privateCorporateTrustee.setId("123");
            List<PrivateCorporateTrusteeApi> corporateTrustees = List.of(privateCorporateTrustee);
            corporateTrusteesStubbing(corporateTrustees);

            when(privateCorporateTrusteeGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

            System.setOut(new PrintStream(outputStreamCaptor));

            var result = privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID,
                    COMPANY_NUMBER);

            assertEquals(0, result.getData().size());
            assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                    "No Corporate Trustee found for Trust Id " + HASHED_TRUST_ID));

        }

        @Test
        void testGetCorporateTrusteesApiErrorResponseExceptionThrownCausesServiceException()
                throws ApiErrorResponseException, URIValidationException, ServiceException {

            trustDetailsStubbings();

            var privateCorporateTrustee = new PrivateCorporateTrusteeApi();
            privateCorporateTrustee.setId("123");
            List<PrivateCorporateTrusteeApi> corporateTrustees = List.of(privateCorporateTrustee);
            corporateTrusteesStubbing(corporateTrustees);

            var exception = new ApiErrorResponseException(
                    new HttpResponseException.Builder(401, "unauthorised", new HttpHeaders()));
            
            when(privateCorporateTrusteeGet.execute()).thenThrow(exception);

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });
        }

        @Test
        void testGetCorporateTrusteesURIValidationExceptionThrown()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();

            var privateCorporateTrustee = new PrivateCorporateTrusteeApi();
            privateCorporateTrustee.setId("123");
            List<PrivateCorporateTrusteeApi> corporateTrustees = List.of(privateCorporateTrustee);
            corporateTrusteesStubbing(corporateTrustees);

            when(privateCorporateTrusteeGet.execute()).thenThrow(new URIValidationException("Error"));

            System.setOut(new PrintStream(outputStreamCaptor));

            ServiceException thrown =assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });

            assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Error Retrieving Corporate Trustee data for Trust Id " + HASHED_TRUST_ID));
            assertEquals("Error", thrown.getMessage());
        }

        @Test
        void testGetCorporateTrusteesHashHelperThrowsError()
                throws ApiErrorResponseException, URIValidationException, NoSuchAlgorithmException {
            trustDetailsStubbings();

            var privateCorporateTrustee = new PrivateCorporateTrusteeApi();
            privateCorporateTrustee.setId("123");
            List<PrivateCorporateTrusteeApi> corporateTrustees = List.of(privateCorporateTrustee);
            corporateTrusteesStubbing(corporateTrustees);

            when(hashHelper.encode(anyString())).thenThrow(new NoSuchAlgorithmException("Error"));

            ServiceException thrown =assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });

            assertEquals("Cannot encode ID", thrown.getMessage());
        }


        @Test
        void testGetCorporateTrusteesNoMatchingId()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();

            var privateCorporateTrustee = new PrivateCorporateTrusteeApi();
            privateCorporateTrustee.setId("123");
            when(privateDataRetrievalService.findMatchingId(HASHED_TRUST_ID, COMPANY_NUMBER)).thenReturn("matchingId");

            List<PrivateCorporateTrusteeApi> corporateTrustees = List.of(privateCorporateTrustee);
            corporateTrusteesStubbing(corporateTrustees);

            System.setOut(new PrintStream(outputStreamCaptor));

            ServiceException thrown =assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getCorporateTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });

            assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(), "Non-hashed ID could not be found for Hashed ID: " + HASHED_TRUST_ID));
            assertEquals("Non-hashed ID could not be found for Hashed ID: " + HASHED_TRUST_ID, thrown.getMessage());

        }
    }

    @Nested
    class TrustLinkDataTests {

        @BeforeEach
        public void init() throws IOException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            when(apiClient.privateTrustLinksResourceHandler()).thenReturn(
                    privateTrustLinksResourceHandler);
            when(privateTrustLinksResourceHandler.getTrustLinks(
                    Mockito.anyString())).thenReturn(privateTrustLinksGet);
        }
        @Test
        void testGetTrustLinksIsSuccessful() throws ApiErrorResponseException, URIValidationException, ServiceException {
            List<PrivateTrustLinksApi> trustLinks = List.of(new PrivateTrustLinksApi());
            var trustLinksList = new PrivateTrustLinksListApi(trustLinks);

            when(privateTrustLinksGet.execute()).thenReturn(privateTrustLinksDataResponse);
            when(privateTrustLinksDataResponse.getData()).thenReturn(trustLinksList);

            var result = privateDataRetrievalService.getTrustLinks((COMPANY_NUMBER));

            verify(apiClientService, times(1)).getInternalApiClient();
            assertEquals(1, result.getData().size());
        }

        @Test
        void testGetTrustLinksReturnsNullTrustList() throws ApiErrorResponseException, URIValidationException, ServiceException {
            PrivateTrustLinksListApi trustLinksList = null;

            when(privateTrustLinksGet.execute()).thenReturn(privateTrustLinksDataResponse);
            when(privateTrustLinksDataResponse.getData()).thenReturn(trustLinksList);

            var result = privateDataRetrievalService.getTrustLinks((COMPANY_NUMBER));

            verify(apiClientService, times(1)).getInternalApiClient();
            assertNull(result);
        }

        @Test
        void testGetTrustLinksApiErrorResponseExceptionThrownNotFoundReturnsEmptyList() throws ApiErrorResponseException, URIValidationException, ServiceException {
            when(privateTrustLinksGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

            var result = privateDataRetrievalService.getTrustLinks((COMPANY_NUMBER));

            verify(apiClientService, times(1)).getInternalApiClient();
            assertEquals(0, result.getData().size());
        }

        @Test
        void testGetTrustLinksApiErrorResponseExceptionThrownCausesServiceException() throws ApiErrorResponseException, URIValidationException, ServiceException {
            var exception = new ApiErrorResponseException(
                    new HttpResponseException.Builder(401, "unauthorised", new HttpHeaders()));
            when(privateTrustLinksGet.execute()).thenThrow(exception);

            assertThrows(
                    ServiceException.class,
                    () -> {
                        privateDataRetrievalService.getTrustLinks((COMPANY_NUMBER));
                    });
        }
        @Test
        void testGetTrustLinksURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException, ServiceException {
            when(privateTrustLinksGet.execute()).thenThrow(new URIValidationException("Error"));

            assertThrows(
                    ServiceException.class,
                    () -> {
                        privateDataRetrievalService.getTrustLinks((COMPANY_NUMBER));
                    });
                }
        }

    class IndividualTrusteeDataTests {

        private static final String HASHED_TRUST_ID = "hashedTrustId";
        private static final String NON_HASHED_TRUST_ID = "trustId";
        private static final String COMPANY_NUMBER = "companyNumber";

        @BeforeEach
        public void init() throws IOException, NoSuchAlgorithmException {
            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

            privateDataRetrievalService.setHashHelper(hashHelper);

            lenient().when(hashHelper.encode(NON_HASHED_TRUST_ID)).thenReturn(HASHED_TRUST_ID);

            outputStreamCaptor = new ByteArrayOutputStream();
        }

        @AfterEach
        void tearDown() {
            outputStreamCaptor.reset();
            System.setOut(System.out);
        }

        private void trustDetailsStubbings()
                throws ApiErrorResponseException, URIValidationException {
            var details = new PrivateTrustDetailsApi();
            details.setId(NON_HASHED_TRUST_ID);

            List<PrivateTrustDetailsApi> trustDetails = List.of(details);
            var trustDetailsList = new PrivateTrustDetailsListApi(trustDetails);

            when(privateTrustDetailsGet.execute()).thenReturn(privateTrustDetailsDataResponse);
            when(privateTrustDetailsDataResponse.getData()).thenReturn(trustDetailsList);
            when(apiClient.privateTrustDetailsResourceHandler()).thenReturn(
                    privateTrustDetailsResourceHandler);
            when(privateTrustDetailsResourceHandler.getTrustDetails(
                    Mockito.anyString())).thenReturn(privateTrustDetailsGet);
        }

        private void individualTrusteesStubbing(
                List<PrivateIndividualTrusteeApi> individualTrusteesList)
                throws ApiErrorResponseException, URIValidationException {

            lenient().when(privateIndividualTrusteeGet.execute()).thenReturn(
                    privateIndividualTrusteeDataResponse);
            lenient().when(privateIndividualTrusteeDataResponse.getData())
                    .thenReturn(new PrivateIndividualTrusteeListApi(individualTrusteesList));

            lenient().when(apiClient.privateIndividualTrusteeDataResourceHandler()).thenReturn(
                    privateIndividualTrusteeDataResourceHandler);
            lenient().when(privateIndividualTrusteeDataResourceHandler.getIndividualTrusteeData(
                    Mockito.anyString())).thenReturn(privateIndividualTrusteeGet);
        }

        @Test
        void testGetIndividualTrusteesIsSuccessful()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();

            var privateIndividualTrustee = new PrivateIndividualTrusteeApi();
            privateIndividualTrustee.setId("123");
            List<PrivateIndividualTrusteeApi> individualTrustees = List.of(privateIndividualTrustee);
            individualTrusteesStubbing(individualTrustees);

            var result = privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID,
                    COMPANY_NUMBER);

            verify(apiClientService, times(2)).getInternalApiClient();
            assertEquals(1, result.getData().size());
        }

        void testGetIndividualTrusteesReturnsNullTrusteeList()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();
            individualTrusteesStubbing(null);

            var result = privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID,
                    COMPANY_NUMBER);

            verify(apiClientService, times(2)).getInternalApiClient();
            assertNull(result.getData());
        }

        @Test
        void testGetIndividualTrusteesApiErrorResponseExceptionThrownNotFoundReturnsEmptyList()
                throws ApiErrorResponseException, URIValidationException, ServiceException {

            trustDetailsStubbings();

            var privateIndividualTrustee = new PrivateIndividualTrusteeApi();
            privateIndividualTrustee.setId("123");
            List<PrivateIndividualTrusteeApi> individualTrustees = List.of(privateIndividualTrustee);
            individualTrusteesStubbing(individualTrustees);

            when(privateIndividualTrusteeGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

            System.setOut(new PrintStream(outputStreamCaptor));

            var result = privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID,
                    COMPANY_NUMBER);

            assertEquals(0, result.getData().size());
            assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                    "No Individual Trustee found for Trust Id " + HASHED_TRUST_ID));
        }

        @Test
        void testGetIndividualTrusteesApiErrorResponseExceptionThrownCausesServiceException()
                throws ApiErrorResponseException, URIValidationException, ServiceException {

            trustDetailsStubbings();

            var privateIndividualTrustee = new PrivateIndividualTrusteeApi();
            privateIndividualTrustee.setId("123");
            List<PrivateIndividualTrusteeApi> individualTrustees = List.of(privateIndividualTrustee);
            individualTrusteesStubbing(individualTrustees);

            var exception = new ApiErrorResponseException(
                    new HttpResponseException.Builder(401, "unauthorised", new HttpHeaders()));

            when(privateIndividualTrusteeGet.execute()).thenThrow(exception);

            assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });
        }

        @Test
        void testGetIndividualTrusteesURIValidationExceptionThrown()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();

            var privateIndividualTrustee = new PrivateIndividualTrusteeApi();
            privateIndividualTrustee.setId("123");
            List<PrivateIndividualTrusteeApi> individualTrustees = List.of(privateIndividualTrustee);
            individualTrusteesStubbing(individualTrustees);

            when(privateIndividualTrusteeGet.execute()).thenThrow(new URIValidationException("Error"));

            System.setOut(new PrintStream(outputStreamCaptor));

            ServiceException thrown =assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });

            assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(), "Error Retrieving Individual Trustee data for Trust Id " + HASHED_TRUST_ID));
            assertEquals("Error", thrown.getMessage());
        }

        @Test
        void testGetIndividualTrusteesHashHelperThrowsError()
                throws ApiErrorResponseException, URIValidationException, NoSuchAlgorithmException {
            trustDetailsStubbings();

            var privateIndividualTrustee = new PrivateIndividualTrusteeApi();
            privateIndividualTrustee.setId("123");
            List<PrivateIndividualTrusteeApi> individualTrustees = List.of(privateIndividualTrustee);
            individualTrusteesStubbing(individualTrustees);

            when(hashHelper.encode(anyString())).thenThrow(new NoSuchAlgorithmException("Error"));

            ServiceException thrown =assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });

            assertEquals("Cannot encode ID", thrown.getMessage());
        }

        @Test
        void testGetIndividualTrusteesNoMatchingId()
                throws ApiErrorResponseException, URIValidationException, ServiceException {
            trustDetailsStubbings();

            var privateIndividualTrustee = new PrivateIndividualTrusteeApi();
            privateIndividualTrustee.setId("123");
            when(privateDataRetrievalService.findMatchingId(HASHED_TRUST_ID, COMPANY_NUMBER)).thenReturn("matchingId");

            List<PrivateIndividualTrusteeApi> individualTrustees = List.of(privateIndividualTrustee);
            individualTrusteesStubbing(individualTrustees);

            System.setOut(new PrintStream(outputStreamCaptor));

            ServiceException thrown =assertThrows(ServiceException.class, () -> {
                privateDataRetrievalService.getIndividualTrustees(HASHED_TRUST_ID, COMPANY_NUMBER);
            });

            assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(), "Non-hashed ID could not be found for Hashed ID: " + HASHED_TRUST_ID));
            assertEquals("Non-hashed ID could not be found for Hashed ID: " + HASHED_TRUST_ID, thrown.getMessage());
        }
    }
}

