package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDtoValidatorTest {

    private static final String CONTEXT = "12345";
    @InjectMocks
    private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;
    @Mock
    private AddressDtoValidator addressDtoValidator;
    @Mock
    private EntityDtoValidator entityDtoValidator;
    @Mock
    private PresenterDtoValidator presenterDtoValidator;
    @Mock
    private OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private EntityDto entityDto = EntityMock.getEntityDto();
    private PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();

    @Test
    void testOverseasEntityDueDiligenceValidator() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(overseasEntityDueDiligenceValidator, times(1)).validate(eq(overseasEntityDueDiligenceDto),any(),any());
    }

    @Test
    void testErrorReportedWhenBeneficialOwnersStatementIsNull() {
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT);
        Err err = Err.invalidBodyBuilderWithLocation(OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
    }
}
