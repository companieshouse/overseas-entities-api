package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesControllerTest {

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @InjectMocks
    private OverseasEntitiesController overseasEntitiesController;


    @Test
    void testCreatingANewSubmissionIsSuccessful() {
        OverseasEntitySubmissionDto overseasEntitySubmission = new OverseasEntitySubmissionDto();

        ResponseEntity<String> response = overseasEntitiesController.createNewSubmission(overseasEntitySubmission);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("This is the Register an Overseas Entity API", response.getBody());

        verify(overseasEntitiesService).createOverseasEntity(overseasEntitySubmission);
    }
}
