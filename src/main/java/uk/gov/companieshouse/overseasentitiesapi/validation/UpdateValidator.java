package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

public class UpdateValidator {

    public Errors validate(UpdateDto updateDto, Errors errors, String loggingContext) {
        if (updateDto.getFilingDate() != null) {
            validateFilingDate(updateDto.getFilingDate(), errors, loggingContext);
        }
        return errors;
    }

    public Errors validateFull(UpdateDto updateDto, Errors errors, String loggingContext) {
        if (updateDto.getFilingDate() == null) {
            String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.UPDATE_FIELD, UpdateDto.FILING_DATE);
            String errorMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        } else {
            validateFilingDate(updateDto.getFilingDate(), errors, loggingContext);
        }
        return errors;
    }
    private boolean validateFilingDate(LocalDate filingDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.UPDATE_FIELD, UpdateDto.FILING_DATE);
        return DateValidators.isDateInPast(filingDate, qualifiedFieldName, errors, loggingContext);
    }
}
