package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.trustees.corporatetrustee.PrivateCorporateTrusteeListApi;
import uk.gov.companieshouse.api.model.trustees.individualtrustee.PrivateIndividualTrusteeListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustLinksListApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.api.model.utils.Hashable;
import uk.gov.companieshouse.api.model.utils.PrivateDataList;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

import jakarta.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;

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
                        managingOfficerDataList.getManagingOfficerData().getFirst()
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
                    .getBeneficialOwners(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/beneficial-owners")
                    .execute()
                    .getData();

            if (beneficialOwnerDataList != null
                    && beneficialOwnerDataList.getBoPrivateData() != null
                    && !beneficialOwnerDataList.getBoPrivateData().isEmpty()) {
                int numberOfBOs = beneficialOwnerDataList.getBoPrivateData().size();
                ApiLogger.info(String.format("Retrieved %d Beneficial Owners for Company Number %s",
                        numberOfBOs, companyNumber));
            } else {
                ApiLogger.info(String.format("Beneficial Owners list was either empty or null for Company Number %s",
                        companyNumber), logMap);
            }

            return beneficialOwnerDataList;
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Beneficial Owners found for Company Number " + companyNumber,
                        logMap);
                return new PrivateBoDataListApi(Collections.emptyList());
            }
            ApiLogger.errorContext(String.format("Error retrieving Beneficial Owners for Company Number %s", companyNumber), e);
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
        writeLogs(logMap, hashedTrustId, companyNumber, nonHashedId, "Corporate Trustee");

        try {
            PrivateCorporateTrusteeListApi corporateTrustees = apiClientService.getInternalApiClient()
                    .privateCorporateTrusteeDataResourceHandler()
                    .getCorporateTrusteeData(OVERSEAS_ENTITY_URI_SECTION + "trusts/" + nonHashedId + "/corporate-trustees")
                    .execute()
                    .getData();

            if (corporateTrustees != null && corporateTrustees.getData() != null
                    && !corporateTrustees.getData().isEmpty()) {
                ApiLogger.info(String.format("Retrieved %d Corporate Trustee for Company Number %s",
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

    public PrivateIndividualTrusteeListApi getIndividualTrustees(String hashedTrustId,
                                                                 String companyNumber) throws ServiceException {

        var logMap = new HashMap<String, Object>();
        String nonHashedId = findMatchingId(hashedTrustId, companyNumber);
        writeLogs(logMap, hashedTrustId, companyNumber, nonHashedId, "Individual Trustee");

        try {
            PrivateIndividualTrusteeListApi individualTrustees = apiClientService.getInternalApiClient()
                    .privateIndividualTrusteeDataResourceHandler()
                    .getIndividualTrusteeData(OVERSEAS_ENTITY_URI_SECTION + "trusts/" + nonHashedId + "/individual-trustees")
                    .execute()
                    .getData();

            if (individualTrustees != null && individualTrustees.getData() != null
                    && !individualTrustees.getData().isEmpty()) {
                ApiLogger.info(String.format("Retrieved %d Individual Trustee for Company Number %s",
                        individualTrustees.getData().size(), hashedTrustId));
            }

            return individualTrustees;

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Individual Trustee found for Trust Id " + hashedTrustId, logMap);
                return new PrivateIndividualTrusteeListApi(Collections.emptyList());
            }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException e) {
            var message = "Error Retrieving Individual Trustee data for Trust Id " + hashedTrustId;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    protected String findMatchingId(String hashedTrustId, String companyNumber) throws ServiceException {
        PrivateTrustDetailsListApi details = getTrustDetails(companyNumber);
        return getHashedId(details, hashedTrustId);
    }

    public PrivateTrustDetailsListApi getTrustDetails(String companyNumber) throws ServiceException {
        var logMap = new HashMap<String, Object>();
        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info("Retrieving Trusts for Company Number " + companyNumber, logMap);

        try {
            PrivateTrustDetailsListApi trusts = apiClientService.getInternalApiClient()
                    .privateTrustDetailsResourceHandler()
                    .getTrustDetails(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/trusts/details")
                    .execute()
                    .getData();

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
            var message = "Error retrieving Trust data for " + companyNumber;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public PrivateTrustLinksListApi getTrustLinks(String companyNumber) throws ServiceException {
        var logMap = new HashMap<String, Object>();
        logMap.put(COMPANY_NUMBER, companyNumber);
        ApiLogger.info("Retrieving Beneficial Owner Trust Links for Company Number " + companyNumber, logMap);
        
        try {
            PrivateTrustLinksListApi trustsLinks = apiClientService.getInternalApiClient()
                    .privateTrustLinksResourceHandler()
                    .getTrustLinks(OVERSEAS_ENTITY_URI_SECTION + companyNumber + "/trusts/beneficial-owners/links")
                    .execute()
                    .getData();

            if (trustsLinks != null && trustsLinks.getData() != null && !trustsLinks.getData().isEmpty()) {
                ApiLogger.info(String.format("Retrieved %d Beneficial Owners Trust links for Company Number %s",
                        trustsLinks.getData().size(), companyNumber));
            }

            return trustsLinks;
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
                ApiLogger.info("No Beneficial Owner Trust links found for Company Number " + companyNumber, logMap);
                return new PrivateTrustLinksListApi(Collections.emptyList());
        }
            throw new ServiceException(e.getStatusMessage(), e);
        } catch (URIValidationException e) {
            var message = "Error Retrieving Beneficial Owner Trust links data for " + companyNumber;
            ApiLogger.errorContext(message, e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public <T extends Hashable> String getHashedId(PrivateDataList<T> hashableDataList, String hashedIdFromEndpoint) throws ServiceException {
        for (Hashable hashableData : hashableDataList) {
            try {
                String hashedId = hashHelper.encode(hashableData.getId());
                if (hashedIdFromEndpoint.equals(hashedId)) {
                    return hashableData.getId();
                }
            } catch (NoSuchAlgorithmException e) {
                throw new ServiceException("Cannot encode ID", e);
            }
        }
        return null;
    }

    private void writeLogs(HashMap<String, Object> logMap, String hashedTrustId, String companyNumber, String nonHashedId, String trusteeType) throws ServiceException {
        logMap.put(TRUST_ID, nonHashedId);
        logMap.put(COMPANY_NUMBER, companyNumber);

        if (nonHashedId == null) {
            var message = "Non-hashed ID could not be found for Hashed ID: " + hashedTrustId;
            ApiLogger.error(message, new IllegalArgumentException(message), logMap);
            throw new ServiceException(message);
        }

        var logMessage = String.format("Retrieving %s data for Trust Id: %s", trusteeType, nonHashedId);
        ApiLogger.info(logMessage, logMap);
    }
}
