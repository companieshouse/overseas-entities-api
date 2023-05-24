package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class EntityNameChange extends Change {
    private static final String CHANGE_NAME = "changeOfEntityName";

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedCorporateBodyName")
    private String proposedCorporateBodyName;

    public EntityNameChange(String proposedCorporateBodyName){
        super.setChangeName(CHANGE_NAME);
        this.proposedCorporateBodyName = proposedCorporateBodyName;
    }

    public String getProposedCorporateBodyName() {
        return proposedCorporateBodyName;
    }

    public void setProposedCorporateBodyName(String proposedCorporateBodyName) {
        this.proposedCorporateBodyName = proposedCorporateBodyName;
    }
}
