package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.*;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class BeneficialOwnerCorporateValidator {

    public static final String NATURE_OF_CONTROL_FIELDS = "nature_of_control";

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public BeneficialOwnerCorporateValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }

    public Errors validate(List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList, Errors errors, String loggingContext) {
        for(BeneficialOwnerCorporateDto beneficialOwnerCorporateDto : beneficialOwnerCorporateDtoList) {

            validateName(beneficialOwnerCorporateDto.getName(), errors, loggingContext);
            validateAddress(BeneficialOwnerCorporateDto.PRINCIPAL_ADDRESS_FIELD, beneficialOwnerCorporateDto.getPrincipalAddress(), errors, loggingContext);

            boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(beneficialOwnerCorporateDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
            if (sameAddressFlagValid && Boolean.FALSE.equals(beneficialOwnerCorporateDto.getServiceAddressSameAsPrincipalAddress())) {
                validateAddress(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, beneficialOwnerCorporateDto.getServiceAddress(), errors, loggingContext);
            }

            validateLegalForm(beneficialOwnerCorporateDto.getLegalForm(), errors,  loggingContext);
            validateLawGoverned(beneficialOwnerCorporateDto.getLawGoverned(), errors, loggingContext);

            boolean onRegister = validateOnRegisterInCountryFormedIn(beneficialOwnerCorporateDto.getOnRegisterInCountryFormedIn(),errors, loggingContext);
            if (onRegister && Boolean.FALSE.equals(beneficialOwnerCorporateDto.getOnRegisterInCountryFormedIn())) {
                validatePublicRegisterName(beneficialOwnerCorporateDto.getPublicRegisterName(), errors, loggingContext);
                validateRegistrationNumber(beneficialOwnerCorporateDto.getRegistrationNumber(), errors, loggingContext);
            }

            validateStartDate(beneficialOwnerCorporateDto.getStartDate(), errors, loggingContext);

            List<NatureOfControlType> fields = new ArrayList<>();
            if (Objects.nonNull(beneficialOwnerCorporateDto.getBeneficialOwnerNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerCorporateDto.getBeneficialOwnerNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerCorporateDto.getNonLegalFirmMembersNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerCorporateDto.getNonLegalFirmMembersNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerCorporateDto.getTrusteesNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerCorporateDto.getTrusteesNatureOfControlTypes());
            }
            validateNatureOfControl(fields, errors, loggingContext);

            validateOnSanctionsList(beneficialOwnerCorporateDto.getOnSanctionsList(), errors, loggingContext);

        }
        return errors;
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.NAME_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        return StringValidators.isNotBlank (legalForm, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(legalForm, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(legalForm, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        return StringValidators.isNotBlank (lawGoverned, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(lawGoverned, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lawGoverned, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateOnRegisterInCountryFormedIn(Boolean onRegister, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD);
        return UtilsValidators.isNotNull(onRegister, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePublicRegisterName(String publicRegisterName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        return StringValidators.isNotBlank (publicRegisterName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(publicRegisterName, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(publicRegisterName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateRegistrationNumber(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        return StringValidators.isNotBlank (registrationNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(registrationNumber, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(registrationNumber, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateStartDate(LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.START_DATE_FIELD);
        return UtilsValidators.isNotNull(startDate, qualifiedFieldName, errors, loggingContext) &&
                DateValidators.isDateIsInPast(startDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNatureOfControl(List<NatureOfControlType> fields, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, NATURE_OF_CONTROL_FIELDS);
        return NatureOfControlValidators.checkAtLeastOneFieldHasValue(fields, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateOnSanctionsList(Boolean onSanctionsList, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.IS_ON_SANCTIONS_LIST_FIELD);
        return UtilsValidators.isNotNull(onSanctionsList, qualifiedFieldName, errors, loggingContext);
    }
}
