package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.service.rest.err.Errors;

public enum UkCountry {
    UNITED_KINGDOM("United Kingdom"),
    ENGLAND("England"),
    NORTHERN_IRELAND("Northern Ireland"),
    SCOTLAND("Scotland"),
    WALES("Wales");

    public final String countryName;

    UkCountry(String country) {
        this.countryName = country;
    }

    public static boolean isValid(String countryIn) {

        for(UkCountry ukCountry : values()) {
            if(ukCountry.countryName.equalsIgnoreCase(countryIn)) {
                return true;
            }
        }
        return false;
    }
}
