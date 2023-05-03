package uk.gov.companieshouse.overseasentitiesapi.service;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Component
public class PrivateDataRetrievalService {
  public static final String COMPANY_NUMBER = "company_number";
  public static final String MANAGING_OFFICER_ID = "managing_officer_id";

  private final ApiClientService apiClientService;
  private ManagingOfficerListDataApi managingOfficerData;

  private PrivateBoDataListApi beneficialOwnerData;

  public PrivateDataRetrievalService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public void initialisePrivateData(String companyNumber)
      throws ServiceException {
    this.managingOfficerData = getManagingOfficerData(companyNumber);
    this.beneficialOwnerData = getBeneficialOwnersData(companyNumber);
  }

  private ManagingOfficerListDataApi getManagingOfficerData(String companyNumber)
      throws ServiceException {
    var logMap = new HashMap<String, Object>();
    logMap.put(COMPANY_NUMBER, companyNumber);
    try {
      var managingOfficerData = apiClientService.getInternalApiClient()
          .privateManagingOfficerDataResourceHandler()
          .getManagingOfficerData("/overseas-entity/" + companyNumber + "/managing-officers")
          .execute()
          .getData();

      if (managingOfficerData != null && managingOfficerData.getManagingOfficerData() != null
          && !managingOfficerData.getManagingOfficerData().isEmpty()) {
        logMap.put(MANAGING_OFFICER_ID,
            managingOfficerData.getManagingOfficerData().get(0).getManagingOfficerId());
      }
      ApiLogger.debug("Retrieving Managing Officer data for Company Number ", logMap);

      return managingOfficerData;
    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving Managing Officer data for " + companyNumber;
      ApiLogger.error(message, e, logMap);
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

      if (beneficialOwnerDataList.getBoPrivateData() != null && !beneficialOwnerDataList.getBoPrivateData().isEmpty()){
        int numberOfBOs = beneficialOwnerDataList.getBoPrivateData().size();
        ApiLogger.info(String.format("Retrieved %d Beneficial Owners for Company Number %s", numberOfBOs, companyNumber));
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

}
