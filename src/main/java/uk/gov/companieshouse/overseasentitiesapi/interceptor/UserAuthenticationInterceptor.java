package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.springframework.web.servlet.HandlerMapping;
import uk.gov.companieshouse.api.util.security.AuthorisationUtil;
import uk.gov.companieshouse.api.util.security.Permission.Key;
import uk.gov.companieshouse.api.util.security.Permission.Value;
import uk.gov.companieshouse.api.util.security.SecurityConstants;
import uk.gov.companieshouse.api.util.security.TokenPermissions;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.TransactionService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.COMPANY_NUMBER_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_AUTHORISED_TOKEN_PERMISSIONS;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

@Component("RoeUserAuthenticationInterceptor")
public class UserAuthenticationInterceptor implements HandlerInterceptor {

    private final TransactionService transactionService;


    @Autowired
    public UserAuthenticationInterceptor(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Pre handle method to authorize the request before it reaches the controller.
     * Retrieves the TokenPermissions stored in the request (which must have been
     * previously added by the TokenPermissionsInterceptor) and checks the relevant
     * permissions
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        final Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final var transactionId = pathVariables.get(TRANSACTION_ID_KEY);

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        String reqId = request.getHeader(ERIC_REQUEST_ID_KEY);

        // skip token permission checks if an api key is used, api key elevated privileges are checked in other interceptors
        // inside company accounts and abridged accounts api services
        if (SecurityConstants.API_KEY_IDENTITY_TYPE.equals(AuthorisationUtil.getAuthorisedIdentityType(request))) {
            ApiLogger.debugContext(reqId, "UserAuthenticationInterceptor skipping token permission checks for api key request", logMap);
            return true;
        }

        // TokenPermissions should have been set up in the request by TokenPermissionsInterceptor
        final var tokenPermissions = getTokenPermissions(request)
                .orElseThrow(() -> new IllegalStateException("UserAuthenticationInterceptor - TokenPermissions object not present in request"));
        String companyNumberInScope = getCompanyNumberInScope(request);
        boolean isRoeUpdateJourneyRequest = isRequestRoeUpdate(companyNumberInScope);

        var authInfoMap = new HashMap<String, Object>();
        authInfoMap.put(TRANSACTION_ID_KEY, transactionId);
        authInfoMap.put("request_method", request.getMethod());
        authInfoMap.put("is_roe_update_journey_request", isRoeUpdateJourneyRequest);

        if (isRoeUpdateJourneyRequest) {
            authInfoMap.put("company_number_in_scope", companyNumberInScope);
            // Check the user has the company_oe_annual_update=create permission for ROE Update
            boolean hasCompanyOeAnnualUpdateCreatePermission = tokenPermissions.hasPermission(Key.COMPANY_OE_ANNUAL_UPDATE, Value.CREATE);
            authInfoMap.put("has_company_oe_annual_update_create_permission", hasCompanyOeAnnualUpdateCreatePermission);
            if (hasCompanyOeAnnualUpdateCreatePermission) {
                return doRoeUpdateJourneyChecks(request, response, reqId, companyNumberInScope, transactionId, authInfoMap);
            }
        } else {
            // Check the user has the company_incorporation=create permission
            boolean hasCompanyIncorporationCreatePermission = tokenPermissions.hasPermission(Key.COMPANY_INCORPORATION, Value.CREATE);
            authInfoMap.put("has_company_incorporation_create_permission", hasCompanyIncorporationCreatePermission);
            if (hasCompanyIncorporationCreatePermission) {
                ApiLogger.debugContext(reqId, "UserAuthenticationInterceptor authorised with company_incorporation=create permission",
                        authInfoMap);
                return true;
            }
        }

        ApiLogger.errorContext(reqId, "UserAuthenticationInterceptor unauthorised", null, authInfoMap);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    protected Optional<TokenPermissions> getTokenPermissions(HttpServletRequest request) {
        return AuthorisationUtil.getTokenPermissions(request);
    }

    private boolean doRoeUpdateJourneyChecks(HttpServletRequest request, HttpServletResponse response, String reqId, String companyNumberInScope,
                                             String transactionId, Map<String, Object> authInfoMap) {
        boolean isValidOeNumber = companyNumberInScope.startsWith("OE");
        if (isValidOeNumber) {
            boolean doCompanyNumbersMatch = doCompanyNumbersMatch(request, response, companyNumberInScope, transactionId);
            authInfoMap.put("roe_update_journey_company_numbers_match", doCompanyNumbersMatch);
            if (doCompanyNumbersMatch) {
                ApiLogger.debugContext(reqId, "UserAuthenticationInterceptor authorised with company_oe_annual_update=create permission",
                        authInfoMap);
                return true;
            } else {
                ApiLogger.errorContext(reqId, "UserAuthenticationInterceptor unauthorised for ROE Update Journey as Company Numbers inScope and Transaction do not match", null, authInfoMap);
                return false;
            }
        } else {
            ApiLogger.debugContext(reqId, "UserAuthenticationInterceptor unauthorised for ROE Update Journey as Company Number is not a valid OE",
                    authInfoMap);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private boolean isRequestRoeUpdate(String companyNumberInScope) {
        // Further check for OE number is done in doRoeUpdateJourneyChecks method
        return StringUtils.isNotEmpty(companyNumberInScope);
    }

    private String getCompanyNumberInScope (HttpServletRequest request) {
        final Map<String, List<String>> privileges = getERICTokenPermissions(request);
        if (privileges.containsKey(COMPANY_NUMBER_KEY)) {
            return privileges.get(COMPANY_NUMBER_KEY).get(0);
        }
        return null;
    }

    private boolean doCompanyNumbersMatch(HttpServletRequest request, HttpServletResponse response, String companyNumberInScope, String transactionId) {
        var companyNumberInTransaction = getCompanyNumberInTransaction(request, response, transactionId);
        if (companyNumberInTransaction != null) {
            if (companyNumberInTransaction.isPresent() && companyNumberInTransaction.get()
                    .equalsIgnoreCase(companyNumberInScope)) {
                return true;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
    }

    private Map<String, List<String>> getERICTokenPermissions(HttpServletRequest request) {
        String tokenPermissionsHeader = request.getHeader(ERIC_AUTHORISED_TOKEN_PERMISSIONS);
        Map<String, List<String>> permissions = new HashMap<>();
        if (tokenPermissionsHeader != null) {
            for (String pair : tokenPermissionsHeader.split(" ")) {
                String[] parts = pair.split("=");
                permissions.put(parts[0], Arrays.asList(parts[1].split(",")));
            }
        }
        return permissions;
    }

    private Optional<String> getCompanyNumberInTransaction(HttpServletRequest request, HttpServletResponse response, String transactionId) {
        String passthroughHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());
        String reqId = request.getHeader(ERIC_REQUEST_ID_KEY);
        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        try {
            final var transaction = transactionService.getTransaction(transactionId, passthroughHeader, reqId);
            logMap.put(COMPANY_NUMBER_KEY, transaction.getCompanyNumber());
            ApiLogger.debugContext(reqId, "Transaction successfully retrieved " + transactionId, logMap);
            if (transaction.getCompanyNumber() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            return Optional.of(transaction.getCompanyNumber());
        } catch (ServiceException e) {
            ApiLogger.errorContext(reqId, "Error retrieving transaction " + transactionId, e, logMap);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

}
