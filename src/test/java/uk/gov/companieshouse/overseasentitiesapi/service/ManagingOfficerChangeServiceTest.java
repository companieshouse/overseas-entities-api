package uk.gov.companieshouse.overseasentitiesapi.service;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.AddressTestUtils.createDummyCommonAddress;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.common.ContactDetails;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;
import uk.gov.companieshouse.api.model.officers.IdentificationApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.CorporateManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.IndividualManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import utils.AddressTestUtils;

class ManagingOfficerChangeServiceTest {

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
    Pair<CompanyOfficerApi, ManagingOfficerDataApi> mockPublicPrivateMoPair2;

    @Mock
    Pair<CompanyOfficerApi, ManagingOfficerDataApi> mockPublicPrivateMoPairLeftNull;

    @Mock
    Pair<CompanyOfficerApi, ManagingOfficerDataApi> mockPublicPrivateMoPairRightNull;

    Map<String, Object> logMap = new HashMap<>();

    ByteArrayOutputStream outputStreamCaptor;

    private static final String[] DEFAULT_ADDRESS = {"John Doe", "98765", "123", " Main Street", "Apartment 4B",
            "Cityville", "Countyshire", "AB12 3CD", "United Kingdom"};

    private static AddressDto createDummyAddressDto() {
        return AddressTestUtils.createDummyAddressDto(DEFAULT_ADDRESS);
    }

    private static Address createDummyAddress() {
        return AddressTestUtils.createDummyAddress(DEFAULT_ADDRESS);
    }


    private static List<Boolean> booleanWrapperValues() {
        return Arrays.asList(true, false, null);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockedIdentificationApi = new IdentificationApi();
        mockedIdentificationApi.setPlaceRegistered("London");
        mockedIdentificationApi.setLegalAuthority("UK Law");
        mockedIdentificationApi.setRegistrationNumber("123456");
        mockedIdentificationApi.setLegalForm("Private Limited");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = mock(CompanyOfficerApi.class);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);
        when(mockLeftPart.getIdentification()).thenReturn(mockedIdentificationApi);

        CompanyOfficerApi mockLeftPart2 = mock(CompanyOfficerApi.class);

        ManagingOfficerDataApi mockRightPart2 = new ManagingOfficerDataApi();
        when(mockPublicPrivateMoPair2.getRight()).thenReturn(mockRightPart2);
        when(mockPublicPrivateMoPair2.getLeft()).thenReturn(mockLeftPart2);
        when(mockLeftPart2.getIdentification()).thenReturn(mockedIdentificationApi);

        when(mockPublicPrivateMoPairLeftNull.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPairLeftNull.getLeft()).thenReturn(null);

        when(mockPublicPrivateMoPairRightNull.getRight()).thenReturn(null);
        when(mockPublicPrivateMoPairRightNull.getLeft()).thenReturn(mockLeftPart);

        managingOfficerChangeService = new ManagingOfficerChangeService();

        outputStreamCaptor = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
        outputStreamCaptor.reset();
        System.setOut(System.out);
    }

    @Test
    void testCollateManagingOfficerChanges() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("John Smith Corp");
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
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(IndividualManagingOfficerChange.class::isInstance));
        assertTrue(result.stream().anyMatch(CorporateManagingOfficerChange.class::isInstance));
    }

    @Test
    void testCollateIndManagingOfficerNameFormerNameNoChange() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");
        managingOfficerIndividualDto.setFormerNames("Darryl Jenkins");
        managingOfficerIndividualDto.setNationality("Irish");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = new CompanyOfficerApi();
        mockLeftPart.setName("DOE, John");
        mockLeftPart.setNationality("Irish,");
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("DARRYL JENKINS");
                }}
        );
        mockLeftPart.setFormerNames(formerNamesApiList);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);


        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateIndManagingOfficerNationalityNoChange() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");
        managingOfficerIndividualDto.setFormerNames("Darryl Jenkins");
        managingOfficerIndividualDto.setNationality("Irish");
        managingOfficerIndividualDto.setSecondNationality("Danish");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = new CompanyOfficerApi();
        mockLeftPart.setName("DOE, John");
        mockLeftPart.setNationality("Irish,Danish");
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("DARRYL JENKINS");
                }}
        );
        mockLeftPart.setFormerNames(formerNamesApiList);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);


        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateIndManagingOfficerNationalityChange() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");
        managingOfficerIndividualDto.setNationality("German");
        managingOfficerIndividualDto.setSecondNationality("Irish");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = new CompanyOfficerApi();
        mockLeftPart.setName("DOE, John");
        mockLeftPart.setNationality("Polish,Italian");


        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);


        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);
        IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                0);

        assertNotNull(result);
        assertEquals("German,Irish",individualManagingOfficerChange.getOfficer().getNationalityOther());
    }

    @Test
    void testCollateIndManagingOfficerFormerNameChange() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");
        managingOfficerIndividualDto.setFormerNames("Darryl Joe");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = new CompanyOfficerApi();
        mockLeftPart.setName("DOE, John");
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("DARRYL JENKINS");
                }}
        );
        mockLeftPart.setFormerNames(formerNamesApiList);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);


        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);
        IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                0);

        assertNotNull(result);
        assertEquals("Darryl Joe",individualManagingOfficerChange.getOfficer().getFormerNames());
    }

    @Test
    void testCollateIndManagingOfficerNameChange() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("DOE");
        managingOfficerIndividualDto.setFormerNames("Darryl Jenkins");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        CompanyOfficerApi mockLeftPart = new CompanyOfficerApi();
        mockLeftPart.setName("DOE, Jane");
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("DARRYL JENKINS");
                }}
        );
        mockLeftPart.setFormerNames(formerNamesApiList);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);


        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);
        IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                0);

        assertNotNull(result);
        assertEquals(new PersonName("John", "DOE"),
                individualManagingOfficerChange.getOfficer().getPersonName());
    }

    @Test
    void testCollateCorporateManagingOfficerNameNoChange() {
        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("John Smith Corp");
        managingOfficerCorporateDto.setChipsReference("1234567890");
        managingOfficerCorporateDto.setContactEmail("test@mail.com");
        managingOfficerCorporateDto.setContactFullName("John Smith");

        ManagingOfficerDataApi mockRightPart = new ManagingOfficerDataApi();
        mockRightPart.setManagingOfficerAppointmentId("123");
        mockRightPart.setContactNameFull("JOHN SMITH");
        mockRightPart.setContactEmailAddress("TEST@MAIL.COM");

        CompanyOfficerApi mockLeftPart = new CompanyOfficerApi();
        mockLeftPart.setName("JOHN SMITH CORP");
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setContactName("JOHN SMITH");
        mockLeftPart.setContactDetails(contactDetails);

        when(mockPublicPrivateMoPair.getRight()).thenReturn(mockRightPart);
        when(mockPublicPrivateMoPair.getLeft()).thenReturn(mockLeftPart);


        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesNoChanges() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
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
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesAddressesTheSame() {
        String[] individualServiceAddressArray = {"Jane Smith", "54321",
                "individualServiceAddressArray", "Broadway", "Apartment 2C", "Townsville",
                "Countyville", "XY98 7ZW", "United Kingdom"};
        String[] individualResidentialAddressArray = {"Alice Johnson", "12345",
                "individualResidentialAddressArray", "Oak Street", "Apartment 3D", "Springfield",
                "Stateville", "98765", "United States"};

        String[] corporateServiceAddressArray = {"ACME Corporation", "7890",
                "corporateServiceAddressArray", "Corporate Avenue", "Floor 10", "Business City",
                "Countyshire", "EF34 5GH", "United Kingdom"};
        String[] corporatePrincipalAddressArray = {"XYZ Corporation", "45678",
                "corporatePrincipalAddressArray", "Park Avenue", "Floor 25", "Metropolis City",
                "Countyshire", "AB12 3CD", "United States"};

        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setServiceAddress(
                AddressTestUtils.createDummyAddressDto(individualServiceAddressArray));
        managingOfficerIndividualDto.setUsualResidentialAddress(
                AddressTestUtils.createDummyAddressDto(individualResidentialAddressArray));

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setChipsReference("1234567890");
        managingOfficerCorporateDto.setServiceAddress(
                AddressTestUtils.createDummyAddressDto(corporateServiceAddressArray));
        managingOfficerCorporateDto.setPrincipalAddress(
                AddressTestUtils.createDummyAddressDto(corporatePrincipalAddressArray));

        when(mockPublicPrivateMoPair.getLeft().getAddress()).thenReturn(
                createDummyCommonAddress(individualServiceAddressArray));
        mockPublicPrivateMoPair.getRight()
                .setResidentialAddress(AddressTestUtils.createDummyManagingOfficerAddressApi(individualResidentialAddressArray));

        when(mockPublicPrivateMoPair2.getLeft().getAddress()).thenReturn(
                createDummyCommonAddress(corporateServiceAddressArray));
        when(mockPublicPrivateMoPair2.getLeft().getPrincipalOfficeAddress()).thenReturn(
                createDummyCommonAddress(corporatePrincipalAddressArray));

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair2);

        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesProducesNoLogsIfNoChipsReference() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("John Smith Corp");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals("", outputStreamCaptor.toString());
        assertEquals(0, result.size());
    }

    @Test
    void testCollateManagingOfficerChangesProducesLogsIfPairIsNull() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("John Smith");
        managingOfficerCorporateDto.setChipsReference("1234567890");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                null);
        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                null);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(2, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No public and no private data found for managing officer"));
        assertEquals(0, result.size());
    }

    @Test
    void testCollateManagingOfficerChangesEmptyListReturnsEmpty() {
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                Collections.emptyList());
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                Collections.emptyList());

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesEmptyRightOfPairNull() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPairRightNull);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No private data found for managing officer - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesIndividualLeftOfPairNull() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");
        managingOfficerIndividualDto.setChipsReference("1234567891");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPairLeftNull);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No public data found for managing officer - continuing with changes"));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(IndividualManagingOfficerChange.class, result.get(0));

        IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                0);
        assertEquals(new PersonName("John", "Doe"),
                individualManagingOfficerChange.getOfficer().getPersonName());
    }

    @Test
    void testCollateManagingOfficerChangesCorporateLeftOfPairNull() {
        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("John Smith Corp");
        managingOfficerCorporateDto.setChipsReference("1234567891");

        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPairLeftNull);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No public data found for managing officer - continuing with changes"));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(CorporateManagingOfficerChange.class, result.get(0));

        CorporateManagingOfficerChange corporateManagingOfficerChange = (CorporateManagingOfficerChange) result.get(
                0);
        assertEquals("John Smith Corp", corporateManagingOfficerChange.getOfficer().getName());
    }

    @Test
    void testCollateManagingOfficerChangesEmptyLeftOfPairNull() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPairLeftNull);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));
        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No public data found for managing officer - continuing with changes"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesIndividualRightOfPairNull() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567891");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPairRightNull);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No private data found for managing officer - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesCorporateRightOfPairNull() {
        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setChipsReference("1234567890");

        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPairRightNull);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, StringUtils.countMatches(outputStreamCaptor.toString(),
                "No private data found for managing officer - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateManagingOfficerChangesInvalidData() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567890");
        managingOfficerIndividualDto.setFirstName(null);
        managingOfficerIndividualDto.setLastName(null);

        mockPublicPrivateMoPair.getLeft().setName("John Doe");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertTrue(result.isEmpty());
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
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(IndividualManagingOfficerChange.class, result.get(0));

        IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                0);
        assertEquals("Individual Managing Officer",
                individualManagingOfficerChange.getAppointmentType());
        assertEquals("123", individualManagingOfficerChange.getAppointmentId());
        assertEquals(new PersonName("John", "Doe"),
                individualManagingOfficerChange.getOfficer().getPersonName());
        assertEquals("Bangladeshi,Indonesian",
                individualManagingOfficerChange.getOfficer().getNationalityOther());
        assertEquals("2022-01-01",
                individualManagingOfficerChange.getOfficer().getStartDate().toString());
        assertEquals("Jonathan Doe, Johnny Doe",
                individualManagingOfficerChange.getOfficer().getFormerNames());
        assertEquals("Occupation", individualManagingOfficerChange.getOfficer().getOccupation());
        assertEquals("Role",
                individualManagingOfficerChange.getOfficer().getRoleAndResponsibilities());
        assertEquals("123",
                individualManagingOfficerChange.getOfficer().getServiceAddress().getHouseNameNum());
        assertEquals("123", individualManagingOfficerChange.getOfficer().getResidentialAddress()
                .getHouseNameNum());
    }


    @Test
    void testConvertManagingOfficerIndividualAppointedOn() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567890");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName("Doe");

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(mockPublicPrivateMoPair.getLeft().getAppointedOn()).thenReturn(
                LocalDate.of(2021, 1, 1));

        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(IndividualManagingOfficerChange.class, result.get(0));

        IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                0);
        assertNull(individualManagingOfficerChange.getOfficer().getStartDate());
    }

    @Test
    void testCollateBeneficialOwnerChangesCorporateAppointedOn() {
        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setChipsReference("1234567890");
        managingOfficerCorporateDto.setName("John Doe");

        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(mockPublicPrivateMoPair.getLeft().getAppointedOn()).thenReturn(
                LocalDate.of(2021, 1, 1));

        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(CorporateManagingOfficerChange.class, result.get(0));

        CorporateManagingOfficerChange corporateManagingOfficerChange = (CorporateManagingOfficerChange) result.get(
                0);
        assertNull(corporateManagingOfficerChange.getOfficer().getStartDate());
    }

    @Test
    void testConvertManagingOfficerLastNameNull() {
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567890");
        managingOfficerIndividualDto.setFirstName("John");
        managingOfficerIndividualDto.setLastName(null);

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);

        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @MethodSource("booleanWrapperValues")
    void testCovertBeneficialOwnerCorporateChangeIsAddressSameFlag(
            Boolean serviceAddressSameAsPrincipalAddress) {

        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setChipsReference("1234567890");
        managingOfficerIndividualDto.setUsualResidentialAddress(createDummyAddressDto());
        managingOfficerIndividualDto.setServiceAddressSameAsUsualResidentialAddress(
                serviceAddressSameAsPrincipalAddress);

        when(publicPrivateMo.get(managingOfficerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersIndividual()).thenReturn(
                List.of(managingOfficerIndividualDto));
        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, result.size());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertInstanceOf(IndividualManagingOfficerChange.class, result.get(0));

        if (result.get(0) instanceof IndividualManagingOfficerChange) {
            IndividualManagingOfficerChange individualManagingOfficerChange = (IndividualManagingOfficerChange) result.get(
                    0);

            assertEquals(createDummyAddress(),
                    individualManagingOfficerChange.getOfficer().getResidentialAddress());

            if (serviceAddressSameAsPrincipalAddress == Boolean.TRUE) {
                assertEquals(createDummyAddress(),
                        individualManagingOfficerChange.getOfficer().getServiceAddress());
            } else {
                assertNull(individualManagingOfficerChange.getOfficer().getServiceAddress());
            }

            assertEquals("123", individualManagingOfficerChange.getAppointmentId());
        }
    }

    @ParameterizedTest
    @MethodSource("booleanWrapperValues")
    void testConvertManagingOfficerCorporateToChangeIsAddressSameFlag(
            Boolean serviceAddressSameAsPrincipalAddress) {

        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setChipsReference("1234567890");
        managingOfficerCorporateDto.setPrincipalAddress(createDummyAddressDto());
        managingOfficerCorporateDto.setServiceAddressSameAsPrincipalAddress(
                serviceAddressSameAsPrincipalAddress);

        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));
        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertEquals(1, result.size());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertInstanceOf(CorporateManagingOfficerChange.class, result.get(0));

        if (result.get(0) instanceof CorporateManagingOfficerChange) {
            CorporateManagingOfficerChange corporateManagingOfficerChange = (CorporateManagingOfficerChange) result.get(
                    0);

            assertEquals(createDummyAddress(), corporateManagingOfficerChange.getOfficer().getRegisteredOffice());

            if (serviceAddressSameAsPrincipalAddress == Boolean.TRUE) {
                assertEquals(createDummyAddress(),
                        corporateManagingOfficerChange.getOfficer().getServiceAddress());
            } else {
                assertNull(corporateManagingOfficerChange.getOfficer().getServiceAddress());
            }

            assertEquals("123", corporateManagingOfficerChange.getAppointmentId());
        }
    }

    @Test
    void testConvertManagingOfficerCorporateToChangeThroughCollateChanges() {
        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setChipsReference("1234567890");
        managingOfficerCorporateDto.setName("John Smith Corp");
        managingOfficerCorporateDto.setServiceAddress(createDummyAddressDto());
        managingOfficerCorporateDto.setPrincipalAddress(createDummyAddressDto());
        managingOfficerCorporateDto.setContactFullName("Contact name");
        managingOfficerCorporateDto.setContactEmail("contact@test.com");
        managingOfficerCorporateDto.setStartDate(LocalDate.of(2022, 1, 1));
        managingOfficerCorporateDto.setRoleAndResponsibilities("Role");
        managingOfficerCorporateDto.setLegalForm("Legal form");
        managingOfficerCorporateDto.setLawGoverned("Law governed");
        managingOfficerCorporateDto.setRegistrationNumber("1234");
        managingOfficerCorporateDto.setPublicRegisterName("Public register name");

        when(publicPrivateMo.get(managingOfficerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateMoPair);
        when(overseasEntitySubmissionDto.getManagingOfficersCorporate()).thenReturn(
                List.of(managingOfficerCorporateDto));

        List<Change> result = managingOfficerChangeService.collateManagingOfficerChanges(
                publicPrivateMo, overseasEntitySubmissionDto, logMap);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertInstanceOf(CorporateManagingOfficerChange.class, result.get(0));

        CorporateManagingOfficerChange corporateManagingOfficerChange = (CorporateManagingOfficerChange) result.get(
                0);
        assertEquals("Corporate Managing Officer",
                corporateManagingOfficerChange.getAppointmentType());
        assertEquals("123", corporateManagingOfficerChange.getAppointmentId());
        assertEquals("John Smith Corp", corporateManagingOfficerChange.getOfficer().getName());
        assertEquals("Contact name", corporateManagingOfficerChange.getOfficer().getContactName());
        assertEquals("contact@test.com", corporateManagingOfficerChange.getOfficer().getEmail());
        assertEquals("2022-01-01",
                corporateManagingOfficerChange.getOfficer().getStartDate().toString());
        assertEquals("Role",
                corporateManagingOfficerChange.getOfficer().getRoleAndResponsibilities());
        assertEquals("Legal form",
                corporateManagingOfficerChange.getOfficer().getCompanyIdentification()
                        .getLegalForm());
        assertEquals("Law governed",
                corporateManagingOfficerChange.getOfficer().getCompanyIdentification()
                        .getGoverningLaw());
        assertEquals("1234", corporateManagingOfficerChange.getOfficer().getCompanyIdentification()
                .getRegistrationNumber());
        assertEquals("Public register name",
                corporateManagingOfficerChange.getOfficer().getCompanyIdentification()
                        .getRegisterLocation());
    }
}
