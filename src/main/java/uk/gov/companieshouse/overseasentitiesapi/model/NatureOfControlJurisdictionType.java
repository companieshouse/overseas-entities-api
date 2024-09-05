package uk.gov.companieshouse.overseasentitiesapi.model;

public enum NatureOfControlJurisdictionType {
    ENGLAND_AND_WALES("ENGLAND_AND_WALES"),
    SCOTLAND("SCOTLAND"),
    NORTHERN_IRELAND("NORTHERN_IRELAND");

    private final String natureOfControlJurisdiction;

    NatureOfControlJurisdictionType(String natureOfControlJurisdiction) {
        this.natureOfControlJurisdiction = natureOfControlJurisdiction;
    }

    public static NatureOfControlJurisdictionType findByNatureOfControlJurisdictionTypeString(String natureOfControlJurisdiction) {
        for (NatureOfControlJurisdictionType type: values()) {
            if(type.natureOfControlJurisdiction.equals(natureOfControlJurisdiction)) {
                return type;
            }
        }
        return null;
    }
}
