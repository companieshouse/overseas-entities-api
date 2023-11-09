package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityNameChange extends Change {

    private static final String CHANGE_NAME = "changeOfEntityName";

    @JsonProperty("proposedCorporateBodyName")
    private String proposedCorporateBodyName;

    public EntityNameChange(String proposedCorporateBodyName) {
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
