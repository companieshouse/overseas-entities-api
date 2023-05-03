package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
    private final DueDiligenceValidator dueDiligenceValidator;

    @Autowired
    public DueDiligenceDataBlockValidator(
            OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator,
            DueDiligenceValidator dueDiligenceValidator) {

        this.overseasEntityDueDiligenceValidator = overseasEntityDueDiligenceValidator;
        this.dueDiligenceValidator = dueDiligenceValidator;

    }

    public boolean validateFullDueDiligenceFields(DueDiligenceDto dueDiligenceDto,
                                              OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                              Errors errors,
                                              String loggingContext) {

        if (onlyOneBlockPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext)) {
            if (Objects.nonNull(overseasEntityDueDiligenceDto) && !overseasEntityDueDiligenceDto.isEmpty()) {
                overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, errors, loggingContext);
            } else {
                dueDiligenceValidator.validate(dueDiligenceDto, errors, loggingContext);
            }
        }
        return false;
    }

    public boolean validatePartialDueDiligenceFields(DueDiligenceDto dueDiligenceDto,
                                              OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                              Errors errors,
                                              String loggingContext) {

        if (onlyOneBlockPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext)) {
            if (Objects.nonNull(overseasEntityDueDiligenceDto) && !overseasEntityDueDiligenceDto.isEmpty()) {
                overseasEntityDueDiligenceValidator.validateWithoutIdentityDate(overseasEntityDueDiligenceDto, errors, loggingContext);
            } else {
                dueDiligenceValidator.validateWithoutIdentityDate(dueDiligenceDto, errors, loggingContext);
            }
        }
        return false;
    }

     private boolean onlyOneBlockPresent(DueDiligenceDto dueDiligenceDto,
                                        OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                        Errors errors,
                                        String loggingContext) {

         return
         !(bothDueDiligencesPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext) ||
         bothDueDiligencesAbsent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext));
    }

    private boolean bothDueDiligencesPresent(DueDiligenceDto dueDiligenceDto,
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

    private boolean bothDueDiligencesAbsent(DueDiligenceDto dueDiligenceDto,
                                                   OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
                                                   Errors errors,
                                                   String loggingContext) {
          if ((Objects.isNull(dueDiligenceDto) ||  dueDiligenceDto.isEmpty()) &&
                  (Objects.isNull(overseasEntityDueDiligenceDto) || overseasEntityDueDiligenceDto.isEmpty())) {

              logValidationErrorMessage(errors, loggingContext, SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE);
              return true;
          }
          return false;
    }

    private void logValidationErrorMessage(Errors errors, String loggingContext, String errorMessage) {
        setErrorMsgToLocation(errors, QUALIFIED_FIELD_NAMES, String.format(errorMessage, QUALIFIED_FIELD_NAMES));
        ApiLogger.infoContext(loggingContext, String.format(errorMessage, QUALIFIED_FIELD_NAMES));
    }
}
