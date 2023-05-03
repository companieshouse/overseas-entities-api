package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.additions;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Cessation {
    @JsonProperty("change")
    private String changeName;

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }
}
