package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class TrustDetailsValidator {

    public Errors validate(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                           Errors errors,
                           String loggingContext,
                           boolean isFullValidation) {

        List<TrustDataDto> trustDataDtoList = overseasEntitySubmissionDto.getTrusts();

        validateDuplicateId(trustDataDtoList, errors, loggingContext);

        for(TrustDataDto trustDataDto : trustDataDtoList) {
            validateName(trustDataDto.getTrustName(), errors, loggingContext);
            validateCreationDate(trustDataDto.getCreationDate(), errors, loggingContext);

            if (isFullValidation) {
               validateTrustInvolvedInOverseasEntity(overseasEntitySubmissionDto, trustDataDto, errors, loggingContext);
               validateCeasedDate(overseasEntitySubmissionDto, trustDataDto, errors, loggingContext);
            }

            validateUnableToObtainAllTrustInfo(trustDataDto.getUnableToObtainAllTrustInfo(), errors, loggingContext);
        }

        return errors;
    }

    private boolean validateTrustInvolvedInOverseasEntity(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                          TrustDataDto trustDataDto,
                                                          Errors errors,
                                                          String loggingContext) {

        final String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.IS_TRUST_INVOLVED_IN_OE);

        if (trustDataDto.isTrustInvolvedInOverseasEntity() == null) {
            // Covers validation for when the yes or no has not been selected resulting in a null value
            final String errorMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
            return false;
        } else  if (trustDataDto.isTrustInvolvedInOverseasEntity() && noBeneficalOwenersArePresent(overseasEntitySubmissionDto)) {
            final String errorMessage = ValidationMessages.INVOLVED_IN_TRUST_WITHOUT_BENEFICIAL_OWNERS_ERROR_MESSAGE;
            setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
            return false;
        }

        return true;
    }

    private boolean validateDuplicateId(List<TrustDataDto> trustDataDtoList, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA,
                TrustDataDto.TRUST_ID_FIELD);
        Set<String> findDuplicates = new HashSet<>();

        for (TrustDataDto trustDataDto : trustDataDtoList) {
            String trustId = trustDataDto.getTrustId();

            if (!StringValidators.isNotBlank(trustId, qualifiedFieldName, errors, loggingContext)) {
                return false;
            }

            if (!findDuplicates.add(trustDataDto.getTrustId().trim())) {
                String message = ValidationMessages.DUPLICATE_TRUST_ID.replace("%s", trustDataDto.getTrustName());
                UtilsValidators.setErrorMsgToLocation(errors, qualifiedFieldName, message);
                ApiLogger.infoContext(loggingContext, qualifiedFieldName + " " + message);

                return false;
            }
        }

        return true;
    }

    private boolean validateName(String trustName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);

        return StringValidators.isNotBlank(trustName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(trustName, 160, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(trustName, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCreationDate(LocalDate creationDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CREATION_DATE_FIELD);

        return UtilsValidators.isNotNull(creationDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(creationDate, qualifiedFieldName, errors, loggingContext);
    }

    private void validateCeasedDate(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                   TrustDataDto trustDataDto,
                                   Errors errors,
                                   String loggingContext) {
        final String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CEASED_DATE_FIELD);
        final LocalDate trustCeasedDate = trustDataDto.getCeasedDate();

        if (isTrustStillRequired(overseasEntitySubmissionDto, trustDataDto.getTrustId(), trustDataDto.isTrustInvolvedInOverseasEntity())) {
            // Cease date of the trust must be 'null' as there are still Individual and/or Corporate BOs associated with this trust
            if (Objects.nonNull(trustCeasedDate)) {
                final String errorMessage = ValidationMessages.NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
                setErrorMsgToLocation(errors, qualifiedFieldName, errorMessage);
                ApiLogger.infoContext(loggingContext, errorMessage);
            }
        } else {
            final LocalDate creationDate = trustDataDto.getCreationDate();

            // Cease date of the trust cannot be 'null' as there are no longer any Individual or Corporate BOs associated with this trust
            if (UtilsValidators.isNotNull(trustCeasedDate, qualifiedFieldName, errors, loggingContext)) {
                DateValidators.isDateInPast(trustCeasedDate, qualifiedFieldName, errors, loggingContext);
                DateValidators.isCeasedDateOnOrAfterCreationDate(trustCeasedDate, creationDate, qualifiedFieldName, errors, loggingContext);

                if (trustDataDto.getIndividuals() != null && !trustDataDto.getIndividuals().isEmpty()) {
                    DateValidators.isCeasedDateOnOrAfterIndividualsDateOfBirth(trustCeasedDate, trustDataDto.getIndividuals(), qualifiedFieldName, errors, loggingContext);
                }
           }
        }
    }

    private boolean noBeneficalOwenersArePresent(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        boolean allIndividualsDisassociated = overseasEntitySubmissionDto.getBeneficialOwnersIndividual() != null
                && overseasEntitySubmissionDto.getBeneficialOwnersIndividual().stream().anyMatch(
                boIndividualDto -> boIndividualDto.getTrustIds() == null
                        || boIndividualDto.getCeasedDate() != null
                        || boIndividualDto.getTrusteesNatureOfControlTypes() == null
                        || boIndividualDto.getTrusteesNatureOfControlTypes().isEmpty());


        boolean allCoporatesDisassociated = overseasEntitySubmissionDto.getBeneficialOwnersCorporate() != null
                && overseasEntitySubmissionDto.getBeneficialOwnersCorporate().stream().anyMatch(
                boCorporateDto -> boCorporateDto.getTrustIds() == null
                        || boCorporateDto.getCeasedDate() != null
                        || boCorporateDto.getTrusteesNatureOfControlTypes() == null
                        || boCorporateDto.getTrusteesNatureOfControlTypes().isEmpty());

        return allIndividualsDisassociated && allCoporatesDisassociated;
    }

    private boolean isTrustStillRequired(OverseasEntitySubmissionDto overseasEntitySubmissionDto, String trustId, Boolean istrustInvolvedInOverseasEntity) {
        // Use the trust id to whizz through all the Beneficial Owners that are associated with this trust. As soon as a
        // matching BO is found where cease date is null and a trust NOC is set then this indicates that the trust is
        // still required

        if ((istrustInvolvedInOverseasEntity != null && !istrustInvolvedInOverseasEntity) || trustId == null) {
            return false;
        }

        // First check the Individual BOs:
        if (overseasEntitySubmissionDto.getBeneficialOwnersIndividual() != null
                && overseasEntitySubmissionDto.getBeneficialOwnersIndividual().stream().anyMatch(
                        boIndividualDto -> boIndividualDto.getTrustIds() != null
                            && boIndividualDto.getTrustIds().contains(trustId)
                            && boIndividualDto.getCeasedDate() == null
                            && boIndividualDto.getTrusteesNatureOfControlTypes() != null
                            && !boIndividualDto.getTrusteesNatureOfControlTypes().isEmpty())) {
            return true;
        }

        // And now the Corporate BOs:
        return (overseasEntitySubmissionDto.getBeneficialOwnersCorporate() != null
                && overseasEntitySubmissionDto.getBeneficialOwnersCorporate().stream().anyMatch(
                        boCorporateDto -> boCorporateDto.getTrustIds() != null
                            && boCorporateDto.getTrustIds().contains(trustId)
                            && boCorporateDto.getCeasedDate() == null
                            && boCorporateDto.getTrusteesNatureOfControlTypes() != null
                            && !boCorporateDto.getTrusteesNatureOfControlTypes().isEmpty()));
    }

    private boolean validateUnableToObtainAllTrustInfo(Boolean partialInfo, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD);

        return UtilsValidators.isNotNull(partialInfo, qualifiedFieldName, errors, loggingContext);
    }
}
