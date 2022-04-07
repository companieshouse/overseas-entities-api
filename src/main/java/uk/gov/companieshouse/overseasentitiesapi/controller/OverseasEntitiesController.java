package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@RestController
@RequestMapping("/transactions/{transaction_id}/overseas-entity")
public class OverseasEntitiesController {

    private final OverseasEntitiesService overseasEntitiesService;

    @Autowired
    public OverseasEntitiesController(OverseasEntitiesService overseasEntitiesService) {
        this.overseasEntitiesService = overseasEntitiesService;
    }

    @PostMapping("/")
    public ResponseEntity<String> createNewSubmission(
            @RequestAttribute("transaction") Transaction transaction,
            @RequestBody OverseasEntitySubmissionDto overseasEntitySubmission) {
        ApiLogger.debug("Called createNewSubmission()");

        this.overseasEntitiesService.createOverseasEntity(overseasEntitySubmission);

        return ResponseEntity.ok().body("This is the Register an Overseas Entity API");
    }
}
