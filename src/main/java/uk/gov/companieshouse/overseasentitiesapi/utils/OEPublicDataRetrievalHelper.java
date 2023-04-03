package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;
import java.util.HashMap;

@Component
public class OEPublicDataRetrievalHelper {
    public static final String COMPANY_NUMBER = "company_number";

    private final ApiClientService apiClientService;

    public OEPublicDataRetrievalHelper(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public void getOverseasEntityPublicData(String companyNumber, String passThroughTokenHeader) throws ServiceException {
        getCompanyProfile(companyNumber, passThroughTokenHeader);
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
}
