package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityNameMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class EntityNameValidatorTest {

    private static final String QUALIFIED_FIELD_NAME = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD + "." + EntityNameDto.NAME_FIELD;
    private static final String LOGGING_CONTEXT = "12345";

    private EntityNameValidator entityNameValidator;
    private EntityNameDto entityNameDto;

    @BeforeEach
    public void init() {
      entityNameValidator = new EntityNameValidator();
      entityNameDto = EntityNameMock.getEntityNameDto();
    }

    @Test
    void testNoValidationErrorReportedWhenEntityNameFieldIsEmpty() {
        entityNameDto.setName("Joe Bloggs Ltd");
        Errors errors = entityNameValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenEntityNameFieldIsNull() {
        entityNameDto.setName(null);
        Errors errors = entityNameValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", QUALIFIED_FIELD_NAME);
        assertError(validationMessage, errors);
    }

    @Test
    void testValidationErrorReportedWhenEntityNameFieldIsEmpty() {
        entityNameDto.setName("  ");
        Errors errors = entityNameValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", QUALIFIED_FIELD_NAME);
        assertError(validationMessage, errors);
    }

    @Test
    void testValidationErrorReportedWhenEntityNameFieldExceedsMaxLength() {
        entityNameDto.setName(StringUtils.repeat("A", 161));
        Errors errors = entityNameValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = QUALIFIED_FIELD_NAME + " must be 160 characters or less";
        assertError(validationMessage, errors);
    }

    @Test
    void testValidationErrorReportedWhenEntityNameFieldContainsInvalidCharacters() {
        entityNameDto.setName("Дракон");
        Errors errors = entityNameValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", QUALIFIED_FIELD_NAME);
        assertError(validationMessage, errors);
    }

    private void assertError(String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(QUALIFIED_FIELD_NAME).withError(message).build();
        assertTrue(errors.containsError(err));
    }


}
