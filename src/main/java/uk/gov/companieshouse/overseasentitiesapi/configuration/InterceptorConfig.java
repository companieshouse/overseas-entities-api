package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.FilingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.TransactionInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    static final String TRANSACTIONS = "/transactions/**";
    static final String FILINGS = "/private/**/filings";

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    private TransactionInterceptor transactionInterceptor;

    @Autowired
    private FilingInterceptor filingInterceptor;

    /**
     * Setup the interceptors to run against endpoints when the endpoints are called
     * Interceptors are executed in the order they are added to the registry
     * @param registry The spring interceptor registry
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        addLoggingInterceptor(registry);
        addTransactionInterceptor(registry);
        addFilingInterceptor(registry);
    }

    /**
     * Interceptor that logs all calls to endpoints
     * @param registry The spring interceptor registry
     */
    private void addLoggingInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    /**
     * Interceptor to get transaction and put in request for endpoints that require a transaction
     * @param registry The spring interceptor registry
     */
    private void addTransactionInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(transactionInterceptor)
                .addPathPatterns(TRANSACTIONS, FILINGS);
    }

    /**
     * Interceptor to check specific conditions for the /filings endpoint
     * @param registry
     */
    private void addFilingInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(filingInterceptor)
                .addPathPatterns(FILINGS);
    }
}
