package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.privatetransaction.PrivateTransactionResourceHandler;
import uk.gov.companieshouse.api.handler.privatetransaction.request.PrivateTransactionPatch;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final String TRANSACTION_ID = "12345678";
    private static final String PASSTHROUGH_HEADER = "passthrough";
    private static final String LOGGING_CONTEXT = "fg4536";

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private InternalApiClient internalApiClient;

    @Mock
    private TransactionsResourceHandler transactionsResourceHandler;

    @Mock
    private PrivateTransactionResourceHandler privateTransactionResourceHandler;

    @Mock
    private TransactionsGet transactionsGet;

    @Mock
    private PrivateTransactionPatch privateTransactionPatch;

    @Mock
    private ApiResponse<Transaction> apiGetResponse;

    @Mock
    private ApiResponse<Void> apiPatchResponse;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGettingATransactionIsSuccessful() throws ServiceException, IOException, URIValidationException {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(apiClientService.getOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.get("/transactions/" + TRANSACTION_ID)).thenReturn(transactionsGet);
        when(transactionsGet.execute()).thenReturn(apiGetResponse);
        when(apiGetResponse.getData()).thenReturn(transaction);

        var response = transactionService.getTransaction(TRANSACTION_ID, PASSTHROUGH_HEADER, LOGGING_CONTEXT);

        assertEquals(transaction, response);
    }

    @Test
    void testServiceExceptionThrownWhenTransactionSdkThrowsURIValidationException() throws IOException, URIValidationException {
        when(apiClientService.getOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.get("/transactions/" + TRANSACTION_ID)).thenReturn(transactionsGet);
        when(transactionsGet.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            transactionService.getTransaction(TRANSACTION_ID, PASSTHROUGH_HEADER, LOGGING_CONTEXT);
        });
    }

    @Test
    void testServiceExceptionThrownWhenTransactionSdkThrowsIOException() throws IOException, URIValidationException {
        when(apiClientService.getOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.get("/transactions/" + TRANSACTION_ID)).thenReturn(transactionsGet);
        when(transactionsGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            transactionService.getTransaction(TRANSACTION_ID, PASSTHROUGH_HEADER, LOGGING_CONTEXT);
        });
    }

    @Test
    void testUpdatingATransactionIsSuccessful() throws IOException, URIValidationException {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(apiClientService.getInternalOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(internalApiClient);
        when(internalApiClient.privateTransaction()).thenReturn(privateTransactionResourceHandler);
        when(privateTransactionResourceHandler.patch("/private/transactions/" + TRANSACTION_ID, transaction)).thenReturn(privateTransactionPatch);
        when(privateTransactionPatch.execute()).thenReturn(apiPatchResponse);
        when(apiPatchResponse.getStatusCode()).thenReturn(204);

        try {
            transactionService.updateTransaction(transaction, PASSTHROUGH_HEADER, LOGGING_CONTEXT);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    void testServiceExceptionThrownWhenTransactionSdkDoesNotReturn204StatusDuringPatch() throws IOException, URIValidationException {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(apiClientService.getInternalOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(internalApiClient);
        when(internalApiClient.privateTransaction()).thenReturn(privateTransactionResourceHandler);
        when(privateTransactionResourceHandler.patch("/private/transactions/" + TRANSACTION_ID, transaction)).thenReturn(privateTransactionPatch);
        when(privateTransactionPatch.execute()).thenReturn(apiPatchResponse);
        when(apiPatchResponse.getStatusCode()).thenReturn(401);

        assertThrows(ServiceException.class, () -> {
            transactionService.updateTransaction(transaction, PASSTHROUGH_HEADER, LOGGING_CONTEXT);
        });
    }

    @Test
    void testServiceExceptionThrownWhenTransactionSdkThrowsURIValidationExceptionDuringPatch() throws IOException, URIValidationException {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(apiClientService.getInternalOauthAuthenticatedClient(PASSTHROUGH_HEADER)).thenReturn(internalApiClient);
        when(internalApiClient.privateTransaction()).thenReturn(privateTransactionResourceHandler);
        when(privateTransactionResourceHandler.patch("/private/transactions/" + TRANSACTION_ID, transaction)).thenReturn(privateTransactionPatch);
        when(privateTransactionPatch.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            transactionService.updateTransaction(transaction, PASSTHROUGH_HEADER, LOGGING_CONTEXT);
        });
    }
}