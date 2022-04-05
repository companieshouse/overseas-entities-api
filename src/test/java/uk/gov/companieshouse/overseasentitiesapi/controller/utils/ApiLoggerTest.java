package uk.gov.companieshouse.overseasentitiesapi.controller.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ApiLoggerTest {

    private static final String CONTEXT = "CONTEXT";
    private static final String TEST_MESSAGE = "TEST";
    private static final String LOG_MAP_KEY = "COMPANY_NUMBER";
    private static final String LOG_MAP_VALUE = "00006400";

    @InjectMocks
    private static ApiLogger apiLogger;

    private Map<String, Object> logMap;

    @BeforeEach
    void setup() {
        logMap = new HashMap<>();
        logMap.put(LOG_MAP_KEY, LOG_MAP_VALUE);
    }

    @Test
    void testDebugContextLoggingDoesNotModifyLogMap() {
        apiLogger.debugContext(CONTEXT, TEST_MESSAGE, logMap);

        assertEquals(1, logMap.size());
        assertEquals(LOG_MAP_VALUE, logMap.get(LOG_MAP_KEY));
    }

    @Test
    void testInfoLoggingDoesNotModifyLogMap() {
        apiLogger.info(TEST_MESSAGE, logMap);

        assertEquals(1, logMap.size());
        assertEquals(LOG_MAP_VALUE, logMap.get(LOG_MAP_KEY));
    }

    @Test
    void testInfoContextLoggingDoesNotModifyLogMap() {
        apiLogger.infoContext(CONTEXT, TEST_MESSAGE, logMap);

        assertEquals(1, logMap.size());
        assertEquals(LOG_MAP_VALUE, logMap.get(LOG_MAP_KEY));
    }

    @Test
    void testErrorContextLoggingDoesNotModifyLogMap() {
        apiLogger.errorContext(CONTEXT, TEST_MESSAGE, new Exception("TEST"), logMap);

        assertEquals(1, logMap.size());
        assertEquals(LOG_MAP_VALUE, logMap.get(LOG_MAP_KEY));
    }
}