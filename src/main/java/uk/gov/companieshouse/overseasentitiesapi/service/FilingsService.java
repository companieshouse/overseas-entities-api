package uk.gov.companieshouse.overseasentitiesapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.TRUST_DATA;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

@Service
public class FilingsService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION_IDENTIFIER}")
    private String filingDescriptionIdentifier;

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION}")
    private String filingDescription;

    @Value("${OVERSEAS_ENTITIES_UPDATE_FILING_DESCRIPTION}")
    private String updateFilingDescription;

    @Value("${OE01_COST}")
    private String registerCostAmount;

    @Value("${OE01_UPDATE_COST}")
    private String updateCostAmount;

    @Value("${FEATURE_FLAG_ENABLE_TRUSTS_CHIPS_1502023}")
    private boolean isTrustsSubmissionThroughWebEnabled;

    private final OverseasEntitiesService overseasEntitiesService;
    private final ApiClientService apiClientService;
    private final Supplier<LocalDate> dateNowSupplier;
    private final ObjectMapper objectMapper;

    private final PublicDataRetrievalService publicDataRetrievalService;

    @Autowired
    public FilingsService(OverseasEntitiesService overseasEntitiesService,
                          ApiClientService apiClientService,
                          Supplier<LocalDate> dateNowSupplier,
                          ObjectMapper objectMapper,
                          PublicDataRetrievalService publicDataRetrievalService) {
        this.overseasEntitiesService = overseasEntitiesService;
        this.apiClientService = apiClientService;
        this.dateNowSupplier = dateNowSupplier;
        this.objectMapper = objectMapper;
        this.publicDataRetrievalService = publicDataRetrievalService;
    }

    public FilingApi generateOverseasEntityFiling(String requestId, String overseasEntityId, Transaction transaction, String passThroughTokenHeader)
            throws SubmissionNotFoundException, ServiceException {
        var filing = new FilingApi();
        filing.setKind(FILING_KIND_OVERSEAS_ENTITY);
        setFilingApiData(filing, requestId, overseasEntityId, transaction, passThroughTokenHeader);
        return filing;
    }

    private void setFilingApiData(FilingApi filing, String requestId, String overseasEntityId, Transaction transaction, String passThroughTokenHeader) throws SubmissionNotFoundException, ServiceException {
        var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        Map<String, Object> userSubmission = new HashMap<>();

        OverseasEntitySubmissionDto submissionDto = setSubmissionData(userSubmission, overseasEntityId, logMap);
        setPaymentData(userSubmission, transaction, passThroughTokenHeader, logMap);

        if (submissionDto.isForUpdate()) {
            publicDataRetrievalService.getOverseasEntityPublicData(submissionDto.getEntityNumber(), passThroughTokenHeader);
        }

        filing.setData(userSubmission);
        if (overseasEntitiesService.isSubmissionAnUpdate(requestId, overseasEntityId)) {
            filing.setCost(updateCostAmount);
        } else {
            filing.setCost(registerCostAmount);
        }

        setDescriptionFields(filing, submissionDto.isForUpdate());
    }

    private OverseasEntitySubmissionDto setSubmissionData(Map<String, Object> data, String overseasEntityId, Map<String, Object> logMap) throws SubmissionNotFoundException, ServiceException {
        Optional<OverseasEntitySubmissionDto> submissionOpt =
                overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        OverseasEntitySubmissionDto submissionDto = submissionOpt
                .orElseThrow(() ->
                        new SubmissionNotFoundException(
                                String.format("Empty submission returned when generating filing for %s", overseasEntityId)));

        if (Objects.isNull(submissionDto.getEntityName())) {
            data.put(ENTITY_NAME_FIELD, null);
        } else {
            data.put(ENTITY_NAME_FIELD, submissionDto.getEntityName().getName());
        }
        data.put(PRESENTER_FIELD, submissionDto.getPresenter());
        data.put(ENTITY_FIELD, submissionDto.getEntity());
        data.put(DUE_DILIGENCE_FIELD, submissionDto.getDueDiligence());
        data.put(OVERSEAS_ENTITY_DUE_DILIGENCE, submissionDto.getOverseasEntityDueDiligence());
        data.put(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, getBeneficialOwnersIndividualSubmissionData(submissionDto));
        data.put(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, submissionDto.getBeneficialOwnersGovernmentOrPublicAuthority());
        data.put(BENEFICIAL_OWNERS_CORPORATE_FIELD, getBeneficialOwnersCorporateSubmissionData(submissionDto));
        data.put(MANAGING_OFFICERS_INDIVIDUAL_FIELD, submissionDto.getManagingOfficersIndividual());
        data.put(MANAGING_OFFICERS_CORPORATE_FIELD, submissionDto.getManagingOfficersCorporate());
        data.put(BENEFICIAL_OWNERS_STATEMENT, submissionDto.getBeneficialOwnersStatement());
        if (isTrustsSubmissionThroughWebEnabled) {
            data.put(TRUST_DATA, submissionDto.getTrusts());
        }
        ApiLogger.debug("Submission data has been set on filing", logMap);

        return submissionDto;
    }

    private List<BeneficialOwnerIndividualDto> getBeneficialOwnersIndividualSubmissionData(OverseasEntitySubmissionDto submissionDto) throws ServiceException {
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualSubmissionData = new ArrayList<>();

        if (Objects.isNull(submissionDto.getBeneficialOwnersIndividual())) {
            return beneficialOwnersIndividualSubmissionData;
        }

        for (BeneficialOwnerIndividualDto beneficialOwner : submissionDto.getBeneficialOwnersIndividual()) {

            if (!isTrustsSubmissionThroughWebEnabled) {
                String noTrustsMessage = "No trusts exist for this filing but a trust id is provided for BO Individual "
                        + beneficialOwner.getFirstName() + " " + beneficialOwner.getLastName();

                List<TrustDataDto> trustData = getTrustData(submissionDto, beneficialOwner.getTrustIds(),
                        noTrustsMessage);
                beneficialOwner.setTrustData(convertTrustDataToString(trustData));
            }
            beneficialOwnersIndividualSubmissionData.add(beneficialOwner);
        }

        return beneficialOwnersIndividualSubmissionData;
    }

    private List<BeneficialOwnerCorporateDto> getBeneficialOwnersCorporateSubmissionData(OverseasEntitySubmissionDto submissionDto) throws ServiceException {
        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateSubmissionData = new ArrayList<>();

        if (Objects.isNull(submissionDto.getBeneficialOwnersCorporate())) {
            return beneficialOwnersCorporateSubmissionData;
        }

        for (BeneficialOwnerCorporateDto beneficialOwner : submissionDto.getBeneficialOwnersCorporate()) {
            if (!isTrustsSubmissionThroughWebEnabled) {
                String noTrustsMessage = "No trusts exist for this filing but a trust id is provided for BO Corporate "
                        + beneficialOwner.getPublicRegisterName();


            List<TrustDataDto> trustData = getTrustData(submissionDto, beneficialOwner.getTrustIds(), noTrustsMessage);
            beneficialOwner.setTrustData(convertTrustDataToString(trustData));
        }
        beneficialOwnersCorporateSubmissionData.add(beneficialOwner);
    }

        return beneficialOwnersCorporateSubmissionData;
    }

    private List<TrustDataDto> getTrustData(OverseasEntitySubmissionDto submissionDto, List<String> trustIds, String noTrustsExceptionMessage) throws ServiceException {
        List<TrustDataDto> trustsDataForBO = new ArrayList<>();
        // Loop through each trustId(s) and lookup the information for that trust
        if (Objects.isNull(trustIds)) {
            return trustsDataForBO;
        }

        for (String trustId : trustIds) {
            List<TrustDataDto> trustData = new ArrayList<>();

            if (Objects.isNull(submissionDto.getTrusts())) {
                throw new ServiceException(noTrustsExceptionMessage);
            }
            for (TrustDataDto trust : submissionDto.getTrusts()) {
                if (trust.getTrustId().equals(trustId)) {
                    trustData.add(trust);
                }
            }

            // If there is more than one trust with the same ID throw an error
            if (trustData.size() > 1) {
                throw new ServiceException("There is more than one trust with the ID: " + trustId);
            }
            // If there are is no trust with that ID throw an error
            if (trustData.isEmpty()) {
                throw new ServiceException("There are no trusts for the ID: " + trustId);
            }

            trustsDataForBO.add(trustData.get(0));
        }

        return trustsDataForBO;
    }

    private String convertTrustDataToString(List<TrustDataDto> trustsDataForBO) throws ServiceException {
        String trustData = "";
        if (!trustsDataForBO.isEmpty()) {
            // Convert trust data to JSON string if it exists on transaction else it's to an empty string
            try {
                trustData = objectMapper.writeValueAsString(trustsDataForBO);
            } catch (JsonProcessingException e) {
                throw new ServiceException("Error converting trust data to JSON " + e.getMessage(), e);
            }
        }
        return trustData;
    }

    private void setPaymentData(Map<String, Object> data, Transaction transaction, String passthroughTokenHeader, Map<String, Object> logMap) throws ServiceException {
        var paymentLink = transaction.getLinks().getPayment();
        var paymentReference = getPaymentReferenceFromTransaction(paymentLink, passthroughTokenHeader);
        var payment = getPayment(paymentReference, passthroughTokenHeader);

        data.put("payment_reference", paymentReference);
        data.put("payment_method", payment.getPaymentMethod());
        ApiLogger.debug("Payment data has been set on filing", logMap);
    }

    private PaymentApi getPayment(String paymentReference, String passthroughTokenHeader) throws ServiceException {
        try {
            return apiClientService
                    .getOauthAuthenticatedClient(passthroughTokenHeader).payment().get("/payments/" + paymentReference).execute().getData();
        } catch (URIValidationException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private String getPaymentReferenceFromTransaction(String uri, String passthroughTokenHeader) throws ServiceException {
        try {
            var transactionPaymentInfo = apiClientService
                    .getOauthAuthenticatedClient(passthroughTokenHeader).transactions().getPayment(uri).execute();

            return transactionPaymentInfo.getData().getPaymentReference();
        } catch (URIValidationException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void setDescriptionFields(FilingApi filing, boolean isUpdateFiling) {
        String formattedDate = dateNowSupplier.get().format(formatter);
        filing.setDescriptionIdentifier(filingDescriptionIdentifier);
        if (isUpdateFiling) {
            filing.setDescription(updateFilingDescription.replace("{date}", formattedDate));
        } else {
            filing.setDescription(filingDescription.replace("{date}", formattedDate));
        }
        Map<String, String> values = new HashMap<>();
        filing.setDescriptionValues(values);
    }
}
