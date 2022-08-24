package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class EntityDtoValidator {

    public Errors validate(EntityDto entityDto, Errors errs) {

       // entityDto.getName()
        return errs;
    }
}
