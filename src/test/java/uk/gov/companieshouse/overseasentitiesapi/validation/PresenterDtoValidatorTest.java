package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER_FIELD;

@ExtendWith(MockitoExtension.class)
class PresenterDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private PresenterDtoValidator presenterDtoValidator;

    private PresenterDto presenterDto;

    @BeforeEach
    public void init() {
        presenterDtoValidator = new PresenterDtoValidator();
        presenterDto = new PresenterDto();
        presenterDto = PresenterMock.getPresenterDto();
    }

    @Test
    void testNoErrorReportedWhenPresenterDtoValuesAreCorrect() {
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        presenterDto.setFullName("  ");
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.FULL_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(PresenterDto.FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        presenterDto.setFullName(null);
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.FULL_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(PresenterDto.FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenNameFieldIsAtMaxLength() {
        presenterDto.setFullName(StringUtils.repeat("A", 256));
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        presenterDto.setFullName(StringUtils.repeat("A", 257));
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.FULL_NAME_FIELD);

        assertError(PresenterDto.FULL_NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        presenterDto.setFullName("Дракон");
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.FULL_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(PresenterDto.FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsEmpty() {
        presenterDto.setEmail("  ");
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.EMAIL_PROPERTY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(PresenterDto.EMAIL_PROPERTY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsNull() {
        presenterDto.setEmail(null);
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.EMAIL_PROPERTY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(PresenterDto.EMAIL_PROPERTY_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenEmailFieldIsAtMaxLength() {
        presenterDto.setEmail(StringUtils.repeat("A", 247) + "@long.com");
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenEmailFieldExceedsMaxLength() {
        presenterDto.setEmail(StringUtils.repeat("A", 257) + "@long.com");
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.EMAIL_PROPERTY_FIELD);

        assertError(PresenterDto.EMAIL_PROPERTY_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"vsocarroll@QQQQQQQT123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk",
            "socarrollA123456789B132456798C123456798D123456789@T123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk",
            "socarrollA123456789B132456798C123456798D123456789E123456789F123XX@T123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk"})
    void testNoErrorReportedWithLongEmailAddress(String email) {
        presenterDto.setEmail(email);
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenEmailFieldContainsInvalidCharacters() {
        presenterDto.setEmail("wrong.com");
        Errors errors = presenterDtoValidator.validate(presenterDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PresenterDto.EMAIL_PROPERTY_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(PresenterDto.EMAIL_PROPERTY_FIELD,  validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = PRESENTER_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return PRESENTER_FIELD + "." + field;
    }
}
