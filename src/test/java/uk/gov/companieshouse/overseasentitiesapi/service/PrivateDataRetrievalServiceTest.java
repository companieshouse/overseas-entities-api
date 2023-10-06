package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.gov.companieshouse.api.handler.trusts.PrivateTrustDetailsResourceHandler;
import uk.gov.companieshouse.api.handler.trusts.request.PrivateTrustDetailsGet;
import uk.gov.companieshouse.api.handler.update.PrivateOverseasEntityDataHandler;
import uk.gov.companieshouse.api.handler.update.request.PrivateOverseasEntityDataGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.corporatetrustee.PrivateCorporateTrusteeApi;
import uk.gov.companieshouse.api.model.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PrivateBeneficialOwnersMock;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

@ExtendWith(MockitoExtension.class)
class PrivateDataRetrievalServiceTest {

  private static final String COMPANY_NUMBER = "OE123456";

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
  private PrivateCorporateTrusteesResourceHandler privateCorporateTrusteesResourceHandler;

  @Mock
  private PrivateCorporateTrusteesGet privateCorporateTrusteesGet;

  @Mock
  private ApiResponse<PrivateCorporateTrusteeListApi> apiCorporateTrusteeListGetResponse;

  @Mock
  private PrivateTrustDetailsResourceHandler privateTrustDetailsResourceHandler;

  @Mock
  private PrivateTrustDetailsGet privateTrustDetailsGet;

  @Mock
  private ApiResponse<PrivateTrustDetailsListApi> apiTrustDetailsGetResponse;

  @Mock
  private HashHelper hashHelper;

  private static final ApiErrorResponseException FOUR_HUNDRED_AND_FOUR_EXCEPTION = ApiErrorResponseException.fromHttpResponseException(
      new HttpResponseException.Builder(404, "ERROR", new HttpHeaders()).build());

  private final String jsonManagingOfficerString = "[" +
          "{" +
          "\"managingOfficerAppointmentId\":\"9001808986\"," +
          "\"residential_address\":{" +
          "\"address_line_1\":\"32 WHITE FIRST ROAD\"," +
          "\"address_line_2\":\"QUOS ADIPISCI OFFICI\"," +
          "\"care_of\":null," +
          "\"country\":\"HUNGARY\"," +
          "\"locality\":\"QUI QUIS EST MAIORE\"," +
          "\"po_box\":null," +
          "\"postal_code\":\"51291\"," +
          "\"premises\":\"DARRYL VALENTINE\"," +
          "\"region\":\"CILLUM EXERCITATION\"" +
          "}," +
          "\"principal_address\":{" +
          "\"address_line_1\":null," +
          "\"address_line_2\":null," +
          "\"care_of\":null," +
          "\"country\":null," +
          "\"locality\":null," +
          "\"po_box\":null," +
          "\"postal_code\":null," +
          "\"premises\":null," +
          "\"region\":null" +
          "}," +
          "\"date_of_birth\":\"2023-01-01 00:00:00.0\"," +
          "\"contact_name_full\":null," +
          "\"contact_email_address\":null" +
          "}," +
          "{" +
          "\"managingOfficerAppointmentId\":\"9001808987\"," +
          "\"residential_address\":{" +
          "\"address_line_1\":null," +
          "\"address_line_2\":null," +
          "\"care_of\":null," +
          "\"country\":null," +
          "\"locality\":null," +
          "\"po_box\":null," +
          "\"postal_code\":null," +
          "\"premises\":null," +
          "\"region\":null" +
          "}," +
          "\"principal_address\":{" +
          "\"address_line_1\":\"29 SOUTH MILTON ROAD\"," +
          "\"address_line_2\":\"TENETUR QUI VOLUPTAT\"," +
          "\"care_of\":null," +
          "\"country\":\"ICELAND\"," +
          "\"locality\":\"QUIA ELIGENDI DOLORE\"," +
          "\"po_box\":null," +
          "\"postal_code\":\"77761\"," +
          "\"premises\":\"WYNNE GARCIA\"," +
          "\"region\":\"VERO CORRUPTI IPSUM\"" +
          "}," +
          "\"date_of_birth\":\"9999-12-31 00:00:00.0\"," +
          "\"contact_name_full\":\"TYLER DELEON\"," +
          "\"contact_email_address\":\"QEZUX@MAILINATOR.COM\"" +
          "}" +
          "]";

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
    }

    @Test
    void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsURIValidationException()
        throws IOException, URIValidationException {
      when(privateBeneficialOwnerGet.execute()).thenThrow(new URIValidationException("ERROR"));

      assertThrows(
          ServiceException.class,
          () -> {
            privateDataRetrievalService.getBeneficialOwnersData((COMPANY_NUMBER));
          });
    }

    @Test
    void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsIOException()
        throws IOException, URIValidationException {

      when(privateBeneficialOwnerGet.execute())
          .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

      assertThrows(
          ServiceException.class,
          () -> {
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
      when(privateOverseasEntityDataHandler.getOverseasEntityData(Mockito.anyString())).thenReturn(
          privateOverseasEntityDataGet);

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

      when(privateManagingOfficerDataGet.execute()).thenThrow(new URIValidationException("ERROR"));

      assertThrows(
          ServiceException.class,
          () -> {
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

      when(privateManagingOfficerDataGet.execute())
          .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

      assertThrows(
          ServiceException.class,
          () -> {
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
      when(privateOverseasEntityDataHandler.getOverseasEntityData(Mockito.anyString())).thenReturn(
              privateOverseasEntityDataGet);
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntitiesOEPrivateDataThrowsURIValidationException()
        throws IOException, URIValidationException {

      when(privateOverseasEntityDataGet.execute()).thenThrow(new URIValidationException("ERROR"));

      assertThrows(
          ServiceException.class,
          () -> {
            privateDataRetrievalService.getOverseasEntityData((COMPANY_NUMBER));
          });
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntitiesOEPrivateDataThrowsNon404Error()
            throws IOException, URIValidationException {

      when(privateOverseasEntityDataGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException()));

      assertThrows(
              ServiceException.class,
              () -> {
                privateDataRetrievalService.getOverseasEntityData((COMPANY_NUMBER));
              });
    }

    @Test
    void testNoEmailWhenGetOverseasEntitiesPrivateDataThrowsApiResponseError404Exception()
        throws ApiErrorResponseException, URIValidationException, ServiceException {

      when(privateOverseasEntityDataGet.execute())
          .thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

      OverseasEntityDataApi result = privateDataRetrievalService.getOverseasEntityData(COMPANY_NUMBER);

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
      when(privateOverseasEntityDataHandler.getOverseasEntityData(Mockito.anyString())).thenReturn(
          privateOverseasEntityDataGet);

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
      var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(Collections.emptyList());
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
      var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(Collections.emptyList());

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
      var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(managingOfficerDataApiList);

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
      var boDataListApi = objectMapper.readValue(PrivateBeneficialOwnersMock.jsonBeneficialOwnerString, PrivateBoDataListApi.class );
      var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString, ManagingOfficerListDataApi.class );

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
      var boDataListApi = objectMapper.readValue(PrivateBeneficialOwnersMock.jsonBeneficialOwnerString, PrivateBoDataListApi.class );
      var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString, ManagingOfficerListDataApi.class );

      when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
      when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

      when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
      when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

      when(privateBeneficialOwnerGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

      assertNotNull(privateDataRetrievalService.getOverseasEntityData(COMPANY_NUMBER));
      assertEquals(privateDataRetrievalService.getBeneficialOwnersData(COMPANY_NUMBER), new PrivateBoDataListApi(Collections.emptyList()));
      assertNotNull(privateDataRetrievalService.getManagingOfficerData(COMPANY_NUMBER));
    }

    @Test
    void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsApiErrorResponseException()
        throws IOException, URIValidationException, ServiceException {
      var objectMapper = new ObjectMapper();

      var overseasEntityApi = new OverseasEntityDataApi();

      //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
      var boDataListApi = objectMapper.readValue(PrivateBeneficialOwnersMock.jsonBeneficialOwnerString, PrivateBoDataListApi.class );
      var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString, ManagingOfficerListDataApi.class );

      when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
      when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

      when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
      when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

      when(privateManagingOfficerDataGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

      assertNotNull(privateDataRetrievalService.getBeneficialOwnersData(COMPANY_NUMBER));
      assertNotNull(privateDataRetrievalService.getOverseasEntityData(COMPANY_NUMBER));
      assertEquals(privateDataRetrievalService.getManagingOfficerData(COMPANY_NUMBER), new ManagingOfficerListDataApi(Collections.emptyList()));
    }
  }

  @Nested
  class CorporateTrusteesTests {

    @BeforeEach
    public void setUp() throws IOException, URIValidationException {
      // Assuming that apiClientService, apiClient, and the other dependencies are
      // correctly mocked and injected in a similar manner as in the ManagingOfficerTests class
      when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
      var privateTrustDetailsListApi = new PrivateTrustDetailsListApi(Collections.emptyList());


      // Corporate Trustee Data Mocks
      when(apiClient.privateCorporateTrusteeDataResourceHandler()).thenReturn(
              privateCorporateTrusteesResourceHandler);
      when(privateCorporateTrusteesResourceHandler.getCorporateTrusteeData(Mockito.anyString())).thenReturn(
              privateCorporateTrusteesGet);


      when(apiClient.privateTrustDetailsResourceHandler()).thenReturn(
              privateTrustDetailsResourceHandler);
      when(privateTrustDetailsResourceHandler.getTrustDetails(Mockito.anyString())).thenReturn(
              privateTrustDetailsGet);
      when(privateTrustDetailsGet.execute()).thenReturn(apiTrustDetailsGetResponse);
      when(apiTrustDetailsGetResponse.getData()).thenReturn(privateTrustDetailsListApi);
      outputStreamCaptor = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
      outputStreamCaptor.reset();
      System.setOut(System.out);
    }

    @Test
    void testServiceExceptionThrownWhenGetCorporateTrusteesThrowsURIValidationException() throws IOException, URIValidationException {
      when(privateCorporateTrusteesGet.execute()).thenThrow(new URIValidationException("ERROR"));

      assertThrows(
              ServiceException.class,
              () -> {
                privateDataRetrievalService.getCorporateTrustees("hashedTrustId123", "companyNumber123");
              });
    }

    @Test
    void testServiceExceptionThrownWhenGetCorporateTrusteesThrowsIOException() throws IOException, URIValidationException {
      when(privateCorporateTrusteesGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

      assertThrows(
              ServiceException.class,
              () -> {
                privateDataRetrievalService.getCorporateTrustees("hashedTrustId123", "companyNumber123");
              });
    }

    @Test
    void testNoCorporateTrusteesFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
      var privateCorporateTrusteeListApi = new PrivateCorporateTrusteeListApi(Collections.emptyList());

      when(privateCorporateTrusteesGet.execute()).thenReturn(apiCorporateTrusteeListGetResponse);
      when(apiCorporateTrusteeListGetResponse.getData()).thenReturn(privateCorporateTrusteeListApi);

      PrivateCorporateTrusteeListApi result = privateDataRetrievalService.getCorporateTrustees("hashedTrustId123", "companyNumber123");

      assertTrue(result.getData().isEmpty());
    }

    @Test
    void testNoCorporateTrusteesFoundWith404StatusCode()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
      var privateCorporateTrusteeApi = new PrivateCorporateTrusteeApi();
      privateCorporateTrusteeApi.setId("1111");


      when(privateCorporateTrusteesGet.execute()).thenThrow(FOUR_HUNDRED_AND_FOUR_EXCEPTION);

      System.setOut(new PrintStream(outputStreamCaptor));

      PrivateCorporateTrusteeListApi result = privateDataRetrievalService.getCorporateTrustees("hashedTrustId123", "companyNumber123");

      assertEquals(0, result.getData().size());
      assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),"No Corporate Trustee found for Trust Id hashedTrustId123"));

    }

    @Test
    void testCorporateTrusteesFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

      var privateCorporateTrusteeApi = new PrivateCorporateTrusteeApi();
      privateCorporateTrusteeApi.setId("1111");

      var privateCorporateTrusteeListApi = new PrivateCorporateTrusteeListApi(List.of(privateCorporateTrusteeApi));

      when(privateCorporateTrusteesGet.execute()).thenReturn(apiCorporateTrusteeListGetResponse);
      when(apiCorporateTrusteeListGetResponse.getData()).thenReturn(privateCorporateTrusteeListApi);

      System.setOut(new PrintStream(outputStreamCaptor));

      PrivateCorporateTrusteeListApi result = privateDataRetrievalService.getCorporateTrustees("hashedTrustId123", "companyNumber123");

      assertEquals(1, result.getData().size());
      assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),"Retrieved 1 Trusts for Company Number hashedTrustId123"));

    }
  }

}
