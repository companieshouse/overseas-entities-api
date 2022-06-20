package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionLinks;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.service.FilingsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilingsControllerTest {

    private static final String OVERSEAS_ENTITY_ID = "abc123";
    private static final String TRANSACTION_ID = "def456";
    private static final String ERIC_REQUEST_ID = "XaBcDeF12345";

    private Transaction transaction;

    @InjectMocks
    private FilingsController filingsController;

    @Mock
    private FilingsService filingsService;

    @BeforeEach
    void init() {
        transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
    }

    @Test
    void testGetFilingReturnsSuccessfully() throws SubmissionNotFoundException, ServiceException {
        FilingApi filing = new FilingApi();
        filing.setDescription("12345678");
        when(filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction)).thenReturn(filing);
        var result = filingsController.getFiling(transaction, OVERSEAS_ENTITY_ID, TRANSACTION_ID, ERIC_REQUEST_ID);
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().length);
        assertEquals("12345678", result.getBody()[0].getDescription());
    }

    @Test
    void testGetFilingSubmissionNotFound() throws SubmissionNotFoundException, ServiceException {
        when(filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction)).thenThrow(SubmissionNotFoundException.class);
        var result = filingsController.getFiling(transaction, OVERSEAS_ENTITY_ID, TRANSACTION_ID, ERIC_REQUEST_ID);
        assertNull(result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
