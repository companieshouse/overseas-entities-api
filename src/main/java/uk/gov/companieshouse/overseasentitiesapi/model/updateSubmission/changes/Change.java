package uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission.changes;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Change {
    @JsonProperty("change")
    private String changeName;

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }
}
