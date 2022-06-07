package uk.gov.companieshouse.overseasentitiesapi.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeneficialOwnerTypes {

    private BeneficialOwnerTypes() {
    }

    public static final Map<String, BeneficialOwnerType> beneficialOwnerTypes = initMap();

    public static Map<String, BeneficialOwnerType> initMap() {
        Map<String, BeneficialOwnerType> map = new HashMap<>();
        map.put("Corporate Interested Person", BeneficialOwnerType.CORPORATE_INTERESTED_PERSON);
        map.put("Corporate Grantor", BeneficialOwnerType.CORPORATE_GRANTOR);
        map.put("Corporate Settler", BeneficialOwnerType.CORPORATE_SETTLER);
        map.put("Corporate Beneficiary", BeneficialOwnerType.CORPORATE_BENEFICIARY);
        map.put("Individual Interested Person", BeneficialOwnerType.INDIVIDUAL_INTERESTED_PERSON);
        map.put("Individual Grantor", BeneficialOwnerType.INDIVIDUAL_GRANTOR);
        map.put("Individual Settler", BeneficialOwnerType.INDIVIDUAL_SETTLER);
        map.put("Individual Beneficiary", BeneficialOwnerType.INDIVIDUAL_BENEFICIARY);
        return Collections.unmodifiableMap(map);
    }

}
