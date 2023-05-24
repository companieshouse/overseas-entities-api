package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.mocks.*;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
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

  private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto =
      OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();

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

  private final LocalDate LOCAL_DATE_TODAY = LocalDate.now();

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
  void testPopulateUpdateSubmissionForPopulatePresenter() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    populateUpdateSubmission.populatePresenter(this.overseasEntitySubmissionDto, updateSubmission);

    assertNotNull(overseasEntitySubmissionDto.getPresenter());
    assertEquals("user@domain.roe", overseasEntitySubmissionDto.getPresenter().getEmail());
    assertEquals("Joe Bloggs", overseasEntitySubmissionDto.getPresenter().getFullName());
  }

  @Test
  void testPopulateUpdateSubmissionForPopulateFilingForDate() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    populateUpdateSubmission.populateFilingForDate(
        this.overseasEntitySubmissionDto, updateSubmission);

    assertEquals( String.valueOf(LOCAL_DATE_TODAY.getDayOfMonth()), updateSubmission.getFilingForDate().getDay());
    assertEquals(String.valueOf(LOCAL_DATE_TODAY.getMonth()), updateSubmission.getFilingForDate().getMonth());
    assertEquals(String.valueOf(LOCAL_DATE_TODAY.getYear()), updateSubmission.getFilingForDate().getYear());
  }

  @Test
  void testPopulateUpdateSubmissionForPopulateByOEDueDiligenceDto() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(true);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    var dueDiligence =
        populateUpdateSubmission.populateByOEDueDiligenceDto(
            new DueDiligence(), this.overseasEntitySubmissionDto.getOverseasEntityDueDiligence());
    assertEquals("John Smith", dueDiligence.getPartnerName());
    assertEquals("2022-01-01", dueDiligence.getDateChecked());
    assertEquals("user@domain.roe", dueDiligence.getEmail());
    assertEquals("ABC Checking Ltd", dueDiligence.getAgentName());
    assertEquals("Super Supervisor", dueDiligence.getSupervisoryBody());
    assertNull(dueDiligence.getDiligence());
  }

  @Test
  void testPopulateUpdateSubmissionForPopulateByDueDiligenceDto() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    var dueDiligence =
        populateUpdateSubmission.populateByDueDiligenceDto(
            new DueDiligence(), this.dueDiligenceDto);
    assertEquals("John Smith", dueDiligence.getPartnerName());
    assertEquals("2022-01-01", dueDiligence.getDateChecked());
    assertEquals("user@domain.roe", dueDiligence.getEmail());
    assertEquals("c0de", dueDiligence.getAgentAssuranceCode());
    assertEquals("Super Supervisor", dueDiligence.getSupervisoryBody());
    assertEquals("agree", dueDiligence.getDiligence());
  }

  @Test
  void testPopulateUpdateSubmissionForPopulate() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();

    assertNotNull(updateSubmission);
    assertNotNull(updateSubmission.getUserSubmission());
    assertNotNull(updateSubmission.getDueDiligence());
    assertNotNull(updateSubmission.getBeneficialOwnerStatement());
    assertNotNull(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
    assertNotNull(updateSubmission.getPresenter());
    assertNotNull(updateSubmission.getFilingForDate());
  }

  @Test
  void testPopulateUpdateSubmissionForBeneficialOwnerStatement() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals("none_identified", updateSubmission.getBeneficialOwnerStatement());
  }

  @Test
  void testPopulateUpdateSubmissionForAnyBOsOrMOsAddedOrCeased() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals(false, updateSubmission.getAnyBOsOrMOsAddedOrCeased());
  }

  @Test
  void testPopulateUpdateSubmissionForFilingForDate() throws Exception {

    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    FilingForDate filingForDate = updateSubmission.getFilingForDate();
    assertEquals( String.valueOf(LOCAL_DATE_TODAY.getDayOfMonth()), updateSubmission.getFilingForDate().getDay());
    assertEquals(String.valueOf(LOCAL_DATE_TODAY.getMonth()), updateSubmission.getFilingForDate().getMonth());
    assertEquals(String.valueOf(LOCAL_DATE_TODAY.getYear()), updateSubmission.getFilingForDate().getYear());

  }

  @Test
  void testPopulateUpdateSubmissionForUserSubmission() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());
    assertEquals(
        overseasEntitySubmissionDto.getPresenter(),
        updateSubmission.getUserSubmission().getPresenter());
    assertEquals(
        overseasEntitySubmissionDto.getUpdate(), updateSubmission.getUserSubmission().getUpdate());
    assertEquals(
        "user@domain.roe", updateSubmission.getUserSubmission().getDueDiligence().getEmail());
    assertEquals(
        "John Smith", updateSubmission.getUserSubmission().getDueDiligence().getPartnerName());
    assertEquals(
        "2022-01-01",
        updateSubmission.getUserSubmission().getDueDiligence().getIdentityDate().toString());
    assertEquals("c0de", updateSubmission.getUserSubmission().getDueDiligence().getAgentCode());
    assertEquals(
        "Super Supervisor",
        updateSubmission.getUserSubmission().getDueDiligence().getSupervisoryName());
  }

  @Test
  void testPopulateUpdateSubmissionForPresenter() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertNotNull(updateSubmission.getPresenter());
    assertEquals("user@domain.roe", updateSubmission.getPresenter().getEmail());
    assertEquals("Joe Bloggs", updateSubmission.getPresenter().getName());
  }

  @Test
  void testPopulateUpdateSubmissionForNoChangesInFilingPeriodStatement() throws Exception {

    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertNull(updateSubmission.getNoChangesInFilingPeriodStatement());
  }

  @Test
  void testPopulateUpdateSubmissionForDueDiligence() throws Exception {

    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);

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
  void testPopulateUpdateSubmissionForOEDueDiligence() throws Exception {
    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto(false);
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    assertEquals("John Smith", updateSubmission.getDueDiligence().getPartnerName());
    assertEquals("2022-01-01", updateSubmission.getDueDiligence().getDateChecked());
    assertEquals("user@domain.roe", updateSubmission.getDueDiligence().getEmail());
    assertEquals("c0de", updateSubmission.getDueDiligence().getAgentAssuranceCode());
    assertEquals("Super Supervisor", updateSubmission.getDueDiligence().getSupervisoryBody());
  }

  private void buildOverseasEntitySubmissionDto(boolean isDueDiligenceNull) {
    overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
    overseasEntitySubmissionDto.setEntityName(entityNameDto);
    overseasEntitySubmissionDto.setEntity(entityDto);
    overseasEntitySubmissionDto.setPresenter(presenterDto);
    overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
    overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
    overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
    overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(
        beneficialOwnerGovernmentOrPublicAuthorityDtoList);
    if ((isDueDiligenceNull)) {
      overseasEntitySubmissionDto.setDueDiligence(null);
    } else {
      overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
    }
    overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
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
