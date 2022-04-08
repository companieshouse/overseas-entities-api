package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OE;

@Service
public class FilingsService {
    public FilingApi generateOverseasEntityFiling(String overseasEntityId, Transaction transaction)
            throws SubmissionNotFoundException, ServiceException {
        var filing = new FilingApi();
        filing.setKind(FILING_KIND_OE);
        return filing;
    }
}
