package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityEmailAddressChange extends Change {
    private static final String CHANGE_NAME = "entityEmailAddress";

    @JsonProperty("proposedEmailAddress")
    private String proposedEmailAddress;

    public EntityEmailAddressChange(String proposedEmailAddress){
        super.setChangeName(CHANGE_NAME);
        this.proposedEmailAddress = proposedEmailAddress;
    }

    public String getProposedEmailAddress() {
        return proposedEmailAddress;
    }

    public void setProposedEmailAddress(String proposedEmailAddress) {
        this.proposedEmailAddress = proposedEmailAddress;
    }
}
