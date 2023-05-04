package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ChangeListItem {
    @JsonProperty("change")
    private String changeName;

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }
}
