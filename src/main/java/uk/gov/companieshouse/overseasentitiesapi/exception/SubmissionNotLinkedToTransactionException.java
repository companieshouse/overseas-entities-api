package uk.gov.companieshouse.overseasentitiesapi.exception;

public class SubmissionNotLinkedToTransactionException extends Exception {

    public SubmissionNotLinkedToTransactionException(String message) {
        super(message);
    }
}
