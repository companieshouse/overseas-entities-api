package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DueDiligenceDataBlockValidator;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
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
    @Mock
    private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
    @Mock
    private ManagingOfficerIndividualValidator managingOfficerIndividualValidator;
    @Mock
    private ManagingOfficerCorporateValidator managingOfficerCorporateValidator;

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

    private List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
    {
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
    };
    private List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
    {
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
    };

    @Test
    void testOverseasEntitySubmissionValidatorWithDueDiligence() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(overseasEntityDueDiligenceValidator, times(0)).validate(eq(overseasEntityDueDiligenceDto),any(),any());
        verify(beneficialOwnersStatementValidator, times(1)).validate(eq(beneficialOwnersStatement),any(),any());
        verify(beneficialOwnerIndividualValidator, times(1)).validate(eq(beneficialOwnerIndividualDtoList), any(),any());
        verify(beneficialOwnerCorporateValidator, times(1)).validate(eq(beneficialOwnerCorporateDtoList),any(),any());
        verify(dueDiligenceValidator, times(1)).validate(eq(dueDiligenceDto),any(),any());
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(eq(beneficialOwnerGovernmentOrPublicAuthorityDtoList),any(),any());
        verify(managingOfficerIndividualValidator, times(1)).validate(eq(managingOfficerIndividualDtoList),any(),any());
        verify(managingOfficerCorporateValidator, times(1)).validate(eq(managingOfficerCorporateDtoList),any(),any());
    }

    @Test
    void testOverseasEntitySubmissionValidatorWithOverseasEntityDueDiligence() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(overseasEntityDueDiligenceValidator, times(1)).validate(eq(overseasEntityDueDiligenceDto),any(),any());
        verify(beneficialOwnersStatementValidator, times(1)).validate(eq(beneficialOwnersStatement),any(),any());
        verify(beneficialOwnerIndividualValidator, times(1)).validate(eq(beneficialOwnerIndividualDtoList), any(),any());
        verify(beneficialOwnerCorporateValidator, times(1)).validate(eq(beneficialOwnerCorporateDtoList),any(),any());
        verify(dueDiligenceValidator, times(0)).validate(eq(dueDiligenceDto),any(),any());
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(eq(beneficialOwnerGovernmentOrPublicAuthorityDtoList),any(),any());
        verify(managingOfficerIndividualValidator, times(1)).validate(eq(managingOfficerIndividualDtoList),any(),any());
        verify(managingOfficerCorporateValidator, times(1)).validate(eq(managingOfficerCorporateDtoList),any(),any());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersIndividualAreNull() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersIndividualAreEmpty() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersCorporateAreNull() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersCorporateAreEmpty() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersGovernmentOrPublicAuthorityAreNull() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersGovernmentOrPublicAuthorityAreEmpty() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersIndividualAreNull() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersIndividualAreEmpty() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersCorporateAreNull() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersCorporateAreEmpty() {
        when(dueDiligenceDataBlockValidator.onlyOneBlockPresent(any(), any(), any(), any())).thenReturn(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
    }
}
