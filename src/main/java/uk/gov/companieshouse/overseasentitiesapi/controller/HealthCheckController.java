package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/private/transactions/healthcheck")
    public HttpEntity<Void> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
