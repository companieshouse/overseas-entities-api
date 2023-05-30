package uk.gov.companieshouse.overseasentitiesapi.utils;

import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public class AddressUtils {
    public static Address convertRegisteredOfficeAddressApiToAddressModel(
            RegisteredOfficeAddressApi registeredOfficeAddressApi) {
        Address address = null;
        if (registeredOfficeAddressApi != null) {
            address = new Address();
            address.setHouseNameNum(registeredOfficeAddressApi.getPremises());
            address.setStreet(registeredOfficeAddressApi.getAddressLine1());
            address.setArea(registeredOfficeAddressApi.getAddressLine2());
            address.setPostTown(registeredOfficeAddressApi.getLocality());
            address.setRegion(registeredOfficeAddressApi.getRegion());
            address.setCountry(registeredOfficeAddressApi.getCountry());
            address.setPostCode(registeredOfficeAddressApi.getPostalCode());
            address.setPoBox(registeredOfficeAddressApi.getPoBox());
            address.setCareOf(registeredOfficeAddressApi.getCareOf());
        }

        return address;
    }

    public static Address convertAddressDtoToAddressModel(AddressDto addressDto) {
        Address address = null;
        if (addressDto != null) {
            address = new Address();
            address.setHouseNameNum(addressDto.getPropertyNameNumber());
            address.setStreet(addressDto.getLine1());
            address.setArea(addressDto.getLine2());
            address.setPostTown(addressDto.getLocality());
            address.setRegion(addressDto.getCounty());
            address.setCountry(addressDto.getCountry());
            address.setPostCode(addressDto.getPostcode());
            address.setPoBox(addressDto.getPoBox());
            address.setCareOf(addressDto.getCareOf());
        }

        return address;
    }
}
