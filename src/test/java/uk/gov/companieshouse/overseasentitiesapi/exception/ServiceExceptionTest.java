package uk.gov.companieshouse.overseasentitiesapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceExceptionTest {
    @Test
    void testServiceException() {
        String msg = "message";
        var exception = new ServiceException(msg);
        assertEquals(msg, exception.getMessage());
    }
}
