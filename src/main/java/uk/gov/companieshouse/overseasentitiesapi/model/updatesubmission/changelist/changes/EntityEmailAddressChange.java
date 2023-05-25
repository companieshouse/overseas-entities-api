package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class EntityEmailAddressChange extends Change {
    private static final String CHANGE_NAME = "entityEmailAddress";

    @JsonInclude(NON_NULL)
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
