package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityNameMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.service.update.PopulateUpdateSubmission;
import uk.gov.companieshouse.overseasentitiesapi.validation.*;

@ExtendWith(MockitoExtension.class)
class PopulateUpdateSubmissionTest {

  private final EntityNameDto entityNameDto = EntityNameMock.getEntityNameDto();
  private final EntityDto entityDto = EntityMock.getEntityDto();
  private final PresenterDto presenterDto = PresenterMock.getPresenterDto();
  private final BeneficialOwnersStatementType beneficialOwnersStatement =
      BeneficialOwnersStatementType.NONE_IDENTIFIED;
  private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();
  private final UpdateDto updateDto = UpdateMock.getUpdateDto();
  private final List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList =
      new ArrayList<>();
  private final List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList =
      new ArrayList<>();
  private final List<BeneficialOwnerGovernmentOrPublicAuthorityDto>
      beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
  private final List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList =
      new ArrayList<>();
  private final List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList =
      new ArrayList<>();
  private final List<TrustDataDto> trustDataDtoList = new ArrayList<>();

  @InjectMocks private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;
  @Mock private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  {
    beneficialOwnerIndividualDtoList.add(
        BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
  }

  {
    beneficialOwnerCorporateDtoList.add(
        BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
  }

  {
    beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(
        BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
  }

  {
    managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
  }

  {
    managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
  }

  {
    trustDataDtoList.add(TrustMock.getTrustDataDto());
  }

  @Test
  void testPopulateUpdateSubmissionForDueDiligence() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals("John Smith", updateSubmission.getDueDiligence().getPartnerName());
    assertEquals("2022-01-01", updateSubmission.getDueDiligence().getDateChecked());
    assertEquals("user@domain.roe", updateSubmission.getDueDiligence().getEmail());
    assertEquals("c0de", updateSubmission.getDueDiligence().getAgentAssuranceCode());
    assertEquals("Super Supervisor", updateSubmission.getDueDiligence().getSupervisoryBody());
    assertEquals("agree", updateSubmission.getDueDiligence().getDiligence());
  }

  @Test
  void testPopulateUpdateSubmissionForBeneficialOwnerStatement() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals("none_identified", updateSubmission.getBeneficialOwnerStatement());
  }

  @Test
  void testPopulateUpdateSubmissionForAnyBOsOrMOsAddedOrCeased() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals(false, updateSubmission.getAnyBOsOrMOsAddedOrCeased());
  }

  @Test
  void testPopulateUpdateSubmissionForUserSubmission() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());
  }

  @Test
  void testPopulateUpdateSubmissionForPresenter() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();

    assertEquals("user@domain.roe", updateSubmission.getPresenter().getEmail());
    assertEquals("Joe Bloggs", updateSubmission.getPresenter().getName());
  }

  @Test
  void testPopulateUpdateSubmissionForOEDueDiligence() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals("John Smith", updateSubmission.getDueDiligence().getPartnerName());
    assertEquals("2022-01-01", updateSubmission.getDueDiligence().getDateChecked());
    assertEquals("user@domain.roe", updateSubmission.getDueDiligence().getEmail());
    assertEquals("c0de", updateSubmission.getDueDiligence().getAgentAssuranceCode());
    assertEquals("Super Supervisor", updateSubmission.getDueDiligence().getSupervisoryBody());
  }

  private void buildOverseasEntitySubmissionDto() {
    overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
    overseasEntitySubmissionDto.setEntityName(entityNameDto);
    overseasEntitySubmissionDto.setEntity(entityDto);
    overseasEntitySubmissionDto.setPresenter(presenterDto);
    overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
    overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
    overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
    overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(
        beneficialOwnerGovernmentOrPublicAuthorityDtoList);
    overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
    overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
    overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
    overseasEntitySubmissionDto.setTrusts(trustDataDtoList);
    overseasEntitySubmissionDto.setUpdate(updateDto);
  }

  private void setIsTrustWebEnabledFeatureFlag(boolean value) {
    ReflectionTestUtils.setField(overseasEntitySubmissionDtoValidator, "isTrustWebEnabled", value);
  }
  @Test
  void testBeneficialOwnersStatementTypeGetValue() throws Exception {
    var enumBeneficialOwnersStatementType =
        BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString("none_identified");
    assertEquals("none_identified", enumBeneficialOwnersStatementType.getValue());
  }
}
