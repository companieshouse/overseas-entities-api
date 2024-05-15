package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.List;

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

    public static boolean isCeasedDateOnOrAfterStartDate(LocalDate ceasedDate,
                                                         LocalDate startDate,
                                                         String qualifiedFieldName,
                                                         Errors errors,
                                                         String loggingContext) {
        return isDateOnOrAfterAnotherDate(ceasedDate, startDate, ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE, qualifiedFieldName, errors, loggingContext);
    }

    public static boolean isCeasedDateOnOrAfterCreationDate(LocalDate ceasedDate,
                                                            LocalDate creationDate,
                                                            String qualifiedFieldName,
                                                            Errors errors,
                                                            String loggingContext) {
        return isDateOnOrAfterAnotherDate(ceasedDate, creationDate, ValidationMessages.CEASED_DATE_BEFORE_CREATION_DATE_ERROR_MESSAGE, qualifiedFieldName, errors, loggingContext);
    }

    private static boolean isDateOnOrAfterAnotherDate(LocalDate aDate,
                                                      LocalDate anotherDate,
                                                      String validationErrorMessage,
                                                      String qualifiedFieldName,
                                                      Errors errors,
                                                      String loggingContext) {
        if (aDate.isBefore(anotherDate)) {
            setErrorMsgToLocation(errors, qualifiedFieldName, validationErrorMessage.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, validationErrorMessage.replace("%s", qualifiedFieldName));
            return false;
        }

        return true;
    }

    public static boolean isCeasedDateOnOrAfterIndividualsDateOfBirth(LocalDate trustCeasedDate,
                                                                           List<TrustIndividualDto> individuals,
                                                                           String qualifiedFieldName,
                                                                           Errors errors,
                                                                           String loggingContext) {

        for(TrustIndividualDto individual : individuals) {
            if (trustCeasedDate.isBefore(individual.getDateOfBirth())) {
                setErrorMsgToLocation(errors, qualifiedFieldName, ValidationMessages.CEASED_DATE_BEFORE_INDIVIDUALS_DATE_OF_BRITH_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
                ApiLogger.infoContext(loggingContext, qualifiedFieldName + " must be on or after all of the trust individual's dates of birth");
                return false;
            }
        }

        return true;
    }
}
