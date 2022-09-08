package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

public class ValidationUtils {

    public static String getQualifiedFieldName(String parentFieldName, String fieldName) {
        return String.format("%s.%s", parentFieldName, fieldName);
    }
}
