package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import uk.gov.companieshouse.api.interceptor.InternalUserInterceptor;
import uk.gov.companieshouse.api.interceptor.TokenPermissionsInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.FilingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.TransactionInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.UserAuthenticationInterceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterceptorConfigTest {

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @Mock
    private InterceptorRegistration interceptorRegistration;

    @Mock
    private LoggingInterceptor loggingInterceptor;

    @Mock
    private TransactionInterceptor transactionInterceptor;

    @Mock
    private FilingInterceptor filingInterceptor;

    @Mock
    private UserAuthenticationInterceptor userAuthenticationInterceptor;

    @Mock
    private InternalUserInterceptor internalUserInterceptor;

    @InjectMocks
    private InterceptorConfig interceptorConfig;

    @Test
    void addInterceptorsTest() {
        when(interceptorRegistry.addInterceptor(any())).thenReturn(interceptorRegistration);

        interceptorConfig.addInterceptors(interceptorRegistry);

        InOrder inOrder = inOrder(interceptorRegistry, interceptorRegistration);

        // Logging interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(loggingInterceptor);

        // Token Permissions interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(any(TokenPermissionsInterceptor.class));
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.USER_AUTH_ENDPOINTS);

        // User authentication interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(userAuthenticationInterceptor);
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.USER_AUTH_ENDPOINTS);

        // Internal User auth interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(internalUserInterceptor);
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.INTERNAL_AUTH_ENDPOINTS);

        // Transaction interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(transactionInterceptor);
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.TRANSACTIONS, InterceptorConfig.FILINGS);

        // Filing interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(filingInterceptor);
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.FILINGS);
    }
}
