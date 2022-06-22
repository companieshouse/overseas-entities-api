package uk.gov.companieshouse.overseasentitiesapi.service;

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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER;
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

    private final OverseasEntitiesService overseasEntitiesService;
    private final ApiClientService apiClientService;
    private final Supplier<LocalDate> dateNowSupplier;

    @Autowired
    public FilingsService(OverseasEntitiesService overseasEntitiesService,
                          ApiClientService apiClientService,
                          Supplier<LocalDate> dateNowSupplier) {
        this.overseasEntitiesService = overseasEntitiesService;
        this.apiClientService = apiClientService;
        this.dateNowSupplier = dateNowSupplier;
    }

    public FilingApi generateOverseasEntityFiling(String overseasEntityId, Transaction transaction, String passthroughTokenHeader)
            throws SubmissionNotFoundException, ServiceException {
        var filing = new FilingApi();
        filing.setKind(FILING_KIND_OVERSEAS_ENTITY);
        setFilingApiData(filing, overseasEntityId, transaction, passthroughTokenHeader);
        return filing;
    }

    private void setFilingApiData(FilingApi filing, String overseasEntityId, Transaction transaction, String passthroughTokenHeader) throws SubmissionNotFoundException, ServiceException {
        var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        Map<String, Object> data = new HashMap<>();

        setSubmissionData(data, overseasEntityId, logMap);
        setPaymentData(data, transaction, passthroughTokenHeader, logMap);

        filing.setData(data);
        setDescriptionFields(filing);
    }

    private void setSubmissionData(Map<String, Object> data, String overseasEntityId, Map<String, Object> logMap) throws SubmissionNotFoundException {
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
        ApiLogger.debug("Submission data has been set on filing", logMap);
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

    private void setDescriptionFields(FilingApi filing) {
        String formattedRegistrationDate = dateNowSupplier.get().format(formatter);
        filing.setDescriptionIdentifier(filingDescriptionIdentifier);
        filing.setDescription(filingDescription.replace("{registration date}", formattedRegistrationDate));
        Map<String, String> values = new HashMap<>();
        filing.setDescriptionValues(values);
    }
}
