package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DueDiligenceDataBlockValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
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
    private final DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
    private final BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;
    private final ManagingOfficerIndividualValidator managingOfficerIndividualValidator;
    private final ManagingOfficerCorporateValidator managingOfficerCorporateValidator;

    @Autowired
    public OverseasEntitySubmissionDtoValidator(EntityDtoValidator entityDtoValidator,
                                                PresenterDtoValidator presenterDtoValidator,
                                                OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator,
                                                BeneficialOwnersStatementValidator beneficialOwnersStatementValidator,
                                                BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator,
                                                BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator,
                                                BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator,
                                                DueDiligenceValidator dueDiligenceValidator,
                                                DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator,
                                                ManagingOfficerIndividualValidator managingOfficerIndividualValidator,
                                                ManagingOfficerCorporateValidator managingOfficerCorporateValidator) {

        this.entityDtoValidator = entityDtoValidator;
        this.presenterDtoValidator = presenterDtoValidator;
        this.overseasEntityDueDiligenceValidator = overseasEntityDueDiligenceValidator;
        this.beneficialOwnersStatementValidator = beneficialOwnersStatementValidator;
        this.beneficialOwnerIndividualValidator = beneficialOwnerIndividualValidator;
        this.beneficialOwnerCorporateValidator = beneficialOwnerCorporateValidator;
        this.beneficialOwnerGovernmentOrPublicAuthorityValidator = beneficialOwnerGovernmentOrPublicAuthorityValidator;
        this.dueDiligenceValidator = dueDiligenceValidator;
        this.dueDiligenceDataBlockValidator = dueDiligenceDataBlockValidator;
        this.managingOfficerIndividualValidator = managingOfficerIndividualValidator;
        this.managingOfficerCorporateValidator = managingOfficerCorporateValidator;
    }

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getEntity(), OverseasEntitySubmissionDto.ENTITY_FIELD, errors, loggingContext)) {
            entityDtoValidator.validate(overseasEntitySubmissionDto.getEntity(), errors, loggingContext);
        }

        if (UtilsValidators.isNotNull(overseasEntitySubmissionDto.getPresenter(), OverseasEntitySubmissionDto.PRESENTER_FIELD, errors, loggingContext)) {
            presenterDtoValidator.validate(overseasEntitySubmissionDto.getPresenter(), errors, loggingContext);
        }

        var dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
        var overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        if (dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(overseasEntitySubmissionDto.getWhoIsRegistering(), dueDiligenceDto, overseasEntityDueDiligenceDto, errors, loggingContext)) {

            if (Objects.nonNull(overseasEntityDueDiligenceDto) && !overseasEntityDueDiligenceDto.isEmpty()) {
                overseasEntityDueDiligenceValidator.validate(overseasEntitySubmissionDto.getOverseasEntityDueDiligence(), errors, loggingContext);
            } else {
                dueDiligenceValidator.validate(overseasEntitySubmissionDto.getDueDiligence(), errors, loggingContext);
            }
        }
        beneficialOwnersStatementValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersStatement(), errors, loggingContext);

        List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
        if (Objects.nonNull(beneficialOwnerIndividualDtoList) && !beneficialOwnerIndividualDtoList.isEmpty()) {
            beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, errors, loggingContext);
        }
        List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
        if (Objects.nonNull(beneficialOwnerCorporateDtoList) && !beneficialOwnerCorporateDtoList.isEmpty()) {
            beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, errors, loggingContext);
        }
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
        if (Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDtoList) && !beneficialOwnerGovernmentOrPublicAuthorityDtoList.isEmpty()) {
            beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, errors, loggingContext);
        }
        List<ManagingOfficerIndividualDto> managingOfficersIndividualDtoList = overseasEntitySubmissionDto.getManagingOfficersIndividual();
        if (Objects.nonNull(managingOfficersIndividualDtoList) && !managingOfficersIndividualDtoList.isEmpty()) {
            managingOfficerIndividualValidator.validate(managingOfficersIndividualDtoList, errors, loggingContext);
        }
        List<ManagingOfficerCorporateDto> managingOfficersCorporateDtoList = overseasEntitySubmissionDto.getManagingOfficersCorporate();
        if (Objects.nonNull(managingOfficersCorporateDtoList) && !managingOfficersCorporateDtoList.isEmpty()) {
            managingOfficerCorporateValidator.validate(managingOfficersCorporateDtoList, errors, loggingContext);
        }
        return errors;
    }
}
