package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class DueDiligenceValidator {

    private final AddressDtoValidator addressDtoValidator;

    @Autowired
    public DueDiligenceValidator(AddressDtoValidator addressDtoValidator) {
        this.addressDtoValidator = addressDtoValidator;
    }

    public Errors validate(DueDiligenceDto dueDiligenceDto, Errors errors, String loggingContext) {
        validateIdentityDate(dueDiligenceDto.getIdentityDate(), errors, loggingContext);
        validateName(dueDiligenceDto.getName(), errors, loggingContext);
        validateAddress(dueDiligenceDto.getAddress(), errors, loggingContext);
        validateEmail(dueDiligenceDto.getEmail(), errors, loggingContext);
        validateSupervisoryName(dueDiligenceDto.getSupervisoryName(), errors, loggingContext);
        if(dueDiligenceDto.getAmlNumber() != null) {
            validateAmlNumber(dueDiligenceDto.getAmlNumber(), errors, loggingContext);
        }
        validateAgentCode(dueDiligenceDto.getAgentCode(), errors, loggingContext);
        validatePartnerName(dueDiligenceDto.getPartnerName(), errors, loggingContext);
        validateDiligence(dueDiligenceDto.getDiligence(), errors, loggingContext);
        return errors;
    }

    private boolean validateIdentityDate(LocalDate identityDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.IDENTITY_DATE_FIELD);
        return UtilsValidators.isNotNull(identityDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(identityDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateWithinLast3Months(identityDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.NAME_FIELD);
        return StringValidators.isNotBlank(name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateAddress(AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.IDENTITY_ADDRESS_FIELD);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getUkCountries(), errors, loggingContext);
        return errors;
    }

    private boolean validateEmail(String email, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.EMAIL_FIELD);
        return StringValidators.isNotBlank(email, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(email, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidEmailAddress(email, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateSupervisoryName(String supervisoryName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.SUPERVISORY_NAME_FIELD);
        return StringValidators.isNotBlank(supervisoryName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(supervisoryName, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(supervisoryName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateAmlNumber(String amlNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.AML_NUMBER_FIELD);
        return StringValidators.isLessThanOrEqualToMaxLength(amlNumber, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(amlNumber, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateAgentCode(String agentCode, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.AGENT_CODE_FIELD);
        return StringValidators.isNotBlank(agentCode, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(agentCode, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(agentCode, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePartnerName(String partnerName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.PARTNER_NAME_FIELD);
        return StringValidators.isNotBlank(partnerName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(partnerName, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(partnerName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateDiligence(String diligence, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD,
                DueDiligenceDto.DILIGENCE_FIELD);
        return StringValidators.isNotBlank(diligence, qualifiedFieldName, errors, loggingContext)
                && StringValidators.checkIsEqual(diligence, "agree", ValidationMessages.SHOULD_BE_AGREE_ERROR_MESSAGE, qualifiedFieldName, errors, loggingContext);
    }
}
