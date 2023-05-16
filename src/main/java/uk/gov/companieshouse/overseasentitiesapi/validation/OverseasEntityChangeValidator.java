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

    public PrincipalAddressChange verifyPrincipalAddressChange(
            RegisteredOfficeAddressApi existing, AddressDto updated) {
        var existingAddressDto = convertToAddressDto(existing);
        return existingAddressDto.equals(updated) ? null : new PrincipalAddressChange(existingAddressDto, updated);
    }

    public CorrespondenceAddressChange verifyCorrespondenceAddressChange(
            RegisteredOfficeAddressApi existing, AddressDto updated) {
        var existingAddressDto = convertToAddressDto(existing);
        return existingAddressDto.equals(updated) ? null : new CorrespondenceAddressChange(existingAddressDto, updated);
    }

    public CompanyIdentificationChange verifyCompanyIdentificationChange(
            CompanyIdentification existing, CompanyIdentification updated) {
        return existing.equals(updated) ? null : new CompanyIdentificationChange(existing, updated);
    }

    public EntityEmailAddressChange verifyEntityEmailAddressChange(String existing, String updated) {
        return StringUtils.equals(existing, updated) ? null : new EntityEmailAddressChange(existing, updated);
    }

    private AddressDto convertToAddressDto(RegisteredOfficeAddressApi existing) {
        var addressDto = new AddressDto();

        addressDto.setPropertyNameNumber(existing.getPremises());
        addressDto.setLine1(existing.getAddressLine1());
        addressDto.setLine2(existing.getAddressLine2());
        addressDto.setTown(existing.getLocality());
        addressDto.setCounty(existing.getRegion());
        addressDto.setCountry(existing.getCountry());
        addressDto.setPostcode(existing.getPostalCode());
        addressDto.setPoBox(existing.getPoBox());
        addressDto.setCareOf(existing.getCareOf());

        return addressDto;
    }
}
