package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class IndividualType {
    public static final String INDIVIDUAL_SETTLERS = "Individual Settlers";
    public static final String INDIVIDUAL_GRANTORS = "Individual Grantors";
    public static final String INDIVIDUAL_BENEFICIARIES = "Individual Beneficiaries";
    public static final String INDIVIDUAL_INTERESTED_PERSONS = "Individual Interested Persons";

    private String value;

    IndividualType(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    /*
    public static IndividualType findByIndividualType(String individualType) {
        for (IndividualType type: values()) {
            if(type.individualType.equals(individualType)) {
                return type;
            }
        }
        return null;
    }

    private static Map<String, IndividualType> individualTypeMap = new HashMap<String, IndividualType>(4);

    static {
        individualTypeMap.put("Individual Settlers", INDIVIDUAL_SETTLERS);
        individualTypeMap.put("Individual Grantors", INDIVIDUAL_GRANTORS);
        individualTypeMap.put("Individual Beneficiaries", INDIVIDUAL_BENEFICIARIES);
        individualTypeMap.put("Individual Interested Persons", INDIVIDUAL_INTERESTED_PERSONS);
    }

    @JsonCreator
    public static IndividualType forValue(String value) {
        return individualTypeMap.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, IndividualType> entry : individualTypeMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null;
    }

     */


}
