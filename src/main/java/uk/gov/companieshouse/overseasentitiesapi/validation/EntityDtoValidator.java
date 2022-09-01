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
        if (sameAddressFlagValid && !entityDto.getServiceAddressSameAsPrincipalAddress()) {
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
        String location = getEntityFieldName(EntityDto.NAME_FIELD);
        return StringValidators.validateStringNotBlank(entityName, location, errors, loggingContext)
                && StringValidators.validateMaxLength(entityName, 160, location, errors, loggingContext)
                && StringValidators.validateCharacters(entityName, location, errors, loggingContext);
    }

    private boolean validateIncorporationCountry(String country, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.INCORPORATION_COUNTRY_FIELD);
        return StringValidators.validateStringNotBlank(country, location, errors, loggingContext);
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String location = getEntityFieldName(addressField);
        addressDtoValidator.validate(location, addressDto, errors, loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.validateNotNull(same, location, errors, loggingContext);
    }

    private boolean validateEmail(String email, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.EMAIL_PROPERTY_FIELD);
        return StringValidators.validateStringNotBlank(email, location, errors, loggingContext)
                && StringValidators.validateMaxLength(email, 250, location, errors, loggingContext)
                && StringValidators.validateEmail(email, location, errors, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.LEGAL_FORM_FIELD);
        return StringValidators.validateStringNotBlank(legalForm, location, errors, loggingContext)
                && StringValidators.validateMaxLength(legalForm, 4000, location, errors, loggingContext)
                && StringValidators.validateCharacters(legalForm, location, errors, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.LAW_GOVERNED_FIELD);
        return StringValidators.validateStringNotBlank(lawGoverned, location, errors, loggingContext)
                && StringValidators.validateMaxLength(lawGoverned, 4000, location, errors, loggingContext)
                && StringValidators.validateCharacters(lawGoverned, location, errors, loggingContext);
    }

    private boolean validatePublicRegisterName(String publicRegisterName, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        return StringValidators.validateStringNotBlank(publicRegisterName, location, errors, loggingContext)
                && StringValidators.validateMaxLength(publicRegisterName, 4000, location, errors, loggingContext)
                && StringValidators.validateCharacters(publicRegisterName, location, errors, loggingContext);
    }

    private boolean validateRegistrationNumber(String registrationNumber, Errors errors, String loggingContext) {
        String location = getEntityFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        return StringValidators.validateStringNotBlank(registrationNumber, location, errors, loggingContext)
                && StringValidators.validateMaxLength(registrationNumber, 32, location, errors, loggingContext)
                && StringValidators.validateCharacters(registrationNumber, location, errors, loggingContext);
    }


    private String getEntityFieldName(String fieldName) {
        return String.format("%s.%s", OverseasEntitySubmissionDto.ENTITY_FIELD, fieldName);
    }
}
