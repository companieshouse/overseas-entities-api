package uk.gov.companieshouse.overseasentitiesapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.WebRequest;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String REQUEST_ID = "1234";
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Captor
    private ArgumentCaptor<Map<String, Object>> logMapCaptor;

    @BeforeEach
    void setUp() {
        this.globalExceptionHandler = new GlobalExceptionHandler();
        setTruncationLength(1000);
    }

    @Test
    void testHandleExceptionReturnsCorrectResponse() {
        when(webRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQUEST_ID);

        ResponseEntity<Object> entity = globalExceptionHandler.handleException(new Exception(), webRequest);

        assertNotNull(entity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
    }

    @Test
    void testHandleExceptionEncodesException() {
        Throwable rootCause = new Throwable("root cause \n");
        Exception exception = new Exception("exception message \n", rootCause);

        when(webRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQUEST_ID);

        try (MockedStatic<ApiLogger> apiLogger = mockStatic(ApiLogger.class)) {

            globalExceptionHandler.handleException(exception, webRequest);

            apiLogger.verify(() -> ApiLogger.errorContext(
                    eq(REQUEST_ID),
                    eq("exception message \\n"),
                    eq(null),
                    logMapCaptor.capture()), times(1));

            Map<String, Object> logMap = logMapCaptor.getValue();
            String stackTraceString = (String)logMap.get("stackTrace");
            assertTrue(stackTraceString.contains("exception message \\n"));

            String rootCauseString = (String)logMap.get("rootCause");
            assertTrue(rootCauseString.contains("root cause \\n"));
        }
    }

    @Test
    void testHandleExceptionTruncatesException() {
        setTruncationLength(20);
        Throwable rootCause = new Throwable("root cause");
        Exception exception = new Exception("12345678901234567890123", rootCause);

        when(webRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQUEST_ID);

        try (MockedStatic<ApiLogger> apiLogger = mockStatic(ApiLogger.class)) {
            globalExceptionHandler.handleException(exception, webRequest);

            apiLogger.verify(() -> ApiLogger.errorContext(
                    eq(REQUEST_ID),
                    eq("12345678901234567890"),
                    eq(null),
                    logMapCaptor.capture()), times(1));

            Map<String, Object> logMap = logMapCaptor.getValue();
            String stackTraceString = (String)logMap.get("stackTrace");
            assertEquals(20, stackTraceString.length());

            String rootCauseString = (String)logMap.get("rootCause");
            assertEquals(20, rootCauseString.length());
        }
    }

    private void setTruncationLength(int length) {
        ReflectionTestUtils.setField(globalExceptionHandler, "truncationLength", length);
    }
}
