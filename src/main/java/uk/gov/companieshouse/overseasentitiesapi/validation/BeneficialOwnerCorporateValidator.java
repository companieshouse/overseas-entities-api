package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.NatureOfControlValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class BeneficialOwnerCorporateValidator {

    public static final String NATURE_OF_CONTROL_FIELDS = "nature_of_control";

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public BeneficialOwnerCorporateValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }


    public Errors validate(List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList, Errors errors, String loggingContext) {
        for (BeneficialOwnerCorporateDto beneficialOwnerCorporateDto : beneficialOwnerCorporateDtoList) {

            validateName(beneficialOwnerCorporateDto.getName(), errors, loggingContext);
            validateAddress(BeneficialOwnerCorporateDto.PRINCIPAL_ADDRESS_FIELD, beneficialOwnerCorporateDto.getPrincipalAddress(), errors, loggingContext);

            boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(beneficialOwnerCorporateDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
            if (sameAddressFlagValid && Boolean.FALSE.equals(beneficialOwnerCorporateDto.getServiceAddressSameAsPrincipalAddress())) {
                validateAddress(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, beneficialOwnerCorporateDto.getServiceAddress(), errors, loggingContext);
            } else {
                validateOtherAddressIsNotSupplied(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, beneficialOwnerCorporateDto.getServiceAddress(), errors, loggingContext);
            }

            validateLegalForm(beneficialOwnerCorporateDto.getLegalForm(), errors,  loggingContext);
            validateLawGoverned(beneficialOwnerCorporateDto.getLawGoverned(), errors, loggingContext);

            boolean onRegister = validateOnRegisterInCountryFormedIn(beneficialOwnerCorporateDto.getOnRegisterInCountryFormedIn(),errors, loggingContext);
            if (onRegister && Boolean.TRUE.equals(beneficialOwnerCorporateDto.getOnRegisterInCountryFormedIn())) {
                validatePublicRegisterName(beneficialOwnerCorporateDto.getPublicRegisterName(), errors, loggingContext);
                validateRegistrationNumber(beneficialOwnerCorporateDto.getRegistrationNumber(), errors, loggingContext);
            } else {
                validatePublicRegisterNameIsNotSupplied(beneficialOwnerCorporateDto.getPublicRegisterName(), errors, loggingContext);
                validateRegistrationNumberIsNotSupplied(beneficialOwnerCorporateDto.getRegistrationNumber(), errors, loggingContext);
            }

            validateStartDate(beneficialOwnerCorporateDto.getStartDate(), errors, loggingContext);

            var fields = Stream.of(
                            beneficialOwnerCorporateDto.getBeneficialOwnerNatureOfControlTypes(),
                            beneficialOwnerCorporateDto.getNonLegalFirmMembersNatureOfControlTypes(),
                            beneficialOwnerCorporateDto.getTrusteesNatureOfControlTypes())
                    .filter(Objects::nonNull).flatMap(Collection::stream)
                    .collect(Collectors.toList());

            validateNatureOfControl(fields, errors, loggingContext);

            validateOnSanctionsList(beneficialOwnerCorporateDto.getOnSanctionsList(), errors, loggingContext);

            if (Objects.nonNull(beneficialOwnerCorporateDto.getCeasedDate())) {
                validateCeasedDate(beneficialOwnerCorporateDto.getCeasedDate(), beneficialOwnerCorporateDto.getStartDate(), errors, loggingContext);
            }
        }
        return errors;
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.NAME_FIELD);
        return StringValidators.isNotBlank (name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 160, qualifiedFieldName, errors, loggingContext)
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

    private Errors validateOtherAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
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

    private Errors validatePublicRegisterNameIsNotSupplied(String publicRegisterName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        StringValidators.checkIsEmpty(publicRegisterName, qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private Errors validateRegistrationNumberIsNotSupplied(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        StringValidators.checkIsEmpty(registrationNumber, qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private boolean validateStartDate(LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.START_DATE_FIELD);
        return UtilsValidators.isNotNull(startDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(startDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNatureOfControl(List<NatureOfControlType> fields, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, NATURE_OF_CONTROL_FIELDS);
        return NatureOfControlValidators.checkAtLeastOneFieldHasValue(fields, null, qualifiedFieldName, errors, loggingContext, false);
    }

    private boolean validateOnSanctionsList(Boolean onSanctionsList, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.IS_ON_SANCTIONS_LIST_FIELD);
        return UtilsValidators.isNotNull(onSanctionsList, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCeasedDate(LocalDate ceasedDate, LocalDate startDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD, BeneficialOwnerCorporateDto.CEASED_DATE_FIELD);
        return DateValidators.isDateInPast(ceasedDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isCeasedDateOnOrAfterStartDate(ceasedDate, startDate, qualifiedFieldName, errors, loggingContext);
    }
}
