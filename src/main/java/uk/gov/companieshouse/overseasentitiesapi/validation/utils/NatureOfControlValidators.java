package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

public class NatureOfControlValidators {

    public static boolean checkAtLeastOneFieldHasValue(List<NatureOfControlType> fields, String qualifiedFieldName, Errors errs, String loggingContext) {

        if (fields.isEmpty()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext , qualifiedFieldName + " Field is empty");
            return false;
        }
        return true;
    }
}
