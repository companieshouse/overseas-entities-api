package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class EntityDtoValidator {

    public Errors validate(EntityDto entityDto, Errors errs, String loggingContext) {
        UtilsValidators.validateNotNull(entityDto.getName(), getEntityFieldName(EntityDto.NAME_FIELD), errs, loggingContext);
        return errs;
    }

    private String getEntityFieldName(String fieldName) {
        return String.format("%s.%s", OverseasEntitySubmissionDto.ENTITY_FIELD, fieldName);
    }
}
