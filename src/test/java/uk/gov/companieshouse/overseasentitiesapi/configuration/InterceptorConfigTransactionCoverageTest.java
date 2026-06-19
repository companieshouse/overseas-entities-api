package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Security tests verifying the TransactionInterceptor is registered for private data paths.
 * These tests verify that the {@code PRIVATE_TRANSACTIONS} pattern defined in
 * {@link InterceptorConfig} matches all private data endpoint paths.
 */
class InterceptorConfigTransactionCoverageTest {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final String PRIVATE_DETAILS = "/private/transactions/12345/overseas-entity/abc123/details";
    private static final String PRIVATE_BENEFICIAL_OWNERS = "/private/transactions/12345/overseas-entity/abc123/beneficial-owners";
    private static final String PRIVATE_MANAGING_OFFICERS = "/private/transactions/12345/overseas-entity/abc123/managing-officers";
    private static final String PRIVATE_TRUST_DETAILS = "/private/transactions/12345/overseas-entity/abc123/trusts/details";
    private static final String PRIVATE_TRUST_LINKS = "/private/transactions/12345/overseas-entity/abc123/trusts/beneficial-owners/links";
    private static final String PRIVATE_INDIVIDUAL_TRUSTEES = "/private/transactions/12345/overseas-entity/abc123/trusts/trust001/individual-trustees";
    private static final String PRIVATE_CORPORATE_TRUSTEES = "/private/transactions/12345/overseas-entity/abc123/trusts/trust001/corporate-trustees";

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/details")
    void transactionInterceptor_mustCoverPrivateDetails() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_DETAILS),
                "PRIVATE_TRANSACTIONS pattern must cover /details endpoint");
    }

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/beneficial-owners")
    void transactionInterceptor_mustCoverPrivateBeneficialOwners() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_BENEFICIAL_OWNERS),
                "PRIVATE_TRANSACTIONS pattern must cover /beneficial-owners endpoint");
    }

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/managing-officers")
    void transactionInterceptor_mustCoverPrivateManagingOfficers() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_MANAGING_OFFICERS),
                "PRIVATE_TRANSACTIONS pattern must cover /managing-officers endpoint");
    }

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/trusts/details")
    void transactionInterceptor_mustCoverPrivateTrustDetails() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_TRUST_DETAILS),
                "PRIVATE_TRANSACTIONS pattern must cover /trusts/details endpoint");
    }

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/trusts/beneficial-owners/links")
    void transactionInterceptor_mustCoverPrivateTrustLinks() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_TRUST_LINKS),
                "PRIVATE_TRANSACTIONS pattern must cover /trusts/beneficial-owners/links endpoint");
    }

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/individual-trustees")
    void transactionInterceptor_mustCoverPrivateIndividualTrustees() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_INDIVIDUAL_TRUSTEES),
                "PRIVATE_TRANSACTIONS pattern must cover /individual-trustees endpoint");
    }

    @Test
    @DisplayName("PRIVATE_TRANSACTIONS pattern must match /private/transactions/**/corporate-trustees")
    void transactionInterceptor_mustCoverPrivateCorporateTrustees() {
        assertTrue(pathMatcher.match(InterceptorConfig.PRIVATE_TRANSACTIONS, PRIVATE_CORPORATE_TRUSTEES),
                "PRIVATE_TRANSACTIONS pattern must cover /corporate-trustees endpoint");
    }
}
