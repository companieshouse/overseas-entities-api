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

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.REMOVE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.UPDATE_FIELD;


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
        removeDto.setIsNotProprietorOfLand(null);

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

        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = ValidationMessages.NOT_VALID_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoFullValidationErrorReportedWhenFilingDateIsNotPresentAndIsNotProprietorOfLandIsTrue() {
        removeDto.setIsNotProprietorOfLand(true);

        Errors errors = removeValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedWhenFilingDateIsPresentAndIsNotProprietorOfLandIsTrue() {
        removeDto.setIsNotProprietorOfLand(true);
        updateDto.setFilingDate(LocalDate.of(2024, 2, 1));

        Errors errors = removeValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorsReportedWhenFilingDateIsPresentAndIsNotProprietorOfLandIsFalse() {
        removeDto.setIsNotProprietorOfLand(false);
        updateDto.setFilingDate(LocalDate.of(2024, 2, 1));

        Errors errors = removeValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFilingDateFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String filingDateValidationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFilingDateFieldName);
        String qualifiedRemoveStatementFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String removeStatementValidationMessage = ValidationMessages.NOT_VALID_ERROR_MESSAGE.replace("%s", qualifiedRemoveStatementFieldName);
        assertError(qualifiedFilingDateFieldName, filingDateValidationMessage, errors);
        assertError(qualifiedRemoveStatementFieldName, removeStatementValidationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenFilingDateIsNotPresentAndRemoveBlockNotPresent() {
        overseasEntitySubmissionDto.setRemove(null);

        Errors errors = removeValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenFilingDateIsNotPresentAndIsNotProprietorOfLandIsNull() {
        overseasEntitySubmissionDto.getRemove().setIsNotProprietorOfLand(null);

        Errors errors = removeValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenUpdateBlockNotPresentAndIsNotProprietorOfLandIsTrue() {
        overseasEntitySubmissionDto.setUpdate(null);
        removeDto.setIsNotProprietorOfLand(true);

        Errors errors = removeValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = UPDATE_FIELD;
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
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
