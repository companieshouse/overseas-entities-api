package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.CorporateEntityBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.LegalPersonBeneficialOwnerAddition;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.*;

@ExtendWith(MockitoExtension.class)
public class BeneficialOwnerAdditionServiceTest {
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private BeneficialOwnerAdditionService beneficialOwnerAdditionService;
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
        beneficialOwnerAdditionService = new BeneficialOwnerAdditionService();
    }

    @Test
    void testBeneficialOwnerAdditions() {
        when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
                .thenReturn(getIndividualBeneficialOwners());
        when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
                .thenReturn(getCorporateBeneficialOwners());
        when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
                .thenReturn(getLegalPersonBeneficialOwners());

        List<Addition> additions =
                beneficialOwnerAdditionService.beneficialOwnerAdditions(overseasEntitySubmissionDto);

        assertEquals(3, additions.size());
        assertIndividualBeneficialOwnerDetails((IndividualBeneficialOwnerAddition) additions.get(0));
        assertCorporateBeneficialOwnerDetails((CorporateEntityBeneficialOwnerAddition) additions.get(1));
        assertLegalPersonBeneficialOwnerDetails((LegalPersonBeneficialOwnerAddition) additions.get(2));
    }

    private List<BeneficialOwnerIndividualDto> getIndividualBeneficialOwners() {
        var individualBeneficialOwner = new BeneficialOwnerIndividualDto();
        individualBeneficialOwner.setStartDate(LocalDate.of(2020, 1, 1));
        individualBeneficialOwner.setCeasedDate(LocalDate.of(2023, 1, 1));
        individualBeneficialOwner.setServiceAddress(TEST_ADDRESS_DTO);
        individualBeneficialOwner.setUsualResidentialAddress(TEST_ADDRESS_DTO);
        individualBeneficialOwner.setBeneficialOwnerNatureOfControlTypes(Arrays.asList(OVER_25_PERCENT_OF_SHARES));
        individualBeneficialOwner.setNonLegalFirmMembersNatureOfControlTypes(Arrays.asList(OVER_25_PERCENT_OF_VOTING_RIGHTS));
        individualBeneficialOwner.setTrusteesNatureOfControlTypes(Arrays.asList(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS));
        individualBeneficialOwner.setFirstName("John");
        individualBeneficialOwner.setLastName("Doe");
        individualBeneficialOwner.setDateOfBirth(LocalDate.of(1990, 5, 15));
        individualBeneficialOwner.setNationality("Irish");
        individualBeneficialOwner.setSecondNationality("Spanish");

        return Arrays.asList(individualBeneficialOwner);
    }

    private void assertIndividualBeneficialOwnerDetails(IndividualBeneficialOwnerAddition individualBeneficialOwnerAddition) {
        assertEquals(LocalDate.of(2020, 1, 1), individualBeneficialOwnerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), individualBeneficialOwnerAddition.getCeasedDate());
        assertEquals("Some country", individualBeneficialOwnerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", individualBeneficialOwnerAddition.getResidentialAddress().getCountry());
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON", individualBeneficialOwnerAddition.getNatureOfControls().get(0));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST", individualBeneficialOwnerAddition.getNatureOfControls().get(1));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM", individualBeneficialOwnerAddition.getNatureOfControls().get(2));
        assertEquals("John", individualBeneficialOwnerAddition.getPersonName().getForename());
        assertEquals("Doe", individualBeneficialOwnerAddition.getPersonName().getSurname());
        assertEquals(LocalDate.of(1990, 5, 15),individualBeneficialOwnerAddition.getBirthDate());
        assertEquals("Irish, Spanish",individualBeneficialOwnerAddition.getNationalityOther());
    }

    private List<BeneficialOwnerCorporateDto> getCorporateBeneficialOwners() {
        var corporateBeneficialOwner = new BeneficialOwnerCorporateDto();
        corporateBeneficialOwner.setStartDate(LocalDate.of(2020, 1, 1));
        corporateBeneficialOwner.setCeasedDate(LocalDate.of(2023, 1, 1));
        corporateBeneficialOwner.setServiceAddress(TEST_ADDRESS_DTO);
        corporateBeneficialOwner.setPrincipalAddress(TEST_ADDRESS_DTO);
        corporateBeneficialOwner.setBeneficialOwnerNatureOfControlTypes(Arrays.asList(OVER_25_PERCENT_OF_SHARES));
        corporateBeneficialOwner.setNonLegalFirmMembersNatureOfControlTypes(Arrays.asList(OVER_25_PERCENT_OF_VOTING_RIGHTS));
        corporateBeneficialOwner.setTrusteesNatureOfControlTypes(Arrays.asList(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS));
        corporateBeneficialOwner.setName("Corporation name");
        corporateBeneficialOwner.setLegalForm("Legal form");
        corporateBeneficialOwner.setLawGoverned("Governing law");
        corporateBeneficialOwner.setPublicRegisterName("Register location");
        corporateBeneficialOwner.setRegistrationNumber("Registration number");

        return Arrays.asList(corporateBeneficialOwner);
    }

    private void assertCorporateBeneficialOwnerDetails(CorporateEntityBeneficialOwnerAddition corporateEntityBeneficialOwnerAddition) {
        assertEquals(LocalDate.of(2020, 1, 1), corporateEntityBeneficialOwnerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), corporateEntityBeneficialOwnerAddition.getCeasedDate());
        assertEquals("Some country", corporateEntityBeneficialOwnerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", corporateEntityBeneficialOwnerAddition.getResidentialAddress().getCountry());
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON", corporateEntityBeneficialOwnerAddition.getNatureOfControls().get(0));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST", corporateEntityBeneficialOwnerAddition.getNatureOfControls().get(1));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM", corporateEntityBeneficialOwnerAddition.getNatureOfControls().get(2));
        assertEquals("Corporation name", corporateEntityBeneficialOwnerAddition.getCorporateName());
        assertEquals("Legal form", corporateEntityBeneficialOwnerAddition.getLegalForm());
        assertEquals("Governing law", corporateEntityBeneficialOwnerAddition.getGoverningLaw());
        assertEquals("Register location", corporateEntityBeneficialOwnerAddition.getRegisterLocation());
        assertEquals("Registration number", corporateEntityBeneficialOwnerAddition.getRegistrationNumber());
    }

    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> getLegalPersonBeneficialOwners() {
        var legalPersonBeneficialOwner = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        legalPersonBeneficialOwner.setStartDate(LocalDate.of(2020, 1, 1));
        legalPersonBeneficialOwner.setCeasedDate(LocalDate.of(2023, 1, 1));
        legalPersonBeneficialOwner.setServiceAddress(TEST_ADDRESS_DTO);
        legalPersonBeneficialOwner.setPrincipalAddress(TEST_ADDRESS_DTO);
        legalPersonBeneficialOwner.setBeneficialOwnerNatureOfControlTypes(Arrays.asList(OVER_25_PERCENT_OF_SHARES));
        legalPersonBeneficialOwner.setNonLegalFirmMembersNatureOfControlTypes(Arrays.asList(OVER_25_PERCENT_OF_VOTING_RIGHTS));
        legalPersonBeneficialOwner.setName("Government Authority");
        legalPersonBeneficialOwner.setLegalForm("Legal form");
        legalPersonBeneficialOwner.setLawGoverned("Governing law");
        legalPersonBeneficialOwner.setName("Government Authority");

        return Arrays.asList(legalPersonBeneficialOwner);
    }

    private void assertLegalPersonBeneficialOwnerDetails(LegalPersonBeneficialOwnerAddition legalPersonBeneficialOwnerAddition) {
        assertEquals(LocalDate.of(2020, 1, 1), legalPersonBeneficialOwnerAddition.getActionDate());
        assertEquals(LocalDate.of(2023, 1, 1), legalPersonBeneficialOwnerAddition.getCeasedDate());
        assertEquals("Some country", legalPersonBeneficialOwnerAddition.getServiceAddress().getCountry());
        assertEquals("Some country", legalPersonBeneficialOwnerAddition.getResidentialAddress().getCountry());
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON", legalPersonBeneficialOwnerAddition.getNatureOfControls().get(0));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM", legalPersonBeneficialOwnerAddition.getNatureOfControls().get(1));
        assertEquals("Government Authority", legalPersonBeneficialOwnerAddition.getCorporateSoleName());
        assertEquals("Legal form", legalPersonBeneficialOwnerAddition.getLegalForm());
        assertEquals("Governing law", legalPersonBeneficialOwnerAddition.getGoverningLaw());
    }
}
