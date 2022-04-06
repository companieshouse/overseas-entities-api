package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final String TRANSACTION_ID = "12345678";
    private static final String PASSTHROUGH_HEADER = "passthrough";

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private TransactionsResourceHandler transactionsResourceHandler;

    @Mock
    private TransactionsGet transactionsGet;

    @Mock
    private ApiResponse<Transaction> apiResponse;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGettingATransactionIsSuccessful() throws ServiceException, IOException, URIValidationException {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(apiClientService.getOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.get("/transactions/" + TRANSACTION_ID)).thenReturn(transactionsGet);
        when(transactionsGet.execute()).thenReturn(apiResponse);
        when(apiResponse.getData()).thenReturn(transaction);

        var response = transactionService.getTransaction(TRANSACTION_ID, PASSTHROUGH_HEADER);

        assertEquals(transaction, response);
    }

    @Test
    void testServiceExceptionThrownWhenTransactionSdkThrowsURIValidationException() throws IOException, URIValidationException {
        when(apiClientService.getOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.get("/transactions/" + TRANSACTION_ID)).thenReturn(transactionsGet);
        when(transactionsGet.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            transactionService.getTransaction(TRANSACTION_ID, PASSTHROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenTransactionSdkThrowsIOException() throws IOException, URIValidationException {
        when(apiClientService.getOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.get("/transactions/" + TRANSACTION_ID)).thenReturn(transactionsGet);
        when(transactionsGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            transactionService.getTransaction(TRANSACTION_ID, PASSTHROUGH_HEADER);
        });
    }
}