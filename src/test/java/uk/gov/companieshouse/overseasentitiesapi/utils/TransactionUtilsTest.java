package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_RESOURCE;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_SELF;

@ExtendWith(MockitoExtension.class)
class TransactionUtilsTest {

    private static final String OVERSEAS_ENTITY_SELF_LINK = "/transaction/1234/overseas-entity/1234";

    @Mock
    private Transaction transaction;
    @Mock
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    private final TransactionUtils transactionUtils = new TransactionUtils();

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseWhenOverseasEntityLinksIsNull() {
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(null);
        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfOverseasEntityLinksDoesNotContainSelfLink() {
        Map<String, String> emptyLinksMap = new HashMap<>();
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(emptyLinksMap);
        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfOverseasEntityLinksHasBlankSelfLink() {
        Map<String, String> overseasEntityLinksMap = new HashMap<>();
        overseasEntityLinksMap.put(LINK_SELF, "  ");
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(overseasEntityLinksMap);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfTransactionResourcesIsNull() {
        Map<String, String> overseasEntityLinksMap = new HashMap<>();
        overseasEntityLinksMap.put(LINK_SELF, OVERSEAS_ENTITY_SELF_LINK);
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(overseasEntityLinksMap);
        when(transaction.getResources()).thenReturn(null);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfNoOverseasEntityFilingKindFoundInTransaction() {
        Map<String, String> overseasEntityLinksMap = new HashMap<>();
        overseasEntityLinksMap.put(LINK_SELF, OVERSEAS_ENTITY_SELF_LINK);
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(overseasEntityLinksMap);

        Map<String, Resource> transactionResources = new HashMap<>();
        Resource accountsResource = new Resource();
        accountsResource.setKind("Accounts");
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsFalseIfNoOverseasEntityMatchFound() {
        Map<String, String> overseasEntityLinksMap = new HashMap<>();
        overseasEntityLinksMap.put(LINK_SELF, OVERSEAS_ENTITY_SELF_LINK);
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(overseasEntityLinksMap);

        Map<String, Resource> transactionResources = new HashMap<>();
        Resource overseasEntityResource = new Resource();
        overseasEntityResource.setKind(FILING_KIND_OVERSEAS_ENTITY);
        Map<String, String> overseasEntityResourceLinks = new HashMap<>();
        String nonMatchingResourceLink = "/transaction/1234/overseas-entity/should-not-match";
        overseasEntityResourceLinks.put(LINK_RESOURCE, nonMatchingResourceLink);
        overseasEntityResource.setLinks(overseasEntityResourceLinks);
        transactionResources.put(nonMatchingResourceLink, overseasEntityResource);
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertFalse(result);
    }

    @Test
    void testIsTransactionLinkedToOverseasEntitySubmissionReturnsTrueIfOverseasEntityMatchFound() {
        Map<String, String> overseasEntityLinksMap = new HashMap<>();
        overseasEntityLinksMap.put(LINK_SELF, OVERSEAS_ENTITY_SELF_LINK);
        when(overseasEntitySubmissionDto.getLinks()).thenReturn(overseasEntityLinksMap);

        Map<String, Resource> transactionResources = new HashMap<>();
        Resource overseasEntityResource = new Resource();
        overseasEntityResource.setKind(FILING_KIND_OVERSEAS_ENTITY);
        Map<String, String> overseasEntityResourceLinks = new HashMap<>();
        overseasEntityResourceLinks.put(LINK_RESOURCE, OVERSEAS_ENTITY_SELF_LINK);
        overseasEntityResource.setLinks(overseasEntityResourceLinks);
        transactionResources.put(OVERSEAS_ENTITY_SELF_LINK, overseasEntityResource);
        when(transaction.getResources()).thenReturn(transactionResources);

        var result = transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, overseasEntitySubmissionDto);
        assertTrue(result);
    }
}
