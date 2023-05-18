package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.utils.Address;

@Component
public class OverseasEntityChangeValidator {
    public EntityNameChange verifyEntityNameChange(String existing, String updated) {
        return StringUtils.equals(existing, updated) || updated == null ?
                null : new EntityNameChange(existing, updated);
    }

    public PrincipalAddressChange verifyPrincipalAddressChange(
            RegisteredOfficeAddressApi existing, AddressDto updated) {
        if (updated == null) {
            return null;
        }

        var existingAddress = convertRegisteredOfficeAddressApiToAddressModel(existing);
        var updatedAddress = convertAddressDtoToAddressModel(updated);

        return existingAddress.equals(updatedAddress) ?
                null : new PrincipalAddressChange(existingAddress, updatedAddress);
    }

    public CorrespondenceAddressChange verifyCorrespondenceAddressChange(
            RegisteredOfficeAddressApi existing, AddressDto updated) {
        if (updated == null) {
            return null;
        }

        var existingAddress = convertRegisteredOfficeAddressApiToAddressModel(existing);
        var updatedAddress = convertAddressDtoToAddressModel(updated);

        return existingAddress.equals(updatedAddress) ?
                null : new CorrespondenceAddressChange(existingAddress, updatedAddress);
    }

    public CompanyIdentificationChange verifyCompanyIdentificationChange(
            CompanyIdentification existing, CompanyIdentification updated) {
        return existing.equals(updated) || updated == null ? null : new CompanyIdentificationChange(existing, updated);
    }

    public EntityEmailAddressChange verifyEntityEmailAddressChange(String existing, String updated) {
        return StringUtils.equals(existing, updated) || updated == null ?
                null : new EntityEmailAddressChange(existing, updated);
    }

    private Address convertRegisteredOfficeAddressApiToAddressModel(
            RegisteredOfficeAddressApi registeredOfficeAddressApi) {
        var address = new Address();

        address.setHouseNameNum(registeredOfficeAddressApi.getPremises());
        address.setStreet(registeredOfficeAddressApi.getAddressLine1());
        address.setArea(registeredOfficeAddressApi.getAddressLine2());
        address.setPostTown(registeredOfficeAddressApi.getLocality());
        address.setRegion(registeredOfficeAddressApi.getRegion());
        address.setCountry(registeredOfficeAddressApi.getCountry());
        address.setPostCode(registeredOfficeAddressApi.getPostalCode());
        address.setPoBox(registeredOfficeAddressApi.getPoBox());
        address.setCareOf(registeredOfficeAddressApi.getCareOf());

        return address;
    }

    private Address convertAddressDtoToAddressModel(AddressDto addressDto) {
        var address = new Address();

        address.setHouseNameNum(addressDto.getPropertyNameNumber());
        address.setStreet(addressDto.getLine1());
        address.setArea(addressDto.getLine2());
        address.setPostTown(addressDto.getLocality());
        address.setRegion(addressDto.getCounty());
        address.setCountry(addressDto.getCountry());
        address.setPostCode(addressDto.getPostcode());
        address.setPoBox(addressDto.getPoBox());
        address.setCareOf(addressDto.getCareOf());

        return address;
    }
}
