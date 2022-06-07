package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

public enum BeneficialOwnerType {
    CORPORATE_INTERESTED_PERSON("Corporate Interested Person"),
    CORPORATE_GRANTOR("Corporate Grantor"),
    CORPORATE_SETTLER("Corporate Settler"),
    CORPORATE_BENEFICIARY("Corporate Beneficiary"),
    INDIVIDUAL_INTERESTED_PERSON("Individual Interested Person"),
    INDIVIDUAL_SETTLER("Individual Settler"),
    INDIVIDUAL_GRANTOR("Individual Grantor"),
    INDIVIDUAL_BENEFICIARY("Individual Beneficiary");

    private final String value;

    BeneficialOwnerType(String beneficialOwnerType) {
        this.value = beneficialOwnerType;
    }

    public static BeneficialOwnerType findByBeneficialOwnerTypeString(String beneficialOwnerType) {
        for (BeneficialOwnerType type : values()) {
            if(type.value.equals(beneficialOwnerType)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
