package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BeneficialOwnerType {
    INTERESTED_PERSON("Interested_Person"),
    GRANTOR("Grantor"),
    SETTLOR("Settlor"),
    BENEFICIARY("Beneficiary");

    private final String value;

    BeneficialOwnerType(String beneficialOwnerType) {
        this.value = beneficialOwnerType;
    }

    @JsonCreator
    public static BeneficialOwnerType findByBeneficialOwnerTypeString(String beneficialOwnerType) {
        for (BeneficialOwnerType type : values()) {
            if(type.value.equalsIgnoreCase(beneficialOwnerType)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
