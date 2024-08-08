package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.validation.OwnersAndOfficersDataBlockValidator.INCORRECTLY_ADDED_BENEFICIAL_OWNER;
import static uk.gov.companieshouse.overseasentitiesapi.validation.OwnersAndOfficersDataBlockValidator.INCORRECTLY_ADDED_MANAGING_OFFICER;
import static uk.gov.companieshouse.overseasentitiesapi.validation.OwnersAndOfficersDataBlockValidator.MISSING_BENEFICIAL_OWNER;
import static uk.gov.companieshouse.overseasentitiesapi.validation.OwnersAndOfficersDataBlockValidator.MISSING_BENEFICIAL_OWNER_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.validation.OwnersAndOfficersDataBlockValidator.MISSING_MANAGING_OFFICER;

@ExtendWith(MockitoExtension.class)
class OwnersAndOfficersDataBlockValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
    @InjectMocks
    private OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator;
    @Mock
    private BeneficialOwnersStatementValidator beneficialOwnersStatementValidator;
    @Mock
    private BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator;
    @Mock
    private BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator;
    @Mock
    private BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;
    @Mock
    private ManagingOfficerIndividualValidator managingOfficerIndividualValidator;
    @Mock
    private ManagingOfficerCorporateValidator managingOfficerCorporateValidator;

    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private final EntityDto entityDto = EntityMock.getEntityDto();
    private final PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();
    private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();

    @Test
    void testFullValidationNoErrorReportedForAllBeneficialOwnersIdentifiedWithOnlyBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedForAllBeneficialOwnersIdentifiedWithNoBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that all can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationErrorReportedForAllBeneficialOwnersIdentifiedWithCeasedBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto ceasedBo = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        ceasedBo.setCeasedDate(LocalDate.now());
        beneficialOwnerCorporateDtoList.add(ceasedBo);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that all can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationErrorReportedForAllBeneficialOwnersIdentifiedWithCeasedManagingOffice() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        ManagingOfficerIndividualDto ceasedMo = ManagingOfficerMock.getManagingOfficerIndividualDto();
        ceasedMo.setResignedOn(LocalDate.now());
        managingOfficerIndividualDtoList.add(ceasedMo);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedForNoBeneficialOwnersIdentifiedWithCeasedManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        ManagingOfficerIndividualDto ceasedMo = ManagingOfficerMock.getManagingOfficerIndividualDto();
        ceasedMo.setResignedOn(LocalDate.now());
        managingOfficerIndividualDtoList.add(ceasedMo);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", MISSING_MANAGING_OFFICER), errors);
    }

    @Test
    void testFullValidationErrorReportedForNoBeneficialOwnersIdentifiedWithActiveManageOfficerAndActiveBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", INCORRECTLY_ADDED_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationErrorReportedForAllBeneficialOwnersIdentifiedWithBeneficialOwnerAndManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that all can be identified", INCORRECTLY_ADDED_MANAGING_OFFICER), errors);
    }

    @Test
    void testFullValidationNoErrorReportedForAllBeneficialOwnersIdentifiedWithActiveManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        ManagingOfficerIndividualDto ceasedIndividualManagingOfficer = ManagingOfficerMock.getManagingOfficerIndividualDto();
        ceasedIndividualManagingOfficer.setResignedOn(LocalDate.now());
        managingOfficerIndividualDtoList.add(ceasedIndividualManagingOfficer);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeBeneficialOwnersIdentifiedWithOnlyBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);

        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }


    @Test
    void testFullValidationNoErrorReportedForSomeBeneficialOwnersIdentifiedWithCorporateManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeBeneficialOwnersIdentifiedWithCeasedCorporateManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
        ManagingOfficerCorporateDto ceasedCorporateManagingOfficer = ManagingOfficerMock.getManagingOfficerCorporateDto();
        ceasedCorporateManagingOfficer.setResignedOn(LocalDate.now());
        managingOfficerCorporateDtoList.add(ceasedCorporateManagingOfficer);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeBeneficialOwnersIdentifiedWithCeasedIndividualManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        ManagingOfficerIndividualDto ceasedIndividualManagingOfficer = ManagingOfficerMock.getManagingOfficerIndividualDto();
        ceasedIndividualManagingOfficer.setResignedOn(LocalDate.now());
        managingOfficerIndividualDtoList.add(ceasedIndividualManagingOfficer);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeCorporateBeneficialOwnersIdentifiedWithNoManagingOfficersAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeIndividualBeneficialOwnersIdentifiedWithNoManagingOfficersAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerIndividualDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeGovernmentOrPublicAuthorityBeneficialOwnersIdentifiedWithNoManagingOfficersAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedForNoBeneficialOwnersIdentifiedWithNoManagingOfficersWhenSomeAreIdentified() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationErrorReportedForNoActiveCorporateBeneficialOwnersIdentifiedWhenSomeAreIdentified() {
        buildOverseasEntitySubmissionDto();
        BeneficialOwnerCorporateDto corporateBeneficialOwnerDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        corporateBeneficialOwnerDto.setCeasedDate(LocalDate.now());
        List<BeneficialOwnerCorporateDto> corporateBeneficialOwnerDtos = new ArrayList<>();
        corporateBeneficialOwnerDtos.add(corporateBeneficialOwnerDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(corporateBeneficialOwnerDtos);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationErrorReportedForNoActiveIndividualBeneficialOwnersIdentifiedWhenSomeAreIdentified() {
        buildOverseasEntitySubmissionDto();
        BeneficialOwnerIndividualDto individualBeneficialOwnerDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        individualBeneficialOwnerDto.setCeasedDate(LocalDate.now());
        List<BeneficialOwnerIndividualDto> individualBeneficialOwnerDtos = new ArrayList<>();
        individualBeneficialOwnerDtos.add(individualBeneficialOwnerDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(individualBeneficialOwnerDtos);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationErrorReportedForNoActiveGovernmentOrPublicAuthorityBeneficialOwnersIdentifiedWhenSomeAreIdentified() {
        buildOverseasEntitySubmissionDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto governmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        governmentOrPublicAuthorityDto.setCeasedDate(LocalDate.now());
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> governmentOrPublicAuthorityDtos = new ArrayList<>();
        governmentOrPublicAuthorityDtos.add(governmentOrPublicAuthorityDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(governmentOrPublicAuthorityDtos);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }



    @Test
    void testFullValidationNoErrorReportedForNoBeneficialOwnersIdentifiedWithOnlyManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedForNoBeneficialOwnersIdentifiedWithNoManagingOfficersWhenNoneAreIdentified() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", MISSING_MANAGING_OFFICER), errors);
    }

    @Test
    void testFullValidationErrorReportedForNoBeneficialOwnersIdentifiedWithManagingOfficerAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", INCORRECTLY_ADDED_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testFullValidationBeneficialOwnerIndividualValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(beneficialOwnerIndividualValidator, times(1)).validate(eq(beneficialOwnerIndividualDtoList), any(), any());
    }

    @Test
    void testFullValidationBeneficialOwnerCorporateValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(beneficialOwnerCorporateValidator, times(1)).validate(eq(beneficialOwnerCorporateDtoList), any(), any());
    }

    @Test
    void testFullValidationBeneficialOwnerGovernmentOrPublicAuthorityValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(eq(beneficialOwnerGovernmentOrPublicAuthorityDtoList), any(), any());
    }

    @Test
    void testFullValidationManagingOfficerIndividualValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(managingOfficerIndividualValidator, times(1)).validate(eq(managingOfficerIndividualDtoList), any(), any());
    }

    @Test
    void testFullValidationManagingOfficerCorporateValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(managingOfficerCorporateValidator, times(1)).validate(eq(managingOfficerCorporateDtoList), any(), any());
    }

    @Test
    void testFullValidationErrorReportedForNullBeneficialOwnerStatement(){
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(null);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        verify(beneficialOwnersStatementValidator, times(1)).validate(eq(null), any(), any());
        assertError(BENEFICIAL_OWNERS_STATEMENT, MISSING_BENEFICIAL_OWNER_STATEMENT, errors);
    }

    @Test
    void testPartialValidationNoErrorsReportedForPresenterOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialValidationNoErrorsReportedForDueDiligenceOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialValidationNoErrorsReportedForOverseasEntitiesDueDiligenceOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialValidationNoErrorsReportedForEntityOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(entityDto);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialValidationNoErrorsReportedForBeneficialOwnerIndividualOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var beneficialOwnersIndividualList = List.of(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnersIndividualList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(beneficialOwnerIndividualValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForBeneficialOwnerCorporateOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var beneficialOwnersCorporateList = List.of(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnersCorporateList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(beneficialOwnerCorporateValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForBeneficialOwnerGovernmentOrPublicAuthorityOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var beneficialOwnersGovernmentOrPublicAuthorityList = List.of(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnersGovernmentOrPublicAuthorityList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForManagingOfficersIndividualOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var managingOfficersIndividualList = List.of(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficersIndividualList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(managingOfficerIndividualValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForManagingOfficersCorporateOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var managingOfficersCorporateList = List.of(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficersCorporateList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(managingOfficerCorporateValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForMultipleFields() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        var beneficialOwnersGovernmentOrPublicAuthorityList = List.of(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnersGovernmentOrPublicAuthorityList);
        var managingOfficersCorporateList = List.of(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficersCorporateList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateRegistrableBeneficialOwnerWithCeasedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(true);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setChipsReference("TEST REF");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference("TEST REF");
        beneficialOwnerIndividualDto.setChipsReference("TEST REF");
        beneficialOwnerCorporateDto.setCeasedDate(LocalDate.now());
        beneficialOwnerIndividualDto.setCeasedDate(LocalDate.now());
        beneficialOwnerGovernmentOrPublicAuthorityDto.setCeasedDate(LocalDate.now());
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateRegistrableBeneficialOwnerWithAddedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(true);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateNonRegistrableBeneficialOwnerWithExistingBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(false);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setChipsReference("TEST REF");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference("TEST REF");
        beneficialOwnerIndividualDto.setChipsReference("TEST REF");
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateNonRegistrableBeneficialOwnerWithAddedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(false);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testValidateNonRegistrableBeneficialOwnerWithAccidentallyAddedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(false);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setChipsReference("");
        beneficialOwnerIndividualDto.setChipsReference("");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference("");
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testValidateRegistrableBeneficialOwnerWithAccidentallyAddedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(true);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setChipsReference("");
        beneficialOwnerIndividualDto.setChipsReference("");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference("");
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }


    @Test
    void testValidateNonRegistrableBeneficialOwnerWithCeasedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(false);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setChipsReference("TEST");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference("TEST");
        beneficialOwnerIndividualDto.setChipsReference("TEST");
        beneficialOwnerCorporateDto.setCeasedDate(LocalDate.now());
        beneficialOwnerIndividualDto.setCeasedDate(LocalDate.now());
        beneficialOwnerGovernmentOrPublicAuthorityDto.setCeasedDate(LocalDate.now());
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    @Test
    void testValidateRegistrableBeneficialOwnerWithNullCeasedBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(true);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setCeasedDate(null);
        beneficialOwnerIndividualDto.setCeasedDate(null);
        beneficialOwnerGovernmentOrPublicAuthorityDto.setCeasedDate(null);
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateRegistrableBeneficialOwnerWithUndefinedChReferenceBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(true);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerCorporateDto.setChipsReference(null);
        beneficialOwnerIndividualDto.setChipsReference(null);
        beneficialOwnerGovernmentOrPublicAuthorityDto.setChipsReference(null);
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateNonRegistrableBeneficialOwnerWithEmptyBeneficialOwners() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(false);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidateRegistrableBeneficialOwnerWithEmptyBeneficialOwners() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setRegistrableBeneficialOwner(true);
        overseasEntitySubmissionDto.setUpdate(updateDto);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertTrue(errors.hasErrors());
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(fieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
