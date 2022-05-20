package uk.gov.companieshouse.overseasentitiesapi.model;

public enum CorporateType {

    CORPORATE_INTERESTED_PERSONS("corporate_interested_persons"),
    CORPORATE_GRANTORS("corporate_grantors"),
    CORPORATE_SETTLERS("corporate_settlers"),
    CORPORATE_BENEFICIARIES("corporate_beneficiaries");

    private final String corporateType;

    CorporateType(String corporateType) {
        this.corporateType = corporateType;
    }

    public static CorporateType findByCorporateType(String corporateType) {
        for (CorporateType type : values()) {
            if(type.corporateType.equals(corporateType)) {
                return type;
            }
        }
        return null;
    }
}
