package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

@Component
public class OwnersAndOfficersDataBlockValidator {

    public static final String MISSING_BENEFICIAL_OWNER = "Missing beneficial owner";
    public static final String MISSING_MANAGING_OFFICER = "Missing managing officer";
    public static final String INCORRECTLY_ADDED_BENEFICIAL_OWNER = "Incorrectly added beneficial owner";
    public static final String INCORRECTLY_ADDED_MANAGING_OFFICER = "Incorrectly added managing officer";
    public static final String INVALID_BENEFICIAL_OWNER_STATEMENT = "Invalid statement supplied";
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
                 if (!hasBeneficialOwners(overseasEntitySubmissionDto)) {
                     logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that all can be identified", MISSING_BENEFICIAL_OWNER));
                     return false;
                 }
                 if (hasManagingOfficers(overseasEntitySubmissionDto)) {
                     logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that all can be identified", INCORRECTLY_ADDED_MANAGING_OFFICER));
                     return false;
                 }
                 break;
            case SOME_IDENTIFIED_ALL_DETAILS:
                if (!hasBeneficialOwners(overseasEntitySubmissionDto)) {
                    logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER));
                    return false;
                }
                break;
            case NONE_IDENTIFIED:
                if (!hasManagingOfficers(overseasEntitySubmissionDto)) {
                    logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that none can be identified", MISSING_MANAGING_OFFICER));
                    return false;
                }
                if (hasBeneficialOwners(overseasEntitySubmissionDto)) {
                    logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that none can be identified", INCORRECTLY_ADDED_BENEFICIAL_OWNER));
                    return false;
                }
                break;
            default:
                logValidationErrorMessage(errors, loggingContext, INVALID_BENEFICIAL_OWNER_STATEMENT);
                return false;

        }
        return true;
    }

    private boolean hasBeneficialOwners(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        return hasIndividualBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()) ||
                hasCorporateBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()) ||
                hasGovernmentOrPublicAuthorityBeneficialOwnersPresent(
                        overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority());
    }

    private boolean hasManagingOfficers(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        return hasIndividualManagingOfficersPresent(overseasEntitySubmissionDto.getManagingOfficersIndividual()) ||
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

