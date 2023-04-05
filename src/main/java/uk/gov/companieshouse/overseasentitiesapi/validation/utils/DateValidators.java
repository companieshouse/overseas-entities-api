package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

public class DateValidators {

    private DateValidators() { }

    public static boolean isDateInPast(LocalDate compareToDate, String qualifiedFieldName, Errors errors, String loggingContext) {
        if (compareToDate.isAfter(LocalDate.now())) {
            setErrorMsgToLocation(errors, qualifiedFieldName, ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, qualifiedFieldName + " date should be in the past");
            return false;
        }

        return true;
    }

    public static boolean isDateWithinLast3Months(LocalDate compareToDate, String qualifiedFieldName, Errors errors, String loggingContext) {
        var localDateNow = LocalDate.now();
        if (compareToDate.isBefore(localDateNow.plusDays(1)) && compareToDate.isAfter(localDateNow.minusMonths(3))) {
            return true;
        }

        setErrorMsgToLocation(errors, qualifiedFieldName, ValidationMessages.DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
        ApiLogger.infoContext(loggingContext, qualifiedFieldName + " must be in the past 3 months");
        return false;
    }

    public static boolean isCeasedDateOnOrAfterStartDate(LocalDate ceasedDate, LocalDate startDate, String qualifiedFieldName, Errors errors, String loggingContext) {
        if (ceasedDate.isBefore(startDate)) {
            setErrorMsgToLocation(errors, qualifiedFieldName, ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            return false;
        }

        return true;
    }
}
