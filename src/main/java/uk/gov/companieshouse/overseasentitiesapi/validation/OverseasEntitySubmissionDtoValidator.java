package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DueDiligenceDataBlockValidator;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

@Component
public class OverseasEntitySubmissionDtoValidator {

    private final EntityDtoValidator entityDtoValidator;
    private final PresenterDtoValidator presenterDtoValidator;
    private final OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
    private final BeneficialOwnersStatementValidator beneficialOwnersStatementValidator;
    private final BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator;
    private final BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator;
    private final DueDiligenceValidator dueDiligenceValidator;
    private final DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
    private final BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;

    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator,
                                                BeneficialOwnersStatementValidator beneficialOwnersStatementValidator,
                                                BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator,
                                                BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator,
                                                BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator,
                                                DueDiligenceValidator dueDiligenceValidator,
                                                DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator) {

        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.overseasEntityDueDiligenceValidator = overseasEntityDueDiligenceValidator;
        this.beneficialOwnersStatementValidator = beneficialOwnersStatementValidator;
        this.beneficialOwnerIndividualValidator = beneficialOwnerIndividualValidator;
        this.beneficialOwnerCorporateValidator = beneficialOwnerCorporateValidator;
        this.beneficialOwnerGovernmentOrPublicAuthorityValidator = beneficialOwnerGovernmentOrPublicAuthorityValidator;
        this.dueDiligenceValidator = dueDiligenceValidator;
        this.dueDiligenceDataBlockValidator = dueDiligenceDataBlockValidator;
    }

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
        presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);

        var dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
        var overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        if (dueDiligenceDataBlockValidator.onlyOneBlockPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext)) {

            if (Objects.nonNull(overseasEntityDueDiligenceDto) && !overseasEntityDueDiligenceDto.isEmpty()) {
                overseasEntityDueDiligenceValidator.validate(overseasEntitySubmissionDto.getOverseasEntityDueDiligence(), errors, loggingContext);
            } else {
                dueDiligenceValidator.validate(overseasEntitySubmissionDto.getDueDiligence(), errors, loggingContext);
            }
        }
        beneficialOwnersStatementValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersStatement(), errors, loggingContext);
        beneficialOwnerIndividualValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersIndividual(), errors, loggingContext);
        beneficialOwnerCorporateValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersCorporate(), errors, loggingContext);
        beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority(), errors, loggingContext);
        return errors;
    }


}
