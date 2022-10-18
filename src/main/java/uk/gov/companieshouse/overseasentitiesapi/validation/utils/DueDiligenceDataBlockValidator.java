package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE;

@Component
public class DueDiligenceDataBlockValidator {

     public static final String QUALIFIED_FIELD_NAMES = OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD + " and " + OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;

     public boolean onlyOneCorrectBlockPresent(DueDiligenceDto dueDiligenceDto,
                                               OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                               Errors errors,
                                               String loggingContext) {


         return !(bothDueDiligencesPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext) ||
                 bothDueDiligencesAbsent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext));
    }

    private boolean bothDueDiligencesPresent(DueDiligenceDto dueDiligenceDto,
                                                    OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                    Errors errors,
                                                    String loggingContext) {
        if ((Objects.nonNull(dueDiligenceDto) && Objects.nonNull(overseasEntityDueDiligenceDto)) &&
                !(dueDiligenceDto.isEmpty() || overseasEntityDueDiligenceDto.isEmpty())) {

            logValidationErrorMessage(QUALIFIED_FIELD_NAMES, errors, loggingContext, SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean bothDueDiligencesAbsent(DueDiligenceDto dueDiligenceDto,
                                                   OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                   Errors errors,
                                                   String loggingContext) {
          if ((Objects.isNull(dueDiligenceDto) ||  dueDiligenceDto.isEmpty()) &&
                  (Objects.isNull(overseasEntityDueDiligenceDto) || overseasEntityDueDiligenceDto.isEmpty())) {

              logValidationErrorMessage(QUALIFIED_FIELD_NAMES, errors, loggingContext, SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE);
              return true;
          }
          return false;
    }

    private void logValidationErrorMessage(String fieldNameData, Errors errors, String loggingContext, String errorMessage) {
        setErrorMsgToLocation(errors, fieldNameData, String.format(errorMessage, fieldNameData));
        ApiLogger.infoContext(loggingContext, String.format(errorMessage, fieldNameData));
    }
}
