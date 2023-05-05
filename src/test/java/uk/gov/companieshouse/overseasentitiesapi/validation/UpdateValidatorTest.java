package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
public class UpdateValidatorTest {

    private static final String FILING_DATE_QUALIFIED_FIELD_NAME = OverseasEntitySubmissionDto.UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
    private static final String LOGGING_CONTEXT = "12345";

    private UpdateValidator updateValidator;
    private UpdateDto updateDto;

    @BeforeEach
    public void init() {
        updateValidator = new UpdateValidator();
        updateDto = UpdateMock.getUpdateDto();
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsNull() {
        updateDto.setFilingDate(null);
        Errors errors = updateValidator.validate(updateDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

}

