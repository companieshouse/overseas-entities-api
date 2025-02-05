package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDto {
    
    public static final String RESOURCE_FIELD = "resource";
    public static final String STATE_FIELD = "state";
    public static final String REDIRECT_URI_FIELD = "redirectUri";
    public static final String REFERENCE_FIELD = "reference";

    @JsonProperty(RESOURCE_FIELD)
    private String resource;

    @JsonProperty(STATE_FIELD)
    private String state;

    @JsonProperty(REDIRECT_URI_FIELD)
    private String redirectUri;

    @JsonProperty(REFERENCE_FIELD)
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
