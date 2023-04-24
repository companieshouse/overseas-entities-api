package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

@Component
public class PublicDataRetrievalService {
    public static final String COMPANY_NUMBER = "company_number";
    public static final String PSCS_NAME = "pscs_name";

    private final ApiClientService apiClientService;

    private CompanyProfileApi companyProfile;
    private PscsApi pscs;

    public PublicDataRetrievalService(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public void getOverseasEntityPublicData(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        this.companyProfile = getCompanyProfile(companyNumber, passThroughTokenHeader);
        this.pscs = getPSCs(companyNumber, passThroughTokenHeader);
    }

    private CompanyProfileApi getCompanyProfile(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        var logMap = new HashMap<String, Object>();
        ApiLogger.info("Retrieving Company Profile for Company Number ", logMap);

        try {
            CompanyProfileApi companyProfileData = apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader)
                    .company()
                    .get("/company/" + companyNumber)
                    .execute()
                    .getData();

            logMap.put(COMPANY_NUMBER, companyProfileData.getCompanyNumber());

            return companyProfileData;
        } catch (URIValidationException | IOException e) {
            logMap.put(COMPANY_NUMBER, companyNumber);

            var message = "Error Retrieving Company Profile data for " + companyNumber;
            ApiLogger.error(message, e, logMap);
            throw new ServiceException(e.getMessage(), e);
        }
    }


    private PscsApi getPSCs(String companyNumber, String passthroughTokenHeader) throws ServiceException{
        var logMap = new HashMap<String, Object>();
        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.debug("Retrieving PSCs data for Company Number", logMap);

        try {
            ApiResponse<PscsApi> pscsApiResponse = apiClientService
                    .getOauthAuthenticatedClient(passthroughTokenHeader)
                    .pscs()
                    .list("/company/" + companyNumber + "/persons-with-significant-control")
                    .execute();

            PscsApi pscsData = pscsApiResponse.getData();

            if (pscsApiResponse.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                logMap.put(COMPANY_NUMBER, companyNumber);
                ApiLogger.info("No PSCs for Company Number " + logMap);
            } else {
                for(PscApi psc :  pscsData.getItems()) {
                    logMap.put(PSCS_NAME, psc.getName());
                    ApiLogger.debug("Retrieved PSCs data for PSC name ", logMap);
                }
            }

            return pscsData;
        } catch (URIValidationException | IOException e) {
            var message = "Error Retrieving PSCs Data for " + companyNumber;
            ApiLogger.error(message, e, logMap);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
