package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

public enum UkCountry {
    ENGLAND("England"),
    NORTHERN_IRELAND("Northern Ireland"),
    SCOTLAND("Scotland"),
    WALES("Wales");

    public final String countryName;

    UkCountry(String country) {
        this.countryName = country;
    }

    public static boolean isValid(String countryName, String location, Errors errs, String loggingContext) {

        for (UkCountry ukCountry : values()) {
            if (ukCountry.countryName.equalsIgnoreCase(countryName)) {
                return true;
            }
        }
        setErrorMsgToLocation(errs, location, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE);
        ApiLogger.infoContext(loggingContext, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE);
        return false;
    }
}
