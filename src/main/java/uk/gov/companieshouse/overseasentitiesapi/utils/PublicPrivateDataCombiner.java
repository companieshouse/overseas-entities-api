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
    var privateOE = privateDataRetrievalService.getOverseasEntityData();
    var publicOE = publicDataRetrievalService.getCompanyProfile();

    return Pair.of(publicOE, privateOE);
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

    var combineBO = new HashMap<String, Pair<PscApi, PrivateBoDataApi>>();

    var privateBOs = privateDataRetrievalService.getBeneficialOwnerData();
    var publicPSCs = publicDataRetrievalService.getPscs();

    for (PrivateBoDataApi privateBO : privateBOs.getBoPrivateData()) {

      String hashedId = null;
      try {
        hashedId = hashHelper.encode(privateBO.getPscId());
      } catch (NoSuchAlgorithmException e) {
        throw new ServiceException("Error hashing PSC ID", e);
      }

      var pairFromMap = combineBO.get(hashedId);
      Pair<PscApi, PrivateBoDataApi> pairToPutInMap;

      if (pairFromMap != null) {
        pairToPutInMap = Pair.of(pairFromMap.getLeft(), privateBO);
      } else {
        pairToPutInMap = Pair.of(null, privateBO);
      }
      combineBO.put(hashedId, pairToPutInMap);
    }

    for (PscApi publicPSC : publicPSCs.getItems()) {
      String[] linkAsArray = publicPSC
          .getLinks()
          .getSelf()
          .split("/");
      String hashedId = linkAsArray[linkAsArray.length - 1];

      var pairFromMap = combineBO.get(hashedId);
      Pair<PscApi, PrivateBoDataApi> pairToGoInMap;

      if (pairFromMap != null) {
        pairToGoInMap = Pair.of(publicPSC, pairFromMap.getRight());
      } else {
        pairToGoInMap = Pair.of(publicPSC, null);
      }
      combineBO.put(hashedId, pairToGoInMap);
    }

    return combineBO;
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
    var combineMO = new HashMap<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>>();

    var privateMOs = privateDataRetrievalService.getManagingOfficerData();
    var publicOfficers = publicDataRetrievalService.getOfficers();

    for (ManagingOfficerDataApi privateMO : privateMOs.getManagingOfficerData()) {
      String hashedId = null;
      try {
        hashedId = hashHelper.encode(privateMO.getManagingOfficerId());
      } catch (NoSuchAlgorithmException e) {
        throw new ServiceException("Error hashing Managing Officer ID", e);
      }

      var pairFromMap = combineMO.get(hashedId);
      Pair<CompanyOfficerApi, ManagingOfficerDataApi> pairToPutInMap;

      if (pairFromMap != null) {
        pairToPutInMap = Pair.of(pairFromMap.getLeft(), privateMO);
      } else {
        pairToPutInMap = Pair.of(null, privateMO);
      }

      combineMO.put(hashedId, pairToPutInMap);
    }

    for (CompanyOfficerApi publicOfficer : publicOfficers.getItems()) {
      String[] linkAsArray = publicOfficer.getLinks().getSelf().split("/");
      String hashedId = linkAsArray[linkAsArray.length - 1];

      var pairFromMap = combineMO.get(hashedId);
      Pair<CompanyOfficerApi, ManagingOfficerDataApi> pairToPutInMap;
      
      if (pairFromMap != null) {
        var pair = combineMO.get(hashedId);
        pairToPutInMap = Pair.of(publicOfficer, pair.getRight());
      } else {
        pairToPutInMap = Pair.of(publicOfficer, null);
      }
      combineMO.put(hashedId, pairToPutInMap);
    }

    return combineMO;
  }
}
