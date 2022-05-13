package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.payment.Cost;

import java.util.Collections;

@Service
public class CostsService {

    private static final String COST_DESC = "Register Overseas Entity fee";
    private static final String PAYMENT_ACCOUNT = "data-maintenance";
    private static final String RESOURCE_KIND = "overseas-entity";
    private static final String PRODUCT_TYPE = "register-overseas-entity";
    private static final String CREDIT_CARD = "credit-card";
    private static final String DESCRIPTION_IDENTIFIER = "description-identifier";
    private static final String PAYMENT_SESSION = "payment-session#payment-session";
    private static final String KEY = "Key";
    private static final String VALUE = "Value";

    @Value("${OE01_COST}")
    private String costAmount;

    public Cost getCosts() {
        var cost = new Cost();
        cost.setAmount(costAmount);
        cost.setAvailablePaymentMethods(Collections.singletonList(CREDIT_CARD));
        cost.setClassOfPayment(Collections.singletonList(PAYMENT_ACCOUNT));
        cost.setDescription(COST_DESC);
        cost.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);
        cost.setDescriptionValues(Collections.singletonMap(KEY, VALUE));
        cost.setKind(PAYMENT_SESSION);
        cost.setResourceKind(RESOURCE_KIND);
        cost.setProductType(PRODUCT_TYPE);
        cost.setVariablePayment(false);

        return cost;
    }
}
