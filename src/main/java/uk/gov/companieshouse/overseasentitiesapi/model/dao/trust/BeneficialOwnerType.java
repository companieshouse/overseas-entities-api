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

    private final String beneficialOwnerType;

    BeneficialOwnerType(String beneficialOwnerType) {
        this.beneficialOwnerType = beneficialOwnerType;
    }

    public static BeneficialOwnerType findByBeneficialOwnerTypeString(String beneficialOwnerType) {
        for (BeneficialOwnerType type : values()) {
            if(type.beneficialOwnerType.equals(beneficialOwnerType)) {
                return type;
            }
        }
        return null;
    }
}
