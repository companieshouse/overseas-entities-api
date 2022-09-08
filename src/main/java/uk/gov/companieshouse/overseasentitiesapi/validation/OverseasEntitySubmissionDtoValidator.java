package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

@Component
public class OverseasEntitySubmissionDtoValidator {

    private final EntityDtoValidator entityDtoValidator;
    private final PresenterDtoValidator presenterDtoValidator;

    private final OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;

    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator) {
        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.overseasEntityDueDiligenceValidator = overseasEntityDueDiligenceValidator;
    }

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errs, String loggingContext) {
        entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errs, loggingContext);
        presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errs, loggingContext);
        if(Objects.nonNull(overseasEntitySubmissionDto.getOverseasEntityDueDiligence())) {
            overseasEntityDueDiligenceValidator.validate(overseasEntitySubmissionDto.getOverseasEntityDueDiligence(), errs, loggingContext);
        }
        return errs;
    }


}
