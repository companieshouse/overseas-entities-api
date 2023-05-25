package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityEmailAddressChange extends Change {
    private static final String CHANGE_NAME = "entityEmailAddress";

    @JsonInclude(NON_NULL)
    @JsonProperty("emailAddress")
    private String emailAddress;

    @JsonProperty("proposedEmailAddress")
    private String proposedEmailAddress;

    public EntityEmailAddressChange(String emailAddress, String proposedEmailAddress){
        super.setChangeName(CHANGE_NAME);
        this.emailAddress = emailAddress;
        this.proposedEmailAddress = proposedEmailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProposedEmailAddress() {
        return proposedEmailAddress;
    }

    public void setProposedEmailAddress(String proposedEmailAddress) {
        this.proposedEmailAddress = proposedEmailAddress;
    }
}
