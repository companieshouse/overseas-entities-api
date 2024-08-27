package uk.gov.companieshouse.overseasentitiesapi.utils;

import uk.gov.companieshouse.overseasentitiesapi.OverseasEntitiesApiApplication;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ApiLogger {

    private ApiLogger() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(OverseasEntitiesApiApplication.APP_NAMESPACE);

    public static void debug(String message, Map<String, Object> dataMap) {
        LOGGER.debug(message, cloneMapData(dataMap));
    }

    public static void debugContext(String context, String message) {
        LOGGER.debugContext(context, message, null);
    }

    public static void debugContext(String context, String message, Map<String, Object> dataMap) {
        LOGGER.debugContext(context, message, cloneMapData(dataMap));
    }

    public static void info(String message) {
        LOGGER.info(message, null);
    }

    public static void info(String message, Map<String, Object> dataMap) {
        LOGGER.info(message, cloneMapData(dataMap));
    }

    public static void infoContext(String context, String message) {
        LOGGER.infoContext(context, message, null);
    }

    public static void infoContext(String context, String message, Map<String, Object> dataMap) {
        LOGGER.infoContext(context, message, cloneMapData(dataMap));
    }

    public static void error(String message, Exception e, Map<String, Object> dataMap) {
        LOGGER.error(message, e, cloneMapData(dataMap));
    }

    public static void errorContext(String context, Exception e) {
        LOGGER.errorContext(context, e, null);
    }

    public static void errorContext(String context, String message, Exception e) {
        LOGGER.errorContext(context, message, e, null);
    }

    public static void errorContext(String context, String message, Exception e, Map<String, Object> dataMap) {
        LOGGER.errorContext(context, message, e, cloneMapData(dataMap));
    }

    /**
     * The Companies House logging implementation modifies the data map content which means that
     * if the same data map is used for subsequent calls any new message that might be passed in
     * is not displayed in certain log format outputs. Creating a clone of the data map gets around
     * this issue.
     *
     * @param dataMap The map data to log
     * @return A cloned copy of the map data
     */
    private static Map<String, Object> cloneMapData(Map<String, Object> dataMap) {
        if(dataMap == null) {
            dataMap = new HashMap<>();
        }

        return new HashMap<>(dataMap);
    }
}
