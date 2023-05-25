package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityNameChange extends Change {
    private static final String CHANGE_NAME = "changeOfEntityName";

    @JsonInclude(NON_NULL)
    @JsonProperty("corporateBodyName")
    private String corporateBodyName;

    @JsonProperty("corporateBodyName")
    private String proposedCorporateBodyName;

    public EntityNameChange(String corporateBodyName, String proposedCorporateBodyName){
        super.setChangeName(CHANGE_NAME);
        this.corporateBodyName = corporateBodyName;
        this.proposedCorporateBodyName = proposedCorporateBodyName;
    }

    public String getCorporateBodyName() {
        return corporateBodyName;
    }

    public void setCorporateBodyName(String corporateBodyName) {
        this.corporateBodyName = corporateBodyName;
    }

    public String getProposedCorporateBodyName() {
        return proposedCorporateBodyName;
    }

    public void setProposedCorporateBodyName(String proposedCorporateBodyName) {
        this.proposedCorporateBodyName = proposedCorporateBodyName;
    }
}
