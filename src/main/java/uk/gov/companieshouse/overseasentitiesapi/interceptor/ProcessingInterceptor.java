package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static uk.gov.companieshouse.api.model.transaction.TransactionStatus.OPEN;

/**
 * A request interceptor class that checks if a web request made to the OE API is allowed - for this to be true a
 * transaction must be present in the request attributes and that transaction must still be 'open'.
 */
@Component
public class ProcessingInterceptor extends AbstractTransactionStatusInterceptor {

    @Override
    boolean handleTransactionStatus(Transaction transaction, String reqId, HashMap<String, Object> logMap, HttpServletResponse response) {
        if (OPEN.equals(transaction.getStatus())) {
            ApiLogger.infoContext(reqId, "Transaction is open - processing allowed", logMap);

            return true;
        }

        ApiLogger.errorContext(reqId, "Transaction is not open - processing disallowed", null, logMap);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }
}
