package uk.gov.companieshouse.overseasentitiesapi.service;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Component
public class PrivateDataRetrievalService {

  public static final String COMPANY_NUMBER = "company_number";
  public static final String MANAGING_OFFICER_ID = "managing_officer_id";

  private final ApiClientService apiClientService;
  private ManagingOfficerDataApi managingOfficerData;

  public PrivateDataRetrievalService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public void initialisePrivateData(String companyNumber)
      throws ServiceException {
    this.managingOfficerData = getManagingOfficerData(companyNumber);
  }

  private ManagingOfficerDataApi getManagingOfficerData(String companyNumber)
      throws ServiceException {
    var logMap = new HashMap<String, Object>();
    logMap.put(COMPANY_NUMBER, companyNumber);
    try {
      var managingOfficerData = apiClientService.getInternalApiClient()
          .privateManagingOfficerDataResourceHandler()
          .getManagingOfficerData("/overseas-entity/" + companyNumber + "/managing-officers")
          .execute()
          .getData();

      logMap.put(MANAGING_OFFICER_ID, managingOfficerData.getManagingOfficerId());
      ApiLogger.debug("Retrieving Managing Officer data for Company Number ", logMap);

      return managingOfficerData;
    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving Managing Officer data for " + companyNumber;
      ApiLogger.error(message, e, logMap);
      throw new ServiceException(e.getMessage(), e);
    }
  }
}
