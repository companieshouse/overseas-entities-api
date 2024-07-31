package uk.gov.companieshouse.overseasentitiesapi.model;

public enum WhoIsRegisteringType {
    AGENT("agent"),
    SOMEONE_ELSE("someone_else");

    private final String whoIsRegistering;

    WhoIsRegisteringType(String whoIsRegistering) {
        this.whoIsRegistering = whoIsRegistering;
    }

    public String getValue() {
        return this.whoIsRegistering;
    }
}
