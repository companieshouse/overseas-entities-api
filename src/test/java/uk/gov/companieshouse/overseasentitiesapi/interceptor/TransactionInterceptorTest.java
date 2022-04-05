package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.TransactionService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

@ExtendWith(MockitoExtension.class)
class TransactionInterceptorTest {

    private static final String TX_ID = "12345678";
    private static final String PASSTHROUGH_HEADER = "passthrough";

    @Mock
    private TransactionService transactionService;

    @Mock
    private HttpServletRequest mockHttpServletRequest;
    @InjectMocks
    private TransactionInterceptor transactionInterceptor;

    @Test
    void testPreHandleIsSuccessful() throws Exception {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setId(TX_ID);

        var pathParams = new HashMap<String, String>();
        pathParams.put(TRANSACTION_ID_KEY, TX_ID);

        when(transactionService.getTransaction(TX_ID, PASSTHROUGH_HEADER)).thenReturn(dummyTransaction);
        when(mockHttpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathParams);
        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);

        assertTrue(transactionInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler));
        verify(mockHttpServletRequest, times(1)).setAttribute("transaction", dummyTransaction);
    }

    @Test
    void testPreHandleIsUnsuccessfulWhenServiceExceptionCaught() throws Exception {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Object mockHandler = new Object();

        var pathParams = new HashMap<String, String>();
        pathParams.put(TRANSACTION_ID_KEY, TX_ID);

        when(mockHttpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathParams);
        when(mockHttpServletRequest.getHeader("ERIC-Access-Token")).thenReturn(PASSTHROUGH_HEADER);
        when(transactionService.getTransaction(TX_ID, PASSTHROUGH_HEADER)).thenThrow(ServiceException.class);

        assertFalse(transactionInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler));
        assertEquals(500,  mockHttpServletResponse.getStatus());
    }
}
