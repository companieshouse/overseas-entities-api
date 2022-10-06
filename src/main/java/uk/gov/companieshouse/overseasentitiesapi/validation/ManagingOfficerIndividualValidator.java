package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class ManagingOfficerIndividualValidator {

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public ManagingOfficerIndividualValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }

    public Errors validate(List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList, Errors errors, String loggingContext) {
        for (ManagingOfficerIndividualDto managingOfficerIndividualDto : managingOfficerIndividualDtoList) {
            validateFirstName(managingOfficerIndividualDto.getFirstName(), errors, loggingContext);
            validateLastName(managingOfficerIndividualDto.getLastName(), errors, loggingContext);
            boolean isHasFormerNamesFlagValid = validateHasFormerNames(managingOfficerIndividualDto.getHasFormerNames(), errors, loggingContext);
            if (isHasFormerNamesFlagValid && Boolean.TRUE.equals(managingOfficerIndividualDto.getHasFormerNames())) {
                validateFormerNames(managingOfficerIndividualDto.getFormerNames(), errors, loggingContext);
            } else {
                validateFormerNamesAreNotSupplied(managingOfficerIndividualDto.getFormerNames(), errors, loggingContext);
            }
            validateDateOfBirth(managingOfficerIndividualDto.getDateOfBirth(), errors, loggingContext);
            validateNationality(managingOfficerIndividualDto.getNationality(), errors, loggingContext);
            validateAddress(ManagingOfficerIndividualDto.USUAL_RESIDENTIAL_ADDRESS_FIELD, managingOfficerIndividualDto.getUsualResidentialAddress(), errors, loggingContext);
            boolean isSameAddressFlagValid = validateIsServiceAddressSameAsUsualResidentialAddress(managingOfficerIndividualDto.getServiceAddressSameAsUsualResidentialAddress(), errors, loggingContext);
            if (isSameAddressFlagValid && Boolean.FALSE.equals(managingOfficerIndividualDto.getServiceAddressSameAsUsualResidentialAddress())) {
                validateAddress(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, managingOfficerIndividualDto.getServiceAddress(), errors, loggingContext);
            } else {
                validateOtherAddressIsNotSupplied(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, managingOfficerIndividualDto.getServiceAddress(), errors, loggingContext);
            }
            validateOccupation(managingOfficerIndividualDto.getOccupation(), errors, loggingContext);
            validateRoleAndResponsibilities(managingOfficerIndividualDto.getRoleAndResponsibilities(), errors, loggingContext);
        }
        return errors;
    }

    private boolean validateFirstName(String firstName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.FIRST_NAME_FIELD);
        return StringValidators.isNotBlank(firstName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(firstName, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(firstName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLastName(String lastName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        return StringValidators.isNotBlank(lastName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(lastName, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lastName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateHasFormerNames(Boolean hasFormerNames, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.HAS_FORMER_NAMES_FIELD);
        return UtilsValidators.isNotNull(hasFormerNames, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateFormerNamesAreNotSupplied(String formerNames, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        StringValidators.checkIsEmpty(formerNames, qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private boolean validateFormerNames(String formerNames, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        return StringValidators.isNotBlank(formerNames, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(formerNames, 260, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(formerNames, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateDateOfBirth(LocalDate dateOfBirth, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD);
        return UtilsValidators.isNotNull(dateOfBirth, qualifiedFieldName, errors, loggingContext)
            && DateValidators.isDateInPast(dateOfBirth, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNationality(String nationality, Errors errors, String loggingContext){
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.NATIONALITY_FIELD);
        return StringValidators.isNotBlank(nationality, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(nationality, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(nationality, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
        return errors;
    }

    private boolean validateIsServiceAddressSameAsUsualResidentialAddress(Boolean isServiceAddressSameAsUsualResidentialAddress, Errors errors, String loggingContext){
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(isServiceAddressSameAsUsualResidentialAddress, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateOtherAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateOccupation(String occupation, Errors errors, String loggingContext){
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.OCCUPATION_FIELD);
        return StringValidators.isNotBlank(occupation, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(occupation, 100, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(occupation, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateRoleAndResponsibilities(String roleAndResponsibilities, Errors errors, String loggingContext){
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD, ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        return StringValidators.isNotBlank(roleAndResponsibilities, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(roleAndResponsibilities, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(roleAndResponsibilities, qualifiedFieldName, errors, loggingContext);
    }
}
