package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE;

public class DueDiligenceOptionValidators {

     public static boolean onlyOneDueDiligencePresent(DueDiligenceDto dueDiligenceDto,
                                                      OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                      Errors errors,
                                                      String loggingContext) {

         return
         !(bothDueDiligencesPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext) ||
         bothDueDiligencesAbsent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext));
    }

    private static boolean bothDueDiligencesPresent(DueDiligenceDto dueDiligenceDto,
                                                    OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                    Errors errors,
                                                    String loggingContext) {
        if ((Objects.nonNull(dueDiligenceDto) && Objects.nonNull(overseasEntityDueDiligenceDto)) &&
                !(dueDiligenceDto.isEmpty() || overseasEntityDueDiligenceDto.isEmpty())) {

            logValidationErrorMessage(errors, loggingContext, SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private static boolean bothDueDiligencesAbsent(DueDiligenceDto dueDiligenceDto,
                                                   OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                   Errors errors,
                                                   String loggingContext) {
          if (Objects.isNull(dueDiligenceDto) && Objects.isNull(overseasEntityDueDiligenceDto) ||
                  dueDiligenceDto.isEmpty() && overseasEntityDueDiligenceDto.isEmpty()) {

              logValidationErrorMessage(errors, loggingContext, SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE);
              return true;
          }
          return false;
    }

    private static void logValidationErrorMessage(Errors errors, String loggingContext, String errorMessage) {
        String qualifiedFieldNames = OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD + " and " + OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
        setErrorMsgToLocation(errors, qualifiedFieldNames, String.format(errorMessage, qualifiedFieldNames));
        ApiLogger.infoContext(loggingContext, String.format(errorMessage, qualifiedFieldNames));
    }
}
