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

import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.CLOSED;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@Component
public class ProcessingInterceptor extends AbstractClosedTransactionInterceptor {

    @Override
    boolean handleClosedTransactionStatus(String reqId, HashMap<String, Object> logMap, HttpServletResponse response) {
        ApiLogger.errorContext(reqId, "Transaction is closed - processing disallowed", null, logMap);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }

    @Override
    boolean handleNonClosedTransactionStatus(String reqId, HashMap<String, Object> logMap, HttpServletResponse response) {
        ApiLogger.infoContext(reqId, "Transaction is not closed - processing allowed", logMap);

        return true;
    }
}
