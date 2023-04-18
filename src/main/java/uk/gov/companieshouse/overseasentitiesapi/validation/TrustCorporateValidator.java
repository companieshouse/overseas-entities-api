package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class TrustCorporateValidator {
    public static final String PARENT_FIELD = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA,
            TrustDataDto.CORPORATES_FIELD);

    public Errors validate(List<TrustDataDto> trustDataDtoList, Errors errors, String loggingContext) {

        for (TrustDataDto trustDataDto : trustDataDtoList) {
            List<TrustCorporateDto> corporates = trustDataDto.getCorporates();

            if (!CollectionUtils.isEmpty(corporates)) {
                for (TrustCorporateDto trustCorporateDto : corporates) {
                    validateName(trustCorporateDto.getName(), errors, loggingContext);
                }
            }
        }

        return errors;
    }

    private boolean validateName(String name, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);

        return StringValidators.isNotBlank(name, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(name, 160, qualifiedFieldName, errors,
                loggingContext)
                && StringValidators.isValidCharacters(name, qualifiedFieldName, errors, loggingContext);
    }
}
