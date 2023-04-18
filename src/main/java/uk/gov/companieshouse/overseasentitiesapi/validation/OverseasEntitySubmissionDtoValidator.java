package uk.gov.companieshouse.overseasentitiesapi.validation;

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
    private final HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator;
    private final TrustCorporateValidator trustCorporateValidator;

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
                                                HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator,
                                                TrustCorporateValidator trustCorporateValidator) {
        this.entityNameValidator = entityNameValidator;
        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.dueDiligenceDataBlockValidator = dueDiligenceDataBlockValidator;
        this.ownersAndOfficersDataBlockValidator = ownersAndOfficersDataBlockValidator;
        this.trustDetailsValidator = trustDetailsValidator;
        this.historicalBeneficialOwnerValidator = historicalBeneficialOwnerValidator;
        this.trustCorporateValidator = trustCorporateValidator;
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
        // Change to Statement Validation once BO/MO Statements are complete
        // ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);

    }

    private void validateFullRegistrationDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        validateFullCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

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


        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext);

        dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                overseasEntitySubmissionDto.getDueDiligence(),
                overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                errors,
                loggingContext);
    }

    private void validateTrustDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if(isTrustWebEnabled &&
                !CollectionUtils.isEmpty(overseasEntitySubmissionDto.getTrusts())) {
            trustDetailsValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
            historicalBeneficialOwnerValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
            trustCorporateValidator.validate(overseasEntitySubmissionDto.getTrusts(), errors, loggingContext);
        }
    }

    public Errors validatePartial(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (isRoeUpdateEnabled && overseasEntitySubmissionDto.isForUpdate()) {
            validatePartialUpdateDetails(overseasEntitySubmissionDto, errors, loggingContext);
        } else {
            validatePartialRegistrationDetails(overseasEntitySubmissionDto, errors, loggingContext);
        }
        return errors;
    }

    public Errors validatePartialUpdateDetails(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        var entityDto = overseasEntitySubmissionDto.getEntity();

        if (Objects.nonNull(entityDto)) {
            var entityEmail = entityDto.getEmail();
            // Temporary as initial public data Entity fetch has no Email Address, Needs to change once private data fetch is implemented
            if (StringUtils.isNotBlank(entityEmail)) {
                entityDtoValidator.validate(entityDto, errors, loggingContext);
            }
        }
        // Temporarily disabling BO/MO validation till it is implemented in Update Journey
        errors = validatePartialCommonDetails(overseasEntitySubmissionDto, errors, loggingContext);

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
            dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                    dueDiligenceDto,
                    overseasEntityDueDiligenceDto,
                    errors,
                    loggingContext);
        }


        validateTrustDetails(overseasEntitySubmissionDto, errors, loggingContext);

        return errors;
    }
}
