package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

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

@Service
public class FilingsService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION_IDENTIFIER}")
    private String filingDescriptionIdentifier;

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION}")
    private String filingDescription;

    private final OverseasEntitiesService overseasEntitiesService;
    private final Supplier<LocalDate> dateNowSupplier;

    @Autowired
    public FilingsService(OverseasEntitiesService overseasEntitiesService,
                          Supplier<LocalDate> dateNowSupplier) {
        this.overseasEntitiesService = overseasEntitiesService;
        this.dateNowSupplier = dateNowSupplier;
    }

    public FilingApi generateOverseasEntityFiling(String overseasEntityId)
            throws SubmissionNotFoundException {
        var filing = new FilingApi();
        filing.setKind(FILING_KIND_OVERSEAS_ENTITY);
        setFilingApiData(filing, overseasEntityId);
        return filing;
    }

    private void setFilingApiData(FilingApi filing, String overseasEntityId) throws SubmissionNotFoundException {
         Optional<OverseasEntitySubmissionDto> submissionOpt =
                overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        OverseasEntitySubmissionDto submissionDto = submissionOpt
                .orElseThrow(() ->
                        new SubmissionNotFoundException(
                                String.format("Empty submission returned when generating filing for %s", overseasEntityId)));

        setSubmissionData(filing, submissionDto);
    }

    private void setSubmissionData(FilingApi filing, OverseasEntitySubmissionDto submissionDto) {
        Map<String, Object> data = new HashMap<>();
        data.put(PRESENTER, submissionDto.getPresenter());
        data.put(ENTITY_FIELD, submissionDto.getEntity());
        data.put(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, submissionDto.getBeneficialOwnersIndividual());
        data.put(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, submissionDto.getBeneficialOwnersGovernmentOrPublicAuthority());
        data.put(BENEFICIAL_OWNERS_CORPORATE_FIELD, submissionDto.getBeneficialOwnersCorporate());
        data.put(MANAGING_OFFICERS_INDIVIDUAL_FIELD, submissionDto.getManagingOfficersIndividual());
        data.put(MANAGING_OFFICERS_CORPORATE_FIELD, submissionDto.getManagingOfficersCorporate());
        data.put(BENEFICIAL_OWNERS_STATEMENT, submissionDto.getBeneficialOwnersStatement());
        filing.setData(data);

        setDescriptionFields(filing);

        ApiLogger.debug("Submission data has been set on filing");
    }

    private void setDescriptionFields(FilingApi filing) {
        String formattedRegistrationDate = dateNowSupplier.get().format(formatter);
        filing.setDescriptionIdentifier(filingDescriptionIdentifier);
        filing.setDescription(filingDescription.replace("{registration date}", formattedRegistrationDate));
        Map<String, String> values = new HashMap<>();
        filing.setDescriptionValues(values);
    }
}
