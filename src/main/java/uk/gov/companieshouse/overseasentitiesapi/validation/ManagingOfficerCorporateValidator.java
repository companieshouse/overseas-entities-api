package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class ManagingOfficerCorporateValidator {
    
    private final AddressDtoValidator addressDtoValidator;
    
    @Autowired
    public ManagingOfficerCorporateValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }
    
    public Errors validate(List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList, Errors errors, String loggingContext) {
        for (ManagingOfficerCorporateDto managingOfficerCorporateDto : managingOfficerCorporateDtoList) {

            validateName(managingOfficerCorporateDto.getName(), errors, loggingContext);
            validateAddress(ManagingOfficerCorporateDto.PRINCIPAL_ADDRESS_FIELD, managingOfficerCorporateDto.getPrincipalAddress(), errors, loggingContext);

            boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(managingOfficerCorporateDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
            if (sameAddressFlagValid && Boolean.FALSE.equals(managingOfficerCorporateDto.getServiceAddressSameAsPrincipalAddress())) {
                validateAddress(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, managingOfficerCorporateDto.getServiceAddress(), errors, loggingContext);
            } else {
                validateServiceAddressIsNotSupplied(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, managingOfficerCorporateDto.getServiceAddress(), errors, loggingContext);
            }

            validateLegalForm(managingOfficerCorporateDto.getLegalForm(), errors, loggingContext);
            validateLawGoverned(managingOfficerCorporateDto.getLawGoverned(), errors, loggingContext);

            boolean onRegister = validateOnRegisterInCountryFormedIn(managingOfficerCorporateDto.getOnRegisterInCountryFormedIn(), errors, loggingContext);
            if (onRegister && Boolean.TRUE.equals(managingOfficerCorporateDto.getOnRegisterInCountryFormedIn())) {
                validatePublicRegisterName(managingOfficerCorporateDto.getPublicRegisterName(), errors, loggingContext);
                validateRegistrationNumber(managingOfficerCorporateDto.getRegistrationNumber(), errors, loggingContext);
            } else {
                validatePublicRegisterNameIsNotSupplied(managingOfficerCorporateDto.getPublicRegisterName(), errors, loggingContext);
                validateRegistrationNumberIsNotSupplied(managingOfficerCorporateDto.getRegistrationNumber(), errors, loggingContext);
            }

            validateRoleAndResponsibilities(managingOfficerCorporateDto.getRoleAndResponsibilities(), errors, loggingContext);
            validateContactFullName(managingOfficerCorporateDto.getContactFullName(), errors, loggingContext);
            validateContactEmail(managingOfficerCorporateDto.getContactEmail(), errors, loggingContext);
        }
        return errors;
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.NAME_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
        return errors;
    }

    private Errors validateServiceAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.LEGAL_FORM_FIELD);
        return StringValidators.isNotBlank (legalForm, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(legalForm, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(legalForm, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD);
        return StringValidators.isNotBlank (lawGoverned, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(lawGoverned, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lawGoverned, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateOnRegisterInCountryFormedIn(Boolean onRegister, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD);
        return UtilsValidators.isNotNull(onRegister, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePublicRegisterName(String publicRegisterName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        return StringValidators.isNotBlank (publicRegisterName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(publicRegisterName, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(publicRegisterName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateRegistrationNumber(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        return StringValidators.isNotBlank (registrationNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(registrationNumber, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(registrationNumber, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validatePublicRegisterNameIsNotSupplied(String publicRegisterName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        StringValidators.checkIsEmpty(publicRegisterName, qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private Errors validateRegistrationNumberIsNotSupplied(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        StringValidators.checkIsEmpty(registrationNumber, qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private boolean validateRoleAndResponsibilities(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharactersForTextBox(name, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateContactFullName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateContactEmail(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD, ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 250, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidEmailAddress(name, qualifiedFieldName, errors, loggingContext);
    }
}
