package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

public class ValidationMessages {

    private ValidationMessages() {}

    public static final String NULL_ERROR_MESSAGE = "%s must be null";
    public static final String NOT_NULL_ERROR_MESSAGE = "%s must not be null";
    public static final String NOT_EMPTY_ERROR_MESSAGE = "%s must not be empty and must not only consist of whitespace";
    public static final String NOT_VALID_ERROR_MESSAGE = "%s is not a valid value";
    public static final String SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE = "%s should not be populated";
    public static final String SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE = "%s should not both be present, only one should be present";
    public static final String SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE = "%s should not both be absent, one should be present";
    public static final String SHOULD_BE_AGREE_ERROR_MESSAGE = "%s Field should be agree";
    public static final String MAX_LENGTH_EXCEEDED_ERROR_MESSAGE = " must be %s characters or less";
    public static final String INVALID_CHARACTERS_ERROR_MESSAGE = "%s must only include letters a to z, numbers, and special characters such as hyphens, spaces and apostrophes";
    public static final String INVALID_EMAIL_ERROR_MESSAGE = "Email address is not in the correct format for %s, like name@example.com";
    public static final String DATE_NOT_IN_PAST_ERROR_MESSAGE = "%s must be in the past";
    public static final String DATE_NOT_ON_OR_BEFORE_MUD_ERROR_MESSAGE = "%s must be on or before the entity's next 'made up to' date (%s)";
    public static final String DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE = "%s must be in the past 3 months";
    public static final String COUNTRY_NOT_ON_LIST_ERROR_MESSAGE = "%s is not on the list of allowed countries";
    public static final String NATIONALITY_NOT_ON_LIST_ERROR_MESSAGE = "%s is not on the list of nationalities";
    public static final String SECOND_NATIONALITY_SHOULD_BE_DIFFERENT = "%s should not be the same as the nationality given";
    public static final String DUPLICATE_TRUST_ID = "Duplicate Trust Id for %s";
    public static final String CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE = "%s must be on or after the appointed date";
    public static final String CEASED_DATE_BEFORE_CREATION_DATE_ERROR_MESSAGE = "%s must be on or after the creation date";
    public static final String TRUST_INDIVIDUAL_TYPE_ERROR_MESSAGE = "Individual trustee type must be one of these values: Beneficiary, Settlor, Grantor and Interested person for %s";
    public static final String TRUST_CORPORATE_TYPE_ERROR_MESSAGE = "Corporate trustee type must be one of these values: Beneficiary, Settlor, Grantor and Interested person for %s";
}
