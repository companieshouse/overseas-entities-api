package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDto {
    
    public static final String RESOURCE = "resource";
    public static final String STATE = "state";
    public static final String REDIRECT_URI = "redirectUri";
    public static final String REFERENCE = "reference";

    @JsonProperty(RESOURCE)
    private String resource;

    @JsonProperty(STATE)
    private String state;

    @JsonProperty(REDIRECT_URI)
    private String redirectUri;

    @JsonProperty(REFERENCE)
    private String reference;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
