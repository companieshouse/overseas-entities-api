package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.OfficersApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.service.PublicDataRetrievalService;

class PublicPrivateDataCombinerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  String pscJsonString = "{" +
      "\"links\": {" +
      "\"self\": \"/company/03681242/persons-with-significant-control/individual/Iq6hRNqa-Twx1eWhHv_FOwTb1i4\""
      +
      "}" +
      "}";

  String pscJsonString2 = "{" +
      "\"links\": {" +
      "\"self\": \"/company/03681242/persons-with-significant-control/individual/5hSqZI0z2kRZvJj_ZcfyUaC26Qo\""
      +
      "}" +
      "}";

  String officersApiString = "{" +
      "\"items\": [" +
      "{" +
      "\"links\": {" +
      "\"self\": \"/company/01913593/appointments/Iq6hRNqa-Twx1eWhHv_FOwTb1i4\"" +
      "}" +
      "}" +
      "]" +
      "}";

  String officersApiStringTwoItems = "{" +
      "\"items\": [" +
      "{" +
      "\"links\": {" +
      "\"self\": \"/company/01913593/appointments/Iq6hRNqa-Twx1eWhHv_FOwTb1i4\"" +
      "}" +
      "}," +
      "{" +
      "\"links\": {" +
      "\"self\": \"/company/01913593/appointments/5hSqZI0z2kRZvJj_ZcfyUaC26Qo\"" +
      "}" +
      "}" +
      "]" +
      "}";

  private final String COMPANY_NUMBER = "OE123456";

  private final String PASS_THROUGH_TOKEN_HEADER = "abc123";

  private PublicDataRetrievalService publicDataRetrievalService;
  private PrivateDataRetrievalService privateDataRetrievalService;
  private PublicPrivateDataCombiner publicPrivateDataCombiner;

  @BeforeEach
  public void setUp() {
    publicDataRetrievalService = Mockito.mock(PublicDataRetrievalService.class);
    privateDataRetrievalService = Mockito.mock(PrivateDataRetrievalService.class);
    publicPrivateDataCombiner = new PublicPrivateDataCombiner(publicDataRetrievalService,
        privateDataRetrievalService,
        "test_salt");
  }

  @Test
  void testBuildMergedOverseasEntityDataPair() throws ServiceException {
    CompanyProfileApi publicOE = new CompanyProfileApi();
    publicOE.setCompanyName("Test Public Company");
    OverseasEntityDataApi privateOE = new OverseasEntityDataApi();
    privateOE.setEmail("john@smith.com");

    when(publicDataRetrievalService.getCompanyProfile(any(), any())).thenReturn(publicOE);
    when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(privateOE);

    var result = publicPrivateDataCombiner.buildMergedOverseasEntityDataPair(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertEquals(publicOE, result.getLeft());
    assertEquals(privateOE, result.getRight());
  }

  /**
   * In this test we will be checking to see if the method to build a map of beneficial owner data
   * with the psc data <br>
   * Note: PscApi uses a hashed version of the ID whereas PrivateBoDataApi uses the unhashed version <br>
   * The value "12345" hashed is "Iq6hRNqa-Twx1eWhHv_FOwTb1i4"
   *
   * @throws ServiceException
   * @throws JsonProcessingException
   */
  @Test
  void testBuildMergedBeneficialOwnerDataMap()
      throws JsonProcessingException, ServiceException {
    // Prepare test data
    PrivateBoDataApi privateBoDataApi = new PrivateBoDataApi();
    privateBoDataApi.setPscId("12345");

    PscApi pscApi = objectMapper.readValue(pscJsonString, PscApi.class);

    PscsApi pscsApi = new PscsApi();
    pscsApi.setItems(Collections.singletonList(pscApi));

    // Configure mock behavior
    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(
        new PrivateBoDataListApi(List.of(privateBoDataApi)));
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(pscsApi);

    // Execute the method to test
    Map<String, Pair<PscApi, PrivateBoDataApi>> results = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    // Verify the result
    assertEquals(1, results.size());

    Pair<PscApi, PrivateBoDataApi> pair = results.get("Iq6hRNqa-Twx1eWhHv_FOwTb1i4");
    assertEquals(pscApi, pair.getLeft());
    assertEquals(privateBoDataApi, pair.getRight());

    // Verify mock interactions
    verify(publicDataRetrievalService, times(1)).getPSCs(any(), any());
    verify(privateDataRetrievalService, times(1)).getBeneficialOwnersData(any());
  }

  @Test
  void testBuildMergedManagingOfficerDataMap()
      throws JsonProcessingException, ServiceException {
    // Prepare test data
    ManagingOfficerDataApi managingOfficerDataApi = new ManagingOfficerDataApi();
    managingOfficerDataApi.setManagingOfficerAppointmentId("12345");

    OfficersApi officersApi = objectMapper.readValue(officersApiString, OfficersApi.class);
    CompanyOfficerApi companyOfficerApi = officersApi.getItems().getFirst();

    // Configure mock behavior
    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(
        new ManagingOfficerListDataApi(List.of(managingOfficerDataApi)));
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(officersApi);

    // Execute the method to test
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> results = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    // Verify the result
    assertEquals(1, results.size());

    Pair<CompanyOfficerApi, ManagingOfficerDataApi> pair = results.get(
        "Iq6hRNqa-Twx1eWhHv_FOwTb1i4");
    assertEquals(companyOfficerApi, pair.getLeft());
    assertEquals(managingOfficerDataApi, pair.getRight());

    // Verify mock interactions
    verify(publicDataRetrievalService, times(1)).getOfficers(any(), any());
    verify(privateDataRetrievalService, times(1)).getManagingOfficerData(any());
  }

  @Test
  void testLogCollatedData() throws ServiceException, JsonProcessingException {

    // Mocking for Managing Officer Data
    ManagingOfficerDataApi managingOfficerDataApi = new ManagingOfficerDataApi();
    managingOfficerDataApi.setManagingOfficerAppointmentId("12345");

    ManagingOfficerDataApi managingOfficerDataApi2 = new ManagingOfficerDataApi();
    managingOfficerDataApi2.setManagingOfficerAppointmentId("123456");

    OfficersApi officersApi = objectMapper.readValue(officersApiStringTwoItems, OfficersApi.class);

    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(
        new ManagingOfficerListDataApi(List.of(managingOfficerDataApi, managingOfficerDataApi2)));
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(officersApi);

    var mo = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    // Mocking for Beneficial Owner Data
    PrivateBoDataApi privateBoDataApi = new PrivateBoDataApi();
    privateBoDataApi.setPscId("12345");

    // Mocking for Beneficial Owner Data
    PrivateBoDataApi privateBoDataApi2 = new PrivateBoDataApi();
    privateBoDataApi2.setPscId("123456");

    PscApi pscApi = objectMapper.readValue(pscJsonString, PscApi.class);
    PscApi pscApi2 = objectMapper.readValue(pscJsonString2, PscApi.class);

    PscsApi pscsApi = new PscsApi();
    pscsApi.setItems(List.of(pscApi, pscApi2));

    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(
        new PrivateBoDataListApi(List.of(privateBoDataApi, privateBoDataApi2)));
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(pscsApi);

    var bo = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    // Mocking for Overseas Entity Data
    CompanyProfileApi publicOE = new CompanyProfileApi();
    publicOE.setCompanyName("Test Public Company");
    publicOE.setCompanyNumber("OE123456");
    OverseasEntityDataApi privateOE = new OverseasEntityDataApi();
    privateOE.setEmail("john@smith.com");

    when(publicDataRetrievalService.getCompanyProfile(any(), any())).thenReturn(publicOE);
    when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(privateOE);

    publicPrivateDataCombiner.buildMergedOverseasEntityDataPair(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    String expectedOutput = "{" +
        "\"Managing Officer Data\": [" +
        "{\"Public Hashed ID\": \"Iq6hRNqa-Twx1eWhHv_FOwTb1i4\", \"Private ID\": \"12345\"}, " +
        "{\"Public Hashed ID\": \"5hSqZI0z2kRZvJj_ZcfyUaC26Qo\", \"Private ID\": \"123456\"}" +
        "]," +
        "\"Beneficial Owner Data\": [" +
        "{\"Public Hashed ID\": \"Iq6hRNqa-Twx1eWhHv_FOwTb1i4\", \"Private ID\": \"12345\"}, " +
        "{\"Public Hashed ID\": \"5hSqZI0z2kRZvJj_ZcfyUaC26Qo\", \"Private ID\": \"123456\"}" +
        "]," +
        "\"Overseas Entity Data\": {" +
        "\"Public Entity Number\": \"OE123456\", \"Private Email\": \"john@smith.com\"" +
        "}" +
        "}";

    String actualOutput = publicPrivateDataCombiner.logCollatedData();

    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  void testLogCollatedDataWrongHash() throws ServiceException, JsonProcessingException {

    // Mocking for Managing Officer Data
    ManagingOfficerDataApi managingOfficerDataApi = new ManagingOfficerDataApi();
    managingOfficerDataApi.setManagingOfficerAppointmentId("12");

    ManagingOfficerDataApi managingOfficerDataApi2 = new ManagingOfficerDataApi();
    managingOfficerDataApi2.setManagingOfficerAppointmentId("123");

    OfficersApi officersApi = objectMapper.readValue(officersApiStringTwoItems, OfficersApi.class);

    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(
        new ManagingOfficerListDataApi(List.of(managingOfficerDataApi, managingOfficerDataApi2)));
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(officersApi);

    var mo = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    // Mocking for Beneficial Owner Data
    PrivateBoDataApi privateBoDataApi = new PrivateBoDataApi();
    privateBoDataApi.setPscId("12");

    // Mocking for Beneficial Owner Data
    PrivateBoDataApi privateBoDataApi2 = new PrivateBoDataApi();
    privateBoDataApi2.setPscId("123");

    PscApi pscApi = objectMapper.readValue(pscJsonString, PscApi.class);
    PscApi pscApi2 = objectMapper.readValue(pscJsonString2, PscApi.class);

    PscsApi pscsApi = new PscsApi();
    pscsApi.setItems(List.of(pscApi, pscApi2));

    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(
        new PrivateBoDataListApi(List.of(privateBoDataApi, privateBoDataApi2)));
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(pscsApi);

    var bo = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    // Mocking for Overseas Entity Data
    CompanyProfileApi publicOE = new CompanyProfileApi();
    publicOE.setCompanyName("Test Public Company");
    publicOE.setCompanyNumber("OE123456");
    OverseasEntityDataApi privateOE = new OverseasEntityDataApi();
    privateOE.setEmail("john@smith.com");

    when(publicDataRetrievalService.getCompanyProfile(any(), any())).thenReturn(publicOE);
    when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(privateOE);

    publicPrivateDataCombiner.buildMergedOverseasEntityDataPair(any(), any());

    String expectedOutput = "{\"Managing Officer Data\": [" +
        "{\"Public Hashed ID\": \"Iq6hRNqa-Twx1eWhHv_FOwTb1i4\", \"Private ID\": \"null\"}, " +
        "{\"Public Hashed ID\": \"5hSqZI0z2kRZvJj_ZcfyUaC26Qo\", \"Private ID\": \"null\"}, " +
        "{\"Public Hashed ID\": \"null\", \"Private ID\": \"12\"}, " +
        "{\"Public Hashed ID\": \"null\", \"Private ID\": \"123\"}" +
        "]," +
        "\"Beneficial Owner Data\": [" +
        "{\"Public Hashed ID\": \"Iq6hRNqa-Twx1eWhHv_FOwTb1i4\", \"Private ID\": \"null\"}, " +
        "{\"Public Hashed ID\": \"5hSqZI0z2kRZvJj_ZcfyUaC26Qo\", \"Private ID\": \"null\"}, " +
        "{\"Public Hashed ID\": \"null\", \"Private ID\": \"12\"}, " +
        "{\"Public Hashed ID\": \"null\", \"Private ID\": \"123\"}" +
        "]," +
        "\"Overseas Entity Data\": {" +
        "\"Public Entity Number\": \"OE123456\", \"Private Email\": \"john@smith.com\"" +
        "}}";

    String actualOutput = publicPrivateDataCombiner.logCollatedData();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  void testBuildMergedBeneficialOwnerDataMapNoPrivatePublicBoData() throws ServiceException {
    // Configure mock behavior
    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(null);
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(null);

    // Execute the method to test
    Map<String, Pair<PscApi, PrivateBoDataApi>> results = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedBeneficialOwnerDataMapNoPrivateBoData() throws JsonProcessingException, ServiceException {
    // Prepare test data
    PscApi pscApi = objectMapper.readValue(pscJsonString, PscApi.class);
    PscsApi pscsApi = new PscsApi();
    pscsApi.setItems(Collections.singletonList(pscApi));

    // Configure mock behavior
    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(null);
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(pscsApi);

    // Execute the method to test
    Map<String, Pair<PscApi, PrivateBoDataApi>> results = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedBeneficialOwnersDataMapNoPublicBoData() throws ServiceException {
    // Prepare test data
    PrivateBoDataApi privateBoDataApi = new PrivateBoDataApi();
    privateBoDataApi.setPscId("12345");

    // Configure mock behavior
    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(new PrivateBoDataListApi(List.of(privateBoDataApi)));
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(null);

    // Execute the method to test
    Map<String, Pair<PscApi, PrivateBoDataApi>> results = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedBeneficialOwnersDataMapPublicHasNoData() throws ServiceException {
    // Prepare test data
    PrivateBoDataListApi privateBoDataApis = new PrivateBoDataListApi(Collections.emptyList());
    PscsApi pscsApi = new PscsApi();
    pscsApi.setItems(null);

    // Configure mock behavior
    when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(privateBoDataApis);
    when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(pscsApi);

    // Execute the method to test
    Map<String, Pair<PscApi, PrivateBoDataApi>> results = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedManagingOfficerDataMapNoPrivatePublicMoData() throws ServiceException {
    // Configure mock behavior
    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(null);
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(null);

    // Execute the method to test
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> results = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedManagingOfficerDataMapNoPrivateMoData() throws JsonProcessingException, ServiceException {
    // Prepare test data
    OfficersApi officersApi = objectMapper.readValue(officersApiString, OfficersApi.class);

    // Configure mock behavior
    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(null);
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(officersApi);

    // Execute the method to test
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> results = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedManagingOfficerDataMapNoPublicMoData() throws ServiceException {
    // Prepare test data
    ManagingOfficerDataApi managingOfficerDataApi = new ManagingOfficerDataApi();
    managingOfficerDataApi.setManagingOfficerAppointmentId("12345");

    // Configure mock behavior
    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(new ManagingOfficerListDataApi(List.of(managingOfficerDataApi)));
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(null);

    // Execute the method to test
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> results = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }

  @Test
  void testBuildMergedManagingOfficerDataMapPublicPrivateHasNoData() throws JsonProcessingException, ServiceException {
    // Prepare test data
    ManagingOfficerListDataApi managingOfficerDataApis = new ManagingOfficerListDataApi(null);

    OfficersApi officersApi = objectMapper.readValue(officersApiString, OfficersApi.class);
    officersApi.setItems(null);

    // Configure mock behavior
    when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(managingOfficerDataApis);
    when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(officersApi);

    // Execute the method to test
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> results = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(COMPANY_NUMBER, PASS_THROUGH_TOKEN_HEADER);

    assertTrue(results.isEmpty());
  }
}
