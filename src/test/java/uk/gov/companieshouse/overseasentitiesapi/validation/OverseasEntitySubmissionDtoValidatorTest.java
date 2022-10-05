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
        verify(managingOfficerCorporateValidator, times(1)).validate(eq(managingOfficerCorporateDtoList),any(),any());
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
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
    }
}
