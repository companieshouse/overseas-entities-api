package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateValidatorsTest {
    private static final LocalDate PAST_DATE_TEST = LocalDate.now().minusDays(3);
    private static final LocalDate FUTURE_DATE_TEST = LocalDate.now().plusDays(3);
    private static final String DUMMY_PARENT_FIELD = "parentField";
    private static final String LOGGING_CONTEXT ="12345";
    private Errors errors;

    @BeforeEach
    void setUp() {
        errors = new Errors();
    }

    @Test
    @DisplayName("Validate date is in the past successfully")
    void validateDateIsInPast_Successful() {
        assertTrue(DateValidators.isDateIsInPast(PAST_DATE_TEST, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate date is in the past unsuccessfully - date in the future")
    void validateDateIsInPast_Unsuccessful() {
        String errMsg = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", DUMMY_PARENT_FIELD);
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();

        boolean isValidDate = DateValidators.isDateIsInPast(FUTURE_DATE_TEST, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isValidDate);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate date is within last 3 months successfully")
    void validateDateIsWithinLast3Months_Successful() {
        assertTrue(DateValidators.isDateIsWithinLast3Months(PAST_DATE_TEST, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate date when date is today successfully")
    void validateDateIsWithinLast3Months_WithTodaysDate() {
        assertTrue(DateValidators.isDateIsWithinLast3Months(LocalDate.now(), DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate date is within last 3 months unsuccessfully - past 5 months")
    void validateDateIsWithinLast3Months_Unsuccessful() {
        String errMsg = ValidationMessages.DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE.replace("%s", DUMMY_PARENT_FIELD);
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();

        boolean isValidDate = DateValidators.isDateIsWithinLast3Months(LocalDate.now().minusMonths(5), DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isValidDate);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }
}
