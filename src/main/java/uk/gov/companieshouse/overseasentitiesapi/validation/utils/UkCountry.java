package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

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

    public static boolean isValid(String countryName) {

        for (UkCountry ukCountry : values()) {
            if (ukCountry.countryName.equalsIgnoreCase(countryName)) {
                return true;
            }
        }
        return false;
    }
}
