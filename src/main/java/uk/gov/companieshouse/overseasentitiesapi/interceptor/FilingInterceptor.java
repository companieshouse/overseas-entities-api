package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.CLOSED;

/**
 * A request interceptor class that checks if a request to retrieve filing data is allowed - for this to be true a
 * transaction must be present in the request attributes and that transaction must be 'closed'.
 */
@Component
public class FilingInterceptor extends AbstractTransactionStatusInterceptor {

    @Override
    boolean handleTransactionStatus(Transaction transaction, String reqId, HashMap<String, Object> logMap, HttpServletRequest request, HttpServletResponse response) {
        if (CLOSED.equals(transaction.getStatus())) {
            ApiLogger.infoContext(reqId, "Transaction is closed - filing allowed", logMap);

            return true;
        }

        ApiLogger.errorContext(reqId, "Transaction is not closed - filing disallowed", null, logMap);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }
}
