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
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.Arrays;
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

    // TODO roe-1640 AC are that at least one BO and at least one MO must be present - if either one is missing an error is logged and returned
    @Test
    void testFullValidationNoErrorReportedForSomeBeneficialOwnersIdentifiedWithNoBeneficialOwnersAndManagingOfficer() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationNoErrorReportedForSomeBeneficialOwnersIdentifiedWithNoManagingOfficersAndBeneficialOwner() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedForNoBeneficialOwnersIdentifiedWithNoManagingOfficersWhenSomeAreIdentified() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(BeneficialOwnersStatementType.SOME_IDENTIFIED_ALL_DETAILS);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertError(BENEFICIAL_OWNERS_STATEMENT, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER + " or " + MISSING_MANAGING_OFFICER), errors);
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
        var beneficialOwnersIndividualList = Arrays.asList(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnersIndividualList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(beneficialOwnerIndividualValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForBeneficialOwnerCorporateOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var beneficialOwnersCorporateList = Arrays.asList(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnersCorporateList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(beneficialOwnerCorporateValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForBeneficialOwnerGovernmentOrPublicAuthorityOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var beneficialOwnersGovernmentOrPublicAuthorityList = Arrays.asList(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnersGovernmentOrPublicAuthorityList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(beneficialOwnerGovernmentOrPublicAuthorityValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForManagingOfficersIndividualOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var managingOfficersIndividualList = Arrays.asList(ManagingOfficerMock.getManagingOfficerIndividualDto());
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficersIndividualList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(managingOfficerIndividualValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationNoErrorsReportedForManagingOfficersCorporateOnly() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var managingOfficersCorporateList = Arrays.asList(ManagingOfficerMock.getManagingOfficerCorporateDto());
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
        var beneficialOwnersGovernmentOrPublicAuthorityList = Arrays.asList(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnersGovernmentOrPublicAuthorityList);
        var managingOfficersCorporateList = Arrays.asList(ManagingOfficerMock.getManagingOfficerCorporateDto());
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficersCorporateList);
        Errors errors = new Errors();
        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
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
