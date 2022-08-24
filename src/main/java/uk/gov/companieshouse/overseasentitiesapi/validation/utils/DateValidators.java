package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

public class DateValidators {

    private DateValidators() { }

    public static boolean validateDateIsInPast(LocalDate compareToDate, String location, Errors errs, String loggingContext) {
        if (compareToDate.isAfter(LocalDate.now())) {
            setErrorMsgToLocation(errs, location, location + ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext, location + " date should be in the past");
            return false;
        }

        return true;
    }

    public static boolean validateDateIsWithinLast3Months(LocalDate compareToDate, String location, Errors errs, String loggingContext) {
        if (!compareToDate.isAfter(LocalDate.now()) && compareToDate.isBefore(LocalDate.now().plusMonths(3))) {
            setErrorMsgToLocation(errs, location, location + ValidationMessages.DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext, location + " date should be in the past");
            return false;
        }

        return true;
    }
}
