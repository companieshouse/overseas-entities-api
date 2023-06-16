package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.DataSanitisation;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.CONCATENATED_STRING_FORMAT;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.CONCATENATED_STRING_FORMAT_NO_COMMA;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class EntityDtoValidator {

    private final AddressDtoValidator addressDtoValidator;

    private final DataSanitisation dataSanitisation;

    @Autowired
    public EntityDtoValidator(AddressDtoValidator addressDtoValidator, DataSanitisation dataSanitisation) {
        this.addressDtoValidator = addressDtoValidator;
        this.dataSanitisation = dataSanitisation;
    }

    public Errors validate(EntityDto entityDto, Errors errors, String loggingContext) {
        validateIncorporationCountry(entityDto.getIncorporationCountry(), errors, loggingContext);
        validateAddress(EntityDto.PRINCIPAL_ADDRESS_FIELD, entityDto.getPrincipalAddress(), errors, loggingContext);
        boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(entityDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
        if (sameAddressFlagValid && Boolean.FALSE.equals(entityDto.getServiceAddressSameAsPrincipalAddress())) {
            validateAddress(EntityDto.SERVICE_ADDRESS_FIELD, entityDto.getServiceAddress(), errors, loggingContext);
        } else {
            validateOtherAddressIsNotSupplied(EntityDto.SERVICE_ADDRESS_FIELD, entityDto.getServiceAddress(), errors, loggingContext);
        }
//        validateEmail(entityDto.getEmail(), errors, loggingContext);
        validateLegalForm(entityDto.getLegalForm(), errors, loggingContext);
        validateLawGoverned(entityDto.getLawGoverned(), errors, loggingContext);

        if (entityDto.isOnRegisterInCountryFormedIn()) {
            validatePublicRegisterName(entityDto.getPublicRegisterName(), entityDto.getPublicRegisterJurisdiction(), errors, loggingContext);
            validatePublicRegisterJurisdiction(entityDto.getPublicRegisterName(), entityDto.getPublicRegisterJurisdiction(), errors, loggingContext);
            validateRegistrationNumber(entityDto.getRegistrationNumber(), errors, loggingContext);
        } else {
            validatePublicRegisterNameIsNotSupplied(entityDto.getPublicRegisterName(), errors, loggingContext);
            validatePublicRegisterJurisdictionIsNotSupplied(entityDto.getPublicRegisterJurisdiction(), errors, loggingContext);
            validateRegistrationNumberIsNotSupplied(entityDto.getRegistrationNumber(), errors, loggingContext);
        }
        return errors;
    }

    private void validateIncorporationCountry(String country, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.INCORPORATION_COUNTRY_FIELD);

        boolean countryNotBlank = StringValidators.isNotBlank(country, qualifiedFieldName, errors, loggingContext);

        if (countryNotBlank) {
            boolean isOnList = CountryLists.getOverseasCountries().contains(country);
            if (!isOnList) {
                var validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, dataSanitisation.makeStringSafeForLogging(country));
                setErrorMsgToLocation(errors, qualifiedFieldName, validationMessage);
                ApiLogger.infoContext(loggingContext, validationMessage);
            }
        }
    }

    private void validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors, loggingContext);
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateOtherAddressIsNotSupplied(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, addressField);
        addressDtoValidator.validateOtherAddressIsNotSupplied(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateEmail(String email, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.EMAIL_PROPERTY_FIELD);
        return StringValidators.isNotBlank(email, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(email, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidEmailAddress(email, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.LEGAL_FORM_FIELD);
        return StringValidators.isNotBlank(legalForm, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(legalForm, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(legalForm, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.LAW_GOVERNED_FIELD);
        return StringValidators.isNotBlank(lawGoverned, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(lawGoverned, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lawGoverned, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePublicRegisterName(String publicRegisterName, String publicRegisterJurisdiction, Errors errors, String loggingContext) {
        String qualifiedFieldNamePublicRegisterName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        String qualifiedFieldNamePublicRegisterJurisdiction = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PUBLIC_REGISTER_JURISDICTION_FIELD);
        var compoundQualifiedFieldName = String.format("%s and %s", qualifiedFieldNamePublicRegisterName, qualifiedFieldNamePublicRegisterJurisdiction);

        return StringValidators.isNotBlank(publicRegisterName, qualifiedFieldNamePublicRegisterName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(String.format(CONCATENATED_STRING_FORMAT_NO_COMMA, publicRegisterName, publicRegisterJurisdiction), 159, compoundQualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(publicRegisterName, qualifiedFieldNamePublicRegisterName, errors, loggingContext);
    }

    private boolean validatePublicRegisterJurisdiction(String publicRegisterName, String publicRegisterJurisdiction, Errors errors, String loggingContext) {
        String qualifiedFieldNamePublicRegisterName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        String qualifiedFieldNamePublicRegisterJurisdiction = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PUBLIC_REGISTER_JURISDICTION_FIELD);
        var compoundQualifiedFieldName = String.format("%s and %s", qualifiedFieldNamePublicRegisterName, qualifiedFieldNamePublicRegisterJurisdiction);

        return StringValidators.isNotBlank(publicRegisterJurisdiction, qualifiedFieldNamePublicRegisterJurisdiction, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(String.format(CONCATENATED_STRING_FORMAT_NO_COMMA, publicRegisterName, publicRegisterJurisdiction), 159, compoundQualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(publicRegisterJurisdiction, qualifiedFieldNamePublicRegisterJurisdiction, errors, loggingContext);
    }

    private boolean validateRegistrationNumber(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.REGISTRATION_NUMBER_FIELD);
        return StringValidators.isNotBlank(registrationNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(registrationNumber, 32, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(registrationNumber, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validatePublicRegisterNameIsNotSupplied(String publicRegisterName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        StringValidators.checkIsEmpty(publicRegisterName, qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private Errors validatePublicRegisterJurisdictionIsNotSupplied(String publicRegisterJurisdiction, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PUBLIC_REGISTER_JURISDICTION_FIELD);
        StringValidators.checkIsEmpty(publicRegisterJurisdiction,  qualifiedFieldName, errors, loggingContext);
        return errors;
    }

    private Errors validateRegistrationNumberIsNotSupplied(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.REGISTRATION_NUMBER_FIELD);
        StringValidators.checkIsEmpty(registrationNumber, qualifiedFieldName, errors, loggingContext);
        return errors;
    }
}
