package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import java.io.IOException;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTIONS_PRIVATE_API_PREFIX;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTIONS_PUBLIC_API_PREFIX;

@Service
public class TransactionService {

    private final ApiClientService apiClientService;

    @Autowired
    public TransactionService(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    public Transaction getTransaction(String transactionId, String passthroughHeader) throws ServiceException {
        try {
            var uri = TRANSACTIONS_PUBLIC_API_PREFIX + transactionId;
            return apiClientService.getOauthAuthenticatedClient(passthroughHeader).transactions().get(uri).execute().getData();
        } catch (URIValidationException | IOException e) {
            throw new ServiceException("Error Retrieving Transaction " + transactionId, e);
        }
    }

    public void updateTransaction(Transaction transaction, String passthroughHeader) throws ServiceException {
        try {
            var uri = TRANSACTIONS_PRIVATE_API_PREFIX + transaction.getId();
            var response = apiClientService.getInternalOauthAuthenticatedClient(passthroughHeader).privateTransaction().patch(uri, transaction).execute();

            if (response.getStatusCode() != 204) {
                throw new IOException("Invalid Status Code received: " + response.getStatusCode());
            }
        } catch (IOException | URIValidationException e) {
            throw new ServiceException("Error Updating Transaction " + transaction.getId(), e);
        }
    }
}
