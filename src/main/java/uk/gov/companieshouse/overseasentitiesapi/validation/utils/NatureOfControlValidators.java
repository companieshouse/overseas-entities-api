package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

public class NatureOfControlValidators {

    private NatureOfControlValidators() {}

    // UAR-1595 Temporary wrapper method
    public static boolean checkAtLeastOneFieldHasValue(List<NatureOfControlType> fields, String qualifiedFieldName, Errors errs, String loggingContext) {
        return checkAtLeastOneFieldHasValue(fields, null, qualifiedFieldName, false, errs, loggingContext);
    }

    // UAR-1595 remove last feature flag parameter when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
    public static boolean checkAtLeastOneFieldHasValue(List<NatureOfControlType> fields, List<NatureOfControlJurisdictionType> jurisdictionFields, String qualifiedFieldName, boolean isPropertyAndLandNocEnabled, Errors errs, String loggingContext) {

        boolean isInvalid = (isPropertyAndLandNocEnabled)? fields.isEmpty() && jurisdictionFields.isEmpty() : fields.isEmpty();
        if (isInvalid) {
            setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext , qualifiedFieldName + " Field is empty");
            return false;
        }
        return true;
    }
}
