package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OLD_FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_RESOURCE;

@ExtendWith(MockitoExtension.class)
class TransactionUtilsTest {

    private static final String OVERSEAS_ENTITY_SELF_LINK = "/transaction/1234/overseas-entity/1234";

    @Mock
    private Transaction transaction;

    private final TransactionUtils transactionUtils = new TransactionUtils();

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseWhenOverseasEntitySelfLinkIsNull() {
        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, null);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseWhenTransactionIsNull() {
        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(null, OVERSEAS_ENTITY_SELF_LINK);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfTransactionResourcesIsNull() {
        when(transaction.getResources()).thenReturn(null);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, OVERSEAS_ENTITY_SELF_LINK);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfNoOverseasEntityFilingKindFoundInTransaction() {
        Map<String, Resource> transactionResources = new HashMap<>();
        Resource accountsResource = new Resource();
        accountsResource.setKind("Accounts");
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, OVERSEAS_ENTITY_SELF_LINK);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfNoOverseasEntityMatchFound() {
        Map<String, Resource> transactionResources = new HashMap<>();
        Resource overseasEntityResource = new Resource();
        overseasEntityResource.setKind(FILING_KIND_OVERSEAS_ENTITY);
        Map<String, String> overseasEntityResourceLinks = new HashMap<>();
        String nonMatchingResourceLink = "/transaction/1234/overseas-entity/should-not-match";
        overseasEntityResourceLinks.put(LINK_RESOURCE, nonMatchingResourceLink);
        overseasEntityResource.setLinks(overseasEntityResourceLinks);
        transactionResources.put(nonMatchingResourceLink, overseasEntityResource);
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, OVERSEAS_ENTITY_SELF_LINK);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsTrueIfOverseasEntityMatchFound() {
        Map<String, Resource> transactionResources = new HashMap<>();
        Resource overseasEntityResource = new Resource();
        overseasEntityResource.setKind(FILING_KIND_OVERSEAS_ENTITY);
        Map<String, String> overseasEntityResourceLinks = new HashMap<>();
        overseasEntityResourceLinks.put(LINK_RESOURCE, OVERSEAS_ENTITY_SELF_LINK);
        overseasEntityResource.setLinks(overseasEntityResourceLinks);
        transactionResources.put(OVERSEAS_ENTITY_SELF_LINK, overseasEntityResource);
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, OVERSEAS_ENTITY_SELF_LINK);
        assertTrue(result);
    }

    @Deprecated
    @Test
    void testIsTransactionLinkedToOldOverseasEntitySubmissionReturnsTrueIfOverseasEntityMatchFound() {
        Map<String, Resource> transactionResources = new HashMap<>();
        Resource overseasEntityResource = new Resource();
        overseasEntityResource.setKind(OLD_FILING_KIND_OVERSEAS_ENTITY);
        Map<String, String> overseasEntityResourceLinks = new HashMap<>();
        overseasEntityResourceLinks.put(LINK_RESOURCE, OVERSEAS_ENTITY_SELF_LINK);
        overseasEntityResource.setLinks(overseasEntityResourceLinks);
        transactionResources.put(OVERSEAS_ENTITY_SELF_LINK, overseasEntityResource);
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, OVERSEAS_ENTITY_SELF_LINK);
        assertTrue(result);
    }
}
