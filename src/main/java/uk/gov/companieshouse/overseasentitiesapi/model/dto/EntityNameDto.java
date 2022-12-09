package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityNameDto {
    public static final String NAME_FIELD = "name";

    @JsonProperty(NAME_FIELD)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
