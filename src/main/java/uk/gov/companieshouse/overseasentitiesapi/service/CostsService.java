package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.payment.Cost;

import java.util.Collections;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;

@Service
public class CostsService {

    private static final String REGISTER_COST_DESCRIPTION = "Register Overseas Entity fee";

    private static final String UPDATE_COST_DESCRIPTION = "Update Overseas Entity fee";

    private static final String PAYMENT_ACCOUNT = "data-maintenance";
    private static final String RESOURCE_KIND = "overseas-entity";
    private static final String REGISTER_PRODUCT_TYPE = "register-overseas-entity";
    private static final String UPDATE_PRODUCT_TYPE = "update-overseas-entity";
    private static final String CREDIT_CARD = "credit-card";
    private static final String DESCRIPTION_IDENTIFIER = "description-identifier";
    private static final String PAYMENT_SESSION = "payment-session#payment-session";
    private static final String KEY = "Key";
    private static final String VALUE = "Value";

    @Value("${OE01_COST}")
    private String registerCostAmount;

    @Value("${OE02_COST}")
    private String updateCostAmount;

    private final OverseasEntitiesService overseasEntitiesService;

    @Autowired
    public CostsService(OverseasEntitiesService overseasEntitiesService) {
        this.overseasEntitiesService = overseasEntitiesService;
    }

    public Cost getCosts(String requestId, String overseasEntityId) throws SubmissionNotFoundException {
        // TODO Return a correct 'Cost' object for a Remove submission
        if (overseasEntitiesService.isSubmissionAnUpdate(requestId, overseasEntityId)) {
            return getCostsForUpdate();
        } else {
            return getCostsForRegistration();
        }
    }

    public Cost getCostsForRegistration() {
        var cost = new Cost();
        cost.setAmount(registerCostAmount);
        cost.setAvailablePaymentMethods(Collections.singletonList(CREDIT_CARD));
        cost.setClassOfPayment(Collections.singletonList(PAYMENT_ACCOUNT));
        cost.setDescription(REGISTER_COST_DESCRIPTION);
        cost.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);
        cost.setDescriptionValues(Collections.singletonMap(KEY, VALUE));
        cost.setKind(PAYMENT_SESSION);
        cost.setResourceKind(RESOURCE_KIND);
        cost.setProductType(REGISTER_PRODUCT_TYPE);

        return cost;
    }

    private Cost getCostsForUpdate() {
        var cost = new Cost();
        cost.setAmount(updateCostAmount);
        cost.setAvailablePaymentMethods(Collections.singletonList(CREDIT_CARD));
        cost.setClassOfPayment(Collections.singletonList(PAYMENT_ACCOUNT));
        cost.setDescription(UPDATE_COST_DESCRIPTION);
        cost.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);
        cost.setDescriptionValues(Collections.singletonMap(KEY, VALUE));
        cost.setKind(PAYMENT_SESSION);
        cost.setResourceKind(RESOURCE_KIND);
        cost.setProductType(UPDATE_PRODUCT_TYPE);

        return cost;
    }
}
