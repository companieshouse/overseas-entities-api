package uk.gov.companieshouse.overseasentitiesapi.service;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.psc.Identification;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.CorporateBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.IndividualBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.OtherBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

class BeneficialOwnerChangeServiceTest {

  @InjectMocks
  BeneficialOwnerChangeService beneficialOwnerChangeService;

  @Mock
  OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  @Mock
  Map<String, Pair<PscApi, PrivateBoDataApi>> publicPrivateBo;

  Identification mockedIdentification;

  @Mock
  Pair<PscApi, PrivateBoDataApi> mockPublicPrivateBoPair;

  @Mock
  Pair<PscApi, PrivateBoDataApi> mockPublicPrivateBoPairLeftNull;

  @Mock
  Pair<PscApi, PrivateBoDataApi> mockPublicPrivateBoPairRightNull;
  @Mock
  Pair<PscApi, PrivateBoDataApi> mockPublicPrivateBoPairBothNull;

  Map<String, Object> logMap = new HashMap<>();

  ByteArrayOutputStream outputStreamCaptor;

  private static AddressDto createDummyAddressDto() {
    AddressDto addressDto = new AddressDto();
    addressDto.setPropertyNameNumber("123");
    addressDto.setLine1("Main Street");
    addressDto.setLine2("Apartment 4B");
    addressDto.setCounty("Countyshire");
    addressDto.setLocality("Cityville");
    addressDto.setCountry("United Kingdom");
    addressDto.setPoBox("98765");
    addressDto.setCareOf("John Doe");
    addressDto.setPostcode("AB12 3CD");

    return addressDto;
  }

  private static Address createDummyAddress() {
    Address address = new Address();
    address.setCareOf("John Doe");
    address.setPoBox("98765");
    address.setHouseNameNum("123");
    address.setStreet("Main Street");
    address.setArea("Apartment 4B");
    address.setPostTown("Cityville");
    address.setRegion("Countyshire");
    address.setPostCode("AB12 3CD");
    address.setCountry("United Kingdom");

    return address;
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mockedIdentification = new Identification();
    mockedIdentification.setPlaceRegistered("London");
    mockedIdentification.setLegalAuthority("UK Law");
    mockedIdentification.setRegistrationNumber("123456");
    mockedIdentification.setCountryRegistered("UK");
    mockedIdentification.setLegalForm("Private Limited");

    PrivateBoDataApi mockRightPart = new PrivateBoDataApi();
    mockRightPart.setPscId("123");
    PscApi mockLeftPart = mock(PscApi.class);

    when(mockPublicPrivateBoPair.getRight()).thenReturn(mockRightPart);
    when(mockPublicPrivateBoPair.getLeft()).thenReturn(mockLeftPart);
    when(mockLeftPart.getIdentification()).thenReturn(mockedIdentification);

    when(mockPublicPrivateBoPairLeftNull.getRight()).thenReturn(mockRightPart);
    when(mockPublicPrivateBoPairLeftNull.getLeft()).thenReturn(null);

    when(mockPublicPrivateBoPairRightNull.getRight()).thenReturn(null);
    when(mockPublicPrivateBoPairRightNull.getLeft()).thenReturn(mockLeftPart);

    when(mockPublicPrivateBoPairBothNull.getRight()).thenReturn(null);
    when(mockPublicPrivateBoPairBothNull.getLeft()).thenReturn(null);

    beneficialOwnerChangeService = new BeneficialOwnerChangeService();

    outputStreamCaptor = new ByteArrayOutputStream();
  }

  @AfterEach
  void tearDown() {
    outputStreamCaptor.reset();
    System.setOut(System.out);
  }

  @Test
  void testConvertBeneficialOwnerCorporateChangeThroughCollateChanges() {
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");
    beneficialOwnerCorporateDto.setChipsReference("1234567890");

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(CorporateBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof CorporateBeneficialOwnerChange) {
      CorporateBeneficialOwnerChange corporateBeneficialOwnerChange = (CorporateBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Smith", corporateBeneficialOwnerChange.getPsc().getCorporateName());
      assertEquals("123", corporateBeneficialOwnerChange.getAppointmentId());
    }
  }

  @Test
  void testConvertBeneficialOwnerIndividualToChangeThroughCollateChanges() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567890");
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Doe");
    beneficialOwnerIndividualDto.setNationality("Bangladeshi");
    beneficialOwnerIndividualDto.setSecondNationality("Indonesian");
    beneficialOwnerIndividualDto.setNonLegalFirmMembersNatureOfControlTypes(
        List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));
    beneficialOwnerIndividualDto.setOnSanctionsList(true);

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(IndividualBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof IndividualBeneficialOwnerChange) {
      IndividualBeneficialOwnerChange individualBeneficialOwnerChange = (IndividualBeneficialOwnerChange) result.get(
          0);
      assertEquals(new PersonName("John", "Doe"),
          individualBeneficialOwnerChange.getPsc().getPersonName());
      assertEquals("Bangladeshi,Indonesian",
          individualBeneficialOwnerChange.getPsc().getNationalityOther());
      assertEquals(List.of("OE_SIGINFLUENCECONTROL_AS_FIRM"),
          individualBeneficialOwnerChange.getPsc().getNatureOfControls());
      assertTrue(individualBeneficialOwnerChange.getPsc().isOnSanctionsList());
      assertEquals("123", individualBeneficialOwnerChange.getAppointmentId());
    }
  }

  @Test
  void testConvertBeneficialOwnerOtherChangeThroughCollateChanges() {
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");
    beneficialOwnerOtherDto.setChipsReference("1234567890");

    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(OtherBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof OtherBeneficialOwnerChange) {
      OtherBeneficialOwnerChange governmentOrPublicAuthorityBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Doe",
          governmentOrPublicAuthorityBeneficialOwnerChange.getPsc().getCorporateName());
      assertEquals("123", governmentOrPublicAuthorityBeneficialOwnerChange.getAppointmentId());
    }
  }

  @Test
  void testConvertBeneficialOwnerOtherChangeCheckCompanyIdentificationThroughCollateChanges() {
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setLegalForm("Private Limited");
    beneficialOwnerOtherDto.setChipsReference("1234567890");

    mockPublicPrivateBoPair.getLeft().getIdentification().setLegalForm("Not Private Limited");

    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(OtherBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof OtherBeneficialOwnerChange) {
      OtherBeneficialOwnerChange governmentOrPublicAuthorityBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(
          0);
      assertEquals("Private Limited",
          governmentOrPublicAuthorityBeneficialOwnerChange.getPsc().getCompanyIdentification()
              .getLegalForm());
    }
  }

  @Test
  void testCollateAllBeneficialOwnerChanges() {
    // setup corporate DTO
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");
    beneficialOwnerCorporateDto.setChipsReference("1234567890");

    // setup individual DTO
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Doe");

    // setup other DTO
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");
    beneficialOwnerOtherDto.setChipsReference("1234567892");

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);

    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(3, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.stream().anyMatch(CorporateBeneficialOwnerChange.class::isInstance));
    assertTrue(result.stream().anyMatch(IndividualBeneficialOwnerChange.class::isInstance));
    assertTrue(result.stream().anyMatch(OtherBeneficialOwnerChange.class::isInstance));
  }

  @Test
  void testCollateAllBeneficialOwnerChangesProducesNoLogsIfNoChipsReference() {
    // setup corporate DTO
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");

    // setup individual DTO
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Doe");

    // setup other DTO
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");
    beneficialOwnerOtherDto.setOnSanctionsList(true);

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);

    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals("", outputStreamCaptor.toString());
    assertEquals(0, result.size());
  }

  @Test
  void testCollateAllBeneficialOwnerChangesProducesLogsIfPairIsNull() throws IOException {
    // setup corporate DTO
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");
    beneficialOwnerCorporateDto.setChipsReference("1234567890");

    // setup individual DTO
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Doe");

    // setup other DTO
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");
    beneficialOwnerOtherDto.setChipsReference("1234567892");

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(null);
    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(null);
    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(null);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    var result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
            publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(3, StringUtils.countMatches(outputStreamCaptor.toString(), "No public and no private data found for beneficial owner"));
    assertEquals(0, result.size());
  }

  @Test
  void testCollateNoBeneficialOwnerChanges() {
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        Collections.emptyList());
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        Collections.emptyList());
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        Collections.emptyList());

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertEquals(0, result.size());
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesEmptyRightOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairRightNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No private data found for beneficial owner - changes cannot be created"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesIndividualLeftOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Smith");
    beneficialOwnerIndividualDto.setChipsReference("1234567891");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No public data found for beneficial owner - continuing with changes"));
    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(IndividualBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof IndividualBeneficialOwnerChange) {
      IndividualBeneficialOwnerChange individualBeneficialOwnerChange = (IndividualBeneficialOwnerChange) result.get(
          0);
      assertEquals(new PersonName("John", "Smith"),
          individualBeneficialOwnerChange.getPsc().getPersonName());
    }
  }

  @Test
  void testCollateBeneficialOwnerChangesCorporateLeftOfPairNull() {
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith Corp");
    beneficialOwnerCorporateDto.setChipsReference("1234567890");

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No public data found for beneficial owner - continuing with changes"));
    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(CorporateBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof CorporateBeneficialOwnerChange) {
      CorporateBeneficialOwnerChange corporateBeneficialOwnerChange = (CorporateBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Smith Corp", corporateBeneficialOwnerChange.getPsc().getCorporateName());
    }
  }

  @Test
  void testCollateBeneficialOwnerChangesOtherLeftOfPairNull() {
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Smith Other");
    beneficialOwnerOtherDto.setChipsReference("1234567892");

    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No public data found for beneficial owner - continuing with changes"));
    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(OtherBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof OtherBeneficialOwnerChange) {
      OtherBeneficialOwnerChange governmentOrPublicAuthorityBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Smith Other",
          governmentOrPublicAuthorityBeneficialOwnerChange.getPsc().getCorporateName());
    }
  }

  @Test
  void testCollateBeneficialOwnerChangesEmptyLeftOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No public data found for beneficial owner - continuing with changes"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesIndividualRightOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairRightNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No private data found for beneficial owner - changes cannot be created"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesCorporateRightOfPairNull() {
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setChipsReference("1234567890");

    beneficialOwnerCorporateDto.setPrincipalAddress(createDummyAddressDto());

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairRightNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No private data found for beneficial owner - changes cannot be created"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesOtherRightOfPairNull() {
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference("1234567892");
    beneficialOwnerGovernmentOrPublicAuthorityDto.setPrincipalAddress(createDummyAddressDto());

    when(publicPrivateBo.get(
        beneficialOwnerGovernmentOrPublicAuthorityDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairRightNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerGovernmentOrPublicAuthorityDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No private data found for beneficial owner - changes cannot be created"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesLeftAndRightOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
            mockPublicPrivateBoPairBothNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
            List.of(beneficialOwnerIndividualDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
            publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No public data found for beneficial owner - continuing with changes"));
    assertTrue(outputStreamCaptor.toString().contains("No private data found for beneficial owner - changes cannot be created"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesPublicPrivateDataNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567891");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(null);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
            List.of(beneficialOwnerIndividualDto));

    System.setOut(new PrintStream(outputStreamCaptor));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
            publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(outputStreamCaptor.toString().contains("No public and no private data found for beneficial owner"));
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesInvalidData() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567890");
    beneficialOwnerIndividualDto.setFirstName(null);
    beneficialOwnerIndividualDto.setLastName(null);

    mockPublicPrivateBoPair.getLeft().setName("John Doe");
    mockPublicPrivateBoPair.getLeft().setSanctioned(true);

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto, logMap);

    assertTrue(result.isEmpty());
  }

}
