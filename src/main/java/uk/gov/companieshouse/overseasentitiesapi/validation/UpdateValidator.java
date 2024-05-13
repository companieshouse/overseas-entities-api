package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.service.PublicDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class UpdateValidator {

    @Autowired
    private PublicDataRetrievalService publicDataRetrievalService;

    public Errors validate(String entityNumber, UpdateDto updateDto, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {
        if (updateDto.getFilingDate() != null) {
            validateFilingDate(entityNumber, updateDto.getFilingDate(), errors, loggingContext, passThroughTokenHeader);
        }
        return errors;
    }

    public Errors validateFull(String entityNumber, UpdateDto updateDto, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {
        if (updateDto == null || updateDto.getFilingDate() == null) {
            String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.UPDATE_FIELD, UpdateDto.FILING_DATE);
            String errorMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        } else {
            validateFilingDate(entityNumber, updateDto.getFilingDate(), errors, loggingContext, passThroughTokenHeader);
        }
        return errors;
    }

    private void validateFilingDate(String entityNumber, LocalDate filingDate, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.UPDATE_FIELD, UpdateDto.FILING_DATE);
        DateValidators.isDateInPast(filingDate, qualifiedFieldName, errors, loggingContext);

        ApiLogger.debugContext(loggingContext, "Check filing date of " + filingDate + " for ROE company "
                + entityNumber + " against the next 'made up to' date");

        var companyProfileApi = publicDataRetrievalService.getCompanyProfile(entityNumber, passThroughTokenHeader);
        var confirmationStatementApi = companyProfileApi.getConfirmationStatement();

        if (confirmationStatementApi == null) {
            throw new ServiceException("Unable to validate the filing date - confirmation statement details are missing");
        }

        LocalDate nextMadeUpToDate = confirmationStatementApi.getNextMadeUpTo();

        ApiLogger.debugContext(loggingContext, "Next 'made up to' date is " + nextMadeUpToDate);

        if (filingDate.isAfter(nextMadeUpToDate)) {
            var errorMessage = String.format(ValidationMessages.DATE_NOT_ON_OR_BEFORE_MUD_ERROR_MESSAGE,
                    qualifiedFieldName, nextMadeUpToDate);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        }
    }
}
