package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnerCorporateValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnerGovernmentOrPublicAuthorityValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnerIndividualValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.BeneficialOwnersStatementValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.ManagingOfficerCorporateValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.ManagingOfficerIndividualValidator;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages.MISSING_OR_INCORRRECT_DATA_FOR_STATEMENT;

@Component
public class OwnersAndOfficersDataBlockValidator {

    private final BeneficialOwnersStatementValidator beneficialOwnersStatementValidator;
    private final BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator;
    private final BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator;
    private final BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;
    private final ManagingOfficerIndividualValidator managingOfficerIndividualValidator;
    private final ManagingOfficerCorporateValidator managingOfficerCorporateValidator;

    @Autowired
    public OwnersAndOfficersDataBlockValidator(BeneficialOwnersStatementValidator beneficialOwnersStatementValidator,
                                               BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator,
                                               BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator,
                                               BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator,
                                               ManagingOfficerIndividualValidator managingOfficerIndividualValidator,
                                               ManagingOfficerCorporateValidator managingOfficerCorporateValidator) {

        this.beneficialOwnersStatementValidator = beneficialOwnersStatementValidator;
        this.beneficialOwnerIndividualValidator = beneficialOwnerIndividualValidator;
        this.beneficialOwnerCorporateValidator = beneficialOwnerCorporateValidator;
        this.beneficialOwnerGovernmentOrPublicAuthorityValidator = beneficialOwnerGovernmentOrPublicAuthorityValidator;
        this.managingOfficerIndividualValidator = managingOfficerIndividualValidator;
        this.managingOfficerCorporateValidator = managingOfficerCorporateValidator;
    }

    public void validateOwnersAndOfficers(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

        beneficialOwnersStatementValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersStatement(), errors, loggingContext);
        if (isCorrectCombinationOfOwnersAndOfficersForStatement(overseasEntitySubmissionDto, errors, loggingContext)) {
            List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
            if (hasIndividualBeneficialOwnersPresent(beneficialOwnerIndividualDtoList)) {
                beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, errors, loggingContext);
            }
            List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
            if (hasCorporateBeneficialOwnersPresent(beneficialOwnerCorporateDtoList)) {
                beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, errors, loggingContext);
            }
            List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
            if (hasGovernmentOrPublicAuthorityBeneficialOwnersPresent(beneficialOwnerGovernmentOrPublicAuthorityDtoList)) {
                beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, errors, loggingContext);
            }
            List<ManagingOfficerIndividualDto> managingOfficersIndividualDtoList = overseasEntitySubmissionDto.getManagingOfficersIndividual();
            if (hasIndividualManagingOfficersPresent(managingOfficersIndividualDtoList)) {
                managingOfficerIndividualValidator.validate(managingOfficersIndividualDtoList, errors, loggingContext);
            }
            List<ManagingOfficerCorporateDto> managingOfficersCorporateDtoList = overseasEntitySubmissionDto.getManagingOfficersCorporate();
            if (hasCorporateManagingOfficersPresent(managingOfficersCorporateDtoList)) {
                managingOfficerCorporateValidator.validate(managingOfficersCorporateDtoList, errors, loggingContext);
            }
        }
    }

    private boolean isCorrectCombinationOfOwnersAndOfficersForStatement(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        switch(overseasEntitySubmissionDto.getBeneficialOwnersStatement()) {
            case ALL_IDENTIFIED_ALL_DETAILS:
                 if (hasNoBeneficialOwners(overseasEntitySubmissionDto) || !hasNoManagingOfficers(overseasEntitySubmissionDto)) {
                     logValidationErrorMessage(errors, loggingContext, MISSING_OR_INCORRRECT_DATA_FOR_STATEMENT);
                     return false;
                 }
            case SOME_IDENTIFIED_ALL_DETAILS:
                if (hasNoBeneficialOwners(overseasEntitySubmissionDto)) {

                   return false;
                }
            case NONE_IDENTIFIED:
                if (!hasNoBeneficialOwners(overseasEntitySubmissionDto) || hasNoManagingOfficers(overseasEntitySubmissionDto)) {

                    return false;
                }
        }
        return true;
    }

    private boolean hasNoBeneficialOwners(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        return hasIndividualBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()) &&
                hasCorporateBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()) &&
                hasGovernmentOrPublicAuthorityBeneficialOwnersPresent(
                        overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority());
    }

    private boolean hasNoManagingOfficers(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        return hasIndividualManagingOfficersPresent(overseasEntitySubmissionDto.getManagingOfficersIndividual()) &&
               hasCorporateManagingOfficersPresent(overseasEntitySubmissionDto.getManagingOfficersCorporate());
    }

    private  boolean hasIndividualBeneficialOwnersPresent(List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList) {
        return Objects.nonNull(beneficialOwnerIndividualDtoList) && !beneficialOwnerIndividualDtoList.isEmpty();
    }

    private boolean hasCorporateBeneficialOwnersPresent(List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList) {
        return Objects.nonNull(beneficialOwnerCorporateDtoList) && !beneficialOwnerCorporateDtoList.isEmpty();
    }

    private boolean hasGovernmentOrPublicAuthorityBeneficialOwnersPresent(List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList) {
        return Objects.nonNull(beneficialOwnerGovernmentOrPublicAuthorityDtoList) && !beneficialOwnerGovernmentOrPublicAuthorityDtoList.isEmpty();
    }

    private boolean hasIndividualManagingOfficersPresent(List<ManagingOfficerIndividualDto> managingOfficersIndividualDtoList) {
        return Objects.nonNull(managingOfficersIndividualDtoList) && !managingOfficersIndividualDtoList.isEmpty();
    }

    private boolean hasCorporateManagingOfficersPresent(List<ManagingOfficerCorporateDto> managingOfficersCorporateDtoList) {
        return Objects.nonNull(managingOfficersCorporateDtoList) && !managingOfficersCorporateDtoList.isEmpty();
    }

    private void logValidationErrorMessage(Errors errors, String loggingContext, String errorMessage) {
        setErrorMsgToLocation(errors, OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT, errorMessage);
        ApiLogger.infoContext(loggingContext, errorMessage);
    }
}

