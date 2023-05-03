package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER_FIELD;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityNameMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private static final String LONG_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @InjectMocks
    private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;
    @Mock
    private EntityNameValidator entityNameValidator;
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
    @Mock
    private TrustDetailsValidator trustDetailsValidator;
    @Mock
    private TrustIndividualValidator trustIndividualValidator;
    @Mock
    private HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator;
    @Mock
    private TrustCorporateValidator trustCorporateValidator;

    private final EntityNameDto entityNameDto = EntityNameMock.getEntityNameDto();
    private final EntityDto entityDto = EntityMock.getEntityDto();
    private final PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
    private final BeneficialOwnersStatementType beneficialOwnersStatement = BeneficialOwnersStatementType.NONE_IDENTIFIED;
    private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

    private final List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = new ArrayList<>();
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

    private final List<TrustDataDto> trustDataDtoList = new ArrayList<>();
    {
        trustDataDtoList.add(TrustMock.getTrustDataDto());
    };

    @Test
    void testOverseasEntitySubmissionValidatorWithDueDiligence() {

        setIsTrustWebEnabledFeatureFlag(true);

        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verifyValidateFull();
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto),any(),any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testOverseasEntitySubmissionValidatorWithOverseasEntityDueDiligence() {

        setIsTrustWebEnabledFeatureFlag(true);

        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verifyValidateFull();
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto),any(),any());
        assertFalse(errors.hasErrors());
    }

    private void verifyValidateFull() {
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateFullDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        verify(trustDetailsValidator, times(1)).validate(any(), any(), any());
        verify(trustIndividualValidator, times(1)).validate(any(), any(), any());
        verify(historicalBeneficialOwnerValidator, times(1)).validate(any(), any(), any());
        verify(trustCorporateValidator, times(1)).validate(any(), any(), any());
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
    void testNoErrorReportedWhenTrustDetailsAreNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setTrusts(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenTrustDetailsAreEmpty() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setTrusts(new ArrayList<>());
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
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
        verify(trustDetailsValidator, times(0)).validate(any(), any(), any());
        verify(trustIndividualValidator, times(0)).validate(any(), any(), any());
        verify(historicalBeneficialOwnerValidator, times(0)).validate(any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingEntityNameFieldAndOtherBlocksForPartialValidation() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        verify(entityNameValidator, times(0)).validate(any(), any(), any());
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
        verify(trustDetailsValidator, times(0)).validate(any(), any(), any());
        verify(trustIndividualValidator, times(0)).validate(any(), any(), any());
        verify(historicalBeneficialOwnerValidator, times(0)).validate(any(), any(), any());
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
    void testErrorReportedForMissingPresenterFieldForUpdate() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111129");
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
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
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
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation() {
        testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation(false);
    }

    @Test
    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialUpdateValidation() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation(true);
    }

    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation(boolean isUpdateTest) {
        buildOverseasEntitySubmissionDto();
        if (isUpdateTest) {
            overseasEntitySubmissionDto.setEntityNumber("OE111129");
        }
        overseasEntitySubmissionDto.setDueDiligence(null);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(1)).validate(any(), any(), any());
        if (!isUpdateTest) {
            verify(entityDtoValidator, times(1)).validate(any(), any(), any());
            verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
            verify(dueDiligenceDataBlockValidator, times(0)).validateFullDueDiligenceFields(any(),
                    any(), any(), any());
        }
    }

    @Test
    void testFullValidationErrorReportedWhenEntityNameBlockIsNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(entityNameValidator, times(0)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationErrorReportedWhenEntityNameBlockIsNull() {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
        verify(entityNameValidator, times(0)).validate(any(), any(), any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", LONG_NAME, "Дракон"} )
    void testFullValidationErrorReportedWhenEntityNameFieldIsNull(String name) {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.getEntityName().setName(name);
        overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityNameValidator, times(1)).validate(any(), any(), any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", LONG_NAME, "Дракон"} )
    void testPartialValidationErrorReportedWhenEntityNameFieldIsNull(String name) {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.getEntityName().setName(name);
        overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityNameValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialUpdateValidation() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullUpdateValidationWithoutTrusts() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(false);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateFullDueDiligenceFields(
                    eq(overseasEntitySubmissionDto.getDueDiligence()),
                    eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                    any(),
                    any());
        verify(ownersAndOfficersDataBlockValidator, times(0)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationNoEntity() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationWithEntity() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationWithEntityNoEntityNumber() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }


    @Test
    void testPartialUpdateValidationNoEntityEmail() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.getEntity().setEmail(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }


    @Test
    void testRegistrationSubmissionCalledWhenEntityNumberIsNullAndUpdateFlagTrue() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildOverseasEntityUpdateSubmissionDtoWithFullDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verifyValidateFull();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testRegistrationSubmissionCalledWithEntityNumberAndUpdateFlagFalse() {
        setIsRoeUpdateEnabledFeatureFlag(false);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        buildOverseasEntityUpdateSubmissionDtoWithFullDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verifyValidateFull();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testOverseasEntityUpdateSubmissionValidatorWithoutOwners() {
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        verifyValidateFull();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForMissingUpdateEntityFieldAndOtherBlocksWithValidationErrors() {
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingUpdateEntityNameField() {
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private void buildOverseasEntitySubmissionDto() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(entityNameDto);
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnerIndividualDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnerCorporateDtoList);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityDtoList);
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficerIndividualDtoList);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficerCorporateDtoList);
        overseasEntitySubmissionDto.setTrusts(trustDataDtoList);
    }

    private void buildPartialOverseasEntityUpdateSubmissionDto() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(true);

        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        overseasEntitySubmissionDto.setEntityName(entityNameDto);
        overseasEntitySubmissionDto.setEntity(entityDto);
        overseasEntitySubmissionDto.setPresenter(presenterDto);
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDto.setTrusts(trustDataDtoList);
    }

    private void buildOverseasEntityUpdateSubmissionDtoWithFullDto() {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(true);

        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        buildPartialOverseasEntityUpdateSubmissionDto();
        buildOverseasEntitySubmissionDto();
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private void setIsRoeUpdateEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(overseasEntitySubmissionDtoValidator, "isRoeUpdateEnabled", value);
    }

    private void setIsTrustWebEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(overseasEntitySubmissionDtoValidator, "isTrustWebEnabled", value);
    }
}
