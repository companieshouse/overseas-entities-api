package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsValidatorsTest {

    private static final String TO_TEST = "toTest";
    private static final String LOCATION = "location";
    private static final String LOGGING_CONTEXT ="12345";
    private Errors errors;

    @BeforeEach
    void setUp() {
        errors = new Errors();
    }

    @Test
    @DisplayName("Validate a string is not null successfully")
    void validateNotNull_Successful() {
        assertTrue(UtilsValidators.isValidNotNull(TO_TEST, LOCATION, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string is not null unsuccessfully - null object")
    void validateNotNull_Unsuccessful() {
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION)
                .withError(LOCATION + ValidationMessages.NOT_NULL_ERROR_MESSAGE).build();

        boolean isNotNull = UtilsValidators.isValidNotNull(null, LOCATION, errors, LOGGING_CONTEXT);

        assertFalse(isNotNull);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Check the error object has been updated successfully")
    void checkSetErrorMsgToLocation_Correctly() {
        String msgErr = LOCATION + ValidationMessages.NOT_NULL_ERROR_MESSAGE;
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION).withError(msgErr).build();

        UtilsValidators.setErrorMsgToLocation(errors, LOCATION, msgErr);

        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }
}
