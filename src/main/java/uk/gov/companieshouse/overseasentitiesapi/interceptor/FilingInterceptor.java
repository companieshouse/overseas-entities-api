package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.stereotype.Component;
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

import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.CLOSED;

@Component
public class FilingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ApiLogger.debug("Called preHandle(...)");

        final var transaction = (Transaction) request.getAttribute(TRANSACTION_KEY);
        final String reqId = request.getHeader(ERIC_REQUEST_ID_KEY);

        if (Objects.isNull(transaction)) {
            ApiLogger.infoContext(reqId, "No transaction found in request - filing disallowed");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return false;
        }

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        if (CLOSED.equals(transaction.getStatus())) {
            ApiLogger.infoContext(reqId, "Transaction is closed - filing allowed", logMap);

            return true;
        }

        ApiLogger.infoContext(reqId, "Transaction is not closed - filing disallowed", logMap);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }
}
