package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.registeredOfficeAddressApiToAddress;
import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.addressDtoToAddress;

class TypeConverterTest {
    @Test
    void privateConstructorThrowsException() {
        Class<TypeConverter> typeConverterClass = TypeConverter.class;

        Constructor<?> constructor = typeConverterClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        Throwable exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertEquals(IllegalAccessError.class, exception.getCause().getClass());
        assertEquals("Use the static methods instead of instantiating Converters", exception.getCause().getMessage());
    }

    @Test
    void registeredOfficeAddressApiToAddressInputNonNull() {
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

        var result = registeredOfficeAddressApiToAddress(inputAddress);

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
    void registeredOfficeAddressApiToAddressInputIsNull() {
        RegisteredOfficeAddressApi inputAddress = null;

        var result = registeredOfficeAddressApiToAddress(inputAddress);

        assertNull(result);
    }

    @Test
    void addressDtoToAddressInputNonNull() {
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

        var result = addressDtoToAddress(inputAddress);

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
    void addressDtoToAddressInputIsNull() {
        AddressDto inputAddress = null;

        var result = addressDtoToAddress(inputAddress);

        assertNull(result);
    }
}
