package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class BeneficialOwnersStatementValidator {

    public Errors validate(BeneficialOwnersStatementType beneficialOwnersStatement, Errors errors, String loggingContext) {
        validateBeneficialOwnersStatement(beneficialOwnersStatement, errors, loggingContext);
        return errors;
    }

    private boolean validateBeneficialOwnersStatement(BeneficialOwnersStatementType beneficialOwnersStatement, Errors errors, String loggingContext) {
        String qualifiedFieldName = OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
        return UtilsValidators.isNotNull(beneficialOwnersStatement, qualifiedFieldName, errors, loggingContext);
    }
}
