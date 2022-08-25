package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class EntityDtoValidatorTest {
    private static final String CONTEXT = "12345";

    private EntityDtoValidator entityDtoValidator;
    private EntityDto entityDto;

    @BeforeEach
    public void init() {
        entityDtoValidator = new EntityDtoValidator();
        entityDto = EntityMock.getEntityDto();
    }

    @Test
    void testValidateEntityDtoNoErrors() {
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateNameIsEmpty() {
        entityDto.setName("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateNameIsNull() {
        entityDto.setName(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateNameLength() {
        entityDto.setName(StringUtils.repeat("A", 161));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, " must be 160 characters or less", errors);
    }

    @Test
    void testValidateNameCharacters() {
        entityDto.setName("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String location = OverseasEntitySubmissionDto.ENTITY_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(location).withError(location + message).build();
        assertTrue(errors.containsError(err));
    }

}
