package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class EntityDtoValidator {

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public EntityDtoValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }

    public Errors validate(EntityDto entityDto, Errors errors, String loggingContext) {
        validateName(entityDto.getName(), errors, loggingContext);
        validateIncorporationCountry(entityDto.getIncorporationCountry(), errors, loggingContext);
        validateAddress(EntityDto.PRINCIPAL_ADDRESS_FIELD, entityDto.getPrincipalAddress(), errors, loggingContext);
        boolean sameAddressFlagValid = validateServiceAddressSameAsPrincipalAddress(entityDto.getServiceAddressSameAsPrincipalAddress(), errors, loggingContext);
        if (sameAddressFlagValid && Boolean.FALSE.equals(entityDto.getServiceAddressSameAsPrincipalAddress())) {
            validateAddress(EntityDto.SERVICE_ADDRESS_FIELD, entityDto.getServiceAddress(), errors, loggingContext);
        }
        validateEmail(entityDto.getEmail(), errors, loggingContext);
        validateLegalForm(entityDto.getLegalForm(), errors, loggingContext);
        validateLawGoverned(entityDto.getLawGoverned(), errors, loggingContext);

        if (entityDto.isOnRegisterInCountryFormedIn()) {
            validatePublicRegisterName(entityDto.getPublicRegisterName(), errors, loggingContext);
            validateRegistrationNumber(entityDto.getRegistrationNumber(), errors, loggingContext);
        }
        return errors;
    }

    private boolean validateName(String entityName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.NAME_FIELD);
        return StringValidators.isValidNotBlank(entityName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(entityName, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(entityName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateIncorporationCountry(String country, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.INCORPORATION_COUNTRY_FIELD);
        return StringValidators.isValidNotBlank(country, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.isValidNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateEmail(String email, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.EMAIL_PROPERTY_FIELD);
        return StringValidators.isValidNotBlank(email, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(email, 250, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidEmailAddress(email, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.LEGAL_FORM_FIELD);
        return StringValidators.isValidNotBlank(legalForm, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(legalForm, 4000, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(legalForm, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.LAW_GOVERNED_FIELD);
        return StringValidators.isValidNotBlank(lawGoverned, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(lawGoverned, 4000, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(lawGoverned, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePublicRegisterName(String publicRegisterName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        return StringValidators.isValidNotBlank(publicRegisterName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(publicRegisterName, 4000, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(publicRegisterName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateRegistrationNumber(String registrationNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedEntityFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        return StringValidators.isValidNotBlank(registrationNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(registrationNumber, 32, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(registrationNumber, qualifiedFieldName, errors, loggingContext);
    }

    private String getQualifiedEntityFieldName(String fieldName) {
        return String.format("%s.%s", OverseasEntitySubmissionDto.ENTITY_FIELD, fieldName);
    }
}
