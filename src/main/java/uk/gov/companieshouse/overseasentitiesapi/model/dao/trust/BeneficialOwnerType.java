package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

public enum BeneficialOwnerType {
    INTERESTED_PERSON("Interested Person"),
    GRANTOR("Grantor"),
    SETTLOR("Settlor"),
    BENEFICIARY("Beneficiary");

    private final String value;

    BeneficialOwnerType(String beneficialOwnerType) {
        this.value = beneficialOwnerType;
    }

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
