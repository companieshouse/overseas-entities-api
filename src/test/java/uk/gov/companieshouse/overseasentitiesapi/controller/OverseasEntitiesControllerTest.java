package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesControllerTest {

    private static final String REQUEST_ID = "fd4gld5h3jhh";

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @InjectMocks
    private OverseasEntitiesController overseasEntitiesController;


    @Test
    void testCreatingANewSubmissionIsSuccessful() {
//        OverseasEntitySubmissionDto overseasEntitySubmission = new OverseasEntitySubmissionDto();
//        Transaction transaction = new Transaction();
//
//        ResponseEntity<String> response = overseasEntitiesController.createNewSubmission(transaction, overseasEntitySubmission);
//
//        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
//        assertEquals("This is the Register an Overseas Entity API", response.getBody());
//
//        verify(overseasEntitiesService).createOverseasEntity(overseasEntitySubmission);
    }
}
