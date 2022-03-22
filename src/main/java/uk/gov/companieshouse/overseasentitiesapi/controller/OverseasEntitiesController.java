package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/overseas-entities")
public class OverseasEntitiesController {

    @PostMapping("/")
    public ResponseEntity<String> createNewSubmission() {
        return ResponseEntity.ok().body("This is the Register an Overseas Entity API");
    }
}
