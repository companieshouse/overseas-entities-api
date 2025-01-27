package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

public class PaymentDao {

    @Field("resource")
    private String paymentResource;

    @Field("state")
    private String paymentState;

    @Field("redirect_uri")
    private String paymentRedirectUri;

    @Field("reference")
    private String paymentReference;

    public String getPaymentResource() {
        return paymentResource;
    }

    public void setPaymentResource(String paymentResource) {
        this.paymentResource = paymentResource;
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
