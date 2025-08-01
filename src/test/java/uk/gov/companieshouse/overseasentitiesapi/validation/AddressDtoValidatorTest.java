package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.DataSanitisation;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRUNCATED_DATA_LENGTH;

@ExtendWith(MockitoExtension.class)
class AddressDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
    private AddressDtoValidator addressDtoValidator;
    private AddressDto addressDto;

    @BeforeEach
    public void init() {
        DataSanitisation dataSanitisation = new DataSanitisation();
        addressDtoValidator = new AddressDtoValidator(dataSanitisation);
        addressDto = AddressMock.getAddressDto();
    }

    @Test
    void testNoErrorReportedWhenAddressDtoValuesAreCorrectAllCountriesWithUkCountry() {
        addressDto.setCountry("England");
        Errors errors = addressDtoValidator.validate(EntityDto.PRINCIPAL_ADDRESS_FIELD, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenAddressDtoValuesAreCorrectAllCountriesWithOverseasCountry() {
        addressDto.setCountry("Slovakia");
        Errors errors = addressDtoValidator.validate(EntityDto.PRINCIPAL_ADDRESS_FIELD, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenAddressDtoValuesAreCorrectUkCountries() {
        addressDto.setCountry("Wales");
        Errors errors = addressDtoValidator.validate(EntityDto.PRINCIPAL_ADDRESS_FIELD, addressDto, CountryLists.getUkCountries(), new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenAddressDtoValuesAreCorrectOverseasAllCountries() {
        addressDto.setCountry("Slovakia");
        Errors errors = addressDtoValidator.validate(EntityDto.PRINCIPAL_ADDRESS_FIELD, addressDto, CountryLists.getOverseasCountries(), new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenUkCountrySuppliedAsAnOverseasCountry() {
        final String invalidCountry = "England";
        addressDto.setCountry(invalidCountry);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getOverseasCountries(), new Errors(), LOGGING_CONTEXT);

        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, invalidCountry);
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOverseasCountrySuppliedAsAUkCountry() {
        final String invalidCountry = "Luxembourg";
        addressDto.setCountry(invalidCountry);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getUkCountries(), new Errors(), LOGGING_CONTEXT);

        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, invalidCountry);
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCountryFieldIsEmpty() {
        addressDto.setCountry("  ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getUkCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.COUNTRY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCountryFieldIsNull() {
        addressDto.setCountry(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getUkCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.COUNTRY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPrincipalAddressIsNull() {
        addressDto = null;
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getUkCountries(), new Errors(), LOGGING_CONTEXT);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, EntityDto.PRINCIPAL_ADDRESS_FIELD);
        assertError(parentField, "", validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenServiceAddressIsNull() {
        addressDto = null;
        String parentField = EntityDto.SERVICE_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getUkCountries(), new Errors(), LOGGING_CONTEXT);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, EntityDto.SERVICE_ADDRESS_FIELD);
        assertError(parentField, "", validationMessage, errors);
    }
    @Test
    void testErrorReportedWhenFictionalCountryIsNotTheListOfAllCountries() {
        String input = "Utopia";
        addressDto.setCountry(input);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, input);
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWithSanitisedStringWhenUnsanitizedCountryIsInput() {
        addressDto.setCountry("Uto\t\npia");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, "Uto\\t\\npia");
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testUnsanitisedCountryNameIsNotFoundInReportedError() {
        String input = "Uto\t\npia";
        addressDto.setCountry(input);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, input);
        String qualifiedFieldName = (StringUtils.isBlank(AddressDto.COUNTRY_FIELD))?  parentField : parentField + "." + AddressDto.COUNTRY_FIELD;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertFalse(errors.containsError(err));
    }

    @Test
    void testErrorReportedWithTruncatedStringWhenLongUnsanitizedCountryIsInput() {
        addressDto.setCountry("Uto\t\npia" + StringUtils.repeat("A", TRUNCATED_DATA_LENGTH));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String sanitised = "Uto\\t\\npia";
        sanitised = sanitised + StringUtils.repeat("A", TRUNCATED_DATA_LENGTH - sanitised.length());
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, sanitised);
        assertError(parentField, AddressDto.COUNTRY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldContainsEmptySpace() {
        addressDto.setPropertyNameNumber(" ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldIsNull() {
        addressDto.setPropertyNameNumber(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldExceedsMaxLength() {
        addressDto.setPropertyNameNumber(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldContainsInvalidCharacters() {
        addressDto.setPropertyNameNumber("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine1FieldIsEmpty() {
        addressDto.setLine1(" ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_1_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine1FieldIsNull() {
        addressDto.setLine1(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_1_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine1FieldExceedsMaxLength() {
        addressDto.setLine1(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);

        assertError(parentField, AddressDto.LINE_1_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLine1ContainsInvalidCharacters() {
        addressDto.setLine1("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_1_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine2FieldExceedsMaxLength() {
        addressDto.setLine2(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_2_FIELD);

        assertError(parentField, AddressDto.LINE_2_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLine2FieldContainsInvalidCharacters() {
        addressDto.setLine2("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_2_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_2_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTownFieldIsEmpty() {
        addressDto.setTown(" ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTownFieldIsNull() {
        addressDto.setTown(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTownFieldExceedsMaxLength() {
        addressDto.setTown(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);

        assertError(parentField, AddressDto.TOWN_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenTownFieldContainsInvalidCharacters() {
        addressDto.setTown("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCountyFieldExceedsMaxLength() {
        addressDto.setCounty(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.COUNTY_FIELD);

        assertError(parentField, AddressDto.COUNTY_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenCountyFieldContainsInvalidCharacters() {
        addressDto.setCounty("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.COUNTY_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(parentField, AddressDto.COUNTY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPostcodeFieldExceedsMaxLength() {
        addressDto.setPostcode(StringUtils.repeat("A", 16));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.POSTCODE_FIELD);
        assertError(parentField, AddressDto.POSTCODE_FIELD, qualifiedFieldName + " must be 15 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPostcodeFieldContainsInvalidCharacters() {
        addressDto.setPostcode("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.POSTCODE_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(parentField, AddressDto.POSTCODE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedForServiceAddressField() {
        addressDto.setPostcode(StringUtils.repeat("A", 16));
        String parentField = EntityDto.SERVICE_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.POSTCODE_FIELD);

        assertError(parentField, AddressDto.POSTCODE_FIELD, qualifiedFieldName + " must be 15 characters or less", errors);
    }

    @Test
    void testErrorsReportedWhenMultipleFieldsAreInvalid() {
        addressDto.setLine1(StringUtils.repeat("A", 51));
        addressDto.setTown(null);

        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, CountryLists.getAllCountries(), new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldNameLine1 = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String qualifiedFieldNameTown = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldNameTown);

        assertEquals(2, errors.size());
        assertError(parentField, AddressDto.LINE_1_FIELD, qualifiedFieldNameLine1 + " must be 50 characters or less", errors);
        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    private void assertError(String addressField, String fieldName, String message, Errors errors) {
        String qualifiedFieldName = (StringUtils.isBlank(fieldName))?  addressField : addressField + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String parentField, String field) {
        return parentField + "." + field;
    }
}
