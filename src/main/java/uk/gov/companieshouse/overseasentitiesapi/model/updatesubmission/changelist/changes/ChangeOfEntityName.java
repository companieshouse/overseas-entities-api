package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

public class ChangeOfEntityName extends Change {
    private final String CHANGE_NAME = "changeOfEntityName";

    @JsonProperty("proposedCorporateBodyName")
    private String proposedCorporateBodyName;

    public ChangeOfEntityName(String entityName){
        super.setChangeName(CHANGE_NAME);
        this.proposedCorporateBodyName = entityName;
    }

    public String getProposedCorporateBodyName() {
        return proposedCorporateBodyName;
    }

    public void setProposedCorporateBodyName(String proposedCorporateBodyName) {
        this.proposedCorporateBodyName = proposedCorporateBodyName;
    }
}
