package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
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

    private AddressDtoValidator addressDtoValidator;
    private EntityDto entityDto;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        entityDtoValidator = new EntityDtoValidator(addressDtoValidator);
        entityDto = EntityMock.getEntityDto();
        entityDto.setPrincipalAddress(AddressMock.getAddressDto());
    }

    @Test
    void testNoErrorReportedWhenEntityDtoValuesAreCorrect() {
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        entityDto.setName("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        entityDto.setName(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        entityDto.setName(StringUtils.repeat("A", 161));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, " must be 160 characters or less", errors, true);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        entityDto.setName("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.NAME_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenIncorporationCountryFieldIsEmpty() {
        entityDto.setIncorporationCountry("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenIncorporationCountryFieldIsNull() {
        entityDto.setIncorporationCountry(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }


    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        entityDto.setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }
    @Test
    void testErrorReportedWhenEmailFieldIsEmpty() {
        entityDto.setEmail("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsNull() {
        entityDto.setEmail(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenEmailFieldExceedsMaxLength() {
        entityDto.setEmail(StringUtils.repeat("A", 251) + "@long.com");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD, " must be 250 characters or less", errors, true);
    }

    @Test
    void testErrorReportedWhenEmailFieldContainsInvalidCharacters() {
        entityDto.setEmail("wrong.com");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD,  "Email address is not in the correct format for entity.email, like name@example.com", errors, false);
    }

    @Test
    void testErrorReportedWhenLegalFormIsEmpty() {
        entityDto.setLegalForm("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LEGAL_FORM_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenLegalFormIsNull() {
        entityDto.setLegalForm(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LEGAL_FORM_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldExceedsMaxLength() {
        entityDto.setLegalForm(StringUtils.repeat("A", 4001));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LEGAL_FORM_FIELD, " must be 4000 characters or less", errors, true);
    }

    @Test
    void testErrorReportedWhenLegalFormContainsInvalidCharacters() {
        entityDto.setLegalForm("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);
        assertError(EntityDto.LEGAL_FORM_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsEmpty() {
        entityDto.setLawGoverned("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LAW_GOVERNED_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsNull() {
        entityDto.setLawGoverned(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LAW_GOVERNED_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldExceedsMaxLength() {
        entityDto.setLawGoverned(StringUtils.repeat("A", 4001));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LAW_GOVERNED_FIELD, " must be 4000 characters or less", errors, true);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldContainsInvalidCharacters() {
        entityDto.setLawGoverned("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.LAW_GOVERNED_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsEmpty() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsNull() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldExceedsMaxLength() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName(StringUtils.repeat("A", 4001));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, " must be 4000 characters or less", errors, true);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldContainsInvalidCharcaters() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsEmpty() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsNull() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors, true);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldExceedsMaxLength() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber(StringUtils.repeat("A", 33));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, " must be 32 characters or less", errors, true);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldContainsInvalidCharacters() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), CONTEXT);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors, true);
    }
    private void assertError(String fieldName, String message, Errors errors, boolean prefix) {
        String location = OverseasEntitySubmissionDto.ENTITY_FIELD + "." + fieldName;
        String errorString = (prefix)? location + message : message;
        Err err = Err.invalidBodyBuilderWithLocation(location).withError(errorString).build();
        assertTrue(errors.containsError(err));
    }
}
