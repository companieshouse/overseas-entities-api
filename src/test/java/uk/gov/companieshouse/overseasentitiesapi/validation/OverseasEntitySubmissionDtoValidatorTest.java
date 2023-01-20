package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
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
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER_FIELD;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
    @InjectMocks
    private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;

    @Mock
    private EntityDtoValidator entityDtoValidator;
    @Mock
    private PresenterDtoValidator presenterDtoValidator;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    @Mock
    private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
    @Mock
    private OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator;

    private final String entityName = "ABC Entity";
    private final EntityDto entityDto = EntityMock.getEntityDto();
    private final PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
    private final BeneficialOwnersStatementType beneficialOwnersStatement = BeneficialOwnersStatementType.NONE_IDENTIFIED;
    private final List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
    private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();
    {
        beneficialOwnerIndividualDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto());
    };
    private final List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = new ArrayList<>();
    {
        beneficialOwnerCorporateDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto());
    };
    private final List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
    {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto());
    };

    private final List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList = new ArrayList<>();
    {
        managingOfficerIndividualDtoList.add(ManagingOfficerMock.getManagingOfficerIndividualDto());
    };
    private final List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList = new ArrayList<>();
    {
        managingOfficerCorporateDtoList.add(ManagingOfficerMock.getManagingOfficerCorporateDto());
    };

    @Test
    void testOverseasEntitySubmissionValidatorWithDueDiligence() {

        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto),any(),any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testOverseasEntitySubmissionValidatorWithOverseasEntityDueDiligence() {

        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        verify(entityDtoValidator, times(1)).validate(eq(entityDto),any(),any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto),any(),any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto),any(),any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersIndividualAreNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersIndividualAreEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersCorporateAreNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersCorporateAreEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersGovernmentOrPublicAuthorityAreNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersGovernmentOrPublicAuthorityAreEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersIndividualAreNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersIndividualAreEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersCorporateAreNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersCorporateAreEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForMissingEntityNameField() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingEntityNameFieldAndOtherBlocksWithValidationErrors() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingEntityField() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testEntityNameFieldValidatorGetsCalled() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setEntity(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(any(), any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorReportedForMissingEntityNameFieldButNotOtherBlocksForPartialValidation() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);


        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(any(), any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorReportedForMissingEntityFieldAndOtherBlocksWithValidationErrors() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingPresenterField() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }


    @Test
    void testErrorReportedForMissingPresenterFieldAndOtherBlocksWithValidationErrors() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorNotReportedForMissingPresenterFieldAndOtherBlocksForPartialValidation() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(any(), any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingEntityFieldAndOtherBlocksForPartialValidation() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(1)).validate(any(), any(), any());
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateDueDiligenceFields(any(), any(), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(null);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(1)).validate(any(), any(), any());
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(0)).validateDueDiligenceFields(any(), any(), any(), any());
    }

    @Test
    void testFullValidationErrorReportedWhenEntityNameFieldIsNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testFullValidationErrorReportedWhenEntityNameFieldIsEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName("  ");
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testFullValidationErrorReportedWhenEntityNameFieldExceedsMaxLength() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(StringUtils.repeat("A", 161));
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testFullValidationErrorReportedWhenEntityNameFieldContainsInvalidCharacters() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName("Дракон");
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testPartialValidationErrorReportedWhenEntityNameFieldIsNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testPartialValidationErrorReportedWhenEntityNameFieldIsEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName("  ");
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testPartialValidationErrorReportedWhenEntityNameFieldExceedsMaxLength() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(StringUtils.repeat("A", 161));
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testPartialValidationErrorReportedWhenEntityNameFieldContainsInvalidCharacters() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName("Дракон");
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(entityName);
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
