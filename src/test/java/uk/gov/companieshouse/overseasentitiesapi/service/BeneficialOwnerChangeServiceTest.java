package uk.gov.companieshouse.overseasentitiesapi.service;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
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

    PrivateBoDataApi mockRightPart = mock(PrivateBoDataApi.class);
    PscApi mockLeftPart = mock(PscApi.class);

    when(mockPublicPrivateBoPair.getRight()).thenReturn(mockRightPart);
    when(mockPublicPrivateBoPair.getLeft()).thenReturn(mockLeftPart);
    when(mockLeftPart.getIdentification()).thenReturn(mockedIdentification);

    when(mockPublicPrivateBoPairLeftNull.getRight()).thenReturn(mockRightPart);
    when(mockPublicPrivateBoPairLeftNull.getLeft()).thenReturn(null);

    when(mockPublicPrivateBoPairRightNull.getRight()).thenReturn(null);
    when(mockPublicPrivateBoPairRightNull.getLeft()).thenReturn(mockLeftPart);

    beneficialOwnerChangeService = new BeneficialOwnerChangeService();
  }

  @Test
  void testCovertBeneficialOwnerCorporateChangeThroughCollateChanges() {
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(CorporateBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof CorporateBeneficialOwnerChange) {
      CorporateBeneficialOwnerChange corporateBeneficialOwnerChange = (CorporateBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Smith", corporateBeneficialOwnerChange.getPsc().getCorporateName());
    }
  }

  @Test
  void testCovertBeneficialOwnerIndividualToChangeThroughCollateChanges() {
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
        publicPrivateBo, overseasEntitySubmissionDto);

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
    }
  }

  @Test
  void testCovertBeneficialOwnerOtherChangeThroughCollateChanges() {
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");

    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPair);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(OtherBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof OtherBeneficialOwnerChange) {
      OtherBeneficialOwnerChange governmentOrPublicAuthorityBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Doe",
          governmentOrPublicAuthorityBeneficialOwnerChange.getPsc().getCorporateSoleName());
    }
  }

  @Test
  void testCollateAllBeneficialOwnerChanges() {
    // setup corporate DTO
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");

    // setup individual DTO
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567890");
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Doe");

    // setup other DTO
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");

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
        publicPrivateBo, overseasEntitySubmissionDto);

    assertEquals(3, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.stream().anyMatch(CorporateBeneficialOwnerChange.class::isInstance));
    assertTrue(result.stream().anyMatch(IndividualBeneficialOwnerChange.class::isInstance));
    assertTrue(result.stream().anyMatch(OtherBeneficialOwnerChange.class::isInstance));
  }

  @Test
  void testCollateAllBeneficialOwnerChangesPairIsNull() {
    // setup corporate DTO
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
    beneficialOwnerCorporateDto.setName("John Smith");

    // setup individual DTO
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setChipsReference("1234567890");
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Doe");

    // setup other DTO
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    beneficialOwnerOtherDto.setName("John Doe");
    beneficialOwnerOtherDto.setOnSanctionsList(true);

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(null);
    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(null);
    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(null);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo, overseasEntitySubmissionDto);

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
        publicPrivateBo, overseasEntitySubmissionDto);

    assertEquals(0, result.size());
    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesEmptyRightOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairRightNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesIndividualLeftOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setFirstName("John");
    beneficialOwnerIndividualDto.setLastName("Smith");

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

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

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

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

    when(publicPrivateBo.get(beneficialOwnerOtherDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerOtherDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(OtherBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof OtherBeneficialOwnerChange) {
      OtherBeneficialOwnerChange governmentOrPublicAuthorityBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(
          0);
      assertEquals("John Smith Other",
          governmentOrPublicAuthorityBeneficialOwnerChange.getPsc().getCorporateSoleName());
    }
  }

  @Test
  void testCollateBeneficialOwnerChangesEmptyLeftOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

    assertTrue(result.isEmpty());
  }

  @Test
  void testCollateBeneficialOwnerChangesIndividualRightOfPairNull() {
    BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
    beneficialOwnerIndividualDto.setDateOfBirth(LocalDate.of(2000, 1, 1));

    when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairRightNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
        List.of(beneficialOwnerIndividualDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(IndividualBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof IndividualBeneficialOwnerChange) {
      IndividualBeneficialOwnerChange individualBeneficialOwnerChange = (IndividualBeneficialOwnerChange) result.get(
          0);
      assertEquals("2000-01-01", individualBeneficialOwnerChange.getPsc().getBirthDate());
    }
  }

  @Test
  void testCollateBeneficialOwnerChangesCorporateRightOfPairNull() {
    BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();

    beneficialOwnerCorporateDto.setPrincipalAddress(createDummyAddressDto());

    when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
        List.of(beneficialOwnerCorporateDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(CorporateBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof CorporateBeneficialOwnerChange) {
      CorporateBeneficialOwnerChange corporateBeneficialOwnerChange = (CorporateBeneficialOwnerChange) result.get(
          0);
      assertEquals(createDummyAddress(),
          corporateBeneficialOwnerChange.getPsc().getResidentialAddress());
    }
  }

  @Test
  void testCollateBeneficialOwnerChangesOtherRightOfPairNull() {
    BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();

    beneficialOwnerGovernmentOrPublicAuthorityDto.setPrincipalAddress(createDummyAddressDto());

    when(publicPrivateBo.get(
        beneficialOwnerGovernmentOrPublicAuthorityDto.getChipsReference())).thenReturn(
        mockPublicPrivateBoPairLeftNull);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()).thenReturn(
        List.of(beneficialOwnerGovernmentOrPublicAuthorityDto));

    List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
        publicPrivateBo,
        overseasEntitySubmissionDto);

    assertEquals(1, result.size());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertInstanceOf(OtherBeneficialOwnerChange.class, result.get(0));

    if (result.get(0) instanceof OtherBeneficialOwnerChange) {
      OtherBeneficialOwnerChange corporateBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(
          0);
      assertEquals(createDummyAddress(),
          corporateBeneficialOwnerChange.getPsc().getResidentialAddress());
    }
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
        publicPrivateBo, overseasEntitySubmissionDto);

    assertTrue(result.isEmpty());
  }

}
