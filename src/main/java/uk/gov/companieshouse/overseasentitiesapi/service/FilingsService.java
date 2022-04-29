package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;

@Service
public class FilingsService {

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION}")
    private String filingDescription;

    private final OverseasEntitiesService overseasEntitiesService;

    @Autowired
    public FilingsService(OverseasEntitiesService overseasEntitiesService) {
        this.overseasEntitiesService = overseasEntitiesService;
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
        data.put("entity", submissionDto.getEntity());
        filing.setData(data);
        setDescription(filing);

        ApiLogger.debug("Submission data has been set on filing");
    }

    private void setDescription(FilingApi filing) {
        filing.setDescriptionIdentifier(filingDescription);
        Map<String, String> values = new HashMap<>();
        filing.setDescriptionValues(values);
    }
}
