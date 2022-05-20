package uk.gov.companieshouse.overseasentitiesapi.model;

public enum IndividualType {

    INDIVIDUAL_SETTLERS("individual_settlers"),
    INDIVIDUAL_GRANTORS("individual_grantors"),
    INDIVIDUAL_BENEFICIARIES("individual_beneficiaries"),
    INDIVIDUAL_INTERESTED_PERSONS("individual_interested_persons");

    private final String individualType;

    IndividualType(String individualType) {
        this.individualType = individualType;
    }

    public static IndividualType findByIndividualType(String individualType) {
        for (IndividualType type: values()) {
            if(type.individualType.equals(individualType)) {
                return type;
            }
        }
        return null;
    }
}
