package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.CLOSED_PENDING_PAYMENT;
import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.OPEN;

/**
 * A request interceptor class that checks if a web request made to the OE API is allowed - for this to be true a
 * transaction must be present in the request attributes and that transaction must still be 'open' OR this must be
 * a GET request and the transaction status must be 'closed pending payment'.
 */
@Component
public class ProcessingInterceptor extends AbstractTransactionStatusInterceptor {

    @Override
    boolean handleTransactionStatus(Transaction transaction, String reqId, HashMap<String, Object> logMap, HttpServletRequest request, HttpServletResponse response) {
        if (OPEN.equals(transaction.getStatus())) {
            ApiLogger.infoContext(reqId, "Transaction is open - processing allowed", logMap);

            return true;
        }

        if (CLOSED_PENDING_PAYMENT.equals(transaction.getStatus()) && request.getMethod().equals(HttpMethod.GET.name())) {
            ApiLogger.infoContext(reqId, "Transaction is closed pending payment and a GET request - processing allowed", logMap);

            return true;
        }

        if ("true".equalsIgnoreCase(request.getParameter("force")) && (request.getMethod().equals(HttpMethod.PUT.name()) || request.getMethod().equals(HttpMethod.GET.name()))) {
            ApiLogger.infoContext(reqId, "Force flag - processing allowed", logMap);
            return true;
        }

        ApiLogger.errorContext(reqId, "Processing disallowed", null, logMap);


        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }
}
