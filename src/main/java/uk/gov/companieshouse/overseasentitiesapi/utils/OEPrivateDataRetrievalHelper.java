package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;
import java.util.HashMap;

@Component
public class OEPrivateDataRetrievalHelper {
  public static final String COMPANY_NUMBER = "company_number";

  private final ApiClientService apiClientService;

  public OEPrivateDataRetrievalHelper(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public void getOverseasEntityPrivateData(String companyNumber) throws ServiceException {
    getOverseasEntityData(companyNumber);
  }

  private OverseasEntityDataApi getOverseasEntityData(String companyNumber)
      throws ServiceException {
    try {
      OverseasEntityDataApi overseasEntityData =
          apiClientService
              .getInternalApiClient()
              .privateOverseasEntityDataHandler()
              .getEmail("/overseas-entity/" + companyNumber + "/entity-data")
              .execute()
              .getData();
      var logMap = new HashMap<String, Object>();
      logMap.put(COMPANY_NUMBER, overseasEntityData.getEmail());

      ApiLogger.info("Retrieving overseas entity data for company number " + logMap);

      return overseasEntityData;
    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving overseas entity data for " + companyNumber;
      ApiLogger.errorContext(message, e);
      throw new ServiceException(e.getMessage(), e);
    }
  }
}
