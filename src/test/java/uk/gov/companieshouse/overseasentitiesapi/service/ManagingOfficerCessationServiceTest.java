package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.CorporateManagingOfficerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualManagingOfficerCessation;

class ManagingOfficerCessationServiceTest {

  @Mock private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  @Mock private Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData;

  private ManagingOfficerCessationService managingOfficerCessationService;

  Map<String, Object> logMap = new HashMap<>();

  ByteArrayOutputStream outputStreamCaptor;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    managingOfficerCessationService = new ManagingOfficerCessationService();
    outputStreamCaptor = new ByteArrayOutputStream();
  }

  @AfterEach
  void tearDown() {
    outputStreamCaptor.reset();
    System.setOut(System.out);
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
    corporateManagingOfficer.setResignedOn(LocalDate.of(2023, 5, 15));
    corporateManagingOfficer.setChipsReference("DEF456");
    corporateManagingOfficers.add(corporateManagingOfficer);

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
        .thenReturn(individualManagingOfficers);
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
        .thenReturn(corporateManagingOfficers);

    String hashedId1 = individualManagingOfficer.getChipsReference();
    String hashedId2 = corporateManagingOfficer.getChipsReference();

    ManagingOfficerDataApi privateMoData1 = new ManagingOfficerDataApi();
    privateMoData1.setManagingOfficerAppointmentId("123");
    ManagingOfficerDataApi privateMoData2 = new ManagingOfficerDataApi();
    privateMoData2.setManagingOfficerAppointmentId("456");

    when(combinedMoData.get(hashedId1)).thenReturn(new ImmutablePair<>(null, privateMoData1));
    when(combinedMoData.get(hashedId2)).thenReturn(new ImmutablePair<>(null, privateMoData2));

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, cessations.size()); // Assuming only one beneficial owner satisfies the filter criteria

    IndividualManagingOfficerCessation individualCessation = (IndividualManagingOfficerCessation) cessations.get(0);
    CorporateManagingOfficerCessation corporateCessation = (CorporateManagingOfficerCessation) cessations.get(1);

    assertEquals("123", individualCessation.getOfficerAppointmentId());
    assertEquals(LocalDate.now(), individualCessation.getActionDate());
    assertEquals(LocalDate.of(1990, 5, 15).toString(), individualCessation.getOfficerDateOfBirth());
    assertEquals("John Doe", new StringJoiner(" ")
            .add(individualCessation.getOfficerName().getForename())
            .add(individualCessation.getOfficerName().getSurname()).toString());
    assertEquals("Individual Managing Officer", individualCessation.getAppointmentType());

    assertEquals("ACME Corporation", corporateCessation.getOfficerName().getSurname());
    assertEquals("2023-05-15", corporateCessation.getActionDate().toString());
    assertEquals("Corporate Managing Officer", corporateCessation.getAppointmentType());
  }

  @Test
  void testManagingOfficerCessationsNoData() {
    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.emptyList());
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.emptyList());

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
            overseasEntitySubmissionDto, combinedMoData, logMap);

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
            overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficersCessationsNoPairFound() {
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
    privateMoData.setManagingOfficerAppointmentId(null);

    when(combinedMoData.get(hashedId)).thenReturn(new ImmutablePair<>(null, privateMoData));

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoPrivateData() {
    ManagingOfficerIndividualDto individualDto = new ManagingOfficerIndividualDto();
    individualDto.setResignedOn(LocalDate.now());
    individualDto.setChipsReference("XYZ987");
    ManagingOfficerCorporateDto corporateDto = new ManagingOfficerCorporateDto();
    corporateDto.setResignedOn(LocalDate.now());
    corporateDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.singletonList(individualDto));
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(corporateDto));

    when(combinedMoData.get(individualDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(new CompanyOfficerApi(), null));
    when(combinedMoData.get(individualDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(new CompanyOfficerApi(), null));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(
                    overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No private data found for managing officer - changes cannot be created"));
    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No Managing Officer ID was found in Private Data"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoPublicData() {
    ManagingOfficerIndividualDto individualDto = new ManagingOfficerIndividualDto();
    individualDto.setResignedOn(LocalDate.now());
    individualDto.setChipsReference("XYZ987");
    ManagingOfficerCorporateDto corporateDto = new ManagingOfficerCorporateDto();
    corporateDto.setResignedOn(LocalDate.now());
    corporateDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.singletonList(individualDto));
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(corporateDto));

    when(combinedMoData.get(individualDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(null, new ManagingOfficerDataApi()));
    when(combinedMoData.get(individualDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(null, new ManagingOfficerDataApi()));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(
                    overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No public data found for managing officer - continuing with changes"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoPublicPrivateData() {
    ManagingOfficerIndividualDto individualDto = new ManagingOfficerIndividualDto();
    individualDto.setResignedOn(LocalDate.now());
    individualDto.setChipsReference("XYZ987");
    ManagingOfficerCorporateDto corporateDto = new ManagingOfficerCorporateDto();
    corporateDto.setResignedOn(LocalDate.now());
    corporateDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.singletonList(individualDto));
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(corporateDto));

    when(combinedMoData.get(individualDto.getChipsReference())).thenReturn(new ImmutablePair<>(null, null));
    when(combinedMoData.get(individualDto.getChipsReference())).thenReturn(new ImmutablePair<>(null, null));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(
                    overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No public data found for managing officer - continuing with changes"));
    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No private data found for managing officer - changes cannot be created"));
    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No Managing Officer ID was found in Private Data"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoAppointmentId() {
    ManagingOfficerIndividualDto individualDto = new ManagingOfficerIndividualDto();
    individualDto.setResignedOn(LocalDate.now());
    individualDto.setChipsReference("XYZ987");
    ManagingOfficerCorporateDto corporateDto = new ManagingOfficerCorporateDto();
    corporateDto.setResignedOn(LocalDate.now());
    corporateDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.singletonList(individualDto));
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(corporateDto));

    CompanyOfficerApi publicOfficer = new CompanyOfficerApi();
    ManagingOfficerDataApi privateOfficerData = new ManagingOfficerDataApi();
    privateOfficerData.setManagingOfficerAppointmentId(null);

    when(combinedMoData.get(individualDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(publicOfficer, privateOfficerData));
    when(combinedMoData.get(corporateDto.getChipsReference()))
            .thenReturn(new ImmutablePair<>(publicOfficer, privateOfficerData));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
        managingOfficerCessationService.managingOfficerCessations(
            overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No Managing Officer ID was found in Private Data"));
    assertEquals(0, cessations.size());
  }

  @Test
  void testManagingOfficerCessationsNoPairFound() {
    ManagingOfficerIndividualDto individualDto = new ManagingOfficerIndividualDto();
    individualDto.setResignedOn(LocalDate.now());
    individualDto.setChipsReference("XYZ987");
    ManagingOfficerCorporateDto corporateDto = new ManagingOfficerCorporateDto();
    corporateDto.setResignedOn(LocalDate.now());
    corporateDto.setChipsReference("XYZ987");

    when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
            .thenReturn(Collections.singletonList(individualDto));
    when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            .thenReturn(Collections.singletonList(corporateDto));

    when(combinedMoData.get(individualDto.getChipsReference())).thenReturn(null);
    when(combinedMoData.get(corporateDto.getChipsReference())).thenReturn(null);

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Cessation> cessations =
            managingOfficerCessationService.managingOfficerCessations(
                    overseasEntitySubmissionDto, combinedMoData, logMap);

    assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),"No matching MO was found in the database"));
    assertEquals(0, cessations.size());
  }
}
