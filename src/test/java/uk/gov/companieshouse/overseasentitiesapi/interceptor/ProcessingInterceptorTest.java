package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessingInterceptorTest {

    private static final String CREATED_BY_ID = "12345";

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @InjectMocks
    private ProcessingInterceptor processingInterceptor;

    private Transaction transaction;
    @BeforeEach
    void init() {
        transaction = new Transaction();
        transaction.setStatus(TransactionStatus.CLOSED);
        transaction.setCreatedBy(Collections.singletonMap("id", CREATED_BY_ID));
        when(mockHttpServletRequest.getAttribute("transaction")).thenReturn(transaction);
    }

    @Test
    void testInterceptorReturnsFalseWhenTransactionIsClosed() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertFalse(result);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenTransactionIsClosedPendingPaymentAndNotAGetRequest() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        transaction.setStatus(TransactionStatus.CLOSED_PENDING_PAYMENT);
        when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethod.POST.name());
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertFalse(result);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenTransactionIsDeleted() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        transaction.setStatus(TransactionStatus.DELETED);
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertFalse(result);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsTrueWhenTransactionIsStillOpen() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        transaction.setStatus(TransactionStatus.OPEN);
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsTrueWhenTransactionIsClosedPendingPaymentAndAGetRequest() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        transaction.setStatus(TransactionStatus.CLOSED_PENDING_PAYMENT);
        when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethod.GET.name());
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenNoTransactionFound() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        when(mockHttpServletRequest.getAttribute("transaction")).thenReturn(null);
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertFalse(result);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsTrueWhenForceFlagIsTrueAndGetRequest() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        when(mockHttpServletRequest.getParameter("force")).thenReturn("true");
        when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethod.GET.name());
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsTrueWhenForceFlagIsTrueAndPutRequest() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        when(mockHttpServletRequest.getParameter("force")).thenReturn("true");
        when(mockHttpServletRequest.getMethod()).thenReturn(HttpMethod.PUT.name());
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenForceFlagIsFalse() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        when(mockHttpServletRequest.getParameter("force")).thenReturn("false");
        var result = processingInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertFalse(result);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  mockHttpServletResponse.getStatus());
    }
}
