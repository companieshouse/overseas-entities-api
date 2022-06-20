package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.*;

@Service
public class FilingsService {

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION}")
    private String filingDescription;

    private final OverseasEntitiesService overseasEntitiesService;

    private final ApiClientService apiClientService;


    @Autowired
    public FilingsService(OverseasEntitiesService overseasEntitiesService, ApiClientService apiClientService) {
        this.overseasEntitiesService = overseasEntitiesService;
        this.apiClientService = apiClientService;
    }

    public FilingApi generateOverseasEntityFiling(String overseasEntityId, Transaction transaction)
            throws SubmissionNotFoundException, ServiceException {
        var filing = new FilingApi();
        filing.setKind(FILING_KIND_OVERSEAS_ENTITY);
        setFilingApiData(filing, overseasEntityId, transaction);
        return filing;
    }

    private void setFilingApiData(FilingApi filing, String overseasEntityId, Transaction transaction) throws SubmissionNotFoundException, ServiceException {
        Map<String, Object> data = new HashMap<>();

        setSubmissionData(data, overseasEntityId);
        setPaymentData(data, transaction, overseasEntityId);

        filing.setData(data);
        setDescription(filing);
    }

    private void setSubmissionData(Map<String, Object> data, String overseasEntityId) throws SubmissionNotFoundException {
        Optional<OverseasEntitySubmissionDto> submissionOpt =
                overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        OverseasEntitySubmissionDto submissionDto = submissionOpt
                .orElseThrow(() ->
                        new SubmissionNotFoundException(
                                String.format("Empty submission returned when generating filing for %s", overseasEntityId)));

        data.put(PRESENTER, submissionDto.getPresenter());
        data.put(ENTITY_FIELD, submissionDto.getEntity());
        data.put(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, submissionDto.getBeneficialOwnersIndividual());
        data.put(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, submissionDto.getBeneficialOwnersGovernmentOrPublicAuthority());
        data.put(BENEFICIAL_OWNERS_CORPORATE_FIELD, submissionDto.getBeneficialOwnersCorporate());
        data.put(MANAGING_OFFICERS_INDIVIDUAL_FIELD, submissionDto.getManagingOfficersIndividual());
        data.put(MANAGING_OFFICERS_CORPORATE_FIELD, submissionDto.getManagingOfficersCorporate());
        data.put(BENEFICIAL_OWNERS_STATEMENT, submissionDto.getBeneficialOwnersStatement());

        var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        ApiLogger.debug("Submission data has been set on filing", logMap);
    }

    private void setPaymentData(Map<String, Object> data, Transaction transaction, String overseasEntityId) throws ServiceException {
        var paymentLink = transaction.getLinks().getPayment();
        var isPayable = !Objects.isNull(paymentLink);

        if (isPayable) {
            var paymentReference = getPaymentReferenceFromTransaction(paymentLink);
            var payment = getPayment(paymentReference);

            data.put("payment_reference", paymentReference);
            data.put("payment_method", payment.getPaymentMethod());

            var logMap = new HashMap<String, Object>();
            logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
            logMap.put(TRANSACTION_ID_KEY, transaction.getId());
            ApiLogger.debug("Payment data has been set on filing", logMap);
        }
    }

    private PaymentApi getPayment(String paymentReference) throws ServiceException {
        try {
            return apiClientService
                    .getApiKeyAuthenticatedClient().payment().get("/payments/" + paymentReference).execute().getData();
        } catch (URIValidationException | ApiErrorResponseException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private String getPaymentReferenceFromTransaction(String uri) throws ServiceException {
        try {
            var transactionPaymentInfo = apiClientService
                    .getApiKeyAuthenticatedClient().transactions().getPayment(uri).execute();

            return transactionPaymentInfo.getData().getPaymentReference();
        } catch (URIValidationException | ApiErrorResponseException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void setDescription(FilingApi filing) {
        filing.setDescriptionIdentifier(filingDescription);
        Map<String, String> values = new HashMap<>();
        filing.setDescriptionValues(values);
    }
}
