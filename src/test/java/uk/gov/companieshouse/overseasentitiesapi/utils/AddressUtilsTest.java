package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.companieshouse.overseasentitiesapi.utils.AddressUtils.convertAddressDtoToAddressModel;
import static uk.gov.companieshouse.overseasentitiesapi.utils.AddressUtils.convertRegisteredOfficeAddressApiToAddressModel;

class AddressUtilsTest {
    @Test
    void convertRegisteredOfficeAddressApiToAddressModelInputNonNull() {
        var inputAddress = new RegisteredOfficeAddressApi();
        inputAddress.setPremises("Premises");
        inputAddress.setAddressLine1("Line 1");
        inputAddress.setAddressLine2("Line 2");
        inputAddress.setLocality("Locality");
        inputAddress.setRegion("Region");
        inputAddress.setCountry("Country");
        inputAddress.setPostalCode("Post code");
        inputAddress.setPoBox("Po box");
        inputAddress.setCareOf("Care of");

        var result = convertRegisteredOfficeAddressApiToAddressModel(inputAddress);

        assertEquals("Premises", result.getHouseNameNum());
        assertEquals("Line 1", result.getStreet());
        assertEquals("Line 2", result.getArea());
        assertEquals("Locality", result.getPostTown());
        assertEquals("Region", result.getRegion());
        assertEquals("Country", result.getCountry());
        assertEquals("Post code", result.getPostCode());
        assertEquals("Po box", result.getPoBox());
        assertEquals("Care of", result.getCareOf());
    }

    @Test
    void convertRegisteredOfficeAddressApiToAddressModelInputIsNull() {
        RegisteredOfficeAddressApi inputAddress = null;

        var result = convertRegisteredOfficeAddressApiToAddressModel(inputAddress);

        assertNull(result);
    }

    @Test
    void convertAddressDtoToAddressModelInputNonNull() {
        var inputAddress = new AddressDto();
        inputAddress.setPropertyNameNumber("Premises");
        inputAddress.setLine1("Line 1");
        inputAddress.setLine2("Line 2");
        inputAddress.setLocality("Locality");
        inputAddress.setCounty("Region");
        inputAddress.setCountry("Country");
        inputAddress.setPostcode("Post code");
        inputAddress.setPoBox("Po box");
        inputAddress.setCareOf("Care of");

        var result = convertAddressDtoToAddressModel(inputAddress);

        assertEquals("Premises", result.getHouseNameNum());
        assertEquals("Line 1", result.getStreet());
        assertEquals("Line 2", result.getArea());
        assertEquals("Locality", result.getPostTown());
        assertEquals("Region", result.getRegion());
        assertEquals("Country", result.getCountry());
        assertEquals("Post code", result.getPostCode());
        assertEquals("Po box", result.getPoBox());
        assertEquals("Care of", result.getCareOf());
    }

    @Test
    void convertAddressDtoToAddressModelInputIsNull() {
        AddressDto inputAddress = null;

        var result = convertAddressDtoToAddressModel(inputAddress);

        assertNull(result);
    }
}
