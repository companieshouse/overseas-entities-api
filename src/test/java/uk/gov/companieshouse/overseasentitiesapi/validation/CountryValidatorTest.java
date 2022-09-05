package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountryValidatorTest {

    private static final String CONTEXT = "12345";

    private AddressDtoValidator addressDtoValidator;
    private AddressDto addressDto;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        addressDto = AddressMock.getAddressDto();
    }

    @Test
    void testCountryEnumForValidCountry() {
        addressDto.setCountry("Bonaire, Sint Eustatius and Saba");
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "Utopia",
            "Росси́я",
            " "
    })
    void testInvalidCountries(String countryName) {
        addressDto.setCountry(countryName);
        String field = EntityDto.PRINCIPAL_ADDRESS_FIELD;
        Errors errors = addressDtoValidator.validate(field, addressDto, new Errors(), CONTEXT);

        assertError(EntityDto.PRINCIPAL_ADDRESS_FIELD, AddressDto.COUNTRY_FIELD, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, errors);
    }

    private void assertError(String addressField, String fieldName, String message, Errors errors) {
        String location = addressField + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(location).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
