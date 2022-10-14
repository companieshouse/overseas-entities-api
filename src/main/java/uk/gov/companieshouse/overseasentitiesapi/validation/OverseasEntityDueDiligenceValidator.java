package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class OverseasEntityDueDiligenceValidator {
    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public OverseasEntityDueDiligenceValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }

    public Errors validate(OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto, Errors errors, String loggingContext) {
        validateIdentityDate(overseasEntityDueDiligenceDto.getIdentityDate(), errors, loggingContext);
        validateName(overseasEntityDueDiligenceDto.getName(), errors, loggingContext);
        validateAddress(overseasEntityDueDiligenceDto.getAddress(), errors, loggingContext);
        validateEmail(overseasEntityDueDiligenceDto.getEmail(), errors, loggingContext);
        if (Objects.nonNull(overseasEntityDueDiligenceDto.getSupervisoryName())) {
            validateSupervisoryName(overseasEntityDueDiligenceDto.getSupervisoryName(), errors, loggingContext);
        }
        if (Objects.nonNull(overseasEntityDueDiligenceDto.getAmlNumber())) {
            validateAmlNumber(overseasEntityDueDiligenceDto.getAmlNumber(), errors, loggingContext);
        }
        if (Objects.nonNull(overseasEntityDueDiligenceDto.getPartnerName())) {
            validatePartnerName(overseasEntityDueDiligenceDto.getPartnerName(), errors, loggingContext);
        }
        return errors;
    }

    private boolean validateIdentityDate(LocalDate identityDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.IDENTITY_DATE_FIELD);

        if (Objects.nonNull(identityDate)) {
            return DateValidators.isDateInPast(identityDate, qualifiedFieldName, errors, loggingContext)
                    && DateValidators.isDateWithinLast3Months(identityDate, qualifiedFieldName, errors, loggingContext);
        }

        return true;
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.NAME_FIELD);
        return StringValidators.isNotBlank(name, qualifiedFieldName, errors, loggingContext) &&
               StringValidators.isLessThanOrEqualToMaxLength(name, 256, qualifiedFieldName, errors, loggingContext) &&
               StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.IDENTITY_ADDRESS_FIELD);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getUkCountries(), errors, loggingContext);
        return errors;
    }

    private boolean validateEmail(String email, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.EMAIL_FIELD);
        return  StringValidators.isNotBlank(email, qualifiedFieldName, errors, loggingContext) &&
                StringValidators.isLessThanOrEqualToMaxLength(email, 256, qualifiedFieldName, errors, loggingContext) &&
                StringValidators.isValidEmailAddress(email, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateSupervisoryName(String supervisoryName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.SUPERVISORY_NAME_FIELD);
        return StringValidators.isLessThanOrEqualToMaxLength(supervisoryName, 256, qualifiedFieldName, errors, loggingContext) &&
               StringValidators.isValidCharacters(supervisoryName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateAmlNumber(String amlNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.AML_NUMBER_FIELD);
        return StringValidators.isLessThanOrEqualToMaxLength(amlNumber, 256, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePartnerName(String partnerName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE,
                OverseasEntityDueDiligenceDto.PARTNER_NAME_FIELD);
        return StringValidators.isLessThanOrEqualToMaxLength(partnerName, 256, qualifiedFieldName, errors, loggingContext) &&
                StringValidators.isValidCharacters(partnerName, qualifiedFieldName, errors, loggingContext);
    }
}
