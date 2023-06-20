package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.registeredOfficeAddressApiToAddress;
import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.addressDtoToAddress;

@Component
public class OverseasEntityChangeComparator {
    public EntityNameChange compareEntityName(String existing, String updated) {
        return StringUtils.equals(existing, updated) || updated == null ? null : new EntityNameChange(updated);
    }

    public PrincipalAddressChange comparePrincipalAddress(
            RegisteredOfficeAddressApi existing, AddressDto updated) {
        if (updated == null) {
            return null;
        }

        var existingAddress = existing != null ? registeredOfficeAddressApiToAddress(existing) : null;
        var updatedAddress = addressDtoToAddress(updated);

        return updatedAddress.equals(existingAddress) ? null : new PrincipalAddressChange(updatedAddress);
    }

    public CorrespondenceAddressChange compareCorrespondenceAddress(
            RegisteredOfficeAddressApi existing, AddressDto updated) {
        if (updated == null) {
            return null;
        }

        var existingAddress = existing != null ? registeredOfficeAddressApiToAddress(existing) : null;
        var updatedAddress = addressDtoToAddress(updated);

        return updatedAddress.equals(existingAddress) ? null : new CorrespondenceAddressChange(updatedAddress);
    }

    public CompanyIdentificationChange compareCompanyIdentification(
            CompanyIdentification existing, CompanyIdentification updated) {
        return existing.equals(updated) || updated == null ?
                null : createCompanyIdentificationChange(existing, updated);
    }

    public EntityEmailAddressChange compareEntityEmailAddress(String existing, String updated) {
        return StringUtils.equals(existing, updated) || updated == null ? null : new EntityEmailAddressChange(updated);
    }

    private CompanyIdentificationChange createCompanyIdentificationChange(
            CompanyIdentification existing, CompanyIdentification updated){
        var proposedLegalForm = existing.getLegalForm() != null &&
                existing.getLegalForm().equals(updated.getLegalForm()) ?
                null : updated.getLegalForm();
        var proposedGoverningLaw = existing.getGoverningLaw() != null &&
                existing.getGoverningLaw().equals(updated.getGoverningLaw()) ?
                null : updated.getGoverningLaw();
        var proposedRegisterLocation = existing.getRegisterLocation() != null &&
                existing.getRegisterLocation().equals(updated.getRegisterLocation()) ?
                null : updated.getRegisterLocation();
        var proposedPlaceRegistered = existing.getPlaceRegistered() != null &&
                existing.getPlaceRegistered().equals(updated.getPlaceRegistered()) ?
                null : updated.getPlaceRegistered();
        var proposedRegistrationNumber = existing.getRegistrationNumber() != null &&
                existing.getRegistrationNumber().equals(updated.getRegistrationNumber()) ?
                null : updated.getRegistrationNumber();

        return new CompanyIdentificationChange(
                proposedLegalForm,
                proposedGoverningLaw,
                proposedRegisterLocation,
                proposedPlaceRegistered,
                proposedRegistrationNumber);
    }
}
