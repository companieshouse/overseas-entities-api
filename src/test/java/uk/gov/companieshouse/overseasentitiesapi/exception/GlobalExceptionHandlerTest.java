package uk.gov.companieshouse.overseasentitiesapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.WebRequest;
import uk.gov.companieshouse.api.util.security.EricConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    public static final String REQUEST_ID = "1234";
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

//    @Mock
//    private Exception exception;

    @BeforeEach
    void setUp() {
        this.globalExceptionHandler = new GlobalExceptionHandler();
        setTruncationLength(1000);
    }

    private void setTruncationLength(int length) {
        ReflectionTestUtils.setField(globalExceptionHandler, "truncationLength", length);
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
        Throwable rootCause = new Throwable("root cause");
        Exception exception = new Exception("exception message", rootCause);

        when(webRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQUEST_ID);

        ResponseEntity<Object> entity = globalExceptionHandler.handleException(exception, webRequest);

        assertNotNull(entity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
    }
}
