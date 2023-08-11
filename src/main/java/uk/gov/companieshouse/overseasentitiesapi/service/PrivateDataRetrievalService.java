package uk.gov.companieshouse.overseasentitiesapi.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
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
  private static final String MANAGING_OFFICER_APPOINTMENT_ID = "managing_officer_appointment_id";
  private static final String OVERSEAS_ENTITY_URI_SECTION = "/overseas-entity/";
  private final ApiClientService apiClientService;

  public PrivateDataRetrievalService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public ManagingOfficerListDataApi getManagingOfficerData(String companyNumber)
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
        logMap.put(MANAGING_OFFICER_APPOINTMENT_ID,
            managingOfficerDataList.getManagingOfficerData().get(0).getManagingOfficerAppointmentId());
      }

      return managingOfficerDataList;
    } catch (ApiErrorResponseException e) {
      if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
        ApiLogger.info("No Managing Officers data found for Company Number "+companyNumber, logMap);
        return new ManagingOfficerListDataApi(Collections.emptyList());
      }
      throw new ServiceException(e.getStatusMessage(), e);
    } catch (URIValidationException e) {
      var message = "Error Retrieving Managing Officer data for " + companyNumber;
      ApiLogger.error(message, e, logMap);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  public OverseasEntityDataApi getOverseasEntityData(String companyNumber)
      throws ServiceException {
    var logMap = new HashMap<String, Object>();
    try {
      var overseasEntityDataApi = apiClientService
          .getInternalApiClient()
          .privateOverseasEntityDataHandler()
          .getOverseasEntityData(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/entity-data")
          .execute()
          .getData();


      logMap.put(COMPANY_NUMBER, companyNumber);
      ApiLogger.info("Retrieving overseas entity data for company number ",  logMap);

      return overseasEntityDataApi;
    } catch (ApiErrorResponseException e) {
      if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
        ApiLogger.info("No overseas entity data for Company Number "+companyNumber, logMap);
        return new OverseasEntityDataApi();
      }
      throw new ServiceException(e.getStatusMessage(), e);
    } catch (URIValidationException e) {
      var message = "Error Retrieving overseas entity data for " + companyNumber;
      ApiLogger.error(message, e, logMap);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  public PrivateBoDataListApi getBeneficialOwnersData(String companyNumber)
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
      }

      return beneficialOwnerDataList;
    } catch (ApiErrorResponseException e) {
      if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
        ApiLogger.info("No Beneficial Owners found for Company Number "+companyNumber, logMap);
        return new PrivateBoDataListApi(Collections.emptyList());
      }
      throw new ServiceException(e.getStatusMessage(), e);
    } catch (URIValidationException e) {
      var message = "Error Retrieving Beneficial Owner data for " + companyNumber;
      ApiLogger.errorContext(message, e);
      throw new ServiceException(e.getMessage(), e);
    }
  }
}