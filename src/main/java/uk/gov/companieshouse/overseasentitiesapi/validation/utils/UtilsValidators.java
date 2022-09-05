package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

public class UtilsValidators {

    private UtilsValidators() { }

    public static boolean isValidNotNull(Object toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (toTest == null) {
            setErrorMsgToLocation(errs, qualifiedFieldName,qualifiedFieldName + ValidationMessages.NOT_NULL_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext , qualifiedFieldName + " Field is null");
            return false;
        }
        return true;
    }

    public static void setErrorMsgToLocation(Errors errors, String qualifiedFieldName, String msg){
        final var error = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(msg).build();
        errors.addError(error);
    }

}
