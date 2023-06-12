package uk.gov.companieshouse.overseasentitiesapi.service;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.IdentificationApi;
import uk.gov.companieshouse.api.model.psc.Identification;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.CorporateBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.IndividualBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.OtherBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.CorporateManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.IndividualManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.IndividualManagingOfficer;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManagingOfficerChangeServiceTest {

    @InjectMocks
    ManagingOfficerChangeService managingOfficerChangeService;

    @Mock
    OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    @Mock
    Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> publicPrivateMo;

    IdentificationApi mockedIdentificationApi;

    @Mock
    Pair<CompanyOfficerApi, ManagingOfficerDataApi> mockPublicPrivateMoPair;

    @Mock
    Pair<CompanyOfficerApi, ManagingOfficerDataApi> mockPublicPrivateMoPairLeftNull;

    @Mock
    Pair<CompanyOfficerApi, ManagingOfficerDataApi> mockPublicPrivateMoPairRightNull;

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

        mockedIdentificationApi = new IdentificationApi();
        mockedIdentificationApi.setPlaceRegistered("London");
        mockedIdentificationApi.setLegalAuthority("UK Law");
        mockedIdentificationApi.setRegistrationNumber("123456");
        //mockedIdentificationApi.setCountryRegistered("UK");
        mockedIdentificationApi.setLegalForm("Private Limited");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = mock(CompanyOfficerApi.class);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);
        when(mockLeftPart.getIdentification()).thenReturn(mockedIdentificationApi);

        when(mockPublicPrivateMoPairLeftNull.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPairLeftNull.getLeft()).thenReturn(null);

        when(mockPublicPrivateMoPairRightNull.getRight()).thenReturn(null);
        when(mockPublicPrivateMoPairRightNull.getLeft()).thenReturn(mockLeftPart);

        managingOfficerChangeService = new ManagingOfficerChangeService();
    }

    @Test
    void testCollateAllManagingOfficerChanges() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("John Smith");
        managingOfficerCorporateDto.setChipsReference("1234567890");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));


        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(IndividualManagingOfficerChange.class::isInstance));
        assertTrue(result.stream().anyMatch(CorporateManagingOfficerChange.class::isInstance));
    }

    @Test
    void testConvertManagingOfficerIndividualToChangeThroughCollateChanges() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567890");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");
        managingOfficerIndividualDto.setNationality("Bangladeshi");
        managingOfficerIndividualDto.setSecondNationality("Indonesian");
        managingOfficerIndividualDto.setStartDate(LocalDate.of(2022, 1, 1));
        managingOfficerIndividualDto.setResignedOn(LocalDate.of(2023, 1, 1));
        managingOfficerIndividualDto.setOccupation("Occupation");
        managingOfficerIndividualDto.setRoleAndResponsibilities("Role");
        managingOfficerIndividualDto.setFormerNames("Jonathan Doe, Johnny Doe");
        managingOfficerIndividualDto.setServiceAddress(createDummyAddressDto());
        managingOfficerIndividualDto.setUsualResidentialAddress(createDummyAddressDto());

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(IndividualManagingOfficerChange.class, result.get(0));

        IndividualManagingOfficerChange individualManagingOfficerChange =
                (IndividualManagingOfficerChange) result.get(0);
        assertEquals(new PersonName("John", "Doe"),
                individualManagingOfficerChange.getOfficer().getPersonName());
        assertEquals("Bangladeshi, Indonesian",
                individualManagingOfficerChange.getOfficer().getNationalityOther());
        assertEquals("2022-01-01", individualManagingOfficerChange.getOfficer().getStartDate().toString());
        assertEquals("2023-01-01", individualManagingOfficerChange.getOfficer().getActionDate().toString());
        assertEquals("Jonathan Doe, Johnny Doe", individualManagingOfficerChange.getOfficer().getFormerNames());
        assertEquals("Occupation", individualManagingOfficerChange.getOfficer().getOccupation());
        assertEquals("Role", individualManagingOfficerChange.getOfficer().getRole());
        assertEquals("123", individualManagingOfficerChange.getOfficer().getServiceAddress().getHouseNameNum());
        assertEquals("123", individualManagingOfficerChange.getOfficer().getResidentialAddress().getHouseNameNum());
    }
}
