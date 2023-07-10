package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

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
                                                UpdateValidator updateValidator) {
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
    }

    public Errors validateFull(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        if (isRoeUpdateEnabled && overseasEntitySubmissionDto.isForUpdate()) {
            validateFullUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext);
        } else {
            validateFullRegistrationDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }
        return errors;
    }

    private void validateFullUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        // Method to be added to as Update journey developed
        validateFullCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);

        updateValidator.validateFull(overseasEntitySubmissionDto.getUpdate(), errors, loggingContext);

        if (!overseasEntitySubmissionDto.getUpdate().isNoChange()) {
            // Method to be added to as Update journey developed
            validateFullCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

            // Change to Statement Validation once BO/MO Statements are complete
            // ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);

            dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(
                    overseasEntitySubmissionDto.getDueDiligence(),
                    overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                    errors,
                    loggingContext);
        }
        else {
            validateNoChangeUpdate(overseasEntitySubmissionDto, errors, loggingContext);
        }

        // Change when trusts are added:
        // call validateTrustDetails with (overseasEntitySubmissionDto, errors, loggingContext)

    }

    private void validateFullRegistrationDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        validateFullCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext);

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

    private void validateTrustDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if(isTrustWebEnabled &&
                !CollectionUtils.isEmpty(overseasEntitySubmissionDto.getTrusts())) {
            trustDetailsValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
            trustIndividualValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
            historicalBeneficialOwnerValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
            trustCorporateValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
        }
    }

    public Errors validatePartial(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (isRoeUpdateEnabled && overseasEntitySubmissionDto.isForUpdate()) {
            // validatePartialUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext);
            return errors;
        } else {
            validatePartialRegistrationDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }
        return errors;
    }

    public Errors validatePartialUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        var entityDto = overseasEntitySubmissionDto.getEntity();

        if (Objects.nonNull(entityDto)) {
            var entityEmail = entityDto.getEmail();
            // Temporary as initial public data Entity fetch has no Email Address. UAR-711
            if (StringUtils.isNotBlank(entityEmail)) {
                entityDtoValidator.validate(entityDto, errors, loggingContext);
            }
        }
        
        if (overseasEntitySubmissionDto.getUpdate() != null) {
            updateValidator.validate(overseasEntitySubmissionDto.getUpdate(), errors,
                    loggingContext);
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

        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);

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


        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext);

        return errors;
    }

    private void validateNoChangeUpdate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getPresenter(), OverseasEntitySubmissionDto.PRESENTER_FIELD, errors, loggingContext)) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        }
    }
}
