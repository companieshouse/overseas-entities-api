package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.NatureOfControlValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class BeneficialOwnerIndividualValidator {

    public static final String NATURE_OF_CONTROL_FIELDS = "nature_of_control";

    private final AddressDtoValidator addressDtoValidator;
    private final NationalityValidator nationalityValidator;

    @Autowired
    public BeneficialOwnerIndividualValidator(AddressDtoValidator addressDtoValidator,
                                              NationalityValidator nationalityValidator) {
        this.addressDtoValidator = addressDtoValidator;
        this.nationalityValidator = nationalityValidator;
    }

    public Errors validate(List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList, Errors errors, String loggingContext) {

        for (BeneficialOwnerIndividualDto beneficialOwnerIndividualDto : beneficialOwnerIndividualDtoList) {
            validateFirstName(beneficialOwnerIndividualDto.getFirstName(), errors, loggingContext);
            validateLastName(beneficialOwnerIndividualDto.getLastName(), errors, loggingContext);
            validateDateOfBirth(beneficialOwnerIndividualDto.getDateOfBirth(), errors, loggingContext);
            validateNationality(beneficialOwnerIndividualDto.getNationality(), errors, loggingContext);
            if (Objects.nonNull(beneficialOwnerIndividualDto.getSecondNationality())) {
                validateSecondNationality(beneficialOwnerIndividualDto.getNationality(), beneficialOwnerIndividualDto.getSecondNationality(), errors, loggingContext);
            }
            validateAddress(BeneficialOwnerIndividualDto.USUAL_RESIDENTIAL_ADDRESS_FIELD, beneficialOwnerIndividualDto.getUsualResidentialAddress(), errors, loggingContext);
            boolean sameAddressFlagValid = validateServiceAddressSameAsUsualResidentialAddress(beneficialOwnerIndividualDto.getServiceAddressSameAsUsualResidentialAddress(), errors, loggingContext);
            if (sameAddressFlagValid && Boolean.FALSE.equals(beneficialOwnerIndividualDto.getServiceAddressSameAsUsualResidentialAddress())) {
                validateAddress(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, beneficialOwnerIndividualDto.getServiceAddress(), errors, loggingContext);
            } else {
                validateOtherAddressIsNotSupplied(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, beneficialOwnerIndividualDto.getServiceAddress(), errors, loggingContext);
            }

            validateStartDate(beneficialOwnerIndividualDto.getStartDate(), errors, loggingContext);
            validateIsOnSanctionsList(beneficialOwnerIndividualDto.getOnSanctionsList(), errors, loggingContext);

            List<NatureOfControlType> fields = new ArrayList<>();
            if (Objects.nonNull(beneficialOwnerIndividualDto.getBeneficialOwnerNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerIndividualDto.getBeneficialOwnerNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerIndividualDto.getNonLegalFirmMembersNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerIndividualDto.getNonLegalFirmMembersNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerIndividualDto.getTrusteesNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerIndividualDto.getTrusteesNatureOfControlTypes());
            }
            validateNatureOfControl(fields, errors, loggingContext);
            if (Objects.nonNull(beneficialOwnerIndividualDto.getCeasedDate())) {
                validateCeasedDate(beneficialOwnerIndividualDto.getCeasedDate(), beneficialOwnerIndividualDto.getStartDate(), errors, loggingContext);
            }
        }

        return errors;
    }

    private boolean validateFirstName(String firstName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        return StringValidators.isNotBlank(firstName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(firstName, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(firstName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLastName(String lastName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        return StringValidators.isNotBlank(lastName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(lastName, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lastName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateDateOfBirth(LocalDate dateOfBirth, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD);
        return UtilsValidators.isNotNull(dateOfBirth, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(dateOfBirth, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateNationality(String nationality, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        boolean nationalityIsPresent = StringValidators.isNotBlank(nationality, qualifiedFieldName, errors, loggingContext);
        if (nationalityIsPresent) {
            nationalityValidator.validateAgainstNationalityList(qualifiedFieldName, nationality, errors, loggingContext);
        }
        return errors;
    }

    private Errors validateSecondNationality(String nationality, String secondNationality, Errors errors, String loggingContext) {
        String qualifiedFieldNameFirstNationality = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        String qualifiedFieldNameSecondNationality = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.SECOND_NATIONALITY_FIELD);
        boolean isSecondNationalityNotBlank = StringUtils.isNotBlank(secondNationality);
        if (isSecondNationalityNotBlank) {
            nationalityValidator.validateSecondNationality(
                    qualifiedFieldNameFirstNationality,
                    qualifiedFieldNameSecondNationality,
                    nationality,
                    secondNationality,
                    errors,
                    loggingContext);
        }
        return errors;
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsUsualResidentialAddress(Boolean same, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateOtherAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateStartDate(LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.START_DATE_FIELD);
        return UtilsValidators.isNotNull(startDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(startDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateIsOnSanctionsList(Boolean isOnSanctionsList, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.IS_ON_SANCTIONS_LIST_FIELD);
        return UtilsValidators.isNotNull(isOnSanctionsList, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNatureOfControl(List<NatureOfControlType> fields, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, NATURE_OF_CONTROL_FIELDS);
        return NatureOfControlValidators.checkAtLeastOneFieldHasValue(fields, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCeasedDate(LocalDate ceasedDate, LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, BeneficialOwnerIndividualDto.CEASED_DATE_FIELD);
        return DateValidators.isDateInPast(ceasedDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isCeasedDateAfterStartDate(ceasedDate, startDate, qualifiedFieldName, errors, loggingContext);
    }
}
