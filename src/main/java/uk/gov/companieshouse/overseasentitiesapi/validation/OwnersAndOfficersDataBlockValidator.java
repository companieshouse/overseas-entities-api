package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

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
    public static final String MISSING_BENEFICIAL_OWNER_STATEMENT = "Unable to validate owner officer combination due to missing beneficial owners statement";
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

    public void validateOwnersAndOfficersAgainstStatement(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        beneficialOwnersStatementValidator.validate(overseasEntitySubmissionDto.getBeneficialOwnersStatement(), errors, loggingContext);
        if (isCorrectCombinationOfOwnersAndOfficersForStatement(overseasEntitySubmissionDto, errors, loggingContext)) {
            validateOwnersAndOfficers(overseasEntitySubmissionDto, errors, loggingContext);
        }
    }

    public void validateOwnersAndOfficers(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {

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

    private boolean isCorrectCombinationOfOwnersAndOfficersForStatement(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        var beneficialOwnersStatementType =  overseasEntitySubmissionDto.getBeneficialOwnersStatement();
        if (beneficialOwnersStatementType == null) {
            logValidationErrorMessage(errors, loggingContext, MISSING_BENEFICIAL_OWNER_STATEMENT);
            return false;
        }
        switch (overseasEntitySubmissionDto.getBeneficialOwnersStatement()) {
            case ALL_IDENTIFIED_ALL_DETAILS:
                 if (!hasActiveBeneficialOwners(overseasEntitySubmissionDto)) {
                     logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that all can be identified", MISSING_BENEFICIAL_OWNER));
                     return false;
                 }
                 if (hasActiveManagingOfficers(overseasEntitySubmissionDto)) {
                     logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that all can be identified", INCORRECTLY_ADDED_MANAGING_OFFICER));
                     return false;
                 }
                 break;
            case SOME_IDENTIFIED_ALL_DETAILS:
                if (!hasActiveBeneficialOwners(overseasEntitySubmissionDto)) {
                    logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that some can be identified", MISSING_BENEFICIAL_OWNER));
                    return false;
                }
                if(!hasActiveManagingOfficers(overseasEntitySubmissionDto)) {
                    logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that some can be identified", MISSING_MANAGING_OFFICER));
                    return false;
                }
                break;
            case NONE_IDENTIFIED:
                if (!hasActiveManagingOfficers(overseasEntitySubmissionDto)) {
                    logValidationErrorMessage(errors, loggingContext, String.format("%s for statement that none can be identified", MISSING_MANAGING_OFFICER));
                    return false;
                }
                if (hasActiveBeneficialOwners(overseasEntitySubmissionDto)) {
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

    public boolean validateRegistrableBeneficialOwnerStatement(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        var isRegistrableBeneficialOwnersAddedOrCeased = isRegistrableBeneficialOwnersAddedOrCeased(overseasEntitySubmissionDto, errors, loggingContext);
        if (!overseasEntitySubmissionDto.getUpdate().isRegistrableBeneficialOwner()) {
            if (isRegistrableBeneficialOwnersAddedOrCeased){
                logValidationErrorMessage(errors, loggingContext, String.format("%s for statement", "Benefical owners have been added or ceased"));
                return false;
            }
        } else {
            if (!isRegistrableBeneficialOwnersAddedOrCeased){
                logValidationErrorMessage(errors, loggingContext, String.format("%s for statement", "No beneficial owners have been added or ceased"));
                return false;
            }
        }
        return true;
    }

    public boolean isRegistrableBeneficialOwnersAddedOrCeased(OverseasEntitySubmissionDto overseasEntitySubmissionDto, Errors errors, String loggingContext) {
        var emptyBeneficialOwnerArray = false;
        var anyIndividualsAddedOrCeased = hasIndividualBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()) ? overseasEntitySubmissionDto.getBeneficialOwnersIndividual().stream().anyMatch(o -> Boolean.FALSE == o.getRelevantPeriod() && (StringUtils.isEmpty(o.getChipsReference()) || o.getCeasedDate() != null)) : emptyBeneficialOwnerArray;
        var anyCorporateAddedOrCeased = hasCorporateBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()) ? overseasEntitySubmissionDto.getBeneficialOwnersCorporate().stream().anyMatch(o -> Boolean.FALSE == o.getRelevantPeriod() && (StringUtils.isEmpty(o.getChipsReference()) || o.getCeasedDate() != null)) : emptyBeneficialOwnerArray;
        var anyGovOrPublicAuthorityAddedOrCeased = hasGovernmentOrPublicAuthorityBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()) ? overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority().stream().anyMatch(o -> Boolean.FALSE == o.getRelevantPeriod() && (StringUtils.isEmpty(o.getChipsReference()) || o.getCeasedDate() != null)) : emptyBeneficialOwnerArray;
        var anyBeneficialOwnersAddedOrCeased = (anyIndividualsAddedOrCeased || anyCorporateAddedOrCeased || anyGovOrPublicAuthorityAddedOrCeased);
        return anyBeneficialOwnersAddedOrCeased;
    }

    private boolean hasActiveBeneficialOwners(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var emptyBeneficialOwnerArray = false;
        var anyActiveIndividuals = hasIndividualBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersIndividual()) ? overseasEntitySubmissionDto.getBeneficialOwnersIndividual().stream().anyMatch(o -> o.getCeasedDate() == null) : emptyBeneficialOwnerArray;
        var anyActiveCorporate = hasCorporateBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersCorporate()) ? overseasEntitySubmissionDto.getBeneficialOwnersCorporate().stream().anyMatch(o -> o.getCeasedDate() == null) : emptyBeneficialOwnerArray;
        var anyActiveGovernmentOrPublicAuthority = hasGovernmentOrPublicAuthorityBeneficialOwnersPresent(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority()) ? overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority().stream().anyMatch(o -> o.getCeasedDate() == null) : emptyBeneficialOwnerArray;
        return (anyActiveCorporate || anyActiveGovernmentOrPublicAuthority || anyActiveIndividuals);
    }

    private boolean hasActiveManagingOfficers(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var emptyManagingOfficerArray = false;
        var anyActiveCorporateManagingOfficers = hasCorporateManagingOfficersPresent(overseasEntitySubmissionDto.getManagingOfficersCorporate()) ? overseasEntitySubmissionDto.getManagingOfficersCorporate().stream().anyMatch(o -> o.getResignedOn() == null) : emptyManagingOfficerArray;
        var anyActiveIndividualManagingOfficers = hasIndividualManagingOfficersPresent(overseasEntitySubmissionDto.getManagingOfficersIndividual()) ? overseasEntitySubmissionDto.getManagingOfficersIndividual().stream().anyMatch(o -> o.getResignedOn() == null) : emptyManagingOfficerArray;
        return (anyActiveCorporateManagingOfficers || anyActiveIndividualManagingOfficers);
            
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
