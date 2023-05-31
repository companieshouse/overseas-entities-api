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
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualManagingOfficerCessation;

class ManagingOfficerCessationServiceTest {

  @Mock private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  @Mock private Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData;

  private ManagingOfficerCessationService managingOfficerCessationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    managingOfficerCessationService = new ManagingOfficerCessationService();
  }

  @Test
  void testManagingOfficerCessations() {
    List<ManagingOfficerIndividualDto> individualManagingOfficers = new ArrayList<>();
    ManagingOfficerIndividualDto individualManagingOfficer = new ManagingOfficerIndividualDto();
    individualManagingOfficer.setFirstName("John");
    individualManagingOfficer.setLastName("Doe");
    individualManagingOfficer.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualManagingOfficer.setResignedOn(LocalDate.now());
    individualManagingOfficer.setChipsReference("ABC123");
    individualManagingOfficers.add(individualManagingOfficer);

    List<ManagingOfficerCorporateDto> corporateManagingOfficers = new ArrayList<>();
    ManagingOfficerCorporateDto corporateManagingOfficer = new ManagingOfficerCorporateDto();
    corporateManagingOfficer.setName("ACME Corporation");
    corporateManagingOfficer.setResignedOn(LocalDate.now());
    corporateManagingOfficer.setChipsReference("DEF456");
    corporateManagingOfficers.add(corporateManagingOfficer);

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
        .thenReturn(individualManagingOfficers);
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
        .thenReturn(corporateManagingOfficers);

    String hashedId1 = individualManagingOfficer.getChipsReference();
    String hashedId2 = corporateManagingOfficer.getChipsReference();

    ManagingOfficerDataApi privateMoData1 = new ManagingOfficerDataApi();
    privateMoData1.setManagingOfficerId("123");
    ManagingOfficerDataApi privateMoData2 = new ManagingOfficerDataApi();
    privateMoData2.setManagingOfficerId("456");

    when(combinedMoData.get(hashedId1)).thenReturn(new ImmutablePair<>(null, privateMoData1));
    when(combinedMoData.get(hashedId2)).thenReturn(new ImmutablePair<>(null, privateMoData2));

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, cessations.size()); // Assuming only one beneficial owner satisfies the filter criteria

    IndividualManagingOfficerCessation individualCessation =
        (IndividualManagingOfficerCessation) cessations.get(0);
    assertEquals("123", individualCessation.getOfficerAppointmentId());
    assertEquals(LocalDate.now(), individualCessation.getActionDate());
    assertEquals(LocalDate.of(1990, 5, 15).toString(), individualCessation.getBirthDate());
    assertEquals("John Doe", individualCessation.getOfficerName());
  }

  @Test
  void testManagingOfficerCessationsNoData() {
    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.emptyList());
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.emptyList());

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations = managingOfficerCessationService.managingOfficerCessations(overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testGetManagingOfficersFiltersOutResignedOnNull() {
    List<ManagingOfficerIndividualDto> managingOfficersIndividual = new ArrayList<>();
    List<ManagingOfficerCorporateDto> managingOfficersCorporate = new ArrayList<>();

    ManagingOfficerIndividualDto individualMo2 = new ManagingOfficerIndividualDto();
    individualMo2.setResignedOn(null);
    individualMo2.setChipsReference("012");

    ManagingOfficerCorporateDto corporateMo2 = new ManagingOfficerCorporateDto();
    corporateMo2.setResignedOn(null);
    corporateMo2.setChipsReference("013");

    managingOfficersIndividual.add(individualMo2);
    managingOfficersCorporate.add(corporateMo2);

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(managingOfficersIndividual);
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(managingOfficersCorporate);

    ManagingOfficerCessationService managingOfficerCessationService = new ManagingOfficerCessationService();

    List<Cessation> cessations = managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testGetManagingOfficersFiltersOutChipsReferenceNull() {
    List<ManagingOfficerIndividualDto> managingOfficersIndividual = new ArrayList<>();
    List<ManagingOfficerCorporateDto> managingOfficersCorporate = new ArrayList<>();

    ManagingOfficerIndividualDto individualMo2 = new ManagingOfficerIndividualDto();
    individualMo2.setResignedOn(LocalDate.now());
    individualMo2.setChipsReference(null);

    ManagingOfficerCorporateDto corporateMo2 = new ManagingOfficerCorporateDto();
    corporateMo2.setResignedOn(LocalDate.now());
    corporateMo2.setChipsReference(null);

    managingOfficersIndividual.add(individualMo2);
    managingOfficersCorporate.add(corporateMo2);

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(managingOfficersIndividual);
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(managingOfficersCorporate);

    ManagingOfficerCessationService managingOfficerCessationService = new ManagingOfficerCessationService();

    List<Cessation> cessations = managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficersCessationsNoPairFound() {
    List<ManagingOfficerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    ManagingOfficerIndividualDto individualManagingOfficer = new ManagingOfficerIndividualDto();
    individualManagingOfficer.setFirstName("John");
    individualManagingOfficer.setLastName("Doe");
    individualManagingOfficer.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualManagingOfficer.setResignedOn(LocalDate.now());
    individualManagingOfficer.setChipsReference("XYZ987");
    individualBeneficialOwners.add(individualManagingOfficer);

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.emptyList());

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficersCessationsNoIdFoundInPrivateData() {
    List<ManagingOfficerIndividualDto> individualManagingOfficers = new ArrayList<>();
    ManagingOfficerIndividualDto individualManagingOfficer = new ManagingOfficerIndividualDto();
    individualManagingOfficer.setFirstName("John");
    individualManagingOfficer.setLastName("Doe");
    individualManagingOfficer.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualManagingOfficer.setResignedOn(LocalDate.now());
    individualManagingOfficer.setChipsReference("XYZ987");
    individualManagingOfficers.add(individualManagingOfficer);

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(individualManagingOfficers);
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.emptyList());

    String hashedId = individualManagingOfficer.getChipsReference();

    ManagingOfficerDataApi privateMoData = new ManagingOfficerDataApi();
    privateMoData.setManagingOfficerId(null);

    when(combinedMoData.get(hashedId)).thenReturn(new ImmutablePair<>(null, privateMoData));

    Map<String, Object> logMap = new HashMap<>();

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsPublicOfficerWithoutPrivateCounterpart() {
    ManagingOfficerIndividualDto officerDto = new ManagingOfficerIndividualDto();
    officerDto.setResignedOn(LocalDate.now());
    officerDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
        .thenReturn(Collections.singletonList(officerDto));

    CompanyOfficerApi publicOfficer = new CompanyOfficerApi();

    when(combinedMoData.get(officerDto.getChipsReference()))
        .thenReturn(new ImmutablePair<>(publicOfficer, null));

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoAppointmentId() {
    ManagingOfficerIndividualDto officerDto = new ManagingOfficerIndividualDto();
    officerDto.setResignedOn(LocalDate.now());
    officerDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
        .thenReturn(Collections.singletonList(officerDto));

    CompanyOfficerApi publicOfficer = new CompanyOfficerApi();
    ManagingOfficerDataApi privateOfficerData = new ManagingOfficerDataApi();
    privateOfficerData.setManagingOfficerId(null);

    when(combinedMoData.get(officerDto.getChipsReference()))
        .thenReturn(new ImmutablePair<>(publicOfficer, privateOfficerData));

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoPairFound() {
    ManagingOfficerCorporateDto officerDto = new ManagingOfficerCorporateDto();
    officerDto.setResignedOn(LocalDate.now());
    officerDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(officerDto));

    when(combinedMoData.get(officerDto.getChipsReference()))
            .thenReturn(null);

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(
                    overseasEntitySubmissionDto, combinedMoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoIdFoundInPrivateData() {
    ManagingOfficerCorporateDto officerDto = new ManagingOfficerCorporateDto();
    officerDto.setResignedOn(LocalDate.now());
    officerDto.setChipsReference("XYZ987");

    ManagingOfficerDataApi privateMoData = new ManagingOfficerDataApi();
    privateMoData.setManagingOfficerId(null); // Set the ID to null

    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(officerDto));

    when(combinedMoData.get(officerDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(null, privateMoData));

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(
                    overseasEntitySubmissionDto, combinedMoData, new HashMap<>());

    assertEquals(0, cessations.size());
  }
}
