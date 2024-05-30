package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

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
                    validateTrustCorporateDto(trustCorporateDto, errors, loggingContext);
                }
            }
        }
        return errors;
    }

    private void validateTrustCorporateDto(TrustCorporateDto trustCorporateDto, Errors errors,
            String loggingContext) {

        validateName(trustCorporateDto.getName(), errors, loggingContext);

        String type = trustCorporateDto.getType();
        if (validateType(type, errors, loggingContext) && BeneficialOwnerType
                .findByBeneficialOwnerTypeString(type)
                .equals(BeneficialOwnerType.INTERESTED_PERSON)) {

            validateDateBecameInterestedPerson(
                    trustCorporateDto.getDateBecameInterestedPerson(),
                    errors,
                    loggingContext);
        }

        validateAddress(TrustCorporateDto.REGISTERED_OFFICE_ADDRESS_FIELD,
                trustCorporateDto.getRegisteredOfficeAddress(), errors, loggingContext);

        boolean isSameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(
                trustCorporateDto.getServiceAddressSameAsPrincipalAddress(), errors,
                loggingContext);

        if (isSameAddressFlagValid && Boolean.FALSE
                .equals(trustCorporateDto.getServiceAddressSameAsPrincipalAddress())) {
            validateAddress(TrustCorporateDto.SERVICE_ADDRESS_FIELD,
                    trustCorporateDto.getServiceAddress(), errors, loggingContext);
        }

        validateIdentificationLegalForm(trustCorporateDto.getIdentificationLegalForm(), errors,
                loggingContext);
        validateIdentificationLegalAuthority(trustCorporateDto.getIdentificationLegalAuthority(),
                errors,
                loggingContext);

        boolean isOnRegisterInCountryFormedInIsValid =
                validateOnRegisteredInCountryFormedIn(trustCorporateDto.
                        getOnRegisterInCountryFormedIn(), errors, loggingContext);

        if (isOnRegisterInCountryFormedInIsValid && Boolean.FALSE
                .equals(trustCorporateDto.getOnRegisterInCountryFormedIn())) {
            validateOnRegisteredInCountryFormedInNotSupplied(trustCorporateDto, errors,
                    loggingContext);
        } else if (isOnRegisterInCountryFormedInIsValid && Boolean.TRUE
                .equals(trustCorporateDto.getOnRegisterInCountryFormedIn())) {
            validateOnRegisteredInCountryFormedInSupplied(trustCorporateDto, errors,
                    loggingContext);
        }
        validateCeasedDate(trustCorporateDto,errors, loggingContext);
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
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.IDENTIFICATION_LEGAL_AUTHORITY_FIELD);

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

    private Errors validateCeasedDate(TrustCorporateDto trustCorporateDto, Errors errors, String loggingContext) {

       Boolean isStillInvolved = trustCorporateDto.isCorporateBodyStillInvolvedInOverseasEntity();
       final String qualifiedFieldNameInvolved = getQualifiedFieldName(PARENT_FIELD,
               TrustCorporateDto.CORPORATE_BODY_STILL_INVOLVED_IN_OVERSEAS_ENTITY);

       if (UtilsValidators.isNotNull(isStillInvolved, qualifiedFieldNameInvolved, errors, loggingContext)) {

           final String qualifiedFieldNameCeased = getQualifiedFieldName(PARENT_FIELD,
                   TrustCorporateDto.CEASED_DATE_FIELD);
           LocalDate ceasedDate = trustCorporateDto.getCeasedDate();

           if (Boolean.TRUE.equals(isStillInvolved)) {
               if (Objects.nonNull(ceasedDate)) {
                   final String errorMessage = ValidationMessages.NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldNameCeased);
                   setErrorMsgToLocation(errors, qualifiedFieldNameCeased, errorMessage);
               }
           } else if (UtilsValidators.isNotNull(ceasedDate, qualifiedFieldNameCeased, errors, loggingContext)) {
               DateValidators.isDateInPast(ceasedDate,  qualifiedFieldNameCeased, errors, loggingContext);
               DateValidators.isCeasedDateOnOrAfterDateBecameInterestedPerson(ceasedDate, trustCorporateDto.getDateBecameInterestedPerson(), qualifiedFieldNameCeased, errors, loggingContext);
           }
       }
       return errors;
    }
}
