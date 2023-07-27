package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;

public class UpdateSubmissionMock {
    public static UpdateSubmission getUpdateSubmissionMock(){
        UpdateSubmission updateSubmissionMock = new UpdateSubmission();
        updateSubmissionMock.setEntityName("Joe Bloggs Ltd");
        return updateSubmissionMock;
    }
}
