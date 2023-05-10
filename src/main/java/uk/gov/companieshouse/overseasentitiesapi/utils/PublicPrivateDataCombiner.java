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
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.service.PublicDataRetrievalService;

public class PublicPrivateDataCombiner {

  private final PublicDataRetrievalService publicDataRetrievalService;
  private final PrivateDataRetrievalService privateDataRetrievalService;
  private final String salt;

  public PublicPrivateDataCombiner(final PublicDataRetrievalService publicDataRetrievalService,
      final PrivateDataRetrievalService privateDataRetrievalService, final String salt) {
    this.privateDataRetrievalService = privateDataRetrievalService;
    this.publicDataRetrievalService = publicDataRetrievalService;
    this.salt = salt;
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
   * @throws NoSuchAlgorithmException
   */
  public Map<String, Pair<PscApi, PrivateBoDataApi>> buildMergedBeneficialOwnerDataMap()
      throws NoSuchAlgorithmException {

    var combineBO = new HashMap<String, Pair<PscApi, PrivateBoDataApi>>();

    var privateBOs = privateDataRetrievalService.getBeneficialOwnerData();
    var publicPSCs = publicDataRetrievalService.getPscs();

    for (var privateBO : privateBOs.getBoPrivateData()) {
      putInPairMap(combineBO, getHashedId(privateBO.getPscId()), privateBO, Position.RIGHT);
    }

    for (var publicPSC : publicPSCs.getItems()) {
      String[] linkAsArray = publicPSC
          .getLinks()
          .getSelf()
          .split("/");
      String hashedId = linkAsArray[linkAsArray.length - 1];
      putInPairMap(combineBO, hashedId, publicPSC, Position.LEFT);
    }

    return combineBO;
  }

  /**
   * Creates a map of the combined Managing Officer data from the public and private APIs <br> Here
   * the Hashed ID is the key, and the value is a Pair of the public data on the left and the
   * private data on the right
   *
   * @return Map of the combined Beneficial Owner data
   * @throws NoSuchAlgorithmException
   */
  public Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> buildMergedManagingOfficerDataMap()
      throws NoSuchAlgorithmException {
    var combineMO = new HashMap<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>>();

    var privateMOs = privateDataRetrievalService.getManagingOfficerData();
    var publicOfficers = publicDataRetrievalService.getOfficers();

    for (var privateMO : privateMOs.getManagingOfficerData()) {
      putInPairMap(combineMO, getHashedId(privateMO.getManagingOfficerId()), privateMO,
          Position.RIGHT);
    }

    for (var publicOfficer : publicOfficers.getItems()) {
      String[] linkAsArray = publicOfficer.getLinks().getSelf().split("/");
      String hashedId = linkAsArray[linkAsArray.length - 1];
      putInPairMap(combineMO, hashedId, publicOfficer, Position.LEFT);
    }

    return combineMO;
  }

  /**
   * Puts a value into a map, either in the left or right of a pair depending on the leftRight
   *
   * @param map      Map to put the value into
   * @param key      Key to put the value under
   * @param value    Value to put into the Pair in the map
   * @param position Whether to put the value in the left or right of the pair
   */
  private <L, R> void putInPairMap(Map<String, Pair<L, R>> map, String key, Object value,
      Position position) {
    if (map.containsKey(key)) {
      var pair = map.get(key);
      map.put(key, position == Position.LEFT ? Pair.of((L) value, pair.getRight())
          : Pair.of(pair.getLeft(), (R) value));
    } else {
      map.put(key, position == Position.LEFT ? Pair.of((L) value, null) : Pair.of(null, (R) value));
    }
  }

  /**
   * Hashes an ID using SHA-1 algorithm before Base 64 Encoding
   *
   * @param id The ID to hash
   * @return Hashed ID using SHA-1 algorithm and Base 64 Encoded
   * @throws NoSuchAlgorithmException
   */
  private String getHashedId(String id) throws NoSuchAlgorithmException {
    var hashHelper = new HashHelper(this.salt);
    return hashHelper.encode(id);
  }

  // Enum to represent whether to put the value in the left or right of a pair
  private enum Position {LEFT, RIGHT}
}
