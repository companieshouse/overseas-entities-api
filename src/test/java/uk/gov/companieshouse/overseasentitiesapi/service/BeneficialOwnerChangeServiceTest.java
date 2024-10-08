package uk.gov.companieshouse.overseasentitiesapi.service;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.test.util.ReflectionTestUtils;
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
import utils.AddressTestUtils;

import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_SHARES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.SCOTLAND;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.ENGLAND_AND_WALES;

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

    private static final String[] DUMMY_ADDRESS = new String[]{"John Doe", "PO Box 123", "42", "Baker Street", "Westminster", "London", "Greater London", "SW1A 2AA", "UK"};

    /**
     * Creates and returns a dummy {@code AddressDto} object with pre-defined data.
     * <p>
     * This method is equivalent to {@code createDummyAddress()} but returns an instance of
     * {@code AddressDto}. The set of data is identical to that in {@code createDummyAddress()}.
     *
     * @return a dummy {@code AddressDto} object with pre-defined data
     */
    private AddressDto createDummyAddressDto() {
        return AddressTestUtils.createDummyAddressDto(DUMMY_ADDRESS);
    }

    /**
     * Creates and returns a dummy {@code AddressDto} object with pre-defined data.
     * <p>
     * This method is equivalent to {@code createDummyAddress()} but returns an instance of
     * {@code AddressDto}. The set of data is identical to that in {@code createDummyAddress()}.
     *
     * @return a dummy {@code AddressDto} object with pre-defined data
     */
    private Address createDummyAddress() {
        return AddressTestUtils.createDummyAddress(DUMMY_ADDRESS);
    }

    private static List<Boolean> booleanWrapperValues() {
        return Arrays.asList(true, false, null);
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
    void testConvertBeneficialOwnerCorporateChangeThroughCollateChangesEnabledFlagFalse() {
        setNewNocsEnabledFeatureFlag(false);
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setName("John Smith");
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());
        beneficialOwnerCorporateDto.setNonLegalFirmMembersNatureOfControlTypes(
                List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));
        beneficialOwnerCorporateDto.setNonLegalFirmControlNatureOfControlTypes(
                List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));


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
            assertTrue(CollectionUtils.isEqualCollection(List.of("OE_SIGINFLUENCECONTROL_AS_FIRM"),
                    corporateBeneficialOwnerChange.getPsc().getNatureOfControls()));
        }
    }


    @Test
    void testConvertBeneficialOwnerCorporateChangeThroughCollateChangesEnabledFlagTrue() {
        setNewNocsEnabledFeatureFlag(true);
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setName("John Smith");
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());
        beneficialOwnerCorporateDto.setNonLegalFirmMembersNatureOfControlTypes(
                List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));
        beneficialOwnerCorporateDto.setNonLegalFirmControlNatureOfControlTypes(
                List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));

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
            assertEquals(List.of("OE_SIGINFLUENCECONTROL_AS_FIRM", "OE_SIGINFLUENCECONTROL_AS_CONTROLOVERFIRM"),
                    corporateBeneficialOwnerChange.getPsc().getNatureOfControls());
        }
    }

    @ParameterizedTest
    @MethodSource("booleanWrapperValues")
    void testCovertBeneficialOwnerCorporateChangeIsAddressSameFlag(
            Boolean serviceAddressSameAsPrincipalAddress) {

        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setPrincipalAddress(createDummyAddressDto());
        beneficialOwnerCorporateDto.setServiceAddressSameAsPrincipalAddress(
                serviceAddressSameAsPrincipalAddress);
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());

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

            assertEquals(createDummyAddress(),
                    corporateBeneficialOwnerChange.getPsc().getResidentialAddress());

            if (serviceAddressSameAsPrincipalAddress == Boolean.TRUE) {
                assertEquals(createDummyAddress(),
                        corporateBeneficialOwnerChange.getPsc().getServiceAddress());
            } else {
                assertNull(corporateBeneficialOwnerChange.getPsc().getServiceAddress());
            }

            assertEquals("123", corporateBeneficialOwnerChange.getAppointmentId());
        }
    }

    @Test
    void testConvertBeneficialOwnerCorporateServiceAddressAndRegistrationAddress() {

        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        String[] serviceAddressData = {"789", "Crescent Lane", "Unit 3A", "Bloomshire", "Lakeview",
                "Canada", "54321", "Alice Smith", "L4M 2C5"};

        beneficialOwnerCorporateDto.setPrincipalAddress(createDummyAddressDto());
        beneficialOwnerCorporateDto.setServiceAddress(AddressTestUtils.createDummyAddressDto(serviceAddressData));
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());

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

            assertEquals(createDummyAddress(),
                    corporateBeneficialOwnerChange.getPsc().getResidentialAddress());
            assertEquals(AddressTestUtils.createDummyAddress(serviceAddressData),
                    corporateBeneficialOwnerChange.getPsc().getServiceAddress());

            assertEquals("123", corporateBeneficialOwnerChange.getAppointmentId());
        }
    }

    @Test
    void testConvertBeneficialOwnerIndividualToChangeThroughCollateChangesEnabledFlagFalse() {
        setNewNocsEnabledFeatureFlag(false);
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567890");
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Doe");
        beneficialOwnerIndividualDto.setNationality("Bangladeshi");
        beneficialOwnerIndividualDto.setSecondNationality("Indonesian");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());
        beneficialOwnerIndividualDto.setNonLegalFirmMembersNatureOfControlTypes(
                List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));
        beneficialOwnerIndividualDto.setNonLegalFirmControlNatureOfControlTypes(
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
            assertTrue(individualBeneficialOwnerChange.getPsc().getIsOnSanctionsList());
            assertEquals("123", individualBeneficialOwnerChange.getAppointmentId());
        }
    }

    @Test
    void testConvertBeneficialOwnerIndividualToChangeThroughCollateChangesEnabledFlagTrue() {
        setNewNocsEnabledFeatureFlag(true);
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567890");
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Doe");
        beneficialOwnerIndividualDto.setNationality("Bangladeshi");
        beneficialOwnerIndividualDto.setSecondNationality("Indonesian");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());
        beneficialOwnerIndividualDto.setNonLegalFirmMembersNatureOfControlTypes(
                List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL));
        beneficialOwnerIndividualDto.setNonLegalFirmControlNatureOfControlTypes(
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
            assertEquals(List.of("OE_SIGINFLUENCECONTROL_AS_FIRM", "OE_SIGINFLUENCECONTROL_AS_CONTROLOVERFIRM"), individualBeneficialOwnerChange.getPsc().getNatureOfControls());
            assertTrue(individualBeneficialOwnerChange.getPsc().getIsOnSanctionsList());
            assertEquals("123", individualBeneficialOwnerChange.getAppointmentId());
        }
    }

    @ParameterizedTest
    @MethodSource("booleanWrapperValues")
    void testConvertBeneficialOwnerIndividualChangeIsAddressSameFlag(
            Boolean serviceAddressSameAsPrincipalAddress) {
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567890");
        beneficialOwnerIndividualDto.setUsualResidentialAddress(createDummyAddressDto());
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());
        beneficialOwnerIndividualDto.setServiceAddressSameAsUsualResidentialAddress(
                serviceAddressSameAsPrincipalAddress);

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

            assertEquals(createDummyAddress(),
                    individualBeneficialOwnerChange.getPsc().getResidentialAddress());

            if (serviceAddressSameAsPrincipalAddress == Boolean.TRUE) {
                assertEquals(createDummyAddress(),
                        individualBeneficialOwnerChange.getPsc().getServiceAddress());
            } else {
                assertNull(individualBeneficialOwnerChange.getPsc().getServiceAddress());
            }

            assertEquals("123", individualBeneficialOwnerChange.getAppointmentId());
        }
    }

    @Test
    void testCovertBeneficialOwnerIndividualChangeServiceAddressAndRegistrationAddress() {

        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567890");
        String[] serviceAddressData = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};

        beneficialOwnerIndividualDto.setUsualResidentialAddress(createDummyAddressDto());
        beneficialOwnerIndividualDto.setServiceAddress(AddressTestUtils.createDummyAddressDto(serviceAddressData));
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

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

            assertEquals(createDummyAddress(),
                    individualBeneficialOwnerChange.getPsc().getResidentialAddress());
            assertEquals(AddressTestUtils.createDummyAddress(serviceAddressData),
                    individualBeneficialOwnerChange.getPsc().getServiceAddress());

            assertEquals("123", individualBeneficialOwnerChange.getAppointmentId());
        }
    }

    @Test
    void testCovertBeneficialOwnerAddressDifferentCasingThroughCollateChanges() {
        String[] fieldNames = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] fieldNamesUpperCase = Arrays.stream(fieldNames).map(String::toUpperCase)
                .toArray(String[]::new);

        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setUsualResidentialAddress(AddressTestUtils.createDummyAddressDto(fieldNames));
        beneficialOwnerIndividualDto.setChipsReference("1234567890");
        beneficialOwnerIndividualDto.setNationality("Bangladeshi");
        beneficialOwnerIndividualDto.setSecondNationality("Indonesian");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

        mockPublicPrivateBoPair.getRight()
                .setUsualResidentialAddress(AddressTestUtils.createDummyModelUtilsAddressApi(fieldNamesUpperCase));
        mockPublicPrivateBoPair.getLeft().setNationality("American");

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPair);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPair);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo, overseasEntitySubmissionDto, new HashMap<>());

        assertEquals(1, result.size());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertInstanceOf(IndividualBeneficialOwnerChange.class, result.get(0));

        if (result.get(0) instanceof IndividualBeneficialOwnerChange) {
            IndividualBeneficialOwnerChange individualBeneficialOwnerChange = (IndividualBeneficialOwnerChange) result.get(
                    0);
            assertNull(individualBeneficialOwnerChange.getPsc().getResidentialAddress());
        }
    }


    @Test
    void testCovertBeneficialOwnerOtherChangeThroughCollateChangesEnabledFlagFalse() {
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
            assertEquals("123",
                    governmentOrPublicAuthorityBeneficialOwnerChange.getAppointmentId());
        }
    }

    @ParameterizedTest
    @MethodSource("booleanWrapperValues")
    void testCovertBeneficialOwnerOtherChangeIsAddressSameFlag(
            Boolean serviceAddressSameAsPrincipalAddress) {
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerOtherDto.setPrincipalAddress(createDummyAddressDto());
        beneficialOwnerOtherDto.setChipsReference("1234567890");
        beneficialOwnerOtherDto.setServiceAddressSameAsPrincipalAddress(
                serviceAddressSameAsPrincipalAddress);

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

            assertEquals(createDummyAddress(),
                    governmentOrPublicAuthorityBeneficialOwnerChange.getPsc()
                            .getResidentialAddress());

            if (serviceAddressSameAsPrincipalAddress == Boolean.TRUE) {
                assertEquals(createDummyAddress(),
                        governmentOrPublicAuthorityBeneficialOwnerChange.getPsc()
                                .getServiceAddress());
            } else {
                assertNull(governmentOrPublicAuthorityBeneficialOwnerChange.getPsc()
                        .getServiceAddress());
            }

            assertEquals("123",
                    governmentOrPublicAuthorityBeneficialOwnerChange.getAppointmentId());
        }
    }

    @Test
    void testCovertBeneficialOwnerOtherChangeServiceAddressAndRegistrationAddress() {

        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerOtherDto.setChipsReference("1234567890");
        String[] serviceAddressData = {"789", "Crescent Lane", "Unit 3A", "Bloomshire", "Lakeview",
                "Canada", "54321", "Alice Smith", "L4M 2C5"};

        beneficialOwnerOtherDto.setPrincipalAddress(createDummyAddressDto());
        beneficialOwnerOtherDto.setServiceAddress(AddressTestUtils.createDummyAddressDto(serviceAddressData));

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

            assertEquals(createDummyAddress(),
                    governmentOrPublicAuthorityBeneficialOwnerChange.getPsc()
                            .getResidentialAddress());
            assertEquals(AddressTestUtils.createDummyAddress(serviceAddressData),
                    governmentOrPublicAuthorityBeneficialOwnerChange.getPsc().getServiceAddress());

            assertEquals("123",
                    governmentOrPublicAuthorityBeneficialOwnerChange.getAppointmentId());
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
                    governmentOrPublicAuthorityBeneficialOwnerChange.getPsc()
                            .getCompanyIdentification()
                            .getLegalForm());
        }
    }

    @Test
    void testCollateAllBeneficialOwnerChanges() {
        List<String> trustIds = List.of("1","2","3");
        // setup corporate DTO
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setName("John Smith");
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setTrustIds(trustIds);

        // setup individual DTO
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Doe");
        beneficialOwnerIndividualDto.setTrustIds(trustIds);

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
        if (result.get(0) instanceof IndividualBeneficialOwnerChange ) {
                IndividualBeneficialOwnerChange corporateBeneficialOwnerChange =  (IndividualBeneficialOwnerChange) result.get(0);
                assertEquals(corporateBeneficialOwnerChange.getPsc().getAddedTrustIds(), trustIds);
        }
        if (result.get(2) instanceof CorporateBeneficialOwnerChange ) {
                CorporateBeneficialOwnerChange corporateBeneficialOwnerChange =  (CorporateBeneficialOwnerChange) result.get(2);
                assertEquals(corporateBeneficialOwnerChange.getPsc().getAddedTrustIds(), trustIds);
        }
    }

    @Test
    void testCollateAllBeneficialOwnerOtherChangesWithAllNocs() {
        setNewNocsEnabledFeatureFlag(true);

        List<String> trustIds = List.of("1","2","3");
        // setup corporate DTO
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setName("John Smith");
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setTrustIds(trustIds);

        // setup individual DTO
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Doe");
        beneficialOwnerIndividualDto.setTrustIds(trustIds);

        // setup other DTO
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerOtherDto.setName("John Doe");
        beneficialOwnerOtherDto.setChipsReference("1234567892");
        beneficialOwnerOtherDto.setBeneficialOwnerNatureOfControlTypes(List.of(OVER_25_PERCENT_OF_SHARES));
        beneficialOwnerOtherDto.setNonLegalFirmMembersNatureOfControlTypes(List.of(OVER_25_PERCENT_OF_VOTING_RIGHTS));
        beneficialOwnerOtherDto.setTrustControlNatureOfControlTypes(List.of(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS));
        beneficialOwnerOtherDto.setOwnerOfLandPersonNatureOfControlJurisdictions(List.of(SCOTLAND));
        beneficialOwnerOtherDto.setOwnerOfLandOtherEntityNatureOfControlJurisdictions(List.of(ENGLAND_AND_WALES));
        beneficialOwnerOtherDto.setNonLegalFirmControlNatureOfControlTypes(List.of(SIGNIFICANT_INFLUENCE_OR_CONTROL));

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

        OtherBeneficialOwnerChange otherBeneficialOwnerChange = (OtherBeneficialOwnerChange) result.get(1);
        List<String> nocs = otherBeneficialOwnerChange.getPsc().getNatureOfControls();

        assertEquals(6, nocs.size());

        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON", nocs.get(0));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM", nocs.get(1));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERTRUST", nocs.get(2));
        assertEquals("OE_REGOWNER_AS_NOMINEEPERSON_SCOTLAND", nocs.get(3));
        assertEquals("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_ENGLANDWALES", nocs.get(4));
        assertEquals("OE_SIGINFLUENCECONTROL_AS_CONTROLOVERFIRM", nocs.get(5));
    }

    @Test
    void testCollateAllBeneficialOwnerChangesProducesNoLogsIfNoChipsReference() {
        // setup corporate DTO
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setName("John Smith");
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());

        // setup individual DTO
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Doe");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

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
    void testCollateAllBeneficialOwnerChangesProducesLogsIfPairIsNull() {
        // setup corporate DTO
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setName("John Smith");
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());

        // setup individual DTO
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Doe");
        
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

        // setup other DTO
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerOtherDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerOtherDto.setName("John Doe");
        beneficialOwnerOtherDto.setChipsReference("1234567892");

        when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(null);
        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                null);
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

        assertEquals(3,
                StringUtils.countMatches(outputStreamCaptor.toString(),
                        "No public and no private data found for beneficial owner"));
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
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString().contains(
                "No private data found for beneficial owner - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesIndividualLeftOfPairNull() {
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setFirstName("John");
        beneficialOwnerIndividualDto.setLastName("Smith");
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setTrustIds(null);

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPairLeftNull);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));
        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString()
                .contains("No public data found for beneficial owner - continuing with changes"));
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
        beneficialOwnerCorporateDto.setTrustIds(null);

        when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPairLeftNull);
        when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
                List.of(beneficialOwnerCorporateDto));

        System.setOut(new PrintStream(outputStreamCaptor));
        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString()
                .contains("No public data found for beneficial owner - continuing with changes"));
        assertEquals(1, result.size());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertInstanceOf(CorporateBeneficialOwnerChange.class, result.get(0));

        if (result.get(0) instanceof CorporateBeneficialOwnerChange) {
            CorporateBeneficialOwnerChange corporateBeneficialOwnerChange = (CorporateBeneficialOwnerChange) result.get(
                    0);
            assertEquals("John Smith Corp",
                    corporateBeneficialOwnerChange.getPsc().getCorporateName());
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
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString()
                .contains("No public data found for beneficial owner - continuing with changes"));
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
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPairLeftNull);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));
        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString()
                .contains("No public data found for beneficial owner - continuing with changes"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesIndividualRightOfPairNull() {
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPairRightNull);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));
        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString().contains(
                "No private data found for beneficial owner - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesCorporateRightOfPairNull() {
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setChipsReference("1234567890");
        beneficialOwnerCorporateDto.setTrustIds(new ArrayList<>());

        beneficialOwnerCorporateDto.setPrincipalAddress(createDummyAddressDto());

        when(publicPrivateBo.get(beneficialOwnerCorporateDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPairRightNull);
        when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()).thenReturn(
                List.of(beneficialOwnerCorporateDto));

        System.setOut(new PrintStream(outputStreamCaptor));
        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString().contains(
                "No private data found for beneficial owner - changes cannot be created"));
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
                publicPrivateBo,
                overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString().contains(
                "No private data found for beneficial owner - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesLeftAndRightOfPairNull() {
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                mockPublicPrivateBoPairBothNull);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo, overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString()
                .contains("No public data found for beneficial owner - continuing with changes"));
        assertTrue(outputStreamCaptor.toString().contains(
                "No private data found for beneficial owner - changes cannot be created"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesPublicPrivateDataNull() {
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567891");
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

        when(publicPrivateBo.get(beneficialOwnerIndividualDto.getChipsReference())).thenReturn(
                null);
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()).thenReturn(
                List.of(beneficialOwnerIndividualDto));

        System.setOut(new PrintStream(outputStreamCaptor));

        List<Change> result = beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                publicPrivateBo, overseasEntitySubmissionDto, logMap);

        assertTrue(outputStreamCaptor.toString()
                .contains("No public and no private data found for beneficial owner"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCollateBeneficialOwnerChangesInvalidData() {
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setChipsReference("1234567890");
        beneficialOwnerIndividualDto.setFirstName(null);
        beneficialOwnerIndividualDto.setLastName(null);
        beneficialOwnerIndividualDto.setTrustIds(new ArrayList<>());

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

    private void setNewNocsEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(beneficialOwnerChangeService, "isPropertyAndLandNocEnabled", value);
    }
}
