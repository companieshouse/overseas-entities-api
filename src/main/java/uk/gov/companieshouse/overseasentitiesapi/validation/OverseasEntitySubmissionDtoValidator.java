package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;
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
    private final BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;

    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator,
                                                BeneficialOwnersStatementValidator beneficialOwnersStatementValidator,
                                                BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator,
                                                BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator,
                                                BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator,
                                                DueDiligenceValidator dueDiligenceValidator) {

        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.overseasEntityDueDiligenceValidator = overseasEntityDueDiligenceValidator;
        this.beneficialOwnersStatementValidator = beneficialOwnersStatementValidator;
        this.beneficialOwnerIndividualValidator = beneficialOwnerIndividualValidator;
        this.beneficialOwnerCorporateValidator = beneficialOwnerCorporateValidator;
        this.beneficialOwnerGovernmentOrPublicAuthorityValidator = beneficialOwnerGovernmentOrPublicAuthorityValidator;
        this.dueDiligenceValidator = dueDiligenceValidator;
    }

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
        presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);

        OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        DueDiligenceDto dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();

        if(Objects.nonNull(overseasEntityDueDiligenceDto)) {
            overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, errors, loggingContext);
        }
        if(Objects.nonNull(dueDiligenceDto)) {
            dueDiligenceValidator.validate(dueDiligenceDto, errors, loggingContext);
        }

        beneficialOwnersStatementValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersStatement(), errors, loggingContext);

        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDto = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
        if (Objects.nonNull(beneficialOwnerIndividualDto) && !beneficialOwnerIndividualDto.isEmpty()) {
            beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDto, errors, loggingContext);
        }
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDto = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
        if (Objects.nonNull(beneficialOwnerCorporateDto) && !beneficialOwnerCorporateDto.isEmpty()) {
            beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDto, errors, loggingContext);
        }
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDto = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
        if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDto) && !beneficialOwnerGovernmentOrPublicAuthorityDto.isEmpty()) {
            beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDto, errors, loggingContext);
        }
        return errors;
    }

}
