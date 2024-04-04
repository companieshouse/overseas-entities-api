package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.UPDATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class RemoveValidator {

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        UpdateDto updateDto = overseasEntitySubmissionDto.getUpdate();
        if (updateDto != null && updateDto.getFilingDate() != null) {
            String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
            String errorMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        }
        RemoveDto removeDto = overseasEntitySubmissionDto.getRemove();
        validateRemoveStatement(removeDto.getIsNotProprietorOfLand(), errors, loggingContext);

        return errors;
    }

    public Errors validateFull(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        var updateDto = overseasEntitySubmissionDto.getUpdate();

        if (updateDto == null) {
            String errorMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", OverseasEntitySubmissionDto.UPDATE_FIELD);
            setErrorMsgToLocation(errors, OverseasEntitySubmissionDto.UPDATE_FIELD, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        } else {
            validateFilingDate(updateDto.getFilingDate(), errors, loggingContext);
        }

        var removeDto = overseasEntitySubmissionDto.getRemove();

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

    private void validateFilingDate(LocalDate filingDate, Errors errors, String loggingContext) {
        // A filing date should NOT be present for a Remove submission
        if (filingDate != null) {
            String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.UPDATE_FIELD,
                    UpdateDto.FILING_DATE);
            String errorMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        }
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