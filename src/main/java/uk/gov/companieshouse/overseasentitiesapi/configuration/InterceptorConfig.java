package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.api.interceptor.CRUDAuthenticationInterceptor;
import uk.gov.companieshouse.api.interceptor.InternalUserInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.FilingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.TransactionInterceptor;

import static uk.gov.companieshouse.api.util.security.Permission.Key.COMPANY_INCORPORATION;
import static uk.gov.companieshouse.api.util.security.Permission.Key.USER_PROFILE;

@Configuration
@ComponentScan("uk.gov.companieshouse.api.interceptor")
public class InterceptorConfig implements WebMvcConfigurer {

    static final String TRANSACTIONS = "/transactions/**";
    static final String FILINGS = "/private/**/filings";

    static final String[] USER_AUTH_ENDPOINTS = {
      TRANSACTIONS
    };

    static final String[] INTERNAL_AUTH_ENDPOINTS = {
      FILINGS
    };

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    private InternalUserInterceptor internalUserInterceptor;

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
        addUserAuthenticationEndpointsInterceptor(registry);
        addInternalUserAuthenticationEndpointsInterceptor(registry);
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
     * Interceptor to authenticate access to specified endpoints using user permissions
     * @param registry
     */
    private void addUserAuthenticationEndpointsInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(getUserCrudAuthenticationInterceptor())
                .addPathPatterns(USER_AUTH_ENDPOINTS);
    }

    /**
     * Interceptor to authenticate access to specified endpoints using internal permissions
     * @param registry
     */
    private void addInternalUserAuthenticationEndpointsInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(internalUserInterceptor)
                .addPathPatterns(INTERNAL_AUTH_ENDPOINTS);
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

    private CRUDAuthenticationInterceptor getUserCrudAuthenticationInterceptor() {
        return new CRUDAuthenticationInterceptor(COMPANY_INCORPORATION);
    }
}
