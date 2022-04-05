package configuration;

import interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("uk.gov.companieshouse.api.interceptor")
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    /**
     * Setup the interceptors to run against endpoints when the endpoints are called
     * Interceptors are executed in order of configuration
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        addLoggingInterceptor(registry);
    }

    /**
     * Interceptor that logs all calls to endpoints
     * @param registry
     */
    private void addLoggingInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}
