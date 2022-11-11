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
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

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

    @Test
    void testNoErrorReportedForAllBeneficialOwnersIdentifiedWithOnlyBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForAllBeneficialOwnersIdentifiedWithNoBeneficialOwners() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that all can be identified", MISSING_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testErrorReportedForAllBeneficialOwnersIdentifiedWithBeneficialOwnerAndManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that all can be identified", INCORRECTLY_ADDED_MANAGING_OFFICER), errors);
    }

    @Test
    void testNoErrorReportedForSomeBeneficialOwnersIdentifiedWithOnlyBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    // TODO roe-1640 AC are that at least one BO and at least one MO must be present - if either one is missing an error is logged and returned
    @Test
    void testNoErrorReportedForSomeBeneficialOwnersIdentifiedWithNoBeneficialOwnersAndManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedForSomeBeneficialOwnersIdentifiedWithNoManagingOfficersAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForNoBeneficialOwnersIdentifiedWithNoManagingOfficersWhenSomeAreIdentified() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER + " or " + MISSING_MANAGING_OFFICER), errors);
    }

    @Test
    void testNoErrorReportedForNoBeneficialOwnersIdentifiedWithOnlyManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForNoBeneficialOwnersIdentifiedWithNoManagingOfficersWhenNoneAreIdentified() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", MISSING_MANAGING_OFFICER), errors);
    }

    @Test
    void testErrorReportedForNoBeneficialOwnersIdentifiedWithManagingOfficerAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", INCORRECTLY_ADDED_BENEFICIAL_OWNER), errors);
    }

    @Test
    void testBeneficialOwnerIndividualValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(beneficialOwnerIndividualValidator, times(1)).validate(eq(beneficialOwnerIndividualDtoList), any(), any());
    }

    @Test
    void testBeneficialOwnerCorporateValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(beneficialOwnerCorporateValidator, times(1)).validate(eq(beneficialOwnerCorporateDtoList), any(), any());
    }

    @Test
    void testBeneficialOwnerGovernmentOrPublicAuthorityValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(eq(beneficialOwnerGovernmentOrPublicAuthorityDtoList), any(), any());
    }

    @Test
    void testManagingOfficerIndividualValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(managingOfficerIndividualValidator, times(1)).validate(eq(managingOfficerIndividualDtoList), any(), any());
    }

    @Test
    void testManagingOfficerCorporateValidatorIsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.NONE_IDENTIFIED);
        List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(managingOfficerCorporateValidator, times(1)).validate(eq(managingOfficerCorporateDtoList), any(), any());
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
