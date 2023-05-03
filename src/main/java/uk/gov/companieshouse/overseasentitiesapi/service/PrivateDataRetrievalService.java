package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import java.io.IOException;
import java.util.HashMap;

@Component
public class PrivateDataRetrievalService {
  public static final String COMPANY_NUMBER = "company_number";

  private final ApiClientService apiClientService;

  private OverseasEntityDataApi overseasEntityData;

  private PrivateBoDataListApi beneficialOwnerData;

  public PrivateDataRetrievalService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public void initialisePrivateData(String companyNumber) throws ServiceException {
    overseasEntityData = getOverseasEntityData(companyNumber);
    beneficialOwnerData = getBeneficialOwnersData(companyNumber);
  }

  private OverseasEntityDataApi getOverseasEntityData(String companyNumber)
      throws ServiceException {
    try {
      OverseasEntityDataApi overseasEntityDataApi = apiClientService
          .getInternalApiClient()
          .privateOverseasEntityDataHandler()
          .getOverseasEntityData("/overseas-entity/" + companyNumber + "/entity-data")
          .execute()
          .getData();
      var logMap = new HashMap<String, Object>();

      logMap.put(COMPANY_NUMBER, companyNumber);
      ApiLogger.info("Retrieving overseas entity data for company number " + logMap);

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
          .getBeneficialOwners("/overseas-entity/" + companyNumber + "/beneficial-owners")
          .execute()
          .getData();

      if (beneficialOwnerDataList != null && beneficialOwnerDataList.getBoPrivateData() != null && !beneficialOwnerDataList.getBoPrivateData().isEmpty()) {
        int numberOfBOs = beneficialOwnerDataList.getBoPrivateData().size();
        ApiLogger
            .info(String.format("Retrieved %d Beneficial Owners for Company Number %s", numberOfBOs, companyNumber));
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
    return beneficialOwnerData;
  }

  public OverseasEntityDataApi getOverseasEntityData() {
    return overseasEntityData;
  }
}
