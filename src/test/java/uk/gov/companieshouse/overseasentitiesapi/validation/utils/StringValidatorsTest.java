package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringValidatorsTest {
    private static final String TO_TEST = "toTest";
    private static final String EMAIL_TEST = "validemailaddress@valid.com";
    private static final String LOCATION = "location";
    private static final String LOGGING_CONTEXT ="12345";
    private Errors errors;

    @BeforeEach
    void setUp() {
        errors = new Errors();
    }

    @Test
    @DisplayName("Validate a string is not null and not empty successfully")
    void validateNotBlank_Successful() {
        assertTrue(StringValidators.validateStringNotBlank(TO_TEST, LOCATION, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string is not null and not empty unsuccessfully - empty string")
    void validateNotBlank_Unsuccessful_EmptyTrimmedString() {
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION)
                .withError(LOCATION + ValidationMessages.NOT_EMPTY_ERROR_MESSAGE).build();

        boolean isNotBlank = StringValidators.validateStringNotBlank("     ", LOCATION, errors, LOGGING_CONTEXT);

        assertFalse(isNotBlank);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate a string is not null and not empty unsuccessfully - null string")
    void validateNotBlank_Unsuccessful_NullString() {
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION)
                .withError(LOCATION + ValidationMessages.NOT_NULL_ERROR_MESSAGE).build();

        boolean isNotBlank = StringValidators.validateStringNotBlank(null, LOCATION, errors, LOGGING_CONTEXT);

        assertFalse(isNotBlank);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate a string is not over the max length successfully")
    void validateLength_Successful() {
        assertTrue(StringValidators.validateLength(TO_TEST, 10, LOCATION, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string length is not over the max length unsuccessfully - over 5 characters")
    void validateLength_Unsuccessful_Over_MaxValue() {
        String errMsg = LOCATION + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", "5");
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION).withError(errMsg).build();

        boolean isNotOverMaxLength = StringValidators.validateLength(TO_TEST, 5, LOCATION, errors, LOGGING_CONTEXT);

        assertFalse(isNotOverMaxLength);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate a string with allowed successfully")
    void validateCharacters_Successful() {
        assertTrue(StringValidators.validateCharacters(TO_TEST, LOCATION, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string with not allowed unsuccessfully - russian characters")
    void validateCharacters_Unsuccessful_Over_MaxValue() {
        String errMsg = LOCATION + ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE;
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION).withError(errMsg).build();

        boolean isValidCharacters = StringValidators.validateCharacters("Дракон", LOCATION, errors, LOGGING_CONTEXT);

        assertFalse(isValidCharacters);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate Email successfully")
    void validateEmail_Successful() {
        assertTrue(StringValidators.validateEmail(EMAIL_TEST, LOCATION, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate Email unsuccessfully")
    void validateEmail_Unsuccessful() {
        String errMsg = ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", LOCATION);
        Err err = Err.invalidBodyBuilderWithLocation(LOCATION).withError(errMsg).build();

        boolean isValidEmail = StringValidators.validateEmail("lorem@ipsum", LOCATION, errors, LOGGING_CONTEXT);

        assertFalse(isValidEmail);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }
}
