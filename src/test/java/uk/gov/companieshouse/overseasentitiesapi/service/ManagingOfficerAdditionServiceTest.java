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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertIndividualManagingOfficerDetails((IndividualManagingOfficerAddition) additions.get(0));
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
        individualManagingOfficers.get(0).setSecondNationality(null);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(individualManagingOfficers);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(Collections.emptyList());

        List<Addition> additions = managingOfficerAdditionService.managingOfficerAdditions(overseasEntitySubmissionDto);

        assertEquals(1, additions.size());
        assertEquals("Irish", ((IndividualManagingOfficerAddition) additions.get(0)).getNationalityOther());
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
        assertEquals("Managing Officer", individualManagingOfficerAddition.getAppointmentType());
        assertEquals(LocalDate.of(2020, 1, 1), individualManagingOfficerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), individualManagingOfficerAddition.getResignedOn());
        assertEquals("Some country", individualManagingOfficerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", individualManagingOfficerAddition.getResidentialAddress().getCountry());
        assertEquals("John", individualManagingOfficerAddition.getPersonName().getForename());
        assertEquals("Doe", individualManagingOfficerAddition.getPersonName().getSurname());
        assertEquals("Some other name", individualManagingOfficerAddition.getFormerNames());
        assertEquals(LocalDate.of(1990, 5, 15), individualManagingOfficerAddition.getBirthDate());
        assertEquals("Irish, Spanish", individualManagingOfficerAddition.getNationalityOther());
        assertEquals("Self employed", individualManagingOfficerAddition.getOccupation());
        assertEquals("Owner", individualManagingOfficerAddition.getRole());
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
        corporateManagingOfficer.setLegalForm("Legal form");
        corporateManagingOfficer.setLawGoverned("Governing law");
        corporateManagingOfficer.setPublicRegisterName("Register location");
        corporateManagingOfficer.setRegistrationNumber("Registration number");

        return List.of(corporateManagingOfficer);
    }

    private void assertCorporateManagingOfficerDetails(CorporateManagingOfficerAddition corporateManagingOfficerAddition) {
        assertEquals("Managing Officer", corporateManagingOfficerAddition.getAppointmentType());
        assertEquals(LocalDate.of(2020, 1, 1), corporateManagingOfficerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), corporateManagingOfficerAddition.getResignedOn());
        assertEquals("Some country", corporateManagingOfficerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", corporateManagingOfficerAddition.getResidentialAddress().getCountry());
        assertEquals("Some corporate MO", corporateManagingOfficerAddition.getName());
        assertEquals("John Doe", corporateManagingOfficerAddition.getContactName());
        assertEquals("test@email.com", corporateManagingOfficerAddition.getContactEmail());
        assertEquals("Legal form", corporateManagingOfficerAddition.getIdentification().getLegalForm());
        assertEquals("Governing law", corporateManagingOfficerAddition.getIdentification().getGoverningLaw());
        assertEquals("Register location", corporateManagingOfficerAddition.getIdentification().getRegisterLocation());
        assertEquals("Registration number", corporateManagingOfficerAddition.getIdentification().getRegistrationNumber());
    }
}
