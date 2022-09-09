package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDtoValidatorTest {

    private static final String CONTEXT = "12345";
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

    @Test
    void testOverseasEntityDueDiligenceValidator() {
        overseasEntitySubmissionDtoValidator = new OverseasEntitySubmissionDtoValidator(
                entityDtoValidator, presenterDtoValidator, overseasEntityDueDiligenceValidator);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);
        verify(entityDtoValidator, times(1)).validate(any(),any(),any());
        verify(presenterDtoValidator, times(1)).validate(any(),any(),any());
        verify(overseasEntityDueDiligenceValidator, times(1)).validate(any(),any(),any());
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(EntityMock.getEntityDto());
        overseasEntitySubmissionDto.setPresenter(PresenterMock.getPresenterDto());
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto());
    }
}
