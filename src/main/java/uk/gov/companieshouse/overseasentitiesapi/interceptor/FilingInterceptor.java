package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
public class FilingInterceptor extends AbstractClosedTransactionInterceptor {

    @Override
    boolean handleClosedTransactionStatus(String reqId, HashMap<String, Object> logMap, HttpServletResponse response) {
        ApiLogger.infoContext(reqId, "Transaction is closed - filing allowed", logMap);

        return true;
    }

    @Override
    boolean handleNonClosedTransactionStatus(String reqId, HashMap<String, Object> logMap, HttpServletResponse response) {
        ApiLogger.errorContext(reqId, "Transaction is not closed - filing disallowed", null, logMap);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return false;
    }
}
