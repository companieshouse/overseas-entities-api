package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.REMOVE_FIELD;

@ExtendWith(MockitoExtension.class)
class RemoveValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private RemoveValidator removeValidator;
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private RemoveDto removeDto;
    private UpdateDto updateDto;

    @BeforeEach
    public void init() {
        removeValidator = new RemoveValidator();
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        removeDto = new RemoveDto();
        updateDto = new UpdateDto();
        overseasEntitySubmissionDto.setRemove(removeDto);
        overseasEntitySubmissionDto.setUpdate(updateDto);
    }
    @Test
    void testNoValidationErrorReportedWhenIsNotProprietorOfLandIsNull() {
        // Given
        removeDto.setIsNotProprietorOfLand(null);

        // When
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        assertFalse(errors.hasErrors());
    }
    
    @Test
    void testNoValidationErrorReportedWhenIsNotProprietorOfLandIsTrue() {
        // Given
        removeDto.setIsNotProprietorOfLand(true);

        // When
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenIsNotProprietorOfLandIsFalse() {
        // Given
        removeDto.setIsNotProprietorOfLand(false);

        // When
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = ValidationMessages.NOT_VALID_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenIsNotProprietorOfLandIsNull() {
        // Given
        removeDto.setIsNotProprietorOfLand(null);

        // When
        Errors errors = removeValidator.validateFull(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoFullValidationErrorReportedWhenIsNotProprietorOfLandIsTrue() {
        // Given
        removeDto.setIsNotProprietorOfLand(true);

        // When
        Errors errors = removeValidator.validateFull(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedWhenIsNotProprietorOfLandIsFalse() {
        // Given
        removeDto.setIsNotProprietorOfLand(false);

        // When
        Errors errors = removeValidator.validateFull(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = ValidationMessages.NOT_VALID_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenFilingDateIsNotPresentAndRemoveBlockNotPresent() {
        // Given
        overseasEntitySubmissionDto.setRemove(null);

        // When
        Errors errors = removeValidator.validateFull(removeDto, new Errors(), LOGGING_CONTEXT);

        // Then
        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();

        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return REMOVE_FIELD + "." + field;
    }
}
