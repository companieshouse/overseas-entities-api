package uk.gov.companieshouse.overseasentitiesapi.validation;

public class ValidationMessages {

    private ValidationMessages() {}

    public static final String NOT_NULL_ERROR_MESSAGE = " must not be null";
    public static final String NOT_EMPTY_ERROR_MESSAGE = " must not be empty and must not only consist of whitespace";
    public static final String MAX_LENGTH_EXCEEDED_ERROR_MESSAGE = " must be %s characters or less";
    public static final String INVALID_CHARACTERS_ERROR_MESSAGE = " must only include letters a to z, numbers, and special characters such as hyphens, spaces and apostrophes";
    public static final String INVALID_EMAIL_ERROR_MESSAGE = "Enter an email address in the correct format for %s, like name@example.com";
}
