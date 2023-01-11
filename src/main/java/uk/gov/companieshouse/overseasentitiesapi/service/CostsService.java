package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.payment.Cost;

import java.util.Collections;

@Service
public class CostsService {

    private static final String COST_REGISTER_DESC = "Register Overseas Entity fee";

    private static final String COST_UPDATE_DESC = "Update Overseas Entity fee";

    private static final String PAYMENT_ACCOUNT = "data-maintenance";
    private static final String RESOURCE_KIND = "overseas-entity";
    private static final String PRODUCT_TYPE_REGISTER = "register-overseas-entity";

    private static final String PRODUCT_TYPE_UPDATE = "update-overseas-entity";

    private static final String CREDIT_CARD = "credit-card";
    private static final String DESCRIPTION_IDENTIFIER = "description-identifier";
    private static final String PAYMENT_SESSION = "payment-session#payment-session";
    private static final String KEY = "Key";
    private static final String VALUE = "Value";

    @Value("${OE01_COST}")
    private String costRegisterAmount;

    @Value("${OE01_UPDATE_COST}")
    private String costUpdateAmount;

    private final OverseasEntitiesService overseasEntitiesService;

    @Autowired
    public CostsService(OverseasEntitiesService overseasEntitiesService) {
        this.overseasEntitiesService = overseasEntitiesService;
    }

    public Cost getCosts(String overseasEntityId) {
        if (overseasEntitiesService.getSubmissionType(overseasEntityId) == SubmissionType.Update) {
            return getCostForUpdate();
        } else {
            return getCostForRegistration();
        }
    }

    private Cost getCostForRegistration() {
        var cost = new Cost();
        cost.setAmount(costRegisterAmount);
        cost.setAvailablePaymentMethods(Collections.singletonList(CREDIT_CARD));
        cost.setClassOfPayment(Collections.singletonList(PAYMENT_ACCOUNT));
        cost.setDescription(COST_REGISTER_DESC);
        cost.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);
        cost.setDescriptionValues(Collections.singletonMap(KEY, VALUE));
        cost.setKind(PAYMENT_SESSION);
        cost.setResourceKind(RESOURCE_KIND);
        cost.setProductType(PRODUCT_TYPE_REGISTER);

        return cost;
    }

    private Cost getCostForUpdate() {
        var cost = new Cost();
        cost.setAmount(costUpdateAmount);
        cost.setAvailablePaymentMethods(Collections.singletonList(CREDIT_CARD));
        cost.setClassOfPayment(Collections.singletonList(PAYMENT_ACCOUNT));
        cost.setDescription(COST_UPDATE_DESC);
        cost.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);
        cost.setDescriptionValues(Collections.singletonMap(KEY, VALUE));
        cost.setKind(PAYMENT_SESSION);
        cost.setResourceKind(RESOURCE_KIND);
        cost.setProductType(PRODUCT_TYPE_UPDATE);

        return cost;
    }

    public void setRegistrationCostAmount(String amount) {
        this.costRegisterAmount = amount;
    }

    public void setUpdateCostAmount(String amount) {
        this.costUpdateAmount = amount;
    }
}
