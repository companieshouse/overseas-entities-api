package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.FilingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.overseasentitiesapi.interceptor.TransactionInterceptor;

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

    @InjectMocks
    private InterceptorConfig interceptorConfig;

    @Test
    void addInterceptorsTest() {
        when(interceptorRegistry.addInterceptor(any())).thenReturn(interceptorRegistration);

        interceptorConfig.addInterceptors(interceptorRegistry);

        InOrder inOrder = inOrder(interceptorRegistry, interceptorRegistration);

        // Logging interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(loggingInterceptor);

        // Transaction interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(transactionInterceptor);
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.TRANSACTIONS);

        // Filing interceptor check
        inOrder.verify(interceptorRegistry).addInterceptor(filingInterceptor);
        inOrder.verify(interceptorRegistration).addPathPatterns(InterceptorConfig.FILINGS);
    }
}
