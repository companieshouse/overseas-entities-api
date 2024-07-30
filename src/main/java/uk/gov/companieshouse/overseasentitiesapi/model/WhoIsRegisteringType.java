package uk.gov.companieshouse.overseasentitiesapi.model;

public enum WhoIsRegisteringType {
    AGENT("agent"),
    SOMEONE_ELSE("someone_else");

    private final String whoIsRegisteringType;

    WhoIsRegisteringType(String whoIsRegisteringType) {
        this.whoIsRegisteringType = whoIsRegisteringType;
    }

    public String getValue() {
        return this.whoIsRegisteringType;
    }
}
