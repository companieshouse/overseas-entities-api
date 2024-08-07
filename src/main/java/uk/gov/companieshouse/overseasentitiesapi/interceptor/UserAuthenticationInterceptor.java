package uk.gov.companieshouse.overseasentitiesapi.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_COMPANY_NUMBER_PREFIX;
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

        Optional<String> companyNumberInTransactionOptional;

        try {
                companyNumberInTransactionOptional = getCompanyNumberInTransaction(request, transactionId);
            } catch (ServiceException e) {
                ApiLogger.errorContext(reqId, "Error retrieving transaction " + transactionId, e, logMap);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return false;
        }

        boolean isRoeRegistrationJourneyRequest = isRequestRoeRegistration(companyNumberInTransactionOptional);
        var authInfoMap = new HashMap<String, Object>();
        authInfoMap.put(TRANSACTION_ID_KEY, transactionId);
        authInfoMap.put("request_method", request.getMethod());

        if (isRoeRegistrationJourneyRequest) {
            // Check the user has the company_incorporation=create permission
            boolean hasCompanyIncorporationCreatePermission = tokenPermissions.hasPermission(Key.COMPANY_INCORPORATION, Value.CREATE);
            authInfoMap.put("has_company_incorporation_create_permission", hasCompanyIncorporationCreatePermission);
            if (hasCompanyIncorporationCreatePermission) {
                ApiLogger.debugContext(reqId, "UserAuthenticationInterceptor authorised with company_incorporation=create permission",
                        authInfoMap);
                return true;
            }
        } else {
            String companyNumberInScope = getCompanyNumberInScope(request);
            authInfoMap.put("company_number_in_scope", convertNullValuesForLogMapOutput(companyNumberInScope));
            boolean isRoeUpdateJourneyRequest = isValidRoeUpdateRequest(companyNumberInScope, companyNumberInTransactionOptional);
            authInfoMap.put("is_roe_update_journey_request", isRoeUpdateJourneyRequest);
            if (isRoeUpdateJourneyRequest) {
                // Check the user has the company_oe_annual_update=create permission for ROE Update
                boolean hasCompanyOeAnnualUpdateCreatePermission = tokenPermissions.hasPermission(Key.COMPANY_OE_ANNUAL_UPDATE, Value.CREATE);
                authInfoMap.put("has_company_oe_annual_update_create_permission", hasCompanyOeAnnualUpdateCreatePermission);
                if (hasCompanyOeAnnualUpdateCreatePermission) {
                    ApiLogger.debugContext(reqId, "UserAuthenticationInterceptor authorised with company_oe_annual_update=create permission",
                            authInfoMap);
                    return true;
                }
            }
        }

        ApiLogger.errorContext(reqId, "UserAuthenticationInterceptor unauthorised", null, authInfoMap);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    protected Optional<TokenPermissions> getTokenPermissions(HttpServletRequest request) {
        return AuthorisationUtil.getTokenPermissions(request);
    }

    private boolean isRequestRoeRegistration(Optional<String> companyNumberInTransaction) {
        return companyNumberInTransaction.isEmpty();
    }

    private boolean isValidRoeUpdateRequest(String companyNumberInScope, Optional<String> companyNumberInTransaction) {
        boolean isValidOeNumberInScope = isValidOeNumber(companyNumberInScope);
        var isValidOeNumberInTransaction = companyNumberInTransaction.isPresent() && isValidOeNumber(companyNumberInTransaction.get());

        if (isValidOeNumberInScope && isValidOeNumberInTransaction) {
            return companyNumberInScope.equalsIgnoreCase(companyNumberInTransaction.get());
        }
        return false;
    }

    private boolean isValidOeNumber(String oeNumber) {
        return StringUtils.isNotEmpty(oeNumber) && oeNumber.startsWith(OVERSEAS_ENTITY_COMPANY_NUMBER_PREFIX);
    }

    private String getCompanyNumberInScope (HttpServletRequest request) {
        final Map<String, List<String>> privileges = getERICTokenPermissions(request);
        if (privileges.containsKey(COMPANY_NUMBER_KEY)) {
            return privileges.get(COMPANY_NUMBER_KEY).getFirst();
        }
        return null;
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

    private Optional<String> getCompanyNumberInTransaction(HttpServletRequest request, String transactionId) throws ServiceException {
        String passThroughHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());
        String reqId = request.getHeader(ERIC_REQUEST_ID_KEY);
        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transactionId);

        final var transaction = transactionService.getTransaction(transactionId, passThroughHeader, reqId);
        if (transaction == null) {
            ApiLogger.debugContext(reqId, "Call to Transaction Service returned null transaction object " + transactionId, logMap);
            return Optional.empty();
        }
        logMap.put("company_number_in_transaction", convertNullValuesForLogMapOutput(transaction.getCompanyNumber()));
        ApiLogger.debugContext(reqId, "Transaction successfully retrieved " + transactionId, logMap);
        return Optional.ofNullable(transaction.getCompanyNumber());

    }

    private String convertNullValuesForLogMapOutput(String aString) {
        if (StringUtils.isNotEmpty(aString)) {
            return aString;
        } else {
            return "NULL";
        }
    }

}
