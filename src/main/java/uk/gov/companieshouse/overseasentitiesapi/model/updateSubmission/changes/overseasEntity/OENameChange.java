package uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission.changes.overseasEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission.changes.Change;

public class OENameChange extends Change {
    private static final String CHANGE = "changeOfEntityName";
    @JsonProperty("proposedCorporateBodyName")
    private String proposedCorporateBodyName;

    public OENameChange(){
        super.setChangeName(CHANGE);
    }

    public String getProposedCorporateBodyName() {
        return proposedCorporateBodyName;
    }

    public void setProposedCorporateBodyName(String proposedCorporateBodyName) {
        this.proposedCorporateBodyName = proposedCorporateBodyName;
    }
}
