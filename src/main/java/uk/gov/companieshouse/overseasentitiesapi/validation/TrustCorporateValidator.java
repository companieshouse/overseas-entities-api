package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.*;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class TrustCorporateValidator {
    public static final String PARENT_FIELD = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA,
            TrustDataDto.CORPORATE_FIELD);

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public TrustCorporateValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }

    public Errors validate(List<TrustDataDto> trustDataDtoList, Errors errors, String loggingContext) {

        for (TrustDataDto trustDataDto : trustDataDtoList) {
            List<TrustCorporateDto> corporates = trustDataDto.getCorporates();

            if (!CollectionUtils.isEmpty(corporates)) {
                for (TrustCorporateDto trustCorporateDto : corporates) {
                    validateName(trustCorporateDto.getName(), errors, loggingContext);
                    validateType(trustCorporateDto.getType(), errors, loggingContext);
                    validateDateBecameInterestedPerson(trustCorporateDto.getDateBecameInterestedPerson(), errors,
                            loggingContext);

                    validateAddress(TrustCorporateDto.REGISTERED_OFFICE_ADDRESS_FIELD,
                            trustCorporateDto.getRegisteredOfficeAddress(), errors, loggingContext);

                    boolean isSameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(
                            trustCorporateDto.getServiceAddressSameAsPrincipalAddress(), errors,
                            loggingContext);

                    if (isSameAddressFlagValid && Boolean.FALSE
                            .equals(trustCorporateDto.getServiceAddressSameAsPrincipalAddress())) {
                        validateAddress(TrustCorporateDto.SERVICE_ADDRESS_FIELD,
                                trustCorporateDto.getServiceAddress(), errors, loggingContext);
                    } else if (isSameAddressFlagValid && Boolean.TRUE
                            .equals(trustCorporateDto.getServiceAddressSameAsPrincipalAddress())) {
                        validateServiceAddressIsNotSupplied(TrustCorporateDto.SERVICE_ADDRESS_FIELD,
                                trustCorporateDto.getServiceAddress(), errors, loggingContext);
                    }

                    validateIdentificationLegalForm(trustCorporateDto.getIdentificationLegalForm(), errors,
                            loggingContext);
                    validateIdentificationLegalAuthority(trustCorporateDto.getIdentificationLegalAuthority(), errors,
                            loggingContext);

                    boolean isOnRegisterInCountryFormedInIsValid =
                            validateOnRegisteredInCountryFormedIn(trustCorporateDto.
                                    getOnRegisterInCountryFormedIn(), errors, loggingContext);

                    if (isOnRegisterInCountryFormedInIsValid && Boolean.FALSE
                            .equals(trustCorporateDto.getOnRegisterInCountryFormedIn())) {
                        validateOnRegisteredInCountryFormedInNotSupplied(trustCorporateDto, errors, loggingContext);
                    } else if(isOnRegisterInCountryFormedInIsValid && Boolean.TRUE
                            .equals(trustCorporateDto.getOnRegisterInCountryFormedIn())) {
                        validateOnRegisteredInCountryFormedInSupplied(trustCorporateDto, errors, loggingContext);
                    }
                }
            }
        }

        return errors;
    }

    private Errors validateServiceAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors,
                                                       String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors,
                                                                 String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);

        return StringValidators.isNotBlank(name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateType(String type, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);

        if (!StringValidators.isNotBlank(type, qualifiedFieldName, errors, loggingContext)) {
            return false;
        }

        if (BeneficialOwnerType.findByBeneficialOwnerTypeString(type) == null) {
            String message = ValidationMessages.TRUST_CORPORATE_TYPE_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, message);
            ApiLogger.infoContext(loggingContext, message);
            return false;
        }

        return true;
    }

    private boolean validateDateBecameInterestedPerson(LocalDate dateBecameInterestedPerson, Errors errors,
                                                       String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.DATE_BECAME_INTERESTED_PERSON_FIELD);

        return UtilsValidators.isNotNull(dateBecameInterestedPerson, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(dateBecameInterestedPerson, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, addressField);
        errors = addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors,
                loggingContext);

        return errors;
    }

    private boolean validateIdentificationLegalForm(String legalForm, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);

        return StringValidators.isNotBlank(legalForm, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(legalForm, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(legalForm, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateIdentificationLegalAuthority(String legalAuthor, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);

        return StringValidators.isNotBlank(legalAuthor, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(legalAuthor, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(legalAuthor, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateOnRegisteredInCountryFormedIn(Boolean onRegisteredInCountryFormedIn, Errors errors,
                                                        String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN);
        return UtilsValidators.isNotNull(onRegisteredInCountryFormedIn, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateIdentificationPlaceRegistered(String placeRegistered, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);

        return StringValidators.isNotBlank(placeRegistered, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(placeRegistered, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(placeRegistered, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateIdentificationCountryRegistration(String countryRegistration, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);

        return StringValidators.isNotBlank(countryRegistration, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(countryRegistration, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(countryRegistration, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateIdentificationRegistrationNumber(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);

        return StringValidators.isNotBlank(registrationNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(registrationNumber, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(registrationNumber, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateOnRegisteredInCountryFormedInSupplied(TrustCorporateDto trustCorporateDto, Errors errors, String loggingContext) {

        if(Objects.nonNull(trustCorporateDto)) {
            validateIdentificationPlaceRegistered(trustCorporateDto.getIdentificationPlaceRegistered(), errors,
                    loggingContext);
            validateIdentificationCountryRegistration(trustCorporateDto.getIdentificationCountryRegistration(), errors,
                    loggingContext);
            validateIdentificationRegistrationNumber(trustCorporateDto.getIdentificationRegistrationNumber(), errors,
                    loggingContext);
        }

        return errors;
    }

    private Errors validateOnRegisteredInCountryFormedInNotSupplied(TrustCorporateDto trustCorporateDto, Errors errors, String loggingContext) {

        if (Objects.nonNull(trustCorporateDto)) {
            StringValidators.checkIsEmpty(trustCorporateDto.getIdentificationCountryRegistration(), getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(trustCorporateDto.getIdentificationCountryRegistration(), getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(trustCorporateDto.getIdentificationRegistrationNumber(), getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD), errors, loggingContext);
        }

        return errors;
    }
}
