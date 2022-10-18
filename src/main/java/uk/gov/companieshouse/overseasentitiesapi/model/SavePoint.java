package uk.gov.companieshouse.overseasentitiesapi.model;

/**
 * This indicates the point in the overseas entity registration journey that *valid* data is being saved at. Validation
 * checks run against all the data up to this point must pass for the save to be successful.
 */
public enum SavePoint {
    PRESENTER,

    // TODO Add further save point identifiers as more are supported

    /**
     * This is the final save point - ALL validation checks must pass.
     */
    FINAL_CHECK
}
