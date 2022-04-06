package uk.gov.companieshouse.overseasentitiesapi.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    public static final String START_TIME_KEY = "start-time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long startTime = System.currentTimeMillis();
        request.getSession().setAttribute(START_TIME_KEY, startTime);

        ApiLogger.infoContext(getRequestId(request), String.format("Start of request. Method: %s Path: %s",
                getRequestMethod(request), getRequestPath(request)), null);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getSession().getAttribute(START_TIME_KEY);
        long responseTime = System.currentTimeMillis() - startTime;

        ApiLogger.infoContext(getRequestId(request), String.format("End of request. Method: %s Path: %s Duration: %sms Status: %s",
                getRequestMethod(request), getRequestPath(request), responseTime, response.getStatus()), null);
    }

    private String getRequestPath(HttpServletRequest request) {
        return (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    }

    private String getRequestMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    private String getRequestId(HttpServletRequest request) {
        return request.getHeader(ERIC_REQUEST_ID_KEY);
    }
}
