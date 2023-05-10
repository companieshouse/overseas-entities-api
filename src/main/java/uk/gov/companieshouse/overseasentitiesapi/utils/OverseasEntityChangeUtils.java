package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;

@Component
public class OverseasEntityChangeUtils {
    public ChangeOfEntityName verifyEntityNameChange(String existing, String updated) {
        return existing.equals(updated) ? null : new ChangeOfEntityName(updated);
    }

    public ChangeOfPrincipalAddress verifyPrincipalAddressChange(AddressDto existing, AddressDto updated) {
        return existing.equals(updated) ? null : new ChangeOfPrincipalAddress(updated);
    }

    public ChangeOfCorrespondenceAddress verifyCorrespondenceAddressChange(AddressDto existing, AddressDto updated) {
        return existing.equals(updated) ? null : new ChangeOfCorrespondenceAddress(updated);
    }

    public ChangeOfCompanyIdentification verifyCompanyIdentificationChange(String existingLegalForm,
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
                    ? null : new ChangeOfCompanyIdentification(
                        updatedLegalForm,
                        updatedGoverningLaw,
                        updatedRegisterLocation,
                        updatedCompanyIdentification);
    }

    public ChangeOfEntityEmailAddress verifyEntityEmailAddressChange(String existing, String updated) {
        return existing.equals(updated) ? null : new ChangeOfEntityEmailAddress(updated);
    }
}
