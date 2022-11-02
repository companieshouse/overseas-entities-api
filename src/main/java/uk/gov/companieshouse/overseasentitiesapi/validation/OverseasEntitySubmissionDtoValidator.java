package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class OverseasEntitySubmissionDtoValidator {

    private final EntityDtoValidator entityDtoValidator;
    private final PresenterDtoValidator presenterDtoValidator;
    private final OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator;
    private final DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;


    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OwnersAndOfficersDataBlockValidator ownersAndOfficersDataBlockValidator,
                                                DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator) {

        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.dueDiligenceDataBlockValidator = dueDiligenceDataBlockValidator;
        this.ownersAndOfficersDataBlockValidator = ownersAndOfficersDataBlockValidator;
    }

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getEntity(), OverseasEntitySubmissionDto.ENTITY_FIELD, errors, loggingContext)) {
            entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
        }

        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getPresenter(), OverseasEntitySubmissionDto.PRESENTER_FIELD, errors, loggingContext)) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        }

        dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                overseasEntitySubmissionDto.getDueDiligence(),
                overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                errors,
                loggingContext);

        ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);
        return errors;
    }

    public Errors validatePartial(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        // todo - how to log missing blocks if the gap is > 1 block eg T, F, F, T

//        boolean isPreviousBlockNull = false;
//
//        // block 1 - presenter
//        if (Objects.nonNull(overseasEntitySubmissionDto.getPresenter())) {
//            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
//        } else { // block is null
//            isPreviousBlockNull = true;
//        }
//
//
//        // block 2 - entity
//        if (Objects.nonNull(overseasEntitySubmissionDto.getEntity())) {
//            entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
//            if (isPreviousBlockNull) {
//                // fail previous block, can't have populated block after a missing block
//                UtilsValidators.setErrorMsgToLocation(errors, "presenter", "Presenter block should not be missing");
//            }
//            isPreviousBlockNull = false;
//        } else { // block is null
//            isPreviousBlockNull = true;
//        }
//
//
//        // block 3 - due diligence
//        if (Objects.nonNull(overseasEntitySubmissionDto.getDueDiligence()) || Objects.nonNull(overseasEntitySubmissionDto.getOverseasEntityDueDiligence())) {
//            dueDiligenceDataBlockValidator.validateDueDiligenceFields(
//                    overseasEntitySubmissionDto.getDueDiligence(),
//                    overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
//                    errors,
//                    loggingContext);
//            if (isPreviousBlockNull) {
//                // fail previous block, can't have populated block after a missing block
//                UtilsValidators.setErrorMsgToLocation(errors, "entity", "Entity block should not be missing");
//            }
//            isPreviousBlockNull = false;
//        } else { // block is null
//            isPreviousBlockNull = true;
//        }
//
//
//        // block 4 - owners and officers
//        if (CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
//            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
//            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
//            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getManagingOfficersCorporate())
//            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getManagingOfficersIndividual())) {
//
//            ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);
//
//            // final null block check as this is final block
//            if (isPreviousBlockNull) {
//                // fail previous block, can't have populated block after a missing block
//                UtilsValidators.setErrorMsgToLocation(errors, "due diligence", "due diligence or overseas entity due diligence block should not be missing");
//            }
//        }
//
//
//        // trusts?
//
//        return errors;

        // ---------------------- new version ----------------------

        Set<Err> missingBlocksErrors = new HashSet<>();

        // block 1 - presenter
        if (Objects.nonNull(overseasEntitySubmissionDto.getPresenter())) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        } else { // block is null
            missingBlocksErrors.add(Err.invalidBodyBuilderWithLocation("presenter").withError("presenter should not be null").build());
        }


        // block 2 - entity
        if (Objects.nonNull(overseasEntitySubmissionDto.getEntity())) {
            entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
            addMissingBlocksToErrors(missingBlocksErrors, errors);
        } else { // block is null
            missingBlocksErrors.add(Err.invalidBodyBuilderWithLocation("entity").withError("entity should not be null").build());
        }


        // block 3 - due diligence
        if (Objects.nonNull(overseasEntitySubmissionDto.getDueDiligence()) || Objects.nonNull(overseasEntitySubmissionDto.getOverseasEntityDueDiligence())) {
            dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                    overseasEntitySubmissionDto.getDueDiligence(),
                    overseasEntitySubmissionDto.getOverseasEntityDueDiligence(),
                    errors,
                    loggingContext);

            addMissingBlocksToErrors(missingBlocksErrors, errors);
        } else { // block is null
            missingBlocksErrors.add(Err.invalidBodyBuilderWithLocation("due diligence").withError("due diligence or overseas entity due diligence should not be null").build());
        }


        // block 4 - owners and officers
        // is this needed? as if this block is present then it indicates a full validation should happen
        if (CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getManagingOfficersCorporate())
            || CollectionUtils.isNotEmpty(overseasEntitySubmissionDto.getManagingOfficersIndividual())) {

            ownersAndOfficersDataBlockValidator.validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);

            addMissingBlocksToErrors(missingBlocksErrors, errors);
        }


        // trusts?

        return errors;

        // option 3 - work backwards?
    }

    /**
     * Add missing blocks errors to the main Errors list
     * Wipes/clears missingBlocksErrors after adding to Errors
     * @param missingBlocksErrors
     * @param errors
     */
    private void addMissingBlocksToErrors(Set<Err> missingBlocksErrors, Errors errors) {
        if (CollectionUtils.isNotEmpty(missingBlocksErrors)) {
            // move missing block errors into "errors"
            missingBlocksErrors.forEach(errors::addError);
            // then empty the set
            missingBlocksErrors.clear();
        }
    }
}
