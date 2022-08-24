package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class EntityDtoValidator {

    public Errors validate(EntityDto entityDto, Errors errs, String loggingContext) {
        validateName(entityDto.getName(), errs, loggingContext);
        return errs;
    }

    private boolean validateName(String entityName, Errors errs, String loggingContext) {
        String location = getEntityFieldName(EntityDto.NAME_FIELD);
        return StringValidators.validateStringNotBlank(entityName, location, errs, loggingContext)
                && StringValidators.validateLength(entityName, 160, location, errs, loggingContext)
                && StringValidators.validateCharacters(entityName, location, errs, loggingContext);
    }

    private String getEntityFieldName(String fieldName) {
        return String.format("%s.%s", OverseasEntitySubmissionDto.ENTITY_FIELD, fieldName);
    }
}
