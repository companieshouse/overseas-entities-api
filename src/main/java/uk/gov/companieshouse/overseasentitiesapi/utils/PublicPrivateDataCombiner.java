package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.service.PublicDataRetrievalService;

public class PublicPrivateDataCombiner {

  private final PublicDataRetrievalService publicDataRetrievalService;
  private final PrivateDataRetrievalService privateDataRetrievalService;
  private final HashHelper hashHelper;

  private Pair<CompanyProfileApi, OverseasEntityDataApi> combinedOEs;
  private Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBOs;
  private Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMOs;
  
  public PublicPrivateDataCombiner(final PublicDataRetrievalService publicDataRetrievalService,
      final PrivateDataRetrievalService privateDataRetrievalService, final String salt) {
    this.privateDataRetrievalService = privateDataRetrievalService;
    this.publicDataRetrievalService = publicDataRetrievalService;

    this.hashHelper = new HashHelper(salt);
  }

  /**
   * Returns an instance of Pair containing the public data on the right and private data on the
   * left
   *
   * @return Pair of the public data on the right and private data on the left
   */
  public Pair<CompanyProfileApi, OverseasEntityDataApi> buildMergedOverseasEntityDataPair() {
    var publicOE = publicDataRetrievalService.getCompanyProfile();
    var privateOE = privateDataRetrievalService.getOverseasEntityData();

    this.combinedOEs = Pair.of(publicOE, privateOE);

    return this.combinedOEs;
  }

  /**
   * Creates a map of the combined Beneficial Owner data from the public and private APIs <br> Here
   * the Hashed ID is the key, and the value is a Pair of the public data on the left and the
   * private data on the right
   *
   * @return Map of the combined Beneficial Owner data
   * @throws ServiceException
   */
  public Map<String, Pair<PscApi, PrivateBoDataApi>> buildMergedBeneficialOwnerDataMap()
      throws ServiceException {

    this.combinedBOs = new HashMap<>();

    var privateBOs = privateDataRetrievalService.getBeneficialOwnerData();
    var publicPSCs = publicDataRetrievalService.getPscs();

    for (PrivateBoDataApi privateBO : privateBOs.getBoPrivateData()) {

      String hashedId = null;
      try {
        hashedId = hashHelper.encode(privateBO.getPscId());
      } catch (NoSuchAlgorithmException e) {
        throw new ServiceException("Error hashing PSC ID", e);
      }

      var pairFromMap = combinedBOs.get(hashedId);
      Pair<PscApi, PrivateBoDataApi> pairToPutInMap;

      if (pairFromMap != null) {
        pairToPutInMap = Pair.of(pairFromMap.getLeft(), privateBO);
      } else {
        pairToPutInMap = Pair.of(null, privateBO);
      }
      combinedBOs.put(hashedId, pairToPutInMap);
    }

    for (PscApi publicPSC : publicPSCs.getItems()) {
      String[] linkAsArray = publicPSC
          .getLinks()
          .getSelf()
          .split("/");
      String hashedId = linkAsArray[linkAsArray.length - 1];

      var pairFromMap = combinedBOs.get(hashedId);
      Pair<PscApi, PrivateBoDataApi> pairToGoInMap;

      if (pairFromMap != null) {
        pairToGoInMap = Pair.of(publicPSC, pairFromMap.getRight());
      } else {
        pairToGoInMap = Pair.of(publicPSC, null);
      }
      combinedBOs.put(hashedId, pairToGoInMap);
    }

    return combinedBOs;
  }

  /**
   * Creates a map of the combined Managing Officer data from the public and private APIs <br> Here
   * the Hashed ID is the key, and the value is a Pair of the public data on the left and the
   * private data on the right
   *
   * @return Map of the combined Beneficial Owner data
   * @throws ServiceException
   */
  public Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> buildMergedManagingOfficerDataMap()
      throws ServiceException {
    this.combinedMOs = new HashMap<>();

    var privateMOs = privateDataRetrievalService.getManagingOfficerData();
    var publicOfficers = publicDataRetrievalService.getOfficers();

    for (ManagingOfficerDataApi privateMO : privateMOs.getManagingOfficerData()) {
      String hashedId = null;
      try {
        hashedId = hashHelper.encode(privateMO.getManagingOfficerId());
      } catch (NoSuchAlgorithmException e) {
        throw new ServiceException("Error hashing Managing Officer ID", e);
      }

      var pairFromMap = combinedMOs.get(hashedId);
      Pair<CompanyOfficerApi, ManagingOfficerDataApi> pairToPutInMap;

      if (pairFromMap != null) {
        pairToPutInMap = Pair.of(pairFromMap.getLeft(), privateMO);
      } else {
        pairToPutInMap = Pair.of(null, privateMO);
      }

      combinedMOs.put(hashedId, pairToPutInMap);
    }

    for (CompanyOfficerApi publicOfficer : publicOfficers.getItems()) {
      String[] linkAsArray = publicOfficer.getLinks().getSelf().split("/");
      String hashedId = linkAsArray[linkAsArray.length - 1];

      var pairFromMap = combinedMOs.get(hashedId);
      Pair<CompanyOfficerApi, ManagingOfficerDataApi> pairToPutInMap;
      
      if (pairFromMap != null) {
        var pair = combinedMOs.get(hashedId);
        pairToPutInMap = Pair.of(publicOfficer, pair.getRight());
      } else {
        pairToPutInMap = Pair.of(publicOfficer, null);
      }
      combinedMOs.put(hashedId, pairToPutInMap);
    }

    return combinedMOs;
  }


  private void createMoLog(StringBuilder result){
    result.append("\"Managing Officer Data\": [");
    var first = true;

    for (var officer : this.combinedMOs.entrySet()) {
      if (first) {
        first = false;
      } else {
        result.append(", ");
      }

      var publicOfficer = officer.getValue().getLeft();
      String publicMoId;

      if(publicOfficer != null) {
        String[] linkAsArray = publicOfficer.getLinks().getSelf().split("/");
        publicMoId = linkAsArray[linkAsArray.length - 1];
      }else{
        publicMoId = "null";
      }

      result.append("{");
      result.append("\"Public Hashed ID\": \"" + publicMoId + "\", ");

      var privateOfficer = officer.getValue().getRight();
      String privateMoId;
      if(privateOfficer != null) {
        privateMoId = privateOfficer.getManagingOfficerId();
      }else{
        privateMoId = "null";
      }

      result.append(
          "\"Private ID\": \"" + privateMoId + "\"");
      result.append("}");
    }
    result.append("],");
  }

  private void createBoLog(StringBuilder result){
    result.append("\"Beneficial Owner Data\": [");
    var first = true;
    for (var owner : this.combinedBOs.entrySet()) {
      if (first) {
        first = false;
      } else {
        result.append(", ");
      }

      var publicBo = owner.getValue().getLeft();
      String publicBoId;

      if(publicBo != null) {
        String[] linkAsArray = publicBo.getLinks().getSelf().split("/");
        publicBoId = linkAsArray[linkAsArray.length - 1];
      }else{
        publicBoId = "null";
      }

      var privateBo = owner.getValue().getRight();
      String privateBoId;

      if(privateBo != null) {
        privateBoId = privateBo.getPscId();
      }else{
        privateBoId = "null";
      }

      result.append("{");
      result.append("\"Public Hashed ID\": \"" + publicBoId + "\", ");
      result.append("\"Private ID\": \"" + privateBoId + "\"");
      result.append("}");
    }
    result.append("],");
  }

  private void createOELog(StringBuilder result){
    result.append("\"Overseas Entity Data\": {");
    if(this.combinedOEs != null && this.combinedOEs.getLeft() != null && this.combinedOEs.getRight() != null) {
      result.append("\"Public Entity Number\": \"" + this.combinedOEs.getLeft().getCompanyNumber() + "\", ");
      result.append("\"Private Email\": \"" + this.combinedOEs.getRight().getEmail() + "\"");
    }
    result.append("}");
  }


  public String logCollatedData() {
    var result = new StringBuilder("{");

    createMoLog(result);
    createBoLog(result);
    createOELog(result);

    result.append("}");

    return result.toString();
  }

}
