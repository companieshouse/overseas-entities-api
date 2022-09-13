package uk.gov.companieshouse.overseasentitiesapi.validation;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BeneficialOwnersStatementValidatorTest {

    private static final String CONTEXT = "12345";
    private BeneficialOwnersStatementValidator beneficialOwnersStatementValidator;


    @Test
    void testNoErrorReportedWhenBeneficialOwnersStatementIsPresent() {
        beneficialOwnersStatementValidator = new BeneficialOwnersStatementValidator();
        Errors errors = beneficialOwnersStatementValidator.validate(BeneficialOwnersStatementType.NONE_IDENTIFIED, new Errors(), CONTEXT);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT);
        Err err = Err.invalidBodyBuilderWithLocation(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT).withError(validationMessage).build();
        assertFalse(errors.containsError(err));
    }

    @Test
    void testErrorReportedWhenBeneficialOwnersStatementIsNull() {
        beneficialOwnersStatementValidator = new BeneficialOwnersStatementValidator();
        Errors errors = beneficialOwnersStatementValidator.validate(null, new Errors(), CONTEXT);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT);
        Err err = Err.invalidBodyBuilderWithLocation(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }
}
