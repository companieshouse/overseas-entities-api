package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    public Errors validate(List<TrustDataDto> trustDataDtoList, Errors errors, String loggingContext) {

        validateDuplicateId(trustDataDtoList, errors, loggingContext);

        for(TrustDataDto trustDataDto : trustDataDtoList) {
            validateName(trustDataDto.getTrustName(), errors, loggingContext);
            validateCreationDate(trustDataDto.getCreationDate(), errors, loggingContext);
            validateUnableToObtainAllTrustInfo(trustDataDto.getUnableToObtainAllTrustInfo(), errors, loggingContext);
        }

        return errors;
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

    private boolean validateCreationDate(LocalDate dateOfBirth, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CREATION_DATE_FIELD);

        return UtilsValidators.isNotNull(dateOfBirth, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(dateOfBirth, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateUnableToObtainAllTrustInfo(Boolean partialInfo, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD);

        return UtilsValidators.isNotNull(partialInfo, qualifiedFieldName, errors, loggingContext);
    }

}
