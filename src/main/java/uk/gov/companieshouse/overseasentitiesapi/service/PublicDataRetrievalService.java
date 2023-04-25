package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.OfficersApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
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
    public static final String OFFICER_NAME = "officer_name";
    public static final String PSCS_NAME = "pscs_name";

    private final ApiClientService apiClientService;

    private CompanyProfileApi companyProfile;
    private OfficersApi officers;
    private PscsApi pscs;

    public PublicDataRetrievalService(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public OfficersApi getOfficerApi() {
        return officers;
    }

    public PscsApi getPscsApi() {
        return pscs;
    }

    public CompanyProfileApi getCompanyProfile() {
        return companyProfile;
    }

    public void initialisePublicData(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        this.companyProfile = getCompanyProfile(companyNumber, passThroughTokenHeader);
        this.officers = getOfficers(companyNumber, passThroughTokenHeader);
        this.pscs = getPSCs(companyNumber, passThroughTokenHeader);
    }

    private CompanyProfileApi getCompanyProfile(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        var logMap = new HashMap<String, Object>();

        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info("Retrieving Company Profile for Company Number ", logMap);

        try {
            return apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader)
                    .company()
                    .get("/company/" + companyNumber)
                    .execute()
                    .getData();
        } catch (URIValidationException | IOException e) {
            var message = "Error Retrieving Company Profile data for " + companyNumber;
            ApiLogger.error(message, e, logMap);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private OfficersApi getOfficers(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        var logMap = new HashMap<String, Object>();

        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.debug("Retrieving Officers data for Company Number ", logMap);

        try {
            ApiResponse<OfficersApi> officersApiResponse = apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader)
                    .officers()
                    .list("/company/" + companyNumber + "/officers")
                    .execute();

            var officersData = officersApiResponse.getData();

            if (officersApiResponse.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Officer for Company Number ", logMap);
            } else {
                for (CompanyOfficerApi officer : officersData.getItems()) {
                    logMap.put(OFFICER_NAME, officer.getName());
                    ApiLogger.debug("Retrieved Officers data for Officer name ", logMap);
                }
            }

            return officersData;
        } catch (URIValidationException | IOException e) {
            var message = "Error Retrieving Officers data for " + companyNumber;
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
