package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRUNCATED_DATA_LENGTH;

@ExtendWith(MockitoExtension.class)
class EntityDtoValidatorTest {
    private static final String LOGGING_CONTEXT = "12345";

    private EntityDtoValidator entityDtoValidator;

    private AddressDtoValidator addressDtoValidator;
    private EntityDto entityDto;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        entityDtoValidator = new EntityDtoValidator(addressDtoValidator);
        entityDto = EntityMock.getEntityDto();
        entityDto.setPrincipalAddress(AddressMock.getAddressDto());
        entityDto.setServiceAddress(new AddressDto());
    }

    @Test
    void testNoErrorReportedWhenEntityDtoValuesAreCorrect() {
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        entityDto.setName("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        entityDto.setName(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        entityDto.setName(StringUtils.repeat("A", 161));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.NAME_FIELD);

        assertError(EntityDto.NAME_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        entityDto.setName("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenPrincipalAddressCountryIsOnTheAllowedList() {
        entityDto.setServiceAddressSameAsPrincipalAddress(true);
        entityDto.setPrincipalAddress(AddressMock.getAddressDto());
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertEquals(0, errors.size());
    }

    @Test
    void testErrorReportedWhenPrincipalAddressCountryIsNotOnTheAllowedList() {
        entityDto.setServiceAddressSameAsPrincipalAddress(true);

        AddressDto addressDto = AddressMock.getAddressDto();
        addressDto.setCountry("Transylvania");

        entityDto.setPrincipalAddress(addressDto);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertEquals(1, errors.size());
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        entityDto.setServiceAddressSameAsPrincipalAddress(false);
        entityDto.setServiceAddress(new AddressDto());
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertTrue(errors.size() > 0);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsTrueWhenServiceAddressNotEmpty() {
        entityDto.setServiceAddressSameAsPrincipalAddress(true);
        entityDto.setServiceAddress(AddressMock.getAddressDto());
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.SERVICE_ADDRESS_FIELD);

        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName + "." + AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        assertError(EntityDto.SERVICE_ADDRESS_FIELD + "." +  AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName + "." + AddressDto.LINE_1_FIELD);
        assertError(EntityDto.SERVICE_ADDRESS_FIELD + "." +  AddressDto.LINE_1_FIELD, validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName + "." + AddressDto.TOWN_FIELD);
        assertError(EntityDto.SERVICE_ADDRESS_FIELD + "." + AddressDto.TOWN_FIELD, validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName + "." + AddressDto.COUNTY_FIELD);
        assertError(EntityDto.SERVICE_ADDRESS_FIELD + "." + AddressDto.COUNTY_FIELD, validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName + "." + AddressDto.COUNTRY_FIELD);
        assertError(EntityDto.SERVICE_ADDRESS_FIELD + "." + AddressDto.COUNTRY_FIELD, validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName + "." + AddressDto.POSTCODE_FIELD);
        assertError(EntityDto.SERVICE_ADDRESS_FIELD + "." + AddressDto.POSTCODE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIncorporationCountryFieldIsEmpty() {
        entityDto.setIncorporationCountry("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.INCORPORATION_COUNTRY_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIncorporationCountryFieldIsNull() {
        entityDto.setIncorporationCountry(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.INCORPORATION_COUNTRY_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIncorporationCountryIsNotOnTheAllowedList() {
        final String invalidCountry = "Wales";  // Wales is not on the list of Overseas Countries and so should cause a validation error
        entityDto.setIncorporationCountry(invalidCountry);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, invalidCountry);

        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenIncorporationCountryIsOnTheAllowedList() {
        entityDto.setIncorporationCountry("Slovakia");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWithTruncatedStringWhenLongUnsanitizedCountryIsInput() {
        entityDto.setIncorporationCountry("Uto\t\npia" + StringUtils.repeat("A", TRUNCATED_DATA_LENGTH));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String sanitised = "Uto\\t\\npia";
        sanitised = sanitised + StringUtils.repeat("A", TRUNCATED_DATA_LENGTH - sanitised.length());
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, sanitised);
        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testUnsanitisedCountryNameIsNotFoundInReportedError() {
        String input = "Uto\t\npia";
        entityDto.setIncorporationCountry(input);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, input);
        String qualifiedFieldName = ENTITY_FIELD + "." + EntityDto.INCORPORATION_COUNTRY_FIELD;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertFalse(errors.containsError(err));
    }

    @Test
    void testErrorReportedWithSanitisedStringWhenUnsanitizedCountryIsInput() {
        entityDto.setIncorporationCountry("Uto\t\npia");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, "Uto\\t\\npia");
        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        entityDto.setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD, validationMessage, errors);
    }
    @Test
    void testErrorReportedWhenEmailFieldIsEmpty() {
        entityDto.setEmail("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.EMAIL_PROPERTY_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsNull() {
        entityDto.setEmail(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.EMAIL_PROPERTY_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldExceedsMaxLength() {
        entityDto.setEmail(StringUtils.repeat("A", 257) + "@long.com");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.EMAIL_PROPERTY_FIELD);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testNoErrorReportedWhenEmailFieldIsMaxLength() {
        entityDto.setEmail(StringUtils.repeat("A", 247) + "@long.com");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertEquals(0, errors.size(), "Errors should be empty");
    }

    @Test
    void testErrorReportedWhenEmailFieldContainsInvalidCharacters() {
        entityDto.setEmail("wrong.com");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.EMAIL_PROPERTY_FIELD);
        String validationMessage = ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.EMAIL_PROPERTY_FIELD,  validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormIsEmpty() {
        entityDto.setLegalForm("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LEGAL_FORM_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(EntityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormIsNull() {
        entityDto.setLegalForm(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LEGAL_FORM_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(EntityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldExceedsMaxLength() {
        entityDto.setLegalForm(StringUtils.repeat("A", 161));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LEGAL_FORM_FIELD);
        assertError(EntityDto.LEGAL_FORM_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testNoErrorReportedWhenLegalFormFieldIsMaxLength() {
        entityDto.setLegalForm(StringUtils.repeat("A", 160));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertEquals(0, errors.size(), "Errors should be empty");
    }

    @Test
    void testErrorReportedWhenLegalFormContainsInvalidCharacters() {
        entityDto.setLegalForm("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LEGAL_FORM_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(EntityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsEmpty() {
        entityDto.setLawGoverned("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LAW_GOVERNED_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(EntityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsNull() {
        entityDto.setLawGoverned(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LAW_GOVERNED_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldExceedsMaxLength() {
        entityDto.setLawGoverned(StringUtils.repeat("A", 161));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LAW_GOVERNED_FIELD);

        assertError(EntityDto.LAW_GOVERNED_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testNoErrorReportedWhenLawGovernedFieldIsMaxLength() {
        entityDto.setLawGoverned(StringUtils.repeat("A", 160));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertEquals(0, errors.size(), "Errors should be empty");
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldContainsInvalidCharacters() {
        entityDto.setLawGoverned("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.LAW_GOVERNED_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsEmpty() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage =  ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsNull() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnRegisterFlagIsFalseWhenPublicRegisterNameFieldNotEmpty() {
        entityDto.setOnRegisterInCountryFormedIn(Boolean.FALSE);
        entityDto.setPublicRegisterName("Name");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnRegisterFlagIsFalseWhenRegistrationNumberFieldNotEmpty() {
        entityDto.setOnRegisterInCountryFormedIn(Boolean.FALSE);
        entityDto.setRegistrationNumber("123456");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }


    @Test
    void testErrorReportedWhenPublicRegisterNameFieldExceedsMaxLength() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName(StringUtils.repeat("A", 161));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testNoErrorReportedWhenPublicRegisterNameFieldIsMaxLength() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName(StringUtils.repeat("A", 160));
        entityDto.setRegistrationNumber("1234");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        assertEquals(0, errors.size(), "Errors should be empty");
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldContainsInvalidCharacters() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsEmpty() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber("  ");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsNull() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber(null);
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldExceedsMaxLength() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber(StringUtils.repeat("A", 33));
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, qualifiedFieldName + " must be 32 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldContainsInvalidCharacters() {
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setRegistrationNumber("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorsReportedWhenMultipleFieldsAreInvalid() {
        entityDto.setName(StringUtils.repeat("A", 161));
        entityDto.setIncorporationCountry(null);
        entityDto.setLawGoverned(StringUtils.repeat("A", 161));
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPublicRegisterName(" ");
        entityDto.setRegistrationNumber("Дракон");
        Errors errors = entityDtoValidator.validate(entityDto, new Errors(), LOGGING_CONTEXT);

        assertEquals(5, errors.size());
        assertError(EntityDto.NAME_FIELD, getQualifiedFieldName(EntityDto.NAME_FIELD) + " must be 160 characters or less", errors);
        assertError(EntityDto.INCORPORATION_COUNTRY_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", getQualifiedFieldName(EntityDto.INCORPORATION_COUNTRY_FIELD)), errors);
        assertError(EntityDto.LAW_GOVERNED_FIELD, getQualifiedFieldName(EntityDto.LAW_GOVERNED_FIELD) + " must be 160 characters or less", errors);
        assertError(EntityDto.PUBLIC_REGISTER_NAME_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s",  getQualifiedFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD)), errors);
        assertError(EntityDto.REGISTRATION_NUMBER_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",  getQualifiedFieldName(EntityDto.REGISTRATION_NUMBER_FIELD)), errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = ENTITY_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return ENTITY_FIELD + "." + field;
    }
}
