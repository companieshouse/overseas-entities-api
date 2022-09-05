package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

public class DateValidators {

    private DateValidators() { }

    public static boolean isValidDateIsInPast(LocalDate compareToDate, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (compareToDate.isAfter(LocalDate.now())) {
            setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, qualifiedFieldName + " date should be in the past");
            return false;
        }

        return true;
    }

    public static boolean isValidDateIsWithinLast3Months(LocalDate compareToDate, String qualifiedFieldName, Errors errs, String loggingContext) {
        var localDateNow = LocalDate.now();
        if (compareToDate.isBefore(localDateNow) && compareToDate.isAfter(localDateNow.minusMonths(3))) {
            return true;
        }

        setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
        ApiLogger.infoContext(loggingContext, qualifiedFieldName + " date should be in the past");
        return false;
    }
}
