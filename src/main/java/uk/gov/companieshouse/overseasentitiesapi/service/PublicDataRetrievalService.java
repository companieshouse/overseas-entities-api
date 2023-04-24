package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

@Component
public class OEPublicDataRetrievalHelper {
    public static final String COMPANY_NUMBER = "company_number";
    public static final String PSCS_NAME = "pscs_name";

    private final ApiClientService apiClientService;

    public OEPublicDataRetrievalHelper(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public void getOverseasEntityPublicData(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        getCompanyProfile(companyNumber, passThroughTokenHeader);
        getCompanyPSCs(companyNumber, passThroughTokenHeader);
    }

    private CompanyProfileApi getCompanyProfile(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        try {
            CompanyProfileApi companyProfileData = apiClientService
                    .getOauthAuthenticatedClient(passThroughTokenHeader).company().get("/company/" + companyNumber).execute().getData();

            var logMap = new HashMap<String, Object>();
            logMap.put(COMPANY_NUMBER, companyProfileData.getCompanyNumber());

            ApiLogger.info("Retrieving Company Profile for Company Number " + logMap);

            return companyProfileData;
        } catch (URIValidationException | IOException e) {
            var message = "Error Retrieving Company Profile data for " + companyNumber;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }


    private PscsApi getCompanyPSCs(String companyNumber, String passthroughTokenHeader) throws ServiceException{
        try {
            ApiResponse<PscsApi> pscsApiResponse = apiClientService
                    .getOauthAuthenticatedClient(passthroughTokenHeader).pscs()
                    .list("/company/" + companyNumber + "/persons-with-significant-control").execute();

            PscsApi pscsData = pscsApiResponse.getData();

            var logMap = new HashMap<String, Object>();

            if (pscsApiResponse.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                logMap.put(COMPANY_NUMBER, companyNumber);
                ApiLogger.info("No PSCs for Company Number " + logMap);
            } else {
                logMap.put(PSCS_NAME, pscsData.getItems().get(0).getName());
                ApiLogger.info("Retrieving PSCs data for PSC name " + logMap);
            }

            return pscsData;
        } catch (URIValidationException | IOException e) {
            var message = "Error Retrieving PSCs Data for " + companyNumber;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
