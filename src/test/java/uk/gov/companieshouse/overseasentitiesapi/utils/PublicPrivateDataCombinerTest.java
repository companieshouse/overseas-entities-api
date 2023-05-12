package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.NoSuchAlgorithmException;
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

  String officersApiString = "{" +
      "\"items\": [" +
      "{" +
      "\"links\": {" +
      "\"self\": \"/company/01913593/appointments/Iq6hRNqa-Twx1eWhHv_FOwTb1i4\"" +
      "}" +
      "}" +
      "]" +
      "}";

  private PublicDataRetrievalService publicDataRetrievalService;
  private PrivateDataRetrievalService privateDataRetrievalService;
  private PublicPrivateDataCombiner publicPrivateDataCombiner;

  @BeforeEach
  public void setUp() {
    publicDataRetrievalService = Mockito.mock(PublicDataRetrievalService.class);
    privateDataRetrievalService = Mockito.mock(PrivateDataRetrievalService.class);
    publicPrivateDataCombiner = new PublicPrivateDataCombiner(publicDataRetrievalService, privateDataRetrievalService,
        "test_salt");
  }

  @Test
  void testBuildMergedOverseasEntityDataPair() {
    CompanyProfileApi publicOE = new CompanyProfileApi();
    publicOE.setCompanyName("Test Public Company");
    OverseasEntityDataApi privateOE = new OverseasEntityDataApi();
    privateOE.setEmail("john@smith.com");

    when(publicDataRetrievalService.getCompanyProfile()).thenReturn(publicOE);
    when(privateDataRetrievalService.getOverseasEntityData()).thenReturn(privateOE);

    var result = publicPrivateDataCombiner.buildMergedOverseasEntityDataPair();

    assertEquals(publicOE, result.getLeft());
    assertEquals(privateOE, result.getRight());
  }

  /**
   * In this test we will be checking to see if the method to build a map of beneficial owner data
   * with the psc data <br>
   * Note: PscApi uses a hashed version of the ID whereas PrivateBoDataApi uses the unhashed version <br>
   * The value "12345" hashed is "Iq6hRNqa-Twx1eWhHv_FOwTb1i4"
   *
   * @throws NoSuchAlgorithmException
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
    when(privateDataRetrievalService.getBeneficialOwnerData()).thenReturn(
        new PrivateBoDataListApi(List.of(privateBoDataApi)));
    when(publicDataRetrievalService.getPscs()).thenReturn(pscsApi);

    // Execute the method to test
    Map<String, Pair<PscApi, PrivateBoDataApi>> results = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap();

    // Verify the result
    assertEquals(1, results.size());

    Pair<PscApi, PrivateBoDataApi> pair = results.get("Iq6hRNqa-Twx1eWhHv_FOwTb1i4");
    assertEquals(pscApi, pair.getLeft());
    assertEquals(privateBoDataApi, pair.getRight());

    // Verify mock interactions
    verify(publicDataRetrievalService, times(1)).getPscs();
    verify(privateDataRetrievalService, times(1)).getBeneficialOwnerData();
  }

  @Test
  void testBuildMergedManagingOfficerDataMap()
      throws JsonProcessingException, ServiceException {
    // Prepare test data
    ManagingOfficerDataApi managingOfficerDataApi = new ManagingOfficerDataApi();
    managingOfficerDataApi.setManagingOfficerId("12345");

    OfficersApi officersApi = objectMapper.readValue(officersApiString, OfficersApi.class);
    CompanyOfficerApi companyOfficerApi = officersApi.getItems().get(0);

        // Configure mock behavior
    when(privateDataRetrievalService.getManagingOfficerData()).thenReturn(new ManagingOfficerListDataApi(List.of(managingOfficerDataApi)));
    when(publicDataRetrievalService.getOfficers()).thenReturn(officersApi);

    // Execute the method to test
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> results = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap();

    // Verify the result
    assertEquals(1, results.size());

    Pair<CompanyOfficerApi, ManagingOfficerDataApi> pair = results.get("Iq6hRNqa-Twx1eWhHv_FOwTb1i4");
    assertEquals(companyOfficerApi, pair.getLeft());
    assertEquals(managingOfficerDataApi, pair.getRight());

    // Verify mock interactions
    verify(publicDataRetrievalService, times(1)).getOfficers();
    verify(privateDataRetrievalService, times(1)).getManagingOfficerData();
  }

}
