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

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AddressDtoValidatorTest {

    private static final String CONTEXT = "12345";

    private AddressDtoValidator addressDtoValidator;
    private AddressDto addressDto;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        addressDto = AddressMock.getAddressDto();
    }

    @Test
    void testValidatePropertyNameNumberIsEmpty(){
        addressDto.setPropertyNameNumber(" ");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.PROPERTY_NAME_NUMBER_FIELD, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidatePropertyNameNumberIsNull(){
        addressDto.setPropertyNameNumber(null);
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.PROPERTY_NAME_NUMBER_FIELD, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidatePropertyNameNumberLength(){
        addressDto.setPropertyNameNumber(StringUtils.repeat("A", 51));
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.PROPERTY_NAME_NUMBER_FIELD, " must be 50 characters or less", errors);
    }

    @Test
    void testValidatePropertyNameNumber(){
        addressDto.setPropertyNameNumber("Дракон");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.PROPERTY_NAME_NUMBER_FIELD, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateLine1IsEmpty(){
        addressDto.setLine1(" ");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.LINE_1, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateLine1IsNull(){
        addressDto.setLine1(null);
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.LINE_1, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateLine1Length(){
        addressDto.setLine1(StringUtils.repeat("A", 51));
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.LINE_1, " must be 50 characters or less", errors);
    }

    @Test
    void testValidateLine1(){
        addressDto.setLine1("Дракон");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.LINE_1, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateLine2Length(){
        addressDto.setLine2(StringUtils.repeat("A", 51));
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.LINE_2, " must be 50 characters or less", errors);
    }

    @Test
    void testValidateLine2(){
        addressDto.setLine2("Дракон");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.LINE_2, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateTownIsEmpty(){
        addressDto.setTown(" ");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.TOWN, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateTownIsNull(){
        addressDto.setTown(null);
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.TOWN, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateTownLength(){
        addressDto.setTown(StringUtils.repeat("A", 51));
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.TOWN, " must be 50 characters or less", errors);
    }

    @Test
    void testValidateTown(){
        addressDto.setTown("Дракон");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.TOWN, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateCountyLength(){
        addressDto.setCounty(StringUtils.repeat("A", 51));
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.COUNTY, " must be 50 characters or less", errors);
    }

    @Test
    void testValidateCounty(){
        addressDto.setCounty("Дракон");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.COUNTY, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateCountryIsEmpty(){
        addressDto.setCountry(" ");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.COUNTRY, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateCountryIsNull(){
        addressDto.setCountry(null);
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.COUNTRY, ValidationMessages.NOT_NULL_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidatePostcodeLength(){
        addressDto.setPostcode(StringUtils.repeat("A", 21));
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.POSTCODE, " must be 20 characters or less", errors);
    }

    @Test
    void testValidatePostcode(){
        addressDto.setPostcode("Дракон");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.POSTCODE, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, errors);
    }

    @Test
    void testValidateServiceAddressField(){
        addressDto.setPostcode(StringUtils.repeat("A", 21));
        String field = EntityDto.SERVICE_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(field, AddressDto.POSTCODE, " must be 20 characters or less", errors);
    }

    private void assertError(String addressField, String fieldName, String message, Errors errors) {
        String location = addressField + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(location).withError(location + message).build();
        assertTrue(errors.containsError(err));
    }
}
