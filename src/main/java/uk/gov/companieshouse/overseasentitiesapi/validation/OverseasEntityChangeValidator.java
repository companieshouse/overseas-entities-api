package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;

@Component
public class OverseasEntityChangeValidator {
    public EntityNameChange verifyEntityNameChange(String existing, String updated) {
        return StringUtils.equals(existing, updated) ? null : new EntityNameChange(existing, updated);
    }

    public PrincipalAddressChange verifyPrincipalAddressChange(RegisteredOfficeAddressApi existing, AddressDto updated) {
        return validateSameAddress(existing, updated) ? null : new PrincipalAddressChange(existing, updated);
    }

    public CorrespondenceAddressChange verifyCorrespondenceAddressChange(RegisteredOfficeAddressApi existing, AddressDto updated) {
        return validateSameAddress(existing, updated) ? null : new CorrespondenceAddressChange(existing, updated);
    }

    public CompanyIdentificationChange verifyCompanyIdentificationChange(String existingLegalForm,
                                                                         String existingGoverningLaw,
                                                                         String existingRegisterLocation,
                                                                         String existingCompanyIdentification,
                                                                         String updatedLegalForm,
                                                                         String updatedGoverningLaw,
                                                                         String updatedRegisterLocation,
                                                                         String updatedCompanyIdentification) {
        return existingLegalForm.equals(updatedLegalForm) &&
                existingGoverningLaw.equals(updatedGoverningLaw) &&
                existingRegisterLocation.equals(updatedRegisterLocation) &&
                existingCompanyIdentification.equals(updatedCompanyIdentification)
                ? null : new CompanyIdentificationChange(
                    existingLegalForm, updatedLegalForm,
                    existingGoverningLaw, updatedGoverningLaw,
                    existingRegisterLocation, updatedRegisterLocation,
                    existingCompanyIdentification, updatedCompanyIdentification);
    }

    public EntityEmailAddressChange verifyEntityEmailAddressChange(String existing, String updated) {
        return existing.equals(updated) ? null : new EntityEmailAddressChange(existing, updated);
    }

    private boolean validateSameAddress(RegisteredOfficeAddressApi existing, AddressDto updated) {
        return StringUtils.equals(existing.getPremises(), updated.getPropertyNameNumber())
                && StringUtils.equals(existing.getAddressLine1(), updated.getLine1())
                && StringUtils.equals(existing.getAddressLine2(), updated.getLine2())
                && StringUtils.equals(existing.getLocality(), updated.getTown())
                && StringUtils.equals(existing.getRegion(), updated.getCounty())
                && StringUtils.equals(existing.getCountry(), updated.getCountry())
                && StringUtils.equals(existing.getPostalCode(), updated.getPostcode())
                && StringUtils.equals(existing.getPoBox(), updated.getPoBox())
                && StringUtils.equals(existing.getCareOf(), updated.getCareOf());
    }
}
