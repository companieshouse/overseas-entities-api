package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.service.FilingsService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/filings")
public class FilingsController {

    @Autowired
    private FilingsService filingService;

    @GetMapping
    public ResponseEntity<FilingApi[]> getFiling(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            HttpServletRequest request) {

        String passThroughTokenHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        ApiLogger.infoContext(requestId, "Calling service to retrieve filing", logMap);
        ApiLogger.infoContext(requestId, "Transaction id is " + transaction.getId(), logMap);
        try {
            FilingApi filing = filingService.generateOverseasEntityFiling(overseasEntityId, transaction, passThroughTokenHeader);
            return ResponseEntity.ok(new FilingApi[] { filing });
        } catch (SubmissionNotFoundException e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return ResponseEntity.internalServerError().build();
        }
    }
}
