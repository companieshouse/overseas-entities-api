package uk.gov.companieshouse.overseasentitiesapi.utils;

public class Constants {

    private Constants() {
    }

    // Request header names
    public static final String ERIC_REQUEST_ID_KEY = "X-Request-Id";

    // URI path attributes
    public static final String TRANSACTION_ID_KEY = "transaction_id";
    public static final String OVERSEAS_ENTITY_ID_KEY = "overseas_entity_id";

    // Request attribute names
    public static final String TRANSACTION_KEY = "transaction";

    // URIs
    public static final String SUBMISSION_URI_PATTERN = "/transactions/%s/overseas-entity/%s";
    public static final String VALIDATION_STATUS_URI_SUFFIX = "/validation-status";
    public static final String TRANSACTIONS_PRIVATE_API_PREFIX = "/private/transactions/";
    public static final String TRANSACTIONS_PUBLIC_API_PREFIX = "/transactions/";

    // Filings
    public static final String FILING_KIND_OVERSEAS_ENTITY = "overseas-entity";
}
