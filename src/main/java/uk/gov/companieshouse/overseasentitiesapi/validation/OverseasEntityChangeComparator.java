package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.CompanyIdentificationChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.CorrespondenceAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityEmailAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityNameChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.PrincipalAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.utils.ComparisonHelper;

import java.util.StringJoiner;

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
        return ComparisonHelper.equals(existing, updated) || updated == null ?
                null : createCompanyIdentificationChange(existing, updated);
    }

    public EntityEmailAddressChange compareEntityEmailAddress(String existing, String updated) {
        return StringUtils.equals(existing, updated) || updated == null ? null : new EntityEmailAddressChange(updated);
    }

    private CompanyIdentificationChange createCompanyIdentificationChange(
            CompanyIdentification existing, CompanyIdentification updated){
        var format = new StringJoiner(",");
        var registerInformationFormat = format.add(updated.getPlaceRegistered()).add(updated.getPlaceRegisteredJurisdiction());

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
                existing.getPlaceRegistered().equals(registerInformationFormat.toString()) ?
                null : updated.getPlaceRegistered();
        var proposedRegisterJurisdiction = existing.getPlaceRegistered() != null &&
                existing.getPlaceRegistered().equals(registerInformationFormat.toString()) ?
                null : updated.getPlaceRegisteredJurisdiction();
        var proposedRegistrationNumber = existing.getRegistrationNumber() != null &&
                existing.getRegistrationNumber().equals(updated.getRegistrationNumber()) ?
                null : updated.getRegistrationNumber();

        return new CompanyIdentificationChange(
                proposedLegalForm,
                proposedGoverningLaw,
                proposedRegisterLocation,
                proposedPlaceRegistered,
                proposedRegisterJurisdiction,
                proposedRegistrationNumber);
    }
}
