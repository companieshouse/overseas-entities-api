package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnerCorporateValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnerGovernmentOrPublicAuthorityValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnerIndividualValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnersStatementValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.ManagingOfficerCorporateValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.ManagingOfficerIndividualValidator;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.OwnersAndOfficersDataBlockValidator.INCORRECTLY_ADDED_BENEFICIAL_OWNER;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.OwnersAndOfficersDataBlockValidator.INCORRECTLY_ADDED_MANAGING_OFFICER;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.OwnersAndOfficersDataBlockValidator.MISSING_BENEFICIAL_OWNER;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.OwnersAndOfficersDataBlockValidator.MISSING_MANAGING_OFFICER;

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
    private EntityDto entityDto = EntityMock.getEntityDto();
    private PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

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

    @Test
    void testErrorReportedForSomeBeneficialOwnersIdentifiedWithNoBeneficialOwnersAndManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER), errors);
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
    void testErrorReportedForNoBeneficialOwnersIdentifiedWithNoManagingOfficers() {
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
        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        List<ManagingOfficerIndividualDto>  managingOfficerIndividualDtoList = new ArrayList<>();
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that none can be identified", INCORRECTLY_ADDED_BENEFICIAL_OWNER), errors);
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
