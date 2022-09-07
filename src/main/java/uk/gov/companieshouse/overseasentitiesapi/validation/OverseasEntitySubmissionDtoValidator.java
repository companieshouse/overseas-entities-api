package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class OverseasEntitySubmissionDtoValidator {

    private final EntityDtoValidator entityDtoValidator;
    private final PresenterDtoValidator presenterDtoValidator;

    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator) {
        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
    }

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errs, String loggingContext) {
        entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errs, loggingContext);
        presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errs, loggingContext);
        return errs;
    }


}
