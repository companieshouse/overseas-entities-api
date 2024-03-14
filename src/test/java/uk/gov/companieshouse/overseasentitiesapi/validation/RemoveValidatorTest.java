package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.RemoveMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.REMOVE_FIELD;


@ExtendWith(MockitoExtension.class)
class RemoveValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private RemoveValidator removeValidator;
    private RemoveDto removeDto;

    @BeforeEach
    public void init() {
        removeValidator = new RemoveValidator();
        removeDto = RemoveMock.getRemoveDto();
    }
    @Test
    void testNoValidationErrorReportedWhenIsNotProprietorOfLandIsNull() {
        removeDto.setIsNotProprietorOfLand(null);
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenIsNotProprietorOfLandIsNotNull() {
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenIsNotProprietorOfLandIsTrue() {
        removeDto.setIsNotProprietorOfLand(true);
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenIsNotProprietorOfLandIsFalse() {
        removeDto.setIsNotProprietorOfLand(false);
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenIsNotProprietorOfLandIsFalseTest() {
        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        removeDto.setIsNotProprietorOfLand(false);
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();

        assertFalse(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return REMOVE_FIELD + "." + field;
    }
}
