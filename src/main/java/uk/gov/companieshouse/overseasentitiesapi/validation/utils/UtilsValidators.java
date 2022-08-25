package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

public class UtilsValidators {

    private UtilsValidators() { }

    public static boolean validateNotNull(Object toTest, String location, Errors errs, String loggingContext) {
        if (toTest == null) {
            setErrorMsgToLocation(errs, location,location + ValidationMessages.NOT_NULL_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext , location + " Field is null");
            return false;
        }
        return true;
    }

    public static void setErrorMsgToLocation(Errors errors, String location, String msg){
        final var error = Err.invalidBodyBuilderWithLocation(location).withError(msg).build();
        errors.addError(error);
    }

}
