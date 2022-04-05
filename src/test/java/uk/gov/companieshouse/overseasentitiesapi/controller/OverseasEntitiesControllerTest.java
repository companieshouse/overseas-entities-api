package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.model.OverseasEntitySubmission;


import static org.junit.jupiter.api.Assertions.assertEquals;

class OverseasEntitiesControllerTest {

    private final OverseasEntitiesController overseasEntitiesController = new OverseasEntitiesController();

    @Test
    void testCreateNewSubmission() {
        ResponseEntity<String> response = overseasEntitiesController.createNewSubmission(new Transaction(), new OverseasEntitySubmission());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("This is the Register an Overseas Entity API", response.getBody());
    }
}
