package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.io.IOException;
import java.util.HashMap;

@Component
public class PrivateDataRetrievalService {
  public static final String COMPANY_NUMBER = "company_number";

  private final ApiClientService apiClientService;

  private OverseasEntityDataApi overseasEntityDataApi;

  public PrivateDataRetrievalService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public void initialisePrivateData(String companyNumber) throws ServiceException {
    this.overseasEntityDataApi = getOverseasEntityData(companyNumber);
  }

  private OverseasEntityDataApi getOverseasEntityData(String companyNumber)
      throws ServiceException {
    try {
      OverseasEntityDataApi overseasEntityData =
          apiClientService
              .getInternalApiClient()
              .privateOverseasEntityDataHandler()
              .getOverseasEntityData("/overseas-entity/" + companyNumber + "/entity-data")
              .execute()
              .getData();
      var logMap = new HashMap<String, Object>();

      logMap.put(COMPANY_NUMBER, companyNumber);
      ApiLogger.info("Retrieving overseas entity data for company number " + logMap);

      return overseasEntityData;
    } catch (URIValidationException | IOException e) {
      var message = "Error Retrieving overseas entity data for " + companyNumber;
      ApiLogger.errorContext(message, e);
      throw new ServiceException(e.getMessage(), e);
    }
  }
}
