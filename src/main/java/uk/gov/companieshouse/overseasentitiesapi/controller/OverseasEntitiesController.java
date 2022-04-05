package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@RestController
@RequestMapping("/transactions/{transaction_id}/overseas-entity")
public class OverseasEntitiesController {

    @PostMapping("/")
    public ResponseEntity<String> createNewSubmission(
            @RequestAttribute("transaction") Transaction transaction) {
        ApiLogger.debug("Called createNewSubmission()");
        ApiLogger.debug("**** " + transaction.getId());

        return ResponseEntity.ok().body("This is the Register an Overseas Entity API");
    }
}
