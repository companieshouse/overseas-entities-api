package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AddressDtoValidatorTest {

    private static final String CONTEXT = "12345";
    private AddressDtoValidator addressDtoValidator;
    private AddressDto addressDto;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        addressDto = AddressMock.getAddressDto();
    }

    @Test
    void testNoErrorReportedWhenAddressDtoValuesAreCorrect() {
        Errors errors = addressDtoValidator.validate(EntityDto.PRINCIPAL_ADDRESS_FIELD, addressDto, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldContainsEmptySpace() {
        addressDto.setPropertyNameNumber(" ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldIsNull() {
        addressDto.setPropertyNameNumber(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldExceedsMaxLength() {
        addressDto.setPropertyNameNumber(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPropertyNameNumberFieldContainsInvalidCharacters() {
        addressDto.setPropertyNameNumber("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.PROPERTY_NAME_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine1FieldIsEmpty() {
        addressDto.setLine1(" ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_1_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine1FieldIsNull() {
        addressDto.setLine1(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_1_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine1FieldExceedsMaxLength() {
        addressDto.setLine1(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);

        assertError(parentField, AddressDto.LINE_1_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLine1ContainsInvalidCharacters() {
        addressDto.setLine1("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_1_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLine2FieldExceedsMaxLength() {
        addressDto.setLine2(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_2_FIELD);

        assertError(parentField, AddressDto.LINE_2_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLine2FieldContainsInvalidCharacters() {
        addressDto.setLine2("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.LINE_2_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.LINE_2_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTownFieldIsEmpty() {
        addressDto.setTown(" ");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTownFieldIsNull() {
        addressDto.setTown(null);
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTownFieldExceedsMaxLength() {
        addressDto.setTown(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);

        assertError(parentField, AddressDto.TOWN_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenTownFieldContainsInvalidCharacters() {
        addressDto.setTown("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCountyFieldExceedsMaxLength() {
        addressDto.setCounty(StringUtils.repeat("A", 51));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.COUNTY_FIELD);

        assertError(parentField, AddressDto.COUNTY_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenCountyFieldContainsInvalidCharacters() {
        addressDto.setCounty("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.COUNTY_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(parentField, AddressDto.COUNTY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPostcodeFieldExceedsMaxLength() {
        addressDto.setPostcode(StringUtils.repeat("A", 21));
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.POSTCODE_FIELD);
        assertError(parentField, AddressDto.POSTCODE_FIELD, qualifiedFieldName + " must be 20 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPostcodeFieldContainsInvalidCharacters() {
        addressDto.setPostcode("Дракон");
        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.POSTCODE_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(parentField, AddressDto.POSTCODE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedForServiceAddressField() {
        addressDto.setPostcode(StringUtils.repeat("A", 21));
        String parentField = EntityDto.SERVICE_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(parentField, AddressDto.POSTCODE_FIELD);

        assertError(parentField, AddressDto.POSTCODE_FIELD, qualifiedFieldName + " must be 20 characters or less", errors);
    }

    @Test
    void testErrorsReportedWhenMultipleFieldsAreInvalid() {
        addressDto.setLine1(StringUtils.repeat("A", 51));
        addressDto.setTown(null);

        String parentField = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(parentField, addressDto, new Errors(), CONTEXT);

        String qualifiedFieldNameLine1 = getQualifiedFieldName(parentField, AddressDto.LINE_1_FIELD);
        String qualifiedFieldNameTown = getQualifiedFieldName(parentField, AddressDto.TOWN_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldNameTown);

        assertEquals(2, errors.size());
        assertError(parentField, AddressDto.LINE_1_FIELD, qualifiedFieldNameLine1 + " must be 50 characters or less", errors);
        assertError(parentField, AddressDto.TOWN_FIELD, validationMessage, errors);
    }

    private void assertError(String addressField, String fieldName, String message, Errors errors) {
        String qualifiedFieldName = addressField + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String parentField, String field) {
        return parentField + "." + field;
    }
}
