package interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long startTime = System.currentTimeMillis();
        request.getSession().setAttribute("start-time", startTime);

        ApiLogger.infoContext(requestId(request), String.format("Start of request. Method: %s Path: %s",
                requestMethod(request), requestPath(request)), null);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getSession().getAttribute("start-time");
        long responseTime = System.currentTimeMillis() - startTime;

        ApiLogger.infoContext(requestId(request), String.format("End of request. Method: %s Path: %s Duration: %sms Status: %s",
                requestMethod(request), requestPath(request), responseTime, response.getStatus()), null);
    }

    private String requestPath(HttpServletRequest request) {
        return (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    }

    private String requestMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    private String requestId(HttpServletRequest request) {
        return request.getHeader(ERIC_REQUEST_ID_KEY);
    }
}
