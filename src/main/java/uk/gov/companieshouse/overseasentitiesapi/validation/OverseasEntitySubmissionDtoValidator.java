package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

@Component
public class OverseasEntitySubmissionDtoValidator {

    private final EntityNameValidator entityNameValidator;
    private final EntityDtoValidator entityDtoValidator;
    private final PresenterDtoValidator presenterDtoValidator;
    private final OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator;
    private final DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
    private final String REGISTRATION = "REGISTRATION";
    private final String UPDATE = "UPDATE";
    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityNameValidator entityNameValidator,
                                                EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator,
                                                DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator) {
        this.entityNameValidator = entityNameValidator;
        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.dueDiligenceDataBlockValidator = dueDiligenceDataBlockValidator;
        this.ownersAndOfficersDataBlockValidator = ownersAndOfficersDataBlockValidator;
    }

    public Errors validateFull(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        String submissionType  = REGISTRATION;

        if(StringUtils.isNotBlank(overseasEntitySubmissionDto.getEntityNumber())) {
            submissionType = UPDATE;
        }

        if(submissionType.equalsIgnoreCase(REGISTRATION)){
            validateRegistrationDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }else {
            validateUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }
        return errors;
    }

    private void validateUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        // Method to be incrementally developed as remaining frontend data for update flows through
        validateCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);
    }

    private void validateRegistrationDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        validateCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, loggingContext);
    }

    private void validateCommonDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getEntityName(), OverseasEntitySubmissionDto.ENTITY_NAME_FIELD, errors, loggingContext)) {
            entityNameValidator.validate(overseasEntitySubmissionDto.getEntityName(), errors, loggingContext);
        }

        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getEntity(), OverseasEntitySubmissionDto.ENTITY_FIELD, errors, loggingContext)) {
            entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
        }

        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getPresenter(), OverseasEntitySubmissionDto.PRESENTER_FIELD, errors, loggingContext)) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        }

        dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                overseasEntitySubmissionDto.getDueDiligence(),
                overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                errors,
                loggingContext);
    }

    public Errors validatePartial(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        var entityNameDto = overseasEntitySubmissionDto.getEntityName();
        if (Objects.nonNull(entityNameDto)) {
            entityNameValidator.validate(entityNameDto, errors, loggingContext);
        }

        var presenterDto = overseasEntitySubmissionDto.getPresenter();
        if (Objects.nonNull(presenterDto)) {
            presenterDtoValidator.validate(presenterDto, errors, loggingContext);
        }

        var dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
        var overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        if (Objects.nonNull(dueDiligenceDto) || Objects.nonNull(overseasEntityDueDiligenceDto)) {
            dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                    dueDiligenceDto,
                    overseasEntityDueDiligenceDto,
                    errors,
                    loggingContext);
        }

        var entityDto = overseasEntitySubmissionDto.getEntity();
        if (Objects.nonNull(entityDto)) {
            entityDtoValidator.validate(entityDto, errors, loggingContext);
        }

        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);
        return errors;
    }

}
