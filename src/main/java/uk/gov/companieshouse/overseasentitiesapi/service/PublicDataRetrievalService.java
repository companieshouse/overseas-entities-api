package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.officers.OfficersApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class PublicDataRetrievalService {
    public static final String COMPANY_NUMBER = "company_number";
    public static final String COMPANY = "/company/";
    private final ApiClientService apiClientService;

    public PublicDataRetrievalService(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public CompanyProfileApi getCompanyProfile(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        var logMap = new HashMap<String, Object>();

        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info(String.format("Retrieving Company Profile for Company Number %s", companyNumber), logMap);

        try {
            return apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader)
                    .company()
                    .get(COMPANY + companyNumber)
                    .execute()
                    .getData();
        } catch (URIValidationException | IOException e) {
            ApiLogger.error(String.format("Error Retrieving  Company Profile data for %s", companyNumber), e, logMap);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public OfficersApi getOfficers(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        var logMap = new HashMap<String, Object>();

        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info(String.format("Retrieving Officers data for Company Number %s", companyNumber), logMap);

        try {
            return apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader)
                    .officers()
                    .list(COMPANY + companyNumber + "/officers")
                    .execute()
                    .getData();
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info(String.format("No Officers data found for Company Number %s", companyNumber), logMap);
                var officersApi = new OfficersApi();
                officersApi.setTotalResults(0);
                return officersApi;
            }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException | IOException e) {
            ApiLogger.error(String.format("Error Retrieving Officers data for %s", companyNumber), e, logMap);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public PscsApi getPSCs(String companyNumber, String passThroughTokenHeader) throws ServiceException{
        var logMap = new HashMap<String, Object>();

        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info(String.format("Retrieving PSCs data for Company Number %s", companyNumber), logMap);

        try {
            return apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader)
                    .pscs()
                    .list(COMPANY + companyNumber + "/persons-with-significant-control")
                    .execute()
                    .getData();
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info(String.format("No PSCs Data found for Company Number %s", companyNumber), logMap);
                var pscsApi = new PscsApi();
                pscsApi.setTotalResults(0L);
                return pscsApi;
            }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException | IOException e) {
            ApiLogger.error(String.format("Error Retrieving PSCs Data for %s", companyNumber), e, logMap);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
