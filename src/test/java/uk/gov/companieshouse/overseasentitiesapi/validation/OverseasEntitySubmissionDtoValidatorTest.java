package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.UPDATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.HAS_SECURE_REGISTER_FIELD;

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

import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.EntityNameMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDtoValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
    private static final String PASS_THROUGH_HEADER = "545345345";
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
    @Mock
    private UpdateValidator updateValidator;
    @Mock
    private RemoveValidator removeValidator;

    private final EntityNameDto entityNameDto = EntityNameMock.getEntityNameDto();
    private final EntityDto entityDto = EntityMock.getEntityDto();
    private final PresenterDto presenterDto = PresenterMock.getPresenterDto();
    private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
    private final BeneficialOwnersStatementType beneficialOwnersStatement = BeneficialOwnersStatementType.NONE_IDENTIFIED;
    private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

    private final UpdateDto updateDto = UpdateMock.getUpdateDto();

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
    void testOverseasEntitySubmissionValidatorWithDueDiligence() throws ServiceException {

        setIsTrustWebEnabledFeatureFlag(true);

        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verifyValidateFull(true);
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto),any(),any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testOverseasEntitySubmissionValidatorWithOverseasEntityDueDiligence() throws ServiceException {

        setIsTrustWebEnabledFeatureFlag(true);

        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verifyValidateFull(true);
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto),any(),any());
        assertFalse(errors.hasErrors());
    }

    private void verifyValidateFull(boolean isRegistration) {
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateFullDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        if (isRegistration) {
            verify(trustDetailsValidator, times(1)).validate(any(), any(), any(), eq(true));
            verify(trustIndividualValidator, times(1)).validate(any(), any(), any(), eq(false));

            verify(historicalBeneficialOwnerValidator, times(1)).validate(any(), any(), any());
            verify(trustCorporateValidator, times(1)).validate(any(), eq(false), any(), any());
        }
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersIndividualAreNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersIndividualAreEmpty() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersCorporateAreNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersCorporateAreEmpty() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersGovernmentOrPublicAuthorityAreNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBeneficialOwnersGovernmentOrPublicAuthorityAreEmpty() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersIndividualAreNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersIndividualAreEmpty() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersCorporateAreNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenManagingOfficersCorporateAreEmpty() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenTrustDetailsAreNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setTrusts(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenTrustDetailsAreEmpty() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(dueDiligenceDto);
        overseasEntitySubmissionDto.setTrusts(new ArrayList<>());
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedForMissingEntityNameField() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingEntityNameFieldAndOtherBlocksWithValidationErrors() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingEntityField() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testEntityNameFieldValidatorGetsCalled() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setEntity(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
        verify(trustDetailsValidator, times(0)).validate(any(), any(), any(), any(Boolean.class));
        verify(trustIndividualValidator, times(0)).validate(any(), any(), any(), eq(false));
        verify(historicalBeneficialOwnerValidator, times(0)).validate(any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingEntityNameFieldAndOtherBlocksForPartialValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(entityNameValidator, times(0)).validate(any(), any(), any());
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
        verify(trustDetailsValidator, times(0)).validate(any(), any(), any(), eq(false));
        verify(trustIndividualValidator, times(0)).validate(any(), any(), any(), eq(false));
        verify(historicalBeneficialOwnerValidator, times(0)).validate(any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorReportedForMissingEntityFieldAndOtherBlocksWithValidationErrors() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingPresenterField() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingPresenterFieldForUpdate() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111129");
        overseasEntitySubmissionDto.setPresenter(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingPresenterFieldForNoChangeUpdate() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111129");
        overseasEntitySubmissionDto.getUpdate().setNoChange(true);
        overseasEntitySubmissionDto.setPresenter(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingPresenterFieldAndOtherBlocksWithValidationErrors() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);
        overseasEntitySubmissionDto.setManagingOfficersCorporate(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = PRESENTER_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorNotReportedForMissingPresenterFieldAndOtherBlocksForPartialValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setPresenter(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(0)).validate(any(), any(), any());
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingEntityFieldAndOtherBlocksForPartialValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(1)).validate(any(), any(), any());
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validatePartialDueDiligenceFields(any(), any(), any(), any());
    }

    @Test
    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation() throws ServiceException {
        testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation(false);
    }

    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialUpdateValidation() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation(true);
    }

    void testErrorNotReportedForMissingDueDiligenceFieldsAndOtherBlocksForPartialValidation(boolean isUpdateTest) throws ServiceException {
        buildOverseasEntitySubmissionDto();
        if (isUpdateTest) {
            overseasEntitySubmissionDto.setEntityNumber("OE111129");
        }
        overseasEntitySubmissionDto.setDueDiligence(null);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(null);
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(null);
        overseasEntitySubmissionDto.setManagingOfficersIndividual(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(presenterDtoValidator, times(1)).validate(any(), any(), any());
        if (!isUpdateTest) {
            verify(entityDtoValidator, times(1)).validate(any(), any(), any());
            verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficers(eq(overseasEntitySubmissionDto), any(), any());
            verify(dueDiligenceDataBlockValidator, times(0)).validateFullDueDiligenceFields(any(),
                    any(), any(), any());
            verify(dueDiligenceDataBlockValidator, times(0)).validatePartialDueDiligenceFields(any(),
                    any(), any(), any());
        }
    }

    @Test
    void testFullValidationErrorReportedWhenEntityNameBlockIsNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(entityNameValidator, times(0)).validate(any(), any(), any());
    }

    @Test
    void testPartialValidationErrorReportedWhenEntityNameBlockIsNull() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
        verify(entityNameValidator, times(0)).validate(any(), any(), any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", LONG_NAME, "Дракон"} )
    void testFullValidationErrorReportedWhenEntityNameFieldIsNull(String name) throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.getEntityName().setName(name);
        overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityNameValidator, times(1)).validate(any(), any(), any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", LONG_NAME, "Дракон"} )
    void testPartialValidationErrorReportedWhenEntityNameFieldIsNull(String name) throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.getEntityName().setName(name);
        overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityNameValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialUpdateValidation() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationNoUpdate() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111129");
        overseasEntitySubmissionDto.setUpdate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialRemoveValidationNoRemoveStatementFilingDateNotPresent() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityRemoveSubmissionDto();
        overseasEntitySubmissionDto.getUpdate().setFilingDate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(removeValidator, never()).validate(any(), any(), any());
    }

    @Test
    void testPartialRemoveValidationNoRemoveStatementFilingDateIsPresent() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityRemoveSubmissionDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(removeValidator, never()).validate(any(), any(), any());
    }

    @Test
    void testPartialRemoveValidationRemoveStatementPresentFilingDateNotPresent() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityRemoveSubmissionDto();

        RemoveDto removeDto = new RemoveDto();
        removeDto.setIsNotProprietorOfLand(true);
        overseasEntitySubmissionDto.setRemove(removeDto);
        overseasEntitySubmissionDto.getUpdate().setFilingDate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        assertFalse(errors.hasErrors());
        verify(removeValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialRemoveValidationRemoveStatementPresentFilingDateIsPresent() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityRemoveSubmissionDto();

        RemoveDto removeDto = new RemoveDto();
        removeDto.setIsNotProprietorOfLand(true);
        overseasEntitySubmissionDto.setRemove(removeDto);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(removeValidator, times(1)).validate(any(), any(), any());
    }

    @Test
    void testPartialUpdateValidationNoFilingDate() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111129");
        overseasEntitySubmissionDto.getUpdate().setFilingDate(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullUpdateValidationWithoutTrusts() throws ServiceException {
        buildOverseasEntitySubmissionDto();

        Errors errors = testFullUpdateRemoveValidationWithoutTrusts();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullRemoveValidationWithoutTrustsFilingDateNotPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        overseasEntitySubmissionDto.getUpdate().setFilingDate(null);
        Errors errors = testFullUpdateRemoveValidationWithoutTrusts();

        assertFalse(errors.hasErrors());
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }

    @Test
    void testFullRemoveValidationWithoutTrustsFilingDateIsPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        Errors errors = testFullUpdateRemoveValidationWithoutTrusts();

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }


    @Test
    void testFullUpdateValidationWithTrusts() throws ServiceException {
        buildOverseasEntitySubmissionDto();

        Errors errors = testFullUpdateRemoveValidationWithTrusts();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullRemoveValidationWithTrustsFilingDateNotPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        overseasEntitySubmissionDto.getUpdate().setFilingDate(null);
        Errors errors = testFullUpdateRemoveValidationWithTrusts();

        assertFalse(errors.hasErrors());
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }

    @Test
    void testFullRemoveValidationWithTrustsFilingDateIsPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        Errors errors = testFullUpdateRemoveValidationWithTrusts();

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }

    @Test
    void testFullUpdateValidationWithoutBeneficialOwners() throws ServiceException {
        buildOverseasEntitySubmissionDto();

        Errors errors = testFullUpdateRemoveValidationWithoutBeneficialOwners();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullRemoveValidationWithoutBeneficialOwnersFilingDateIsPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        Errors errors = testFullUpdateRemoveValidationWithoutBeneficialOwners();

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }

    @Test
    void testFullUpdateValidationWithoutManagingOfficers() throws ServiceException {
        buildOverseasEntitySubmissionDto();

        Errors errors = testFullUpdateRemoveValidationWithoutManagingOfficers();
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullRemoveValidationWithoutManagingOfficersFilingDateNotPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        overseasEntitySubmissionDto.getUpdate().setFilingDate(null);
        Errors errors = testFullUpdateRemoveValidationWithoutManagingOfficers();

        assertFalse(errors.hasErrors());
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }

    @Test
    void testFullRemoveValidationWithoutManagingOfficersFilingDateIsPresent() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        Errors errors = testFullUpdateRemoveValidationWithoutManagingOfficers();

        String qualifiedFieldName = UPDATE_FIELD + "." + UpdateDto.FILING_DATE;
        String validationMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
        verify(removeValidator, times(1)).validateFull(any(), any(), any());
    }

    @Test
    void testFullRemoveValidationErrorReportedWhenUpdateBlockNotPresent() throws ServiceException {
        // Given
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setIsRemove(true);
        overseasEntitySubmissionDto.setUpdate(null);

        // When
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        // Then
        String qualifiedFieldName = UPDATE_FIELD;
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testPartialUpdateValidationNoEntity() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationWithEntity() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityDtoValidator, times(0)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationWithEntityNoEntityNumber() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityDtoValidator, times(1)).validate(any(), any(), any());
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationNoDueDiligence() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(null);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testPartialUpdateValidationNoAgentDueDiligence() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }


    @Test
    void testPartialUpdateValidationNoOverseasEntityDueDiligence() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testRegistrationSubmissionCalledWhenEntityNumberIsNullAndUpdateFlagTrue() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        buildOverseasEntityUpdateSubmissionDtoWithFullDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verifyValidateFull(false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testRegistrationSubmissionCalledWithEntityNumberAndUpdateFlagFalse() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(false);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        buildOverseasEntityUpdateSubmissionDtoWithFullDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verifyValidateFull(false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testOverseasEntityUpdateSubmissionValidatorWithoutOwners() throws ServiceException {
        buildPartialOverseasEntityUpdateSubmissionDto();
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verifyValidateFull(false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testOverseasEntityUpdateSubmissionValidatorNoChange() throws ServiceException {
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.getUpdate().setNoChange(true);
        overseasEntitySubmissionDto.setDueDiligence(null);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(dueDiligenceDataBlockValidator, times(0)).validateFullDueDiligenceFields(eq(overseasEntitySubmissionDto.getDueDiligence()), any(), any(), any());
        verify(entityNameValidator, times(0)).validate(eq(entityNameDto), any(), any());
        verify(entityDtoValidator, times(0)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        assertFalse(errors.hasErrors());
    }
    @Test
    void testErrorReportedForMissingUpdateEntityFieldAndOtherBlocksWithValidationErrors() throws ServiceException {
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntity(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = ENTITY_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedForMissingUpdateEntityNameField() throws ServiceException {
        buildPartialOverseasEntityUpdateSubmissionDto();
        overseasEntitySubmissionDto.setEntityName(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = ENTITY_NAME_FIELD;
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNotErrorReportedDuringRegistrationWhenHasSecureRegisterFieldIsFalseForFullValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedDuringRegistrationWhenHasSecureRegisterFieldSetToTrueForFullValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setHasSecureRegister(true);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = HAS_SECURE_REGISTER_FIELD + ": " + true;
        String validationMessage = String.format(ValidationMessages.NOT_VALID_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedDuringRegistrationForMissingHasSecureRegisterFieldForFullValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setHasSecureRegister(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = HAS_SECURE_REGISTER_FIELD + ": " + null;
        String validationMessage = String.format(ValidationMessages.NOT_VALID_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNotErrorReportedDuringRegistrationWhenHasSecureRegisterFieldIsFalseForPartialValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedDuringRegistrationWhenHasSecureRegisterFieldIsTrueForPartialValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setHasSecureRegister(true);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = HAS_SECURE_REGISTER_FIELD + ": " + true;
        String validationMessage = String.format(ValidationMessages.NOT_VALID_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedDuringRegistrationForMissingHasSecureRegisterFieldForPartialValidation() throws ServiceException {
        buildOverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setHasSecureRegister(null);

        Errors errors = overseasEntitySubmissionDtoValidator.validatePartial(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = HAS_SECURE_REGISTER_FIELD + ": " + null;
        String validationMessage = String.format(ValidationMessages.NOT_VALID_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private Errors testFullUpdateRemoveValidationWithoutTrusts() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(false);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateFullDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateRegistrableBeneficialOwnerStatement(eq(overseasEntitySubmissionDto), any(), any());

       return errors;
    }

    private Errors testFullUpdateRemoveValidationWithTrusts() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(true);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        List<TrustDataDto> trustsDataDtos = new ArrayList<>();
        TrustDataDto trustDataDto = new TrustDataDto();
        trustDataDto.setTrustName("TEST TRUST");
        trustsDataDtos.add(trustDataDto);
        overseasEntitySubmissionDto.setTrusts(trustDataDtoList);
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateFullDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateRegistrableBeneficialOwnerStatement(eq(overseasEntitySubmissionDto), any(), any());
        verify(trustDetailsValidator, times(1)).validate(eq(overseasEntitySubmissionDto), any(), any(), eq(true));
        verify(trustCorporateValidator, times(1)).validate(eq(overseasEntitySubmissionDto.getTrusts()), eq(true), any(), any());
        verify(historicalBeneficialOwnerValidator, times(1)).validate(eq(overseasEntitySubmissionDto.getTrusts()), any(), any());
        verify(trustIndividualValidator, times(1)).validate(eq(overseasEntitySubmissionDto.getTrusts()), any(), any(), eq(true));
        verify(historicalBeneficialOwnerValidator, times(1)).validate(eq(overseasEntitySubmissionDto.getTrusts()), any(), any());

        return errors;
    }

    private Errors testFullUpdateRemoveValidationWithoutBeneficialOwners() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(false);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(new ArrayList<>());
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(new ArrayList<>());
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(new ArrayList<>());
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateRegistrableBeneficialOwnerStatement(eq(overseasEntitySubmissionDto), any(), any());

        return errors;
    }

    private Errors testFullUpdateRemoveValidationWithoutManagingOfficers() throws ServiceException {
        setIsRoeUpdateEnabledFeatureFlag(true);
        setIsTrustWebEnabledFeatureFlag(false);
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        Errors errors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        verify(entityDtoValidator, times(1)).validate(eq(entityDto), any(), any());
        verify(presenterDtoValidator, times(1)).validate(eq(presenterDto), any(), any());
        verify(dueDiligenceDataBlockValidator, times(1)).validateFullDueDiligenceFields(
                eq(overseasEntitySubmissionDto.getDueDiligence()),
                eq(overseasEntitySubmissionDto.getOverseasEntityDueDiligence()),
                any(),
                any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateOwnersAndOfficersAgainstStatement(eq(overseasEntitySubmissionDto), any(), any());
        verify(ownersAndOfficersDataBlockValidator, times(1)).validateRegistrableBeneficialOwnerStatement(eq(overseasEntitySubmissionDto), any(), any());

        return errors;
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
        overseasEntitySubmissionDto.setUpdate(updateDto);
        overseasEntitySubmissionDto.setHasSecureRegister(false);
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
        overseasEntitySubmissionDto.setUpdate(updateDto);
        overseasEntitySubmissionDto.setHasSecureRegister(false);
    }

    private void buildPartialOverseasEntityRemoveSubmissionDto() {
        buildPartialOverseasEntityUpdateSubmissionDto();

        overseasEntitySubmissionDto.setIsRemove(true);
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
