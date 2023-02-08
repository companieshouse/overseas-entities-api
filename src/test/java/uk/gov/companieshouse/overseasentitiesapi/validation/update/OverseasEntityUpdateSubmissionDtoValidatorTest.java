package uk.gov.companieshouse.overseasentitiesapi.validation.update;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.*;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
import uk.gov.companieshouse.overseasentitiesapi.validation.*;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.*;

@ExtendWith(MockitoExtension.class)
public class OverseasEntityUpdateSubmissionDtoValidatorTest {
    private static final String LOGGING_CONTEXT = "123456";

    private static final String LONG_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABB";

    @InjectMocks
    private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;
    @Mock
    private EntityNameValidator entityNameValidator;
    @Mock
    private EntityDtoValidator entityDtoValidator;
    @Mock
    private PresenterDtoValidator presenterDtoValidator;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    @Mock
    private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;

    private final EntityNameDto entityNameDto = EntityNameMock.getEntityNameDto();
    private final EntityDto entityDto = EntityMock.getEntityDto();
    private final PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
    private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

    @Test
    void testOverseasEntityUpdateSubmissionValidatorWithoutOwners() {
        buildOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForMissingEntityFieldAndOtherBlocksWithValidationErrors() {
        buildOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingUpdateEntityNameField() {
        buildOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingUpdatePresenterField() {
        buildOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", LONG_NAME, "Дракон"} )
    void testFullValidationErrorReportedWhenEntityNameFieldIsNull(String name) {
        buildOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.getEntityName().setName(name);
        overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityNameValidator, times(1)).validate(any(), any(), any());
    }

    private void buildOverseasEntityUpdateSubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        overseasEntitySubmissionDto.setEntityName(entityNameDto);
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}