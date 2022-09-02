package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

public enum UkCountry {
    UNITED_KINGDOM("United Kingdom"),
    ENGLAND("England"),
    SCOTLAND("Scotland"),
    WALES("Wales"),
    NORTHERN_IRELAND("Northern Ireland");

     public final String countryName;

     UkCountry(String country) {
          this.countryName = country;
     }

     public static boolean isValid(String countryIn, String location, Errors errs, String loggingContext) {

          for(UkCountry ukCountry : values()) {
               if(ukCountry.countryName.equalsIgnoreCase(countryIn)) {
                    return true;
               }
          }
          setErrorMsgToLocation(errs, location, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE);
          ApiLogger.infoContext(loggingContext, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE);
          return false;
     }
}
