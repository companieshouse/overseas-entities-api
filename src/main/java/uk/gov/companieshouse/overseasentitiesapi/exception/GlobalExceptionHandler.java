package uk.gov.companieshouse.overseasentitiesapi.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.HashMap;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${GLOBAL_EXCEPTION_HANDLER_TRUNCATE_LENGTH_CHARS:15000}")
    private int truncationLength;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest webRequest) {
        var context = webRequest.getHeader(ERIC_REQUEST_ID_KEY);
        var exceptionMessage = truncate(Encode.forJava(ex.getMessage()));
        var encodedStackTrace = truncate(Encode.forJava(ExceptionUtils.getStackTrace(ex)));
        var encodedRootCause = truncate(Encode.forJava(ExceptionUtils.getStackTrace(ExceptionUtils.getRootCause(ex))));

        HashMap<String, Object> logMap = new HashMap<>();
        logMap.put("error", ex.getClass());
        logMap.put("stackTrace", encodedStackTrace);
        logMap.put("rootCause", encodedRootCause);

        ApiLogger.errorContext(context, exceptionMessage, null, logMap);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String truncate(String input) {
        if (Objects.isNull(input)) {
            return null;
        }
        return StringUtils.truncate(input, truncationLength);
    }

}
