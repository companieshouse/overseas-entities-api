package uk.gov.companieshouse.overseasentitiesapi.service;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Component
public class PrivateDataRetrievalService {

  private static final String COMPANY_NUMBER = "company_number";
  private static final String MANAGING_OFFICER_ID = "managing_officer_id";
  private static final String OVERSEAS_ENTITY_URI_SECTION = "/overseas-entity/";
  private final ApiClientService apiClientService;
  private ManagingOfficerListDataApi managingOfficerData;
  private PrivateBoDataListApi beneficialOwnerData;
  private OverseasEntityDataApi overseasEntityData;

  public PrivateDataRetrievalService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public void initialisePrivateData(String companyNumber)
      throws ServiceException {
    this.beneficialOwnerData = getBeneficialOwnersData(companyNumber);
    this.overseasEntityData = getOverseasEntityData(companyNumber);
    this.managingOfficerData = getManagingOfficerData(companyNumber);
  }

  private ManagingOfficerListDataApi getManagingOfficerData(String companyNumber)
      throws ServiceException {

    var logMap = new HashMap<String, Object>();
    logMap.put(COMPANY_NUMBER, companyNumber);
    try {
      var managingOfficerDataList = apiClientService.getInternalApiClient()
          .privateManagingOfficerDataResourceHandler()
          .getManagingOfficerData(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/managing-officers")
          .execute()
          .getData();

      if (managingOfficerDataList != null && managingOfficerDataList.getManagingOfficerData() != null
          && !managingOfficerDataList.getManagingOfficerData().isEmpty()) {
        logMap.put(MANAGING_OFFICER_ID,
            managingOfficerDataList.getManagingOfficerData().get(0).getManagingOfficerId());
      }

      ApiLogger.debug("Retrieving Managing Officer data for Company Number ", logMap);
      return managingOfficerDataList;

    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving Managing Officer data for " + companyNumber;
      ApiLogger.error(message, e, logMap);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  private OverseasEntityDataApi getOverseasEntityData(String companyNumber)
      throws ServiceException {
    try {
      OverseasEntityDataApi overseasEntityDataApi = apiClientService
          .getInternalApiClient()
          .privateOverseasEntityDataHandler()
          .getOverseasEntityData(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/entity-data")
          .execute()
          .getData();
      var logMap = new HashMap<String, Object>();

      logMap.put(COMPANY_NUMBER, companyNumber);
      ApiLogger.info("Retrieving overseas entity data for company number ",  logMap);

      return overseasEntityDataApi;
    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving overseas entity data for " + companyNumber;
      ApiLogger.errorContext(message, e);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  private PrivateBoDataListApi getBeneficialOwnersData(String companyNumber)
      throws ServiceException {

    var logMap = new HashMap<String, Object>();
    logMap.put(COMPANY_NUMBER, companyNumber);
    ApiLogger.info("Retrieving Beneficial Owners for Company Number " + companyNumber, logMap);

    try {
      PrivateBoDataListApi beneficialOwnerDataList = apiClientService.getInternalApiClient()
          .privateBeneficialOwnerResourceHandler()
          .getBeneficialOwners(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/beneficial-owners")
          .execute()
          .getData();

      if (beneficialOwnerDataList != null && beneficialOwnerDataList.getBoPrivateData() != null
          && !beneficialOwnerDataList.getBoPrivateData().isEmpty()) {
        int numberOfBOs = beneficialOwnerDataList.getBoPrivateData().size();
        ApiLogger
            .info(String.format("Retrieved %d Beneficial Owners for Company Number %s",
                numberOfBOs, companyNumber));
      } else {
        ApiLogger.info("No Beneficial Owners found for Company Number " + companyNumber);
      }

      return beneficialOwnerDataList;

    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving Beneficial Owner data for " + companyNumber;
      ApiLogger.errorContext(message, e);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  public PrivateBoDataListApi getBeneficialOwnerData() {
    return this.beneficialOwnerData;
  }

  public OverseasEntityDataApi getOverseasEntityData() {
    return this.overseasEntityData;
  }

  public ManagingOfficerListDataApi getManagingOfficerData() {
    return this.managingOfficerData;
  }
}