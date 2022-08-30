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

    public Errors validate(EntityDto entityDto, Errors errs, String loggingContext) {
        validateName(entityDto.getName(), errs, loggingContext);
        validateIncorporationCountry(entityDto.getIncorporationCountry(), errs, loggingContext);
        validatePrincipalAddress(entityDto.getPrincipalAddress(), errs, loggingContext);
        validateServiceAddressSameAsPrincipalAddress(entityDto.getServiceAddressSameAsPrincipalAddress(), errs, loggingContext);
        if (!entityDto.getServiceAddressSameAsPrincipalAddress()) {
            validateServiceAddress(entityDto.getServiceAddress(), errs, loggingContext);
        }
        validateEmail(entityDto.getEmail(), errs, loggingContext);
        validateLegalForm(entityDto.getLegalForm(), errs, loggingContext);
        validateLawGoverned(entityDto.getLawGoverned(), errs, loggingContext);

        if (entityDto.isOnRegisterInCountryFormedIn()) {
            validatePublicRegisterName(entityDto.getPublicRegisterName(), errs, loggingContext);
            validateRegistrationNumber(entityDto.getRegistrationNumber(), errs, loggingContext);
        }
        return errs;
    }

    private boolean validateName(String entityName, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.NAME_FIELD);
        return StringValidators.validateStringNotBlank(entityName, location, errs, loggingContext)
                && StringValidators.validateLength(entityName, 160, location, errs, loggingContext)
                && StringValidators.validateCharacters(entityName, location, errs, loggingContext);
    }

    private boolean validateIncorporationCountry(String country, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.INCORPORATION_COUNTRY_FIELD);
        return StringValidators.validateStringNotBlank(country, location, errs, loggingContext);
    }

    private Errors validatePrincipalAddress(AddressDto addressDto, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.PRINCIPAL_ADDRESS_FIELD);
        addressDtoValidator.validate(location, addressDto, errs, loggingContext);
        return errs;
    }

    private boolean validateServiceAddressSameAsPrincipalAddress(Boolean same, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        return UtilsValidators.validateNotNull(same, location, errs, loggingContext);
    }

    private Errors validateServiceAddress(AddressDto addressDto, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.SERVICE_ADDRESS_FIELD);
        addressDtoValidator.validate(location, addressDto, errs, loggingContext);
        return errs;
    }

    private boolean validateEmail(String email, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.EMAIL_PROPERTY_FIELD);
        return StringValidators.validateStringNotBlank(email, location, errs, loggingContext)
                && StringValidators.validateLength(email, 250, location, errs, loggingContext)
                && StringValidators.validateEmail(email, location, errs, loggingContext);
    }

    private boolean validateLegalForm(String legalForm, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.LEGAL_FORM_FIELD);
        return StringValidators.validateStringNotBlank(legalForm, location, errs, loggingContext)
                && StringValidators.validateLength(legalForm, 4000, location, errs, loggingContext)
                && StringValidators.validateCharacters(legalForm, location, errs, loggingContext);
    }

    private boolean validateLawGoverned(String lawGoverned, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.LAW_GOVERNED_FIELD);
        return StringValidators.validateStringNotBlank(lawGoverned, location, errs, loggingContext)
                && StringValidators.validateLength(lawGoverned, 4000, location, errs, loggingContext)
                && StringValidators.validateCharacters(lawGoverned, location, errs, loggingContext);
    }

    private boolean validatePublicRegisterName(String publicRegisterName, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.PUBLIC_REGISTER_NAME_FIELD);
        return StringValidators.validateStringNotBlank(publicRegisterName, location, errs, loggingContext)
                && StringValidators.validateLength(publicRegisterName, 4000, location, errs, loggingContext)
                && StringValidators.validateCharacters(publicRegisterName, location, errs, loggingContext);
    }

    private boolean validateRegistrationNumber(String registrationNumber, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.REGISTRATION_NUMBER_FIELD);
        return StringValidators.validateStringNotBlank(registrationNumber, location, errs, loggingContext)
                && StringValidators.validateLength(registrationNumber, 32, location, errs, loggingContext)
                && StringValidators.validateCharacters(registrationNumber, location, errs, loggingContext);
    }


    private String getEntityFieldName(String fieldName) {
        return String.format("%s.%s", OverseasEntitySubmissionDto.ENTITY_FIELD, fieldName);
    }
}
