package uk.gov.companieshouse.overseasentitiesapi.service;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.api.model.utils.Hashable;
import uk.gov.companieshouse.api.model.utils.PrivateDataList;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

@Component
public class PrivateDataRetrievalService {

    private static final String COMPANY_NUMBER = "company_number";
    private static final String TRUST_ID = "trust_id";
    private static final String MANAGING_OFFICER_APPOINTMENT_ID = "managing_officer_appointment_id";
    private static final String OVERSEAS_ENTITY_URI_SECTION = "/overseas-entity/";
    private final ApiClientService apiClientService;

    private HashHelper hashHelper;

    public PrivateDataRetrievalService(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public void setHashHelper(HashHelper hashHelper) {
        this.hashHelper = hashHelper;
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

            if (managingOfficerDataList != null
                    && managingOfficerDataList.getManagingOfficerData() != null
                    && !managingOfficerDataList.getManagingOfficerData().isEmpty()) {
                logMap.put(MANAGING_OFFICER_APPOINTMENT_ID,
                        managingOfficerDataList.getManagingOfficerData().get(0)
                                .getManagingOfficerAppointmentId());
            }

            return managingOfficerDataList;
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info(
                        "No Managing Officers data found for Company Number " + companyNumber,
                        logMap);
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
            var overseasEntityDataApi = apiClientService.getInternalApiClient()
                    .privateOverseasEntityDataHandler()
                    .getOverseasEntityData(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/entity-data")
                    .execute()
                    .getData();

            logMap.put(COMPANY_NUMBER, companyNumber);
            ApiLogger.info("Retrieving overseas entity data for company number ", logMap);

            return overseasEntityDataApi;
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No overseas entity data for Company Number " + companyNumber,
                        logMap);
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
                    .getBeneficialOwners( OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/beneficial-owners")
                    .execute()
                    .getData();

            if (beneficialOwnerDataList != null
                    && beneficialOwnerDataList.getBoPrivateData() != null
                    && !beneficialOwnerDataList.getBoPrivateData().isEmpty()) {
                int numberOfBOs = beneficialOwnerDataList.getBoPrivateData().size();
                ApiLogger.info(String.format("Retrieved %d Beneficial Owners for Company Number %s",
                        numberOfBOs, companyNumber));
            }

            return beneficialOwnerDataList;
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Beneficial Owners found for Company Number " + companyNumber,
                        logMap);
                return new PrivateBoDataListApi(Collections.emptyList());
            }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException e) {
            var message = "Error Retrieving Beneficial Owner data for " + companyNumber;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public PrivateCorporateTrusteeListApi getCorporateTrustees(String hashedTrustId,
            String companyNumber) throws ServiceException {

        var logMap = new HashMap<String, Object>();

        String nonHashedId = findMatchingId(hashedTrustId, companyNumber);

        logMap.put(TRUST_ID, nonHashedId);

        if (nonHashedId == null) {
            ApiLogger.info("Non-hashed ID could not be found for Hashed ID: " + hashedTrustId,
                    logMap);
            throw new ServiceException(
                    String.format("Non-hashed ID could not be found for Hashed ID: %s",
                            hashedTrustId));
        }

        ApiLogger.info("Retrieving Corporate Trustee data for Trust Id" + nonHashedId, logMap);

        try {
            PrivateCorporateTrusteeListApi corporateTrustees = apiClientService.getInternalApiClient()
                    .privateCorporateTrusteeDataResourceHandler().getCorporateTrusteeData(
                            OVERSEAS_ENTITY_URI_SECTION + "trusts/" + nonHashedId
                                    + "/corporate-trustees").execute().getData();

            if (corporateTrustees != null && corporateTrustees.getData() != null
                    && !corporateTrustees.getData().isEmpty()) {
                ApiLogger.info(String.format("Retrieved %d Trusts for Company Number %s",
                        corporateTrustees.getData().size(), hashedTrustId));
            }

            return corporateTrustees;

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Corporate Trustee found for Trust Id " + hashedTrustId, logMap);
                return new PrivateCorporateTrusteeListApi(Collections.emptyList());
            }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException e) {
            var message = "Error Retrieving Corporate Trustee data for Trust Id " + hashedTrustId;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }

    }

    public PrivateTrustDetailsListApi getTrustDetails(String companyNumber)
            throws ServiceException {
        var logMap = new HashMap<String, Object>();
        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info("Retrieving Trusts for Company Number " + companyNumber, logMap);

        try {
            PrivateTrustDetailsListApi trusts = apiClientService.getInternalApiClient()
                    .privateTrustDetailsResourceHandler().getTrustDetails(
                            OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/trusts/details")
                    .execute().getData();

            if (trusts != null && trusts.getData() != null && !trusts.getData().isEmpty()) {
                ApiLogger.info(String.format("Retrieved %d Trusts for Company Number %s",
                        trusts.getData().size(), companyNumber));
            }

            return trusts;
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Trusts found for Company Number " + companyNumber, logMap);
                return new PrivateTrustDetailsListApi(Collections.emptyList());
            }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException e) {
            var message = "Error Retrieving Trust data for " + companyNumber;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private String findMatchingId(String hashedId, String companyNumber) throws ServiceException {
        PrivateTrustDetailsListApi details = getTrustDetails(companyNumber);
        return getHashedIdMap(details, hashedId);
    }

    private <T extends Hashable> String getHashedIdMap(PrivateDataList<T> hashableDataList, String hashedIdFromEndpoint) throws ServiceException {
        Map<String, String> output = new HashMap<>();
        for (Hashable hashableData : hashableDataList) {
            try {
                String hashedId = hashHelper.encode(hashableData.getId());
                if(hashedIdFromEndpoint.equals(hashedId)){
                    return hashableData.getId();
                }
            } catch (NoSuchAlgorithmException e) {
                throw new ServiceException("Cannot encode ID", e);
            }
        }
        return null;
    }

}