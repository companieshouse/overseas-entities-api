package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class PresenterDtoValidator {

    public Errors validate(PresenterDto presenterDto, Errors errors, String loggingContext) {
        validateFullName(presenterDto.getFullName(), errors, loggingContext);
        validateEmail(presenterDto.getEmail(), errors, loggingContext);
        return errors;
    }

    private boolean validateFullName(String presenterFullName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.PRESENTER_FIELD, PresenterDto.FULL_NAME_FIELD);
        return StringValidators.isNotBlank(presenterFullName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(presenterFullName, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(presenterFullName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateEmail(String email, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.PRESENTER_FIELD, PresenterDto.EMAIL_PROPERTY_FIELD);
        return StringValidators.isNotBlank(email, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(email, 256, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidEmailAddress(email, qualifiedFieldName, errors, loggingContext);
    }

}
