package uk.gov.companieshouse.overseasentitiesapi.validation;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class OverseasEntitySubmissionDtoValidator {

    private final EntityNameValidator entityNameValidator;
    private final EntityDtoValidator entityDtoValidator;
    private final PresenterDtoValidator presenterDtoValidator;
    private final OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator;
    private final DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
    private final TrustDetailsValidator trustDetailsValidator;
    private final TrustIndividualValidator trustIndividualValidator;
    private final HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator;
    private final TrustCorporateValidator trustCorporateValidator;

    private final UpdateValidator updateValidator;
    private final RemoveValidator removeValidator;

    @Value("${FEATURE_FLAG_ENABLE_ROE_UPDATE_24112022:false}")
    private boolean isRoeUpdateEnabled;

    @Value("${FEATURE_FLAG_ENABLE_TRUSTS_WEB_07112022:false}")
    private boolean isTrustWebEnabled;

    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityNameValidator entityNameValidator,
                                                EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator,
                                                DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator,
                                                TrustDetailsValidator trustDetailsValidator,
                                                TrustIndividualValidator trustIndividualValidator,
                                                TrustCorporateValidator trustCorporateValidator,
                                                HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator,
                                                UpdateValidator updateValidator,
                                                RemoveValidator removeValidator) {
        this.entityNameValidator = entityNameValidator;
        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.dueDiligenceDataBlockValidator = dueDiligenceDataBlockValidator;
        this.ownersAndOfficersDataBlockValidator = ownersAndOfficersDataBlockValidator;
        this.trustDetailsValidator = trustDetailsValidator;
        this.trustIndividualValidator = trustIndividualValidator;
        this.trustCorporateValidator = trustCorporateValidator;
        this.historicalBeneficialOwnerValidator = historicalBeneficialOwnerValidator;
        this.updateValidator = updateValidator;
        this.removeValidator = removeValidator;
    }

    public Errors validateFull(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {

        if (isRoeUpdateEnabled && overseasEntitySubmissionDto.isForUpdate()) {
            validateFullUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext, passThroughTokenHeader);
        } else if (overseasEntitySubmissionDto.isForRemove()) {
            validateFullRemoveDetails(overseasEntitySubmissionDto, errors, loggingContext);
        } else {
            validateFullRegistrationDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }
        return errors;
    }

    private void validateFullUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {
        updateValidator.validateFull(overseasEntitySubmissionDto.getEntityNumber(), overseasEntitySubmissionDto.getUpdate(), errors, loggingContext, passThroughTokenHeader);

        validateUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext);
    }

    private void validateUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (!overseasEntitySubmissionDto.getUpdate().isNoChange()) {

            validateFullCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

            ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, loggingContext);
            ownersAndOfficersDataBlockValidator.validateRegistrableBeneficialOwnerStatement(overseasEntitySubmissionDto, errors, loggingContext);

            dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(
                    overseasEntitySubmissionDto.getDueDiligence(),
                    overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                    errors,
                    loggingContext);
        }
        else {
            validateNoChangeUpdate(overseasEntitySubmissionDto, errors, loggingContext);
        }

        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext, true);
    }

    private void validateFullRemoveDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        var updateDto = overseasEntitySubmissionDto.getUpdate();

        if (updateDto == null) {
            String errorMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", OverseasEntitySubmissionDto.UPDATE_FIELD);
            setErrorMsgToLocation(errors, OverseasEntitySubmissionDto.UPDATE_FIELD, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        } else {
            validateUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext);
            validateFilingDate(updateDto.getFilingDate(), errors, loggingContext);
        }
        var removeDto = overseasEntitySubmissionDto.getRemove();
        removeValidator.validateFull(removeDto, errors, loggingContext);
    }

    private void validateFullRegistrationDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        validateFullCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext, true);

        dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(
                overseasEntitySubmissionDto.getDueDiligence(),
                overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                errors,
                loggingContext);

        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficersAgainstStatement(overseasEntitySubmissionDto, errors, loggingContext);
    }

    private void validateFullCommonDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getEntityName(), OverseasEntitySubmissionDto.ENTITY_NAME_FIELD, errors, loggingContext)) {
            entityNameValidator.validate(overseasEntitySubmissionDto.getEntityName(), errors, loggingContext);
        }

        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getEntity(), OverseasEntitySubmissionDto.ENTITY_FIELD, errors, loggingContext)) {
            entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
        }

        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getPresenter(), OverseasEntitySubmissionDto.PRESENTER_FIELD, errors, loggingContext)) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        }
    }

    private void validateTrustDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                      Errors errors,
                                      String loggingContext,
                                      boolean isFullValidation) {
        if (isTrustWebEnabled && !CollectionUtils.isEmpty(overseasEntitySubmissionDto.getTrusts())) {
            // The Trust Details Validator will only check the 'ceased date' if full validation is being performed
            trustDetailsValidator.validate(overseasEntitySubmissionDto, errors, loggingContext, isFullValidation);

            if (!overseasEntitySubmissionDto.isForUpdateOrRemove()) {
                // Note that this validation is only done for registrations
                trustIndividualValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext, overseasEntitySubmissionDto.isForUpdateOrRemove());
                historicalBeneficialOwnerValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
                trustCorporateValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
            }

            else if (overseasEntitySubmissionDto.isForUpdateOrRemove()) {
                trustIndividualValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext, overseasEntitySubmissionDto.isForUpdateOrRemove());
            }
        }
    }

    public Errors validatePartial(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {
        if (isRoeUpdateEnabled && overseasEntitySubmissionDto.isForUpdate()) {
             validatePartialUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext, passThroughTokenHeader);
             return errors;
        } else if (overseasEntitySubmissionDto.isForRemove()) {
            validatePartialRemoveDetails(overseasEntitySubmissionDto, errors, loggingContext);
            return errors;
        } else {
            validatePartialRegistrationDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }
        return errors;
    }

    public Errors validatePartialUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext, String passThroughTokenHeader) throws ServiceException {

        errors = validatePartialCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

        if (overseasEntitySubmissionDto.getUpdate() != null) {
            updateValidator.validate(overseasEntitySubmissionDto.getEntityNumber(), overseasEntitySubmissionDto.getUpdate(), errors,
                    loggingContext, passThroughTokenHeader);
        }

        return errors;
    }

    private Errors validatePartialRemoveDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        errors = validatePartialCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

        var updateDto = overseasEntitySubmissionDto.getUpdate();
        if (updateDto != null) {
            validateFilingDate(updateDto.getFilingDate(), errors, loggingContext);
        }

        var removeDto = overseasEntitySubmissionDto.getRemove();
        if (removeDto != null) {
            removeValidator.validate(removeDto, errors, loggingContext);
        }

        return errors;
    }

    public Errors validatePartialRegistrationDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        var entityNameDto = overseasEntitySubmissionDto.getEntityName();
        if (Objects.nonNull(entityNameDto)) {
            entityNameValidator.validate(entityNameDto, errors, loggingContext);
        }

        var entityDto = overseasEntitySubmissionDto.getEntity();
        if (Objects.nonNull(entityDto)) {
            entityDtoValidator.validate(entityDto, errors, loggingContext);
        }

        errors = validatePartialCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

        return errors;
    }

    public Errors validatePartialCommonDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        var presenterDto = overseasEntitySubmissionDto.getPresenter();
        if (Objects.nonNull(presenterDto)) {
            presenterDtoValidator.validate(presenterDto, errors, loggingContext);
        }

        var dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
        var overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        if (Objects.nonNull(dueDiligenceDto) || Objects.nonNull(overseasEntityDueDiligenceDto)) {
            dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(
                    dueDiligenceDto,
                    overseasEntityDueDiligenceDto,
                    errors,
                    loggingContext);
        }

        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);
        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext, false);

        return errors;
    }

    private void validateNoChangeUpdate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getPresenter(), OverseasEntitySubmissionDto.PRESENTER_FIELD, errors, loggingContext)) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        }
    }

    private void validateFilingDate(LocalDate filingDate, Errors errors, String loggingContext) {
        // A filing date should NOT be present for a Remove submission
        if (filingDate != null) {
            String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.UPDATE_FIELD,
                    UpdateDto.FILING_DATE);
            String errorMessage = ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        }
    }
}
