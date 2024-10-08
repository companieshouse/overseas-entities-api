package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
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

    @Value("${FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024:false}")
    private boolean isPropertyAndLandNocEnabled;

    @Autowired
    public BeneficialOwnerGovernmentOrPublicAuthorityValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }
    
    public Errors validate(List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList, 
                           Errors errors, String loggingContext) {
        for (BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto : beneficialOwnerGovernmentOrPublicAuthorityDtoList) {
            validateName(beneficialOwnerGovernmentOrPublicAuthorityDto.getName(), errors, loggingContext);
            validateAddress(BeneficialOwnerGovernmentOrPublicAuthorityDto.PRINCIPAL_ADDRESS_FIELD, beneficialOwnerGovernmentOrPublicAuthorityDto.getPrincipalAddress(), errors, loggingContext);

            boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
            if (sameAddressFlagValid && Boolean.FALSE.equals(beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddressSameAsPrincipalAddress())) {
                validateAddress(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddress(), errors, loggingContext);
            } else {
                validateOtherAddressIsNotSupplied(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddress(), errors, loggingContext);
            }

            validateLegalForm(beneficialOwnerGovernmentOrPublicAuthorityDto.getLegalForm(), errors,  loggingContext);
            validateLawGoverned(beneficialOwnerGovernmentOrPublicAuthorityDto.getLawGoverned(), errors, loggingContext);
           
            validateStartDate(beneficialOwnerGovernmentOrPublicAuthorityDto.getStartDate(), errors, loggingContext);

            validateAllNatureOfControlFields(beneficialOwnerGovernmentOrPublicAuthorityDto, errors, loggingContext);

            validateOnSanctionsList(beneficialOwnerGovernmentOrPublicAuthorityDto.getOnSanctionsList(), errors, loggingContext);

            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getCeasedDate())) {
                validateCeasedDate(beneficialOwnerGovernmentOrPublicAuthorityDto.getCeasedDate(), beneficialOwnerGovernmentOrPublicAuthorityDto.getStartDate(), errors, loggingContext);
            }
        }
        return errors;
    }

    private void validateAllNatureOfControlFields(BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto, Errors errors, String loggingContext) {

        List<NatureOfControlType> fields = new ArrayList<>();
        List<NatureOfControlJurisdictionType> jurisdictionFields = new ArrayList<>();
        if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getBeneficialOwnerNatureOfControlTypes())) {
            fields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getBeneficialOwnerNatureOfControlTypes());
        }
        if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmMembersNatureOfControlTypes())) {
            fields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmMembersNatureOfControlTypes());
        }


        if (isPropertyAndLandNocEnabled) {
            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmControlNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmControlNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getTrustControlNatureOfControlTypes())) {
                fields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getTrustControlNatureOfControlTypes());
            }
            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getOwnerOfLandPersonNatureOfControlJurisdictions())) {
                jurisdictionFields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getOwnerOfLandPersonNatureOfControlJurisdictions());
            }
            if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto.getOwnerOfLandOtherEntityNatureOfControlJurisdictions())) {
                jurisdictionFields.addAll(beneficialOwnerGovernmentOrPublicAuthorityDto.getOwnerOfLandOtherEntityNatureOfControlJurisdictions());
            }
        }
        validateNatureOfControl(fields, jurisdictionFields, errors, loggingContext);
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
        return errors;
    }

    private Errors validateOtherAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
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
        return UtilsValidators.isNotNull(startDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(startDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNatureOfControl(List<NatureOfControlType> fields, List<NatureOfControlJurisdictionType> jurisdictionFields, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, NATURE_OF_CONTROL_FIELDS);
        return NatureOfControlValidators.checkAtLeastOneFieldHasValue(fields, jurisdictionFields, qualifiedFieldName, isPropertyAndLandNocEnabled, errors, loggingContext);
    }

    private boolean validateOnSanctionsList(Boolean onSanctionsList, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,  BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_ON_SANCTIONS_LIST_FIELD);
        return UtilsValidators.isNotNull(onSanctionsList, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCeasedDate(LocalDate ceasedDate, LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, BeneficialOwnerGovernmentOrPublicAuthorityDto.CEASED_DATE_FIELD);
        return DateValidators.isDateInPast(ceasedDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isCeasedDateOnOrAfterStartDate(ceasedDate, startDate, qualifiedFieldName, errors, loggingContext);
    }
}
