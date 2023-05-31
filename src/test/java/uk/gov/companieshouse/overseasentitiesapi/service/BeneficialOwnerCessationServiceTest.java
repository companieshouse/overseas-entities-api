package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.service.BeneficialOwnerCessationService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

class BeneficialOwnerCessationServiceTest {

  @Mock private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  @Mock private Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData;

  public static final String NO_PAIR_FOUND = "No matching BO was found in the database";

  private BeneficialOwnerCessationService beneficialOwnerCessationService;
  public static final String NO_ID_FOUND_IN_PRIVATE_DATA = "No Beneficial Owner ID was found in Private Data";
  public static final String SERVICE = "BeneficialOwnerCessationService";
  @Mock
  private ApiLogger logger;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    beneficialOwnerCessationService = new BeneficialOwnerCessationService();
  }

  @Test
  void testBeneficialOwnerCessations() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    BeneficialOwnerIndividualDto individualBeneficialOwner = new BeneficialOwnerIndividualDto();
    individualBeneficialOwner.setFirstName("John");
    individualBeneficialOwner.setLastName("Doe");
    individualBeneficialOwner.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualBeneficialOwner.setCeasedDate(LocalDate.now());
    individualBeneficialOwner.setChipsReference("ABC123");
    individualBeneficialOwners.add(individualBeneficialOwner);

    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    BeneficialOwnerCorporateDto corporateBeneficialOwner = new BeneficialOwnerCorporateDto();
    corporateBeneficialOwner.setName("ACME Corporation");
    corporateBeneficialOwner.setCeasedDate(LocalDate.now());
    corporateBeneficialOwner.setChipsReference("DEF456");
    corporateBeneficialOwners.add(corporateBeneficialOwner);

    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners =
        new ArrayList<>();
    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBeneficialOwner =
        new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBeneficialOwner.setName("Government Authority");
    legalPersonBeneficialOwner.setCeasedDate(LocalDate.now());
    legalPersonBeneficialOwner.setChipsReference("GHI789");
    legalPersonBeneficialOwners.add(legalPersonBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
        .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
        .thenReturn(corporateBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
        .thenReturn(legalPersonBeneficialOwners);

    String hashedId1 = individualBeneficialOwner.getChipsReference();
    String hashedId2 = corporateBeneficialOwner.getChipsReference();
    String hashedId3 = legalPersonBeneficialOwner.getChipsReference();

    PrivateBoDataApi privateBoData1 = new PrivateBoDataApi();
    privateBoData1.setPscId("123");
    PrivateBoDataApi privateBoData2 = new PrivateBoDataApi();
    privateBoData2.setPscId("456");
    PrivateBoDataApi privateBoData3 = new PrivateBoDataApi();
    privateBoData3.setPscId("789");

    when(combinedBoData.get(hashedId1)).thenReturn(new ImmutablePair<>(null, privateBoData1));
    when(combinedBoData.get(hashedId2)).thenReturn(new ImmutablePair<>(null, privateBoData2));
    when(combinedBoData.get(hashedId3)).thenReturn(new ImmutablePair<>(null, privateBoData3));

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations =
        beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, cessations.size()); // Assuming only one beneficial owner satisfies the filter criteria

    IndividualBeneficialOwnerCessation individualCessation = (IndividualBeneficialOwnerCessation) cessations.get(0);
    assertEquals("123", individualCessation.getAppointmentId());
    assertEquals(LocalDate.now(), individualCessation.getActionDate());
    assertEquals(LocalDate.of(1990, 5, 15), individualCessation.getBirthDate());
    assertEquals("John", individualCessation.getPersonName().getForename());
    assertEquals("Doe", individualCessation.getPersonName().getSurname());
  }

  @Test
  void testBeneficialOwnerCessationsNoData() {
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.emptyList());
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.emptyList());
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.emptyList());

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations = beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testGetBeneficialOwnersFiltersOutCeasedDateNull() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners = new ArrayList<>();

    BeneficialOwnerIndividualDto individualBo2 = new BeneficialOwnerIndividualDto();
    individualBo2.setCeasedDate(null);
    individualBo2.setChipsReference("012");

    BeneficialOwnerCorporateDto corporateBo2 = new BeneficialOwnerCorporateDto();
    corporateBo2.setCeasedDate(null);
    corporateBo2.setChipsReference("013");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBo2 = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBo2.setCeasedDate(null);
    legalPersonBo2.setChipsReference("014");

    individualBeneficialOwners.add(individualBo2);
    corporateBeneficialOwners.add(corporateBo2);
    legalPersonBeneficialOwners.add(legalPersonBo2);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(corporateBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(legalPersonBeneficialOwners);

    BeneficialOwnerCessationService beneficialOwnerCessationService = new BeneficialOwnerCessationService();

    List<Cessation> cessations = beneficialOwnerCessationService.beneficialOwnerCessations(
            overseasEntitySubmissionDto, combinedBoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testGetBeneficialOwnersFiltersOutChipsReferenceNull() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners = new ArrayList<>();

    BeneficialOwnerIndividualDto individualBo2 = new BeneficialOwnerIndividualDto();
    individualBo2.setCeasedDate(LocalDate.now());
    individualBo2.setChipsReference(null);

    BeneficialOwnerCorporateDto corporateBo2 = new BeneficialOwnerCorporateDto();
    corporateBo2.setCeasedDate(LocalDate.now());
    corporateBo2.setChipsReference(null);

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBo2 = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBo2.setCeasedDate(LocalDate.now());
    legalPersonBo2.setChipsReference(null);

    individualBeneficialOwners.add(individualBo2);
    corporateBeneficialOwners.add(corporateBo2);
    legalPersonBeneficialOwners.add(legalPersonBo2);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(corporateBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(legalPersonBeneficialOwners);

    BeneficialOwnerCessationService beneficialOwnerCessationService = new BeneficialOwnerCessationService();

    List<Cessation> cessations = beneficialOwnerCessationService.beneficialOwnerCessations(
            overseasEntitySubmissionDto, combinedBoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoPairFound() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    BeneficialOwnerIndividualDto individualBeneficialOwner = new BeneficialOwnerIndividualDto();
    individualBeneficialOwner.setFirstName("John");
    individualBeneficialOwner.setLastName("Doe");
    individualBeneficialOwner.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualBeneficialOwner.setCeasedDate(LocalDate.now());
    individualBeneficialOwner.setChipsReference("XYZ987");
    individualBeneficialOwners.add(individualBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.emptyList());
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.emptyList());

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoIdFoundInPrivateData() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    BeneficialOwnerIndividualDto individualBeneficialOwner = new BeneficialOwnerIndividualDto();
    individualBeneficialOwner.setFirstName("John");
    individualBeneficialOwner.setLastName("Doe");
    individualBeneficialOwner.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualBeneficialOwner.setCeasedDate(LocalDate.now());
    individualBeneficialOwner.setChipsReference("XYZ987");
    individualBeneficialOwners.add(individualBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.emptyList());
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.emptyList());

    String hashedId = individualBeneficialOwner.getChipsReference();

    PrivateBoDataApi privateBoData = new PrivateBoDataApi();
    privateBoData.setPscId(null);

    when(combinedBoData.get(hashedId)).thenReturn(new ImmutablePair<>(null, privateBoData));

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(0, cessations.size());
  }
}


