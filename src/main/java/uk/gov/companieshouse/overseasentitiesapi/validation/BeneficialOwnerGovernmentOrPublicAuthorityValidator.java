package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.NatureOfControlValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class BeneficialOwnerGovernmentOrPublicAuthorityValidator {

    public static final String NATURE_OF_CONTROL_FIELDS = "nature_of_control";

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public BeneficialOwnerGovernmentOrPublicAuthorityValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }
    
    public Errors validate(List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList, 
                           Errors errors, String loggingContext) {
        for (BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto : beneficialOwnerGovernmentOrPublicAuthorityDtoList) {
            validateName(beneficialOwnerGovernmentOrPublicAuthorityDto.getName(), errors, loggingContext);
            validateAddress(beneficialOwnerGovernmentOrPublicAuthorityDto.PRINCIPAL_ADDRESS_FIELD, beneficialOwnerGovernmentOrPublicAuthorityDto.getPrincipalAddress(), errors, loggingContext);

            boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
            if (sameAddressFlagValid && Boolean.FALSE.equals(beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddressSameAsPrincipalAddress())) {
                validateAddress(beneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddress(), errors, loggingContext);
            }

            validateLegalForm(beneficialOwnerGovernmentOrPublicAuthorityDto.getLegalForm(), errors,  loggingContext);
            validateLawGoverned(beneficialOwnerGovernmentOrPublicAuthorityDto.getLawGoverned(), errors, loggingContext);
           
            validateStartDate(beneficialOwnerGovernmentOrPublicAuthorityDto.getStartDate(), errors, loggingContext);

            List<NatureOfControlType> fields = new ArrayList<>();
            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getBeneficialOwnerNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getBeneficialOwnerNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmMembersNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmMembersNatureOfControlTypes());
            }            
            validateNatureOfControl(fields, errors, loggingContext);

            validateOnSanctionsList(beneficialOwnerGovernmentOrPublicAuthorityDto.getOnSanctionsList(), errors, loggingContext);
        }
        return errors;
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD);
        return StringValidators.isNotBlank (legalForm, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(legalForm, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(legalForm, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD);
        return StringValidators.isNotBlank (lawGoverned, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(lawGoverned, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lawGoverned, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateStartDate(LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.START_DATE_FIELD);
        return UtilsValidators.isNotNull(startDate, qualifiedFieldName, errors, loggingContext) &&
                DateValidators.isDateIsInPast(startDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNatureOfControl(List<NatureOfControlType> fields, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, NATURE_OF_CONTROL_FIELDS);
        return NatureOfControlValidators.checkAtLeastOneFieldHasValue(fields, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateOnSanctionsList(Boolean onSanctionsList, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_ON_SANCTIONS_LIST_FIELD);
        return UtilsValidators.isNotNull(onSanctionsList, qualifiedFieldName, errors, loggingContext);
    }
}
