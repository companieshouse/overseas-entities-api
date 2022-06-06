package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeneficialOwnerTypes {

    private BeneficialOwnerTypes() {
    }

    public static final Map<String, String> beneficialOwnerTypes = initMap();

    public static Map<String, String> initMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Corporate Interested Person", "corporate_interested_person");
        map.put("Corporate Grantor", "corporate_grantor");
        map.put("Corporate Settler", "corporate_settler");
        map.put("Corporate Beneficiary", "corporate_beneficiary");
        map.put("Individual Interested Person", "individual_interested_person");
        map.put("Individual Grantor", "individual_grantor");
        map.put("Individual Settler", "individual_settler");
        map.put("Individual Beneficiary", "individual_beneficiary");
        return Collections.unmodifiableMap(map);
    }

}
