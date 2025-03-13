package uk.gov.companieshouse.overseasentitiesapi.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

public class Constants {

    private Constants() {
    }

    // Request header names
    public static final String ERIC_REQUEST_ID_KEY = "X-Request-Id";
    public static final String ERIC_IDENTITY = "ERIC-identity";
    public static final String ERIC_AUTHORISED_USER = "ERIC-Authorised-User";

    public static final String ERIC_AUTHORISED_TOKEN_PERMISSIONS
            = "ERIC-Authorised-Token-Permissions";

    // URI path attributes
    public static final String TRANSACTION_ID_KEY = "transaction_id";

    public static final String COMPANY_NUMBER_KEY = "company_number";

    public static final String TRUST_ID = "trust_id";

    public static final String OVERSEAS_ENTITY_COMPANY_NUMBER_PREFIX = "OE";

    public static final String OVERSEAS_ENTITY_ID_KEY = "overseas_entity_id";

    // Request attribute names
    public static final String TRANSACTION_KEY = "transaction";

    // URIs
    public static final String SUBMISSION_URI_PATTERN = "/transactions/%s/overseas-entity/%s";
    public static final String VALIDATION_STATUS_URI_SUFFIX = "/validation-status";
    public static final String COSTS_URI_SUFFIX = "/costs";
    public static final String TRANSACTIONS_PRIVATE_API_PREFIX = "/private/transactions/";
    public static final String TRANSACTIONS_PUBLIC_API_PREFIX = "/transactions/";
    public static final String RESUME_JOURNEY_URI_PATTERN = "/register-an-overseas-entity/transaction/%s/overseas-entity/%s/resume";

    public static final String UPDATE_RESUME_JOURNEY_URI_PATTERN = "/update-an-overseas-entity/transaction/%s/overseas-entity/%s/resume";

    // Filings
    public static final String FILING_KIND_OVERSEAS_ENTITY = "overseas-entity#registration";
    public static final String FILING_KIND_OVERSEAS_ENTITY_UPDATE = "overseas-entity#update";
    public static final String FILING_KIND_OVERSEAS_ENTITY_REMOVE = "overseas-entity#cessation";
    @Deprecated
    public static final String OLD_FILING_KIND_OVERSEAS_ENTITY = "overseas-entity";

    public static final String LINK_SELF = "self";
    public static final String LINK_RESOURCE = "resource";

    public static final int TRUNCATED_DATA_LENGTH = 50;

    public static final String CONCATENATED_STRING_FORMAT = "%s,%s";
    public static final String CONCATENATED_STRING_FORMAT_NO_COMMA = "%s%s";
    public static final SchemaVersion CURRENT_MONGO_SCHEMA_VERSION = SchemaVersion.VERSION_3_2;
}
