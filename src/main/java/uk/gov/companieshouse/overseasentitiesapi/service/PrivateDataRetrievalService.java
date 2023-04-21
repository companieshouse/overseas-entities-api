package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import java.io.IOException;
import java.util.HashMap;

@Component
public class PrivateDataRetrievalService {

    public static final String COMPANY_NUMBER = "company_number";

    private final ApiClientService apiClientService;

    private PrivateBoDataListApi beneficialOwnerData;


    public PrivateDataRetrievalService(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public void initialisePrivateData(String companyNumber) throws ServiceException {
        beneficialOwnerData = getBeneficialOwnersData(companyNumber);
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
