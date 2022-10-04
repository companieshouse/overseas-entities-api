package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.List;

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
    private EntityDtoValidator entityDtoValidator;
    @Mock
    private PresenterDtoValidator presenterDtoValidator;
    @Mock
    private OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
    @Mock
    private BeneficialOwnersStatementValidator beneficialOwnersStatementValidator;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    @Mock
    private BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator;
    @Mock
    private BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator;
    @Mock
    private DueDiligenceValidator dueDiligenceValidator;
    @Mock
    private BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;

    private EntityDto entityDto = EntityMock.getEntityDto();
    private PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
    private BeneficialOwnersStatementType beneficialOwnersStatement = BeneficialOwnersStatementType.NONE_IDENTIFIED;
    private List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
    private DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();
    {
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
    };
    private List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
    {
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
    };
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
    {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
    };

    @Test
    void testOverseasEntitySubmissionValidatorWithDueDiligenceOnly() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(new OverseasEntityDueDiligenceDto());
        overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(overseasEntityDueDiligenceValidator, times(0)).validate(eq(overseasEntityDueDiligenceDto),any(),any());
        verify(beneficialOwnersStatementValidator, times(1)).validate(eq(beneficialOwnersStatement),any(),any());
        verify(beneficialOwnerIndividualValidator, times(1)).validate(eq(beneficialOwnerIndividualDtoList), any(),any());
        verify(beneficialOwnerCorporateValidator, times(1)).validate(eq(beneficialOwnerCorporateDtoList),any(),any());
        verify(dueDiligenceValidator, times(1)).validate(eq(dueDiligenceDto),any(),any());
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(eq(beneficialOwnerGovernmentOrPublicAuthorityDtoList),any(),any());
    }

    @Test
    void testOverseasEntitySubmissionValidatorWithOverseasEntitiyDueDiligenceOnly() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(new DueDiligenceDto());
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(overseasEntityDueDiligenceValidator, times(1)).validate(eq(overseasEntityDueDiligenceDto),any(),any());
        verify(beneficialOwnersStatementValidator, times(1)).validate(eq(beneficialOwnersStatement),any(),any());
        verify(beneficialOwnerIndividualValidator, times(1)).validate(eq(beneficialOwnerIndividualDtoList), any(),any());
        verify(beneficialOwnerCorporateValidator, times(1)).validate(eq(beneficialOwnerCorporateDtoList),any(),any());
        verify(dueDiligenceValidator, times(0)).validate(eq(dueDiligenceDto),any(),any());
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(eq(beneficialOwnerGovernmentOrPublicAuthorityDtoList),any(),any());
    }

    @Test
    void testOverseasEntitySubmissionValidatorFailsWhenBothOverseasEntitiesArePresent() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);

        String qualifiedFieldName = OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD + " and " + OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
        String message = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, qualifiedFieldName);
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testOverseasEntitySubmissionValidatorFailsWhenBothOverseasEntitiesAreAbsent() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(new DueDiligenceDto());
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(new OverseasEntityDueDiligenceDto());
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), CONTEXT);

        String qualifiedFieldName = OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD + " and " + OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
        String message = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, qualifiedFieldName);
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
    }
}
