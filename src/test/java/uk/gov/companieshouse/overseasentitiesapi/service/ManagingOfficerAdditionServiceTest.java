package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.CorporateManagingOfficerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualManagingOfficerAddition;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import utils.AddressTestUtils;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagingOfficerAdditionServiceTest {
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private ManagingOfficerAdditionService managingOfficerAdditionService;
    public static final AddressDto TEST_ADDRESS_DTO = new AddressDto() {{
        setPropertyNameNumber("1");
        setLine1("My address");
        setLine2("Some street");
        setCountry("Some country");
        setLocality("Some town");
        setCounty("Some county");
        setPostcode("Some Postcode");
    }};

    public static final String[] TEST_SERVICE_ADDRESS = {"1", "PO Box 123", "1234", "Main Street", "Apartment 1A", "Metropolis", "Metro County", "12345", "United States"};

    public static final String[] TEST_RESIDENTIAL_ADDRESS = {"2", "", "5678", "Park Avenue", "Unit 2B", "Cityville", "City County", "67890", "United States"};


    @BeforeEach
    public void setUp() {
        managingOfficerAdditionService = new ManagingOfficerAdditionService();
    }

    @Test
    void testManagingOfficerAdditions() {
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(getIndividualManagingOfficers());
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(getCorporateManagingOfficers());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);

        assertEquals(2, additions.size());
        assertIndividualManagingOfficerDetails((IndividualManagingOfficerAddition) additions.getFirst());
        assertCorporateManagingOfficerDetails((CorporateManagingOfficerAddition) additions.get(1));
    }

    @Test
    void testManagingOfficerAdditionsNoDataReturnsEmptyList() {
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(Collections.emptyList());
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);

        assertEquals(0, additions.size());
    }

    @Test
    void testManagingOfficerAdditionsNullReturnsEmptyList() {
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(null);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(null);

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);

        assertEquals(0, additions.size());
    }

    @Test
    void testManagingOfficerNoAdditionsInSubmissionReturnsEmptyList() {
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
                .thenReturn(List.of(new ManagingOfficerIndividualDto() {{
                    setChipsReference("123456789");
                }}));
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
                .thenReturn(List.of(new ManagingOfficerCorporateDto() {{
                    setChipsReference("123456789");
                }}));

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);

        assertEquals(0, additions.size());
    }

    @Test
    void testManagingOfficerAdditionsNoSecondNationalityInSubmissionReturnsNationality() {
        var individualManagingOfficers = getIndividualManagingOfficers();
        individualManagingOfficers.getFirst().setSecondNationality(null);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(individualManagingOfficers);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);

        assertEquals(1, additions.size());
        assertEquals("Irish", ((IndividualManagingOfficerAddition) additions.getFirst()).getNationalityOther());
    }

    private List<ManagingOfficerIndividualDto> getIndividualManagingOfficers() {
        var individualManagingOfficer = new ManagingOfficerIndividualDto();
        individualManagingOfficer.setStartDate(LocalDate.of(2020, 1, 1));
        individualManagingOfficer.setResignedOn(LocalDate.of(2023, 1, 1));
        individualManagingOfficer.setServiceAddress(TEST_ADDRESS_DTO);
        individualManagingOfficer.setUsualResidentialAddress(TEST_ADDRESS_DTO);
        individualManagingOfficer.setFirstName("John");
        individualManagingOfficer.setLastName("Doe");
        individualManagingOfficer.setFormerNames("Some other name");
        individualManagingOfficer.setDateOfBirth(LocalDate.of(1990, 5, 15));
        individualManagingOfficer.setNationality("Irish");
        individualManagingOfficer.setSecondNationality("Spanish");
        individualManagingOfficer.setOccupation("Self employed");
        individualManagingOfficer.setRoleAndResponsibilities("Owner");

        return List.of(individualManagingOfficer);
    }

    private void assertIndividualManagingOfficerDetails(IndividualManagingOfficerAddition individualManagingOfficerAddition) {
        assertEquals("Individual Managing Officer", individualManagingOfficerAddition.getAppointmentType());
        assertEquals(LocalDate.of(2020, 1, 1), individualManagingOfficerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), individualManagingOfficerAddition.getResignedOn());
        assertEquals("Some country", individualManagingOfficerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", individualManagingOfficerAddition.getResidentialAddress().getCountry());
        assertEquals("John", individualManagingOfficerAddition.getPersonName().getForename());
        assertEquals("Doe", individualManagingOfficerAddition.getPersonName().getSurname());
        assertEquals("Some other name", individualManagingOfficerAddition.getFormerNames());
        assertEquals(LocalDate.of(1990, 5, 15), individualManagingOfficerAddition.getBirthDate());
        assertEquals("Irish,Spanish", individualManagingOfficerAddition.getNationalityOther());
        assertEquals("Self employed", individualManagingOfficerAddition.getOccupation());
        assertEquals("Owner", individualManagingOfficerAddition.getRoleAndResponsibilities());
    }

    private List<ManagingOfficerCorporateDto> getCorporateManagingOfficers() {
        var corporateManagingOfficer = new ManagingOfficerCorporateDto();
        corporateManagingOfficer.setStartDate(LocalDate.of(2020, 1, 1));
        corporateManagingOfficer.setResignedOn(LocalDate.of(2023, 1, 1));
        corporateManagingOfficer.setServiceAddress(TEST_ADDRESS_DTO);
        corporateManagingOfficer.setPrincipalAddress(TEST_ADDRESS_DTO);
        corporateManagingOfficer.setName("Some corporate MO");
        corporateManagingOfficer.setContactFullName("John Doe");
        corporateManagingOfficer.setContactEmail("test@email.com");
        corporateManagingOfficer.setRoleAndResponsibilities("Manager");
        corporateManagingOfficer.setLegalForm("Legal form");
        corporateManagingOfficer.setLawGoverned("Governing law");
        corporateManagingOfficer.setPublicRegisterName("Register name");
        corporateManagingOfficer.setRegistrationNumber("Registration number");

        return List.of(corporateManagingOfficer);
    }

    private void assertCorporateManagingOfficerDetails(CorporateManagingOfficerAddition corporateManagingOfficerAddition) {
        assertEquals("Corporate Managing Officer", corporateManagingOfficerAddition.getAppointmentType());
        assertEquals(LocalDate.of(2020, 1, 1), corporateManagingOfficerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), corporateManagingOfficerAddition.getResignedOn());
        assertEquals("Some country", corporateManagingOfficerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", corporateManagingOfficerAddition.getResidentialAddress().getCountry());
        assertEquals("Some corporate MO", corporateManagingOfficerAddition.getName());
        assertEquals("John Doe", corporateManagingOfficerAddition.getContactName());
        assertEquals("test@email.com", corporateManagingOfficerAddition.getContactEmail());
        assertEquals("Manager", corporateManagingOfficerAddition.getRoleAndResponsibilities());
        assertEquals("Legal form", corporateManagingOfficerAddition.getCompanyIdentification().getLegalForm());
        assertEquals("Governing law", corporateManagingOfficerAddition.getCompanyIdentification().getGoverningLaw());
        assertEquals("Register name", corporateManagingOfficerAddition.getCompanyIdentification().getRegisterLocation());
        assertEquals("Registration number", corporateManagingOfficerAddition.getCompanyIdentification().getRegistrationNumber());
    }

    @Test
    void testCorporateManagingOfficerAdditionsAddressSameAsFlagTrue() {
        var corporateManagingOfficers = getCorporateManagingOfficers();
        corporateManagingOfficers.getFirst().setServiceAddress(null);
        corporateManagingOfficers.getFirst().setPrincipalAddress(AddressTestUtils.createDummyAddressDto(
                TEST_RESIDENTIAL_ADDRESS));
        corporateManagingOfficers.getFirst().setServiceAddressSameAsPrincipalAddress(true);

        when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
                .thenReturn(corporateManagingOfficers);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);
        var corporateManagingOfficerAddition = ((CorporateManagingOfficerAddition) additions.getFirst());

        assertEquals(1, additions.size());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), corporateManagingOfficerAddition.getResidentialAddress());
        assertNotNull(corporateManagingOfficerAddition.getServiceAddress());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), corporateManagingOfficerAddition.getServiceAddress());
    }

    @Test
    void testCorporateManagingOfficerAdditionsAddressSameAsFlagFalse() {
        var corporateManagingOfficers = getCorporateManagingOfficers();
        corporateManagingOfficers.getFirst().setServiceAddress(AddressTestUtils.createDummyAddressDto(TEST_SERVICE_ADDRESS));
        corporateManagingOfficers.getFirst().setPrincipalAddress(AddressTestUtils.createDummyAddressDto(
                TEST_RESIDENTIAL_ADDRESS));
        corporateManagingOfficers.getFirst().setServiceAddressSameAsPrincipalAddress(false);

        when(overseasEntitySubmissionDto.getManagingOfficersCorporate())
                .thenReturn(corporateManagingOfficers);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);
        var corporateManagingOfficerAddition = ((CorporateManagingOfficerAddition) additions.getFirst());

        assertEquals(1, additions.size());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), corporateManagingOfficerAddition.getResidentialAddress());
        assertNotEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), corporateManagingOfficerAddition.getServiceAddress());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_SERVICE_ADDRESS), corporateManagingOfficerAddition.getServiceAddress());
    }

    @Test
    void testIndividualManagingOfficerAdditionsAddressSameAsFlagTrue() {
        var individualManagingOfficers = getIndividualManagingOfficers();
        individualManagingOfficers.getFirst().setServiceAddress(null);
        individualManagingOfficers.getFirst().setUsualResidentialAddress(AddressTestUtils.createDummyAddressDto(
                TEST_RESIDENTIAL_ADDRESS));
        individualManagingOfficers.getFirst().setServiceAddressSameAsUsualResidentialAddress(true);

        when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
                .thenReturn(individualManagingOfficers);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);
        var individualManagingOfficerAddition = ((IndividualManagingOfficerAddition) additions.getFirst());

        assertEquals(1, additions.size());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), individualManagingOfficerAddition.getResidentialAddress());
        assertNotNull(individualManagingOfficerAddition.getServiceAddress());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), individualManagingOfficerAddition.getServiceAddress());
    }

    @Test
    void testIndividualManagingOfficerAdditionsAddressSameAsFlagFalse() {
        var individualManagingOfficers = getIndividualManagingOfficers();
        individualManagingOfficers.getFirst().setServiceAddress(AddressTestUtils.createDummyAddressDto(TEST_SERVICE_ADDRESS));
        individualManagingOfficers.getFirst().setUsualResidentialAddress(AddressTestUtils.createDummyAddressDto(
                TEST_RESIDENTIAL_ADDRESS));
        individualManagingOfficers.getFirst().setServiceAddressSameAsUsualResidentialAddress(false);

        when(overseasEntitySubmissionDto.getManagingOfficersIndividual())
                .thenReturn(individualManagingOfficers);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);
        var individualManagingOfficerAddition = ((IndividualManagingOfficerAddition) additions.getFirst());

        assertEquals(1, additions.size());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), individualManagingOfficerAddition.getResidentialAddress());
        assertNotEquals(AddressTestUtils.createDummyAddress(TEST_RESIDENTIAL_ADDRESS), individualManagingOfficerAddition.getServiceAddress());
        assertEquals(AddressTestUtils.createDummyAddress(TEST_SERVICE_ADDRESS), individualManagingOfficerAddition.getServiceAddress());
    }
}
