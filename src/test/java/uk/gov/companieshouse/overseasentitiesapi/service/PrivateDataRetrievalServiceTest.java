package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseException.Builder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.beneficialowner.PrivateBeneficialOwnerResourceHandler;
import uk.gov.companieshouse.api.handler.beneficialowner.request.PrivateBeneficialOwnerGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.managingofficerdata.PrivateManagingOfficerDataResourceHandler;
import uk.gov.companieshouse.api.handler.managingofficerdata.request.PrivateManagingOfficerDataGet;
import uk.gov.companieshouse.api.handler.update.PrivateOverseasEntityDataHandler;
import uk.gov.companieshouse.api.handler.update.request.PrivateOverseasEntityDataGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
class PrivateDataRetrievalServiceTest {
  // Note: Mocks for these tests are dependent on the order of the calls in PrivateDataRetrievalService::initialisePrivateData
  // If the order of the calls changes, the mocks will need to be updated accordingly
  // Tests on a particular method needs the mocks for the previous methods to be set up
  private static final String COMPANY_NUMBER = "OE123456";

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

  private static final ApiErrorResponseException FourHundredAndFourException = ApiErrorResponseException.fromHttpResponseException(
      new HttpResponseException.Builder(404, "ERROR", new HttpHeaders()).build());

  private final String jsonBeneficialOwnerString = "["
          + "{"
          + "\"id\":\"9001808903\","
          + "\"date_became_registrable\":\"2023-04-13 00:00:00.0\","
          + "\"is_service_address_same_as_usual_address\":\"N\","
          + "\"date_of_birth\":\"2023-04-13 00:00:00.0\","
          + "\"usual_residential_address\":{"
          + "\"address_line_1\":\"491 SOUTH GREEN OLD BOULEVARD\","
          + "\"address_line_2\":\"SIT NON PRAESENTIUM\","
          + "\"care_of\":null,"
          + "\"country\":\"ITALY\","
          + "\"locality\":\"EXPEDITA IN OFFICIIS\","
          + "\"po_box\":null,"
          + "\"postal_code\":\"29765\","
          + "\"premises\":\"DUSTIN MONTOYA\","
          + "\"region\":\"ULLAM AUTEM IMPEDIT\""
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
          + "}"
          + "},"
          + "{"
          + "\"id\":\"9001808904\","
          + "\"date_became_registrable\":\"2023-04-13 00:00:00.0\","
          + "\"is_service_address_same_as_usual_address\":\"N\","
          + "\"date_of_birth\":null,"
          + "\"usual_residential_address\":{"
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
          + "\"address_line_1\":\"204 NOBEL DRIVE\","
          + "\"address_line_2\":\"ASPERIORES VOLUPTATE\","
          + "\"care_of\":null,"
          + "\"country\":\"MACEDONIA\","
          + "\"locality\":\"VELIT UT FACILIS VE\","
          + "\"po_box\":null,"
          + "\"postal_code\":\"41608\","
          + "\"premises\":\"MEGAN WILDER\","
          + "\"region\":\"ID AUT OFFICIA CUPI\""
          + "}"
          + "},"
          + "{"
          + "\"id\":\"9001808905\","
          + "\"date_became_registrable\":\"2023-04-13 00:00:00.0\","
          + "\"is_service_address_same_as_usual_address\":\"N\","
          + "\"date_of_birth\":null,"
          + "\"usual_residential_address\":{"
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
          + "\"address_line_1\":\"55 GREEN COWLEY PARKWAY\","
          + "\"address_line_2\":\"DISTINCTIO FACILIS\","
          + "\"care_of\":null,"
          + "\"country\":\"MAURITIUS\","
          + "\"locality\":\"CULPA EOS ENIM QUI\","
          + "\"po_box\":null,"
          + "\"postal_code\":\"90079\","
          + "\"premises\":\"CORA BENTON\","
          + "\"region\":\"IN VELIT ET EIUS MIN\""
          + "}"
          + "}"
          + "]";

  private final String jsonManagingOfficerString = "[" +
          "{" +
          "\"managingOfficerId\":\"9001808986\"," +
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
          "\"managingOfficerId\":\"9001808987\"," +
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
            privateDataRetrievalService.initialisePrivateData((COMPANY_NUMBER));
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
            privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
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
            privateDataRetrievalService.initialisePrivateData((COMPANY_NUMBER));
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
            privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
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

      // Beneficial Owner Data needs to be mocked for the Overseas Entities tests
      when(apiClient.privateBeneficialOwnerResourceHandler()).thenReturn(
          privateBeneficialOwnerResourceHandler);
      when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(
          Mockito.anyString())).thenReturn(privateBeneficialOwnerGet);
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntitiesPrivateDataThrowsURIValidationException()
        throws IOException, URIValidationException {
      var privateBoDataListApi = new PrivateBoDataListApi(Collections.emptyList());

      when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
      when(apiBoDataListGetResponse.getData()).thenReturn(privateBoDataListApi);

      when(privateOverseasEntityDataGet.execute()).thenThrow(new URIValidationException("ERROR"));

      assertThrows(
          ServiceException.class,
          () -> {
            privateDataRetrievalService.initialisePrivateData((COMPANY_NUMBER));
          });
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntitiesPrivateDataThrowsIOException()
        throws IOException, URIValidationException {
      var privateBoDataListApi = new PrivateBoDataListApi(Collections.emptyList());

      when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
      when(apiBoDataListGetResponse.getData()).thenReturn(privateBoDataListApi);

      when(privateOverseasEntityDataGet.execute())
          .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

      assertThrows(
          ServiceException.class,
          () -> {
            privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
          });
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
    void testGetPrivateDataBeneficialOwnerDataNotFound()
        throws IOException, URIValidationException, ServiceException {
      var overseasEntityApi = new OverseasEntityDataApi();
      var managingOfficerDataApiListApi = new ManagingOfficerListDataApi(Collections.emptyList());
      var boDataListApiResponse = new ApiResponse<PrivateBoDataListApi>(404, null);

      when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
      when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

      when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
      when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

      when(privateBeneficialOwnerGet.execute()).thenReturn(boDataListApiResponse);

      privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
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

      privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
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

      privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
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

      privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);
      verify(apiClientService, times(3)).getInternalApiClient();
    }

    @Test
    void testGettersForBoMoEntityData()
            throws ServiceException, ApiErrorResponseException, URIValidationException, JsonProcessingException {

      var objectMapper = new ObjectMapper();

      var overseasEntityApi = new OverseasEntityDataApi();

      //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
      var boDataListApi = objectMapper.readValue(jsonBeneficialOwnerString, PrivateBoDataListApi.class );
      var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString, ManagingOfficerListDataApi.class );

      when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
      when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

      when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
      when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

      when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
      when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

      privateDataRetrievalService.initialisePrivateData(COMPANY_NUMBER);

      assertNotNull(privateDataRetrievalService.getBeneficialOwnerData());
      assertNotNull(privateDataRetrievalService.getOverseasEntityData());
      assertNotNull(privateDataRetrievalService.getManagingOfficerData());
    }

    @Test
    void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsApiErrorResponseException()
        throws IOException, URIValidationException, ServiceException {
      var objectMapper = new ObjectMapper();

      var overseasEntityApi = new OverseasEntityDataApi();

      //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
      var boDataListApi = objectMapper.readValue(jsonBeneficialOwnerString, PrivateBoDataListApi.class );
      var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString, ManagingOfficerListDataApi.class );

      when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
      when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

      when(privateManagingOfficerDataGet.execute()).thenReturn(managingOfficerDataResponse);
      when(managingOfficerDataResponse.getData()).thenReturn(managingOfficerDataApiListApi);

      when(privateBeneficialOwnerGet.execute()).thenThrow(FourHundredAndFourException);

      privateDataRetrievalService.initialisePrivateData((COMPANY_NUMBER));

      assertEquals(privateDataRetrievalService.getBeneficialOwnerData(), new PrivateBoDataListApi(Collections.emptyList()));
    }

    @Test
    void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsApiErrorResponseException()
        throws IOException, URIValidationException, ServiceException {
      var objectMapper = new ObjectMapper();

      var overseasEntityApi = new OverseasEntityDataApi();

      //Note: If relevant models change in private-api-sdk-java then these tests may fail. Update local JSON to reflect changes in private-api-sdk-java.
      var boDataListApi = objectMapper.readValue(jsonBeneficialOwnerString, PrivateBoDataListApi.class );
      var managingOfficerDataApiListApi = objectMapper.readValue(jsonManagingOfficerString, ManagingOfficerListDataApi.class );

      when(privateOverseasEntityDataGet.execute()).thenReturn(overseasEntityDataResponse);
      when(overseasEntityDataResponse.getData()).thenReturn(overseasEntityApi);

      when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
      when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

      when(privateManagingOfficerDataGet.execute()).thenThrow(FourHundredAndFourException);

      privateDataRetrievalService.initialisePrivateData((COMPANY_NUMBER));

      assertEquals(privateDataRetrievalService.getManagingOfficerData(), new ManagingOfficerListDataApi(Collections.emptyList()));
    }
  }
}
