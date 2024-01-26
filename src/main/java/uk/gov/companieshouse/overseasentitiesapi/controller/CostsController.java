package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.api.model.payment.Cost;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@RestController
@RequestMapping("/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/costs")
public class CostsController {

    private final CostsService costsService;

    @Autowired
    public CostsController(CostsService costService) {
        this.costsService = costService;
    }

    @GetMapping
    public ResponseEntity<List<Cost>> getCosts(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) {

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        ApiLogger.infoContext(requestId, "Calling CostsService to retrieve costs", logMap);

        try {
            Cost cost = costsService.getCosts(requestId, overseasEntityId);

            return ResponseEntity.ok(Collections.singletonList(cost));
        } catch (SubmissionNotFoundException e) {
            ApiLogger.errorContext(requestId, "Error determining submission costs", e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
