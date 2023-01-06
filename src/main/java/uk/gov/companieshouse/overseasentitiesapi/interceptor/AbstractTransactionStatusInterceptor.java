package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

/**
 * An abstract class which can be extended by request interceptors to implement specific behaviour, based on the status
 * of the transaction associated with the Overseas Entity submission.
 * <p/>
 * Note that a transaction object instance is expected to already be present in the request attributes. Any concrete
 * implementations must therefore run after the <code>TransactionInterceptor</code> has completed.
 */
public abstract class AbstractTransactionStatusInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        final String reqId = request.getHeader(ERIC_REQUEST_ID_KEY);
        ApiLogger.debugContext(reqId, "Called preHandle(...)", null);

        final var transaction = (Transaction) request.getAttribute(TRANSACTION_KEY);

        if (Objects.isNull(transaction)) {
            ApiLogger.errorContext(reqId, "No transaction found in request - action disallowed", null);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return false;
        }

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        return handleTransactionStatus(transaction, reqId, logMap, response);
    }

    abstract boolean handleTransactionStatus(Transaction transaction, String reqId, HashMap<String, Object> logMap, HttpServletResponse response);
}
