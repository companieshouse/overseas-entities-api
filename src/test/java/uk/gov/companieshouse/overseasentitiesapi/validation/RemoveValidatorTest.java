package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.RemoveMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.service.rest.err.Errors;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;


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
    void testNoValidationErrorReportedWhenIsNotProprietorOfLandIsFalse() {
        removeDto.setIsNotProprietorOfLand(false);
        Errors errors = removeValidator.validate(removeDto, new Errors(), LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }
    
}
