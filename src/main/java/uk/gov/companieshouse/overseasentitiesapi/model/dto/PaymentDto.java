package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDto {
    
    public static final String RESOURCE = "resource";
    public static final String STATE = "state";
    public static final String REDIRECT_URI = "redirectUri";
    public static final String REFERENCE = "reference";

    @JsonProperty(RESOURCE)
    private String paymentResource;

    @JsonProperty(STATE)
    private String paymentState;

    @JsonProperty(REDIRECT_URI)
    private String paymentRedirectUri;

    @JsonProperty(REFERENCE)
    private String paymentReference;

    public String getPaymentResource() {
        return paymentResource;
    }

    public void setPaymentResource(String resource) {
        this.paymentResource = resource;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getPaymentRedirectUri() {
        return paymentRedirectUri;
    }

    public void setPaymentRedirectUri(String paymentRedirectUri) {
        this.paymentRedirectUri = paymentRedirectUri;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
}
