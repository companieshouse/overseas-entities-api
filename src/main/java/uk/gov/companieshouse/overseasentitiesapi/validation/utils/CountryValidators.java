package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

public class CountryValidators {

    public static boolean checkListofCountries(String parentAddressField, String country, String qualifiedFieldName, Errors errors, String loggingContext) {

        String entityPrincipalAdddressPath = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.PRINCIPAL_ADDRESS_FIELD);
        String entityServiceAdddressPath = getQualifiedFieldName(OverseasEntitySubmissionDto.ENTITY_FIELD, EntityDto.SERVICE_ADDRESS_FIELD);
        String oeDueDiligenceAdddressPath = getQualifiedFieldName(OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE, OverseasEntityDueDiligenceDto.IDENTITY_ADDRESS_FIELD);

        boolean isValid = false;
        if (!(parentAddressField.equalsIgnoreCase(entityPrincipalAdddressPath) ||
                parentAddressField.equalsIgnoreCase(entityServiceAdddressPath))) {
            isValid = UkCountry.isValid(country);
        }

        if (!isValid && !parentAddressField.equalsIgnoreCase(oeDueDiligenceAdddressPath)) {
            isValid = Country.isValid(country);
        }

        if(!isValid) {
            setErrorMsgToLocation(errors, qualifiedFieldName, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext, ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE);
        }
        return isValid;
    }
}
