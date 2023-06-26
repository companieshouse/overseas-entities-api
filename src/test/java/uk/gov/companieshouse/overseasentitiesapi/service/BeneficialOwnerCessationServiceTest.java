package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
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

  private BeneficialOwnerCessationService beneficialOwnerCessationService;
  @Mock
  private ApiLogger logger;
  Map<String, Object> logMap = new HashMap<>();
  ByteArrayOutputStream outputStreamCaptor;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    beneficialOwnerCessationService = new BeneficialOwnerCessationService();
    outputStreamCaptor = new ByteArrayOutputStream();
  }

  @AfterEach
  void tearDown() {
    outputStreamCaptor.reset();
    System.setOut(System.out);
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

    List<Cessation> cessations = beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testGetBeneficialOwnersFiltersOutCeasedDateNull() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners = new ArrayList<>();

    BeneficialOwnerIndividualDto individualBo = new BeneficialOwnerIndividualDto();
    individualBo.setCeasedDate(null);
    individualBo.setChipsReference("012");

    BeneficialOwnerCorporateDto corporateBo = new BeneficialOwnerCorporateDto();
    corporateBo.setCeasedDate(null);
    corporateBo.setChipsReference("013");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBo = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBo.setCeasedDate(null);
    legalPersonBo.setChipsReference("014");

    individualBeneficialOwners.add(individualBo);
    corporateBeneficialOwners.add(corporateBo);
    legalPersonBeneficialOwners.add(legalPersonBo);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(corporateBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(legalPersonBeneficialOwners);

    BeneficialOwnerCessationService beneficialOwnerCessationService = new BeneficialOwnerCessationService();

    List<Cessation> cessations = beneficialOwnerCessationService.beneficialOwnerCessations(
            overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testGetBeneficialOwnersFiltersOutChipsReferenceNull() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners = new ArrayList<>();

    BeneficialOwnerIndividualDto individualBo = new BeneficialOwnerIndividualDto();
    individualBo.setCeasedDate(LocalDate.now());
    individualBo.setChipsReference(null);

    BeneficialOwnerCorporateDto corporateBo = new BeneficialOwnerCorporateDto();
    corporateBo.setCeasedDate(LocalDate.now());
    corporateBo.setChipsReference(null);

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBo = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBo.setCeasedDate(LocalDate.now());
    legalPersonBo.setChipsReference(null);

    individualBeneficialOwners.add(individualBo);
    corporateBeneficialOwners.add(corporateBo);
    legalPersonBeneficialOwners.add(legalPersonBo);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(corporateBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(legalPersonBeneficialOwners);

    BeneficialOwnerCessationService beneficialOwnerCessationService = new BeneficialOwnerCessationService();

    List<Cessation> cessations = beneficialOwnerCessationService.beneficialOwnerCessations(
            overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoPairFound() {
    BeneficialOwnerIndividualDto individual = new BeneficialOwnerIndividualDto();
    individual.setCeasedDate(LocalDate.now());
    individual.setChipsReference("XYZ123");

    BeneficialOwnerCorporateDto corporate = new BeneficialOwnerCorporateDto();
    corporate.setCeasedDate(LocalDate.now());
    corporate.setChipsReference("XYZ456");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPerson = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPerson.setCeasedDate(LocalDate.now());
    legalPerson.setChipsReference("XYZ789");

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.singletonList(individual));
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.singletonList(corporate));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.singletonList(legalPerson));

    when(combinedBoData.get(individual.getChipsReference())).thenReturn(null);
    when(combinedBoData.get(corporate.getChipsReference())).thenReturn(null);
    when(combinedBoData.get(legalPerson.getChipsReference())).thenReturn(null);

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No matching BO was found in the database"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoPublicData() {
    BeneficialOwnerIndividualDto individual = new BeneficialOwnerIndividualDto();
    individual.setCeasedDate(LocalDate.now());
    individual.setChipsReference("XYZ123");

    BeneficialOwnerCorporateDto corporate = new BeneficialOwnerCorporateDto();
    corporate.setCeasedDate(LocalDate.now());
    corporate.setChipsReference("XYZ456");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPerson = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPerson.setCeasedDate(LocalDate.now());
    legalPerson.setChipsReference("XYZ789");

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.singletonList(individual));
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.singletonList(corporate));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.singletonList(legalPerson));

    PscApi pscApi = null;
    PrivateBoDataApi privateBoData = new PrivateBoDataApi();
    privateBoData.setPscId(null);

    when(combinedBoData.get(individual.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(corporate.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(legalPerson.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No public data found for beneficial owner"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoPrivateData() {
    BeneficialOwnerIndividualDto individual = new BeneficialOwnerIndividualDto();
    individual.setCeasedDate(LocalDate.now());
    individual.setChipsReference("XYZ123");

    BeneficialOwnerCorporateDto corporate = new BeneficialOwnerCorporateDto();
    corporate.setCeasedDate(LocalDate.now());
    corporate.setChipsReference("XYZ456");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPerson = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPerson.setCeasedDate(LocalDate.now());
    legalPerson.setChipsReference("XYZ789");

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.singletonList(individual));
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.singletonList(corporate));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.singletonList(legalPerson));

    PscApi pscApi = new PscApi();
    PrivateBoDataApi privateBoData = null;

    when(combinedBoData.get(individual.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(corporate.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(legalPerson.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No private data found for beneficial owner"));
    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No Beneficial Owner ID was found in Private Data"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoPublicPrivateData() {
    BeneficialOwnerIndividualDto individual = new BeneficialOwnerIndividualDto();
    individual.setCeasedDate(LocalDate.now());
    individual.setChipsReference("XYZ123");

    BeneficialOwnerCorporateDto corporate = new BeneficialOwnerCorporateDto();
    corporate.setCeasedDate(LocalDate.now());
    corporate.setChipsReference("XYZ456");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPerson = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPerson.setCeasedDate(LocalDate.now());
    legalPerson.setChipsReference("XYZ789");

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.singletonList(individual));
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.singletonList(corporate));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.singletonList(legalPerson));

    PscApi pscApi = null;
    PrivateBoDataApi privateBoData = null;

    when(combinedBoData.get(individual.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(corporate.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(legalPerson.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No public data found for beneficial owner"));
    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No private data found for beneficial owner"));
    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No Beneficial Owner ID was found in Private Data"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsPublicPrivateDataNull() {
    BeneficialOwnerIndividualDto individual = new BeneficialOwnerIndividualDto();
    individual.setCeasedDate(LocalDate.now());
    individual.setChipsReference("XYZ123");

    BeneficialOwnerCorporateDto corporate = new BeneficialOwnerCorporateDto();
    corporate.setCeasedDate(LocalDate.now());
    corporate.setChipsReference("XYZ456");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPerson = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPerson.setCeasedDate(LocalDate.now());
    legalPerson.setChipsReference("XYZ789");

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.singletonList(individual));
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.singletonList(corporate));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.singletonList(legalPerson));

    PscApi pscApi = null;
    PrivateBoDataApi privateBoData = null;

    combinedBoData = null;

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No public and no private data found for managing officer"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testBeneficialOwnerCessationsNoIdFoundInPrivateData() {
    BeneficialOwnerIndividualDto individual = new BeneficialOwnerIndividualDto();
    individual.setCeasedDate(LocalDate.now());
    individual.setChipsReference("XYZ123");

    BeneficialOwnerCorporateDto corporate = new BeneficialOwnerCorporateDto();
    corporate.setCeasedDate(LocalDate.now());
    corporate.setChipsReference("XYZ456");

    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPerson = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPerson.setCeasedDate(LocalDate.now());
    legalPerson.setChipsReference("XYZ789");

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(Collections.singletonList(individual));
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(Collections.singletonList(corporate));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(Collections.singletonList(legalPerson));

    PscApi pscApi = new PscApi();
    PrivateBoDataApi privateBoData = new PrivateBoDataApi();
    privateBoData.setPscId(null);

    when(combinedBoData.get(individual.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(corporate.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));
    when(combinedBoData.get(legalPerson.getChipsReference())).thenReturn(new ImmutablePair<>(pscApi, privateBoData));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(),"No Beneficial Owner ID was found in Private Data"));
    assertEquals(0, cessations.size());
  }
}


