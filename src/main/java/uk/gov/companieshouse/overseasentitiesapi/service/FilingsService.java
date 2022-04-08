package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OE;

@Service
public class FilingsService {

    @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION}")
    private String filingDescription;

    private final OverseasEntitiesService overseasEntitiesService;

    @Autowired
    public FilingsService(OverseasEntitiesService overseasEntitiesService) {
        this.overseasEntitiesService = overseasEntitiesService;
    }

    public FilingApi generateOverseasEntityFiling(String overseasEntityId, Transaction transaction)
            throws SubmissionNotFoundException, ServiceException {
        var filing = new FilingApi();
        filing.setKind(FILING_KIND_OE);
        setFilingApiData(filing, overseasEntityId, transaction);
        return filing;
    }

    private void setFilingApiData(FilingApi filing, String overseasEntityId, Transaction transaction) throws SubmissionNotFoundException, ServiceException {
         Optional<OverseasEntitySubmissionDto> submissionOpt =
                overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        OverseasEntitySubmissionDto submissionDto = submissionOpt
                .orElseThrow(() ->
                        new SubmissionNotFoundException(
                                String.format("Empty submission returned when generating filing for %s", overseasEntityId)));

        setSubmissionData(overseasEntityId, filing, submissionDto);
    }

    private void setSubmissionData(String overseasEntityId, FilingApi filing, OverseasEntitySubmissionDto submissionDto) throws SubmissionNotFoundException {
        Map<String, Object> data = new HashMap<>(); // TODO map object to kv in this stub
        data.put("name", submissionDto.getEntity().getName());
        if (submissionDto != null) {
            filing.setData(data);
            setDescription(filing);
        } else {
            throw new SubmissionNotFoundException(
                    String.format("Submission contains no data %s", overseasEntityId));
        }
    }

    private void setDescription(FilingApi filing) {
        filing.setDescriptionIdentifier(filingDescription);
        Map<String, String> values = new HashMap<>();
        filing.setDescriptionValues(values);
    }
}
