package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

public class ChangeOfEntityEmailAddress extends Change {
    private final String CHANGE_NAME = "entityEmailAddress";

    @JsonProperty("proposedNewEmailAddress")
    private String proposedNewEmailAddress;

    public ChangeOfEntityEmailAddress(String emailAddress){
        super.setChangeName(CHANGE_NAME);
        this.proposedNewEmailAddress = emailAddress;
    }

    public String getProposedNewEmailAddress() {
        return proposedNewEmailAddress;
    }

    public void setProposedNewEmailAddress(String proposedNewEmailAddress) {
        this.proposedNewEmailAddress = proposedNewEmailAddress;
    }
}
