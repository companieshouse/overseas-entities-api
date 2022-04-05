package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.companieshouse.overseasentitiesapi.model.OverseasEntitySubmission;

@RestController
@RequestMapping("/overseas-entities")
public class OverseasEntitiesController {

    @PostMapping("/")
    public ResponseEntity<String> createNewSubmission(
            @RequestBody OverseasEntitySubmission overseasEntitySubmission) {
        return ResponseEntity.ok().body("This is the Register an Overseas Entity API");
    }
}
