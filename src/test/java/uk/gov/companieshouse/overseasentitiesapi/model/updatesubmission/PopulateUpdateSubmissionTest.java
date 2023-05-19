package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class PopulateUpdateSubmissionTest {

  private static final String LOGGING_CONTEXT = "12345";
  final String DUE_DILIGENCE_JSON = "\"dueDiligence\":{\"dateChecked\":\"2022-01-01\"";

  final String USER_SUBMISSION_JSON =
      "\"userSubmission\":{\"entity_name\":{\"name\":\"Joe Bloogs Ltd\"}";

  final String PRESENTER_JSON = "\"presenter\":{\"full_name\":\"Joe Bloggs\"";

  final String BENEFICIAL_OWNER_JSON = "\"beneficialOwnerStatement\":\"none_identified\"";

  final String ANY_BOS_OR_MOS_ADDED_OR_CEASED_JSON = "\"anyBOsOrMOsAddedOrCeased\":false,\"";
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

  String output = "";
  @InjectMocks private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;
  @Mock private EntityNameValidator entityNameValidator;
  @Mock private EntityDtoValidator entityDtoValidator;
  @Mock private PresenterDtoValidator presenterDtoValidator;
  @Mock private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
  @Mock private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
  @Mock private OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator;
  @Mock private TrustDetailsValidator trustDetailsValidator;
  @Mock private TrustIndividualValidator trustIndividualValidator;
  @Mock private HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator;
  @Mock private TrustCorporateValidator trustCorporateValidator;

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
  void testPopulateUpdateSubmissionJSON() throws Exception {
    output = getJsonOutput();
    assertEquals("{", output.substring(0, 1));
    assertEquals("}", output.substring(output.length() - 1));
  }

  @Test
  void testPopulateUpdateSubmissionForPresenterJSON() throws Exception {
    output = getJsonOutput();
    assertTrue(output.contains(PRESENTER_JSON));
  }

  @Test
  void testPopulateUpdateSubmissionForDueDiligenceJSON() throws Exception {
    output = getJsonOutput();
    assertTrue(output.contains(DUE_DILIGENCE_JSON));
  }

  @Test
  void testPopulateUpdateSubmissionForUserSubmissionJSON() throws Exception {
    output = getJsonOutput();
    assertTrue(output.contains(USER_SUBMISSION_JSON));
  }

  @Test
  void testPopulateUpdateSubmissionForBeneficialOwnerJSON() throws Exception {
    output = getJsonOutput();
    assertTrue(output.contains(BENEFICIAL_OWNER_JSON));
  }

  @Test
  void testPopulateUpdateSubmissionForBosOrMosAddedOrCeasedJSON() throws Exception {
    output = getJsonOutput();
    assertTrue(output.contains(ANY_BOS_OR_MOS_ADDED_OR_CEASED_JSON));
  }

  private String getJsonOutput() throws Exception {

    setIsTrustWebEnabledFeatureFlag(true);
    buildOverseasEntitySubmissionDto();
    overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
    Errors errors =
        overseasEntitySubmissionDtoValidator.validateFull(
            overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
    verifyValidateFull();
    verify(ownersAndOfficersDataBlockValidator, times(1))
        .validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto), any(), any());
    PopulateUpdateSubmission populateUpdateSubmission =
        new PopulateUpdateSubmission(overseasEntitySubmissionDto, new UpdateSubmission());
    UpdateSubmission updateSubmission = populateUpdateSubmission.populate();
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper = JsonMapper.builder().findAndAddModules().build(); //
    output = objectMapper.writeValueAsString(updateSubmission);
    return output;
  }

  private void verifyValidateFull() {
    verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
    verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
    verify(dueDiligenceDataBlockValidator, times(1))
        .validateFullDueDiligenceFields(
            eq(overseasEntitySubmissionDto.getDueDiligence()),
            eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
            any(),
            any());
    verify(trustDetailsValidator, times(1)).validate(any(), any(), any());
    verify(trustIndividualValidator, times(1)).validate(any(), any(), any());
    verify(historicalBeneficialOwnerValidator, times(1)).validate(any(), any(), any());
    verify(trustCorporateValidator, times(1)).validate(any(), any(), any());
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
}
