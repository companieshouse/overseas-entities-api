package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class RemoveValidator {

    public Errors validate(RemoveDto removeDto, Errors errors, String loggingContext) {

        validateRemoveStatement(removeDto.getIsNotProprietorOfLand(), errors, loggingContext);

        return errors;
    }

    public Errors validateFull(RemoveDto removeDto, Errors errors, String loggingContext) {

        if (removeDto == null || removeDto.getIsNotProprietorOfLand() == null) {
            String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.REMOVE_FIELD,
                    RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
            String errorMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        } else {
            validateRemoveStatement(removeDto.getIsNotProprietorOfLand(), errors, loggingContext);
        }
        return errors;
    }

    private void validateRemoveStatement(Boolean isNotProprietorOfLand, Errors errors, String loggingContext) {
        if (Boolean.FALSE.equals(isNotProprietorOfLand)) {
            String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.REMOVE_FIELD,
                    RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD);
            String errorMessage = ValidationMessages.NOT_VALID_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        }
    }
}