package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.cessations;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Addition {
    @JsonProperty("change")
    private String changeName;

    @JsonProperty("kind")
    private String kind;

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
