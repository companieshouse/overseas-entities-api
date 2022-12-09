package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;

@ExtendWith(MockitoExtension.class)
class EntityNameDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private EntityNameDtoValidator entityNameDtoValidator;
    private EntityNameDto entityNameDto;

    @BeforeEach
    void init() {
        entityNameDtoValidator = new EntityNameDtoValidator();
        entityNameDto = new EntityNameDto();
    }

    @Test
    void testNoErrorReportedWhenEntityNameDtoValuesAreCorrect() {
        entityNameDto.setName("Joe Bloggs Ltd");
        Errors errors = entityNameDtoValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        entityNameDto.setName("  ");
        Errors errors = entityNameDtoValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityNameDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityNameDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        entityNameDto.setName(null);
        Errors errors = entityNameDtoValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityNameDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityNameDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        entityNameDto.setName(StringUtils.repeat("A", 161));
        Errors errors = entityNameDtoValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityNameDto.NAME_FIELD);

        assertError(EntityNameDto.NAME_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        entityNameDto.setName("Дракон");
        Errors errors = entityNameDtoValidator.validate(entityNameDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityNameDto.NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityNameDto.NAME_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = ENTITY_NAME_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return ENTITY_NAME_FIELD + "." + field;
    }

}
