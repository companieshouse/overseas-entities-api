package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.OPEN;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

/**
 * A request interceptor class that checks if a web request made to the OE API is allowed - for this to be true a
 * transaction must be present in the request attributes and that transaction must still be 'open'.
 */
@Component
public class ProcessingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        final String reqId = request.getHeader(ERIC_REQUEST_ID_KEY);
        ApiLogger.debugContext(reqId, "Called preHandle(...)", null);

        final var transaction = (Transaction) request.getAttribute(TRANSACTION_KEY);

        if (Objects.isNull(transaction)) {
            ApiLogger.errorContext(reqId, "No transaction found in request - processing disallowed", null);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return false;
        }

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        if (OPEN.equals(transaction.getStatus())) {
            ApiLogger.infoContext(reqId, "Transaction is open - processing allowed", logMap);

            return true;
        }

        ApiLogger.errorContext(reqId, "Transaction is not open - processing disallowed", null, logMap);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }
}
