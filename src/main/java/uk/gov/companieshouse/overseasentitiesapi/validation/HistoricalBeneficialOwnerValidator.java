package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBeneficialOwnerDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class HistoricalBeneficialOwnerValidator {
    public static final String PARENT_FIELD = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA,
            TrustDataDto.HISTORICAL_BO_FIELD);

    public Errors validate(List<TrustDataDto> trustDataDtoList, Errors errors, String loggingContext) {

        for (TrustDataDto trustDataDto : trustDataDtoList) {
            List<HistoricalBeneficialOwnerDto> historicalBeneficialOwners = trustDataDto
                    .getHistoricalBeneficialOwners();
            if (!CollectionUtils.isEmpty(historicalBeneficialOwners)) {
                for (HistoricalBeneficialOwnerDto historicalBeneficialOwnerDto : historicalBeneficialOwners) {
                    validateCeasedDate(historicalBeneficialOwnerDto.getCeasedDate(), errors, loggingContext);
                    validateNotifiedDate(historicalBeneficialOwnerDto.getNotifiedDate(), errors, loggingContext);
                    if (!historicalBeneficialOwnerDto.isCorporateIndicator()) {
                        validateForename(historicalBeneficialOwnerDto.getForename(), errors, loggingContext);
                        validateSurname(historicalBeneficialOwnerDto.getSurname(), errors, loggingContext);
                    } else {
                        validateCorporateName(historicalBeneficialOwnerDto.getCorporateName(), errors, loggingContext);
                    }

                }
            }
        }

        return errors;
    }

    private boolean validateForename(String forename, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, HistoricalBeneficialOwnerDto.FORENAME_FIELD);

        return StringValidators.isNotBlank(forename, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(forename, 160, qualifiedFieldName, errors,
                        loggingContext)
                && StringValidators.isValidCharacters(forename, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateSurname(String surname, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, HistoricalBeneficialOwnerDto.SURNAME_FIELD);

        return StringValidators.isNotBlank(surname, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(surname, 160, qualifiedFieldName, errors,
                        loggingContext)
                && StringValidators.isValidCharacters(surname, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCeasedDate(LocalDate ceasedDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, HistoricalBeneficialOwnerDto.CEASED_DATE_FIELD);

        return UtilsValidators.isNotNull(ceasedDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(ceasedDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateNotifiedDate(LocalDate notifiedDate, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.NOTIFIED_DATE_FIELD);

        return UtilsValidators.isNotNull(notifiedDate, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(notifiedDate, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCorporateName(String corporateName, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CORPORATE_NAME_FIELD);

        return StringValidators.isNotBlank(corporateName, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(corporateName, 160, qualifiedFieldName, errors,
                        loggingContext)
                && StringValidators.isValidCharacters(corporateName, qualifiedFieldName, errors, loggingContext);
    }

}
