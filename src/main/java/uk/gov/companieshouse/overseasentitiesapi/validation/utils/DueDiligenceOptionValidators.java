package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages.SHOULD_NOT_BOTH_BE_POPULATED_ERROR_MESSAGE;

public class DueDiligenceOptionValidators {

     public static boolean bothDueDiligenceOptionsNotSupplied(DueDiligenceDto dueDiligenceDto,
                                                              OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                              Errors errs,
                                                              String loggingContext) {

         if ((Objects.nonNull(dueDiligenceDto) && Objects.nonNull(overseasEntityDueDiligenceDto)) &&
                 !(dueDiligenceDto.isEmpty() || overseasEntityDueDiligenceDto.isEmpty())) {
             String qualifiedFieldNames = OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD + " and " + OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
             setErrorMsgToLocation(errs, qualifiedFieldNames, String.format(SHOULD_NOT_BOTH_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldNames));
             ApiLogger.infoContext(loggingContext, "Both the " + qualifiedFieldNames + " Fields should not be populated");
             return false;
         }
         return true;
    }
}
