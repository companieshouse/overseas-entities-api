package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringValidatorsTest {
    private static final String TO_TEST = "toTest";
    private static final String EMAIL_TEST = "validemailaddress@valid.com";
    private static final String DUMMY_PARENT_FIELD = "parentField";
    private static final String LOGGING_CONTEXT ="12345";
    private Errors errors;

    @BeforeEach
    void setUp() {
        errors = new Errors();
    }

    @Test
    @DisplayName("Validate a string is not null and not empty successfully")
    void validateNotBlank_Successful() {
        assertTrue(StringValidators.isNotBlank(TO_TEST, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string is not null and not empty unsuccessfully - empty string")
    void validateNotBlank_Unsuccessful_EmptyTrimmedString() {
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD)
                .withError(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", DUMMY_PARENT_FIELD)).build();

        boolean isNotBlank = StringValidators.isNotBlank("     ", DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isNotBlank);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate a string is not null and not empty unsuccessfully - null string")
    void validateNotBlank_Unsuccessful_NullString() {
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD)
                .withError(ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", DUMMY_PARENT_FIELD)).build();

        boolean isNotBlank = StringValidators.isNotBlank(null, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isNotBlank);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate a string is not over the max length successfully")
    void validateLength_Successful() {
        assertTrue(StringValidators.isLessThanOrEqualToMaxLength(TO_TEST, 10, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string length is not over the max length unsuccessfully - over 5 characters")
    void validateLength_Unsuccessful_Over_MaxValue() {
        String errMsg = DUMMY_PARENT_FIELD + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", "5");
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();

        boolean isNotOverMaxLength = StringValidators.isLessThanOrEqualToMaxLength(TO_TEST, 5, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isNotOverMaxLength);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate a string with allowed successfully")
    void validateCharacters_Successful() {
        assertTrue(StringValidators.isValidCharacters(TO_TEST, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate a string with not allowed unsuccessfully - russian characters")
    void validateCharacters_Unsuccessful_Over_MaxValue() {
        String errMsg = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", DUMMY_PARENT_FIELD);
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();

        boolean isValidCharacters = StringValidators.isValidCharacters("Дракон", DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isValidCharacters);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate Email successfully")
    void validateEmail_Successful() {
        assertTrue(StringValidators.isValidEmailAddress(EMAIL_TEST, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate Email unsuccessfully")
    void validateEmail_Unsuccessful() {
        String errMsg = ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", DUMMY_PARENT_FIELD);
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();

        boolean isValidEmail = StringValidators.isValidEmailAddress("lorem@ipsum", DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);

        assertFalse(isValidEmail);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate strings are not equal successfully")
    void validateStringsAreNotEqual_Successful() {
        assertTrue(StringValidators.checkIsNotEqual("Wales", "England", ValidationMessages.SECOND_NATIONALITY_SHOULD_BE_DIFFERENT, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate strings are not equal unsuccessfully")
    void validateStringsAreNotEqual_Unsuccessful() {
        String errMsg = String.format(ValidationMessages.SECOND_NATIONALITY_SHOULD_BE_DIFFERENT, DUMMY_PARENT_FIELD);
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();
        boolean isNotEqual = StringValidators.checkIsNotEqual("Wales", "Wales", ValidationMessages.SECOND_NATIONALITY_SHOULD_BE_DIFFERENT, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);
        assertFalse(isNotEqual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }

    @Test
    @DisplayName("Validate strings are equal successfully")
    void validateStringsAreEqual_Successful() {
        assertTrue(StringValidators.checkIsEqual("agree", "agree", ValidationMessages.SHOULD_BE_AGREE_ERROR_MESSAGE, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT));
    }

    @Test
    @DisplayName("Validate strings are equal unsuccessfully")
    void validateStringsAreEqual_Unsuccessful() {
        String errMsg = String.format(ValidationMessages.SHOULD_BE_AGREE_ERROR_MESSAGE, DUMMY_PARENT_FIELD);
        Err err = Err.invalidBodyBuilderWithLocation(DUMMY_PARENT_FIELD).withError(errMsg).build();
        boolean isEqual = StringValidators.checkIsEqual("agree", "disagree", ValidationMessages.SHOULD_BE_AGREE_ERROR_MESSAGE, DUMMY_PARENT_FIELD, errors, LOGGING_CONTEXT);
        assertFalse(isEqual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsError(err));
    }
}
