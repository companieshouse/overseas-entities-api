package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

public class ValidationMessages {

    private ValidationMessages() {}

    public static final String NOT_NULL_ERROR_MESSAGE = " must not be null";
    public static final String NOT_EMPTY_ERROR_MESSAGE = " must not be empty and must not only consist of whitespace";
    public static final String MAX_LENGTH_EXCEEDED_ERROR_MESSAGE = " must be %s characters or less";
    public static final String INVALID_CHARACTERS_ERROR_MESSAGE = " must only include letters a to z, numbers, and special characters such as hyphens, spaces and apostrophes";
    public static final String INVALID_EMAIL_ERROR_MESSAGE = "Email address is not in the correct format for %s, like name@example.com";
    public static final String DATE_NOT_IN_PAST_ERROR_MESSAGE = " must be in the past";
    public static final String DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE = " must be in the past 3 months";

    public static final String COUNTRY = "Select a country from the list";

}
