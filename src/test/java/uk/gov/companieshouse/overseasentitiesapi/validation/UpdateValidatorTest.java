package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
public class UpdateValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private UpdateValidator updateValidator;
    private UpdateDto updateDto;

    @BeforeEach
    public void init() {
        updateValidator = new UpdateValidator();
        updateDto = UpdateMock.getUpdateDto();
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsNull() {
        updateDto.setFilingDate(null);
        Errors errors = updateValidator.validate(updateDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsNotNull() {
        Errors errors = updateValidator.validate(updateDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsNow() {
        updateDto.setFilingDate(LocalDate.now());
        Errors errors = updateValidator.validate(updateDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsInPast() {
        updateDto.setFilingDate(LocalDate.now().minusDays(1));
        Errors errors = updateValidator.validate(updateDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenFilingDateIsInFuture() {
        updateDto.setFilingDate(LocalDate.now().plusDays(1));
        Errors errors = updateValidator.validate(updateDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE, qualifiedFieldName);

        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    @Test
    void testNoFullValidationErrorReportedWhenFilingDateIsInPast() {
        updateDto.setFilingDate(LocalDate.now().minusDays(1));
        Errors errors = updateValidator.validateFull(updateDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedWhenNoUpdate() {
        Errors errors = updateValidator.validateFull(null, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenNoUpdateFilingDate() {
        updateDto.setFilingDate(null);
        Errors errors = updateValidator.validateFull(updateDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = OverseasEntitySubmissionDto.UPDATE_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}

