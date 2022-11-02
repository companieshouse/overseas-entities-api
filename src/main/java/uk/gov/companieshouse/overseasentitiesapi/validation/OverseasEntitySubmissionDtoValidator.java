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

    public Errors validateFull(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
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
        Set<Err> missingBlocksErrors = new HashSet<>();

        // block 1 - presenter
        var presenterDto = overseasEntitySubmissionDto.getPresenter();
        if (Objects.nonNull(presenterDto)) {
            presenterDtoValidator.validate(presenterDto, errors, loggingContext);
        } else { // block is null
            missingBlocksErrors.add(Err.invalidBodyBuilderWithLocation("presenter").withError("presenter should not be null").build());
        }


        // block 2 - entity
        var entityDto = overseasEntitySubmissionDto.getEntity();
        if (Objects.nonNull(entityDto)) {
            entityDtoValidator.validate(entityDto, errors, loggingContext);
            addMissingBlocksToErrors(missingBlocksErrors, errors);
        } else { // block is null
            missingBlocksErrors.add(Err.invalidBodyBuilderWithLocation("entity").withError("entity should not be null").build());
        }


        // block 3 - due diligence
        var dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
        var overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        if (Objects.nonNull(dueDiligenceDto) || Objects.nonNull(overseasEntityDueDiligenceDto)) {
            dueDiligenceDataBlockValidator.validateDueDiligenceFields(
                    dueDiligenceDto,
                    overseasEntityDueDiligenceDto,
                    errors,
                    loggingContext);

            addMissingBlocksToErrors(missingBlocksErrors, errors);
        } else { // both due diligence blocks are null
            missingBlocksErrors.add(Err.invalidBodyBuilderWithLocation("due_diligence, overseas_entity_due_diligence").withError("due_diligence and overseas_entity_due_diligence should not both be null").build());
        }


        // do we handle beneficial_owner_statement separately or as part of validateOwnersAndOfficers?

        // block 4 - owners and officers
        // is this needed?  if this block is present then it indicates a full validation should happen so the 'full' validate method should get used
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
