package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PresenterDto {

    public static final String FULL_NAME_FIELD = "full_name";

    public static final String EMAIL_PROPERTY_FIELD = "email";

    @JsonProperty(FULL_NAME_FIELD)
    private String fullName;

    @JsonProperty(EMAIL_PROPERTY_FIELD)
    private String email;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.isNull(email) ? null : email.trim();
    }
}
