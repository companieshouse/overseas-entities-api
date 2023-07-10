package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.util.security.Permission;
import uk.gov.companieshouse.api.util.security.SecurityConstants;
import uk.gov.companieshouse.api.util.security.TokenPermissions;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.TransactionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationInterceptorTest {

    private static final String TX_ID = "12345678";
    private static final String REQ_ID = "43hj5jh345";
    private static final String PASSTHROUGH_HEADER = "passthrough";

    private static final String VALID_OE_NUMBER = "OE006400";

    private static final String INVALID_OE_NUMBER = "00006400";
    private static final String TOKEN_PERMISSIONS = "token_permissions";
    public static final String ERIC_IDENTITY_TYPE = "ERIC-Identity-Type";


    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private TokenPermissions mockTokenPermissions;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private UserAuthenticationInterceptor userAuthenticationInterceptor;

    @BeforeEach
    void init() {
        var pathParams = new HashMap<String, String>();
        pathParams.put(TRANSACTION_ID_KEY, TX_ID);
        when(mockHttpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathParams);
    }

    @Test
    void testInterceptorReturnsTrueWhenRequestHasCorrectRegistrationTokenPermission() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(null);

        when(mockHttpServletRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQ_ID);
        when(mockHttpServletRequest.getHeader(ERIC_IDENTITY_TYPE)).thenReturn(null);
        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);

        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);
        when(mockTokenPermissions.hasPermission(Permission.Key.COMPANY_INCORPORATION, Permission.Value.CREATE)).thenReturn(true);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenTransactionHasNullCompanyNumberIncorrectRegPermissions() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(null);


        when(mockHttpServletRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQ_ID);
        when(mockHttpServletRequest.getHeader(ERIC_IDENTITY_TYPE)).thenReturn(null);
        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);
        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);
        when(mockTokenPermissions.hasPermission(Permission.Key.COMPANY_INCORPORATION, Permission.Value.CREATE)).thenReturn(false);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsTrueWhenRequestHasCorrectRoeUpdateTokenPermission() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(VALID_OE_NUMBER);

        ericTokenSet(true);

        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);
        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);
        when(mockTokenPermissions.hasPermission(Permission.Key.COMPANY_OE_ANNUAL_UPDATE, Permission.Value.CREATE)).thenReturn(true);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenRequestHasIncorrectRoeUpdateTokenPermission() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(VALID_OE_NUMBER);

        ericTokenSet(true);

        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);
        when(mockTokenPermissions.hasPermission(Permission.Key.COMPANY_OE_ANNUAL_UPDATE, Permission.Value.CREATE)).thenReturn(false);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED,  mockHttpServletResponse.getStatus());
    }


    @Test
    void testInterceptorReturnsFalseWhenCompanyInScopeNullCorrectUpdatePermissions() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(VALID_OE_NUMBER);

        ericTokenSet(false);

        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);
        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);


        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenCompanyNumberInTransactionAndScopeDoesNotMatch() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(INVALID_OE_NUMBER);

        ericTokenSet(true);

        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);
        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED,  mockHttpServletResponse.getStatus());
    }

    @Test
    void testInterceptorReturnsFalseWhenCompanyInScopeHasInvalidOENumberIncorrectPermissions() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);
        dummyTransaction.setCompanyNumber(VALID_OE_NUMBER);


        String tokenValue = "company_number=" + INVALID_OE_NUMBER;
        setStubbing(tokenValue);

        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(eq(TX_ID), eq(PASSTHROUGH_HEADER), any())).thenReturn(dummyTransaction);
        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED,  mockHttpServletResponse.getStatus());
    }


    @Test
    void testInterceptorReturns500StatusWhenTransactionCallFails() throws ServiceException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        when(mockHttpServletRequest.getAttribute(TOKEN_PERMISSIONS)).thenReturn(mockTokenPermissions);
        when(transactionService.getTransaction(any(), any(), any())).thenThrow(new ServiceException("Transaction Service Mock"));
        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  mockHttpServletResponse.getStatus());
    }


    @Test
    void testInterceptorReturnsTrueWhenAnApiKeyIsUsed() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        when(mockHttpServletRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQ_ID);
        when(mockHttpServletRequest.getHeader(ERIC_IDENTITY_TYPE)).thenReturn(SecurityConstants.API_KEY_IDENTITY_TYPE);

        var result = userAuthenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK,  mockHttpServletResponse.getStatus());
    }

    private void ericTokenSet(boolean companyExists) {
        String tokenValue = null;
        if (companyExists) {
            tokenValue = "company_number=" + VALID_OE_NUMBER;
        }
        setStubbing(tokenValue);
    }


    private void setStubbing(String companyNumberToken) {
        when(mockHttpServletRequest.getHeader(ERIC_REQUEST_ID_KEY)).thenReturn(REQ_ID);
        when(mockHttpServletRequest.getHeader(ERIC_IDENTITY_TYPE)).thenReturn(null);
        when(mockHttpServletRequest.getHeader("ERIC-Authorised-Token-Permissions")).thenReturn(companyNumberToken);
    }
}
