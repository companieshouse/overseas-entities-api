package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesControllerTest {

    private static final ResponseEntity<Object> CREATED_SUCCESS_RESPONSE = ResponseEntity.created(URI.create("URI")).body("Created");

    private static final String REQUEST_ID = "fd4gld5h3jhh";
    private static final String PASSTHROUGH = "13456";
    private static final String SUBMISSION_ID = "abc123";
    private static final String TRANSACTION_ID = "test-1";
    private static final String USER_ID = "22334455";
    private static final String USER_DETAILS = "demo@ch.gov.uk; forename=demoForename; surname=demoSurname";

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private OverseasEntitiesController overseasEntitiesController;

    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void init() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("ERIC-Access-Token", PASSTHROUGH);

        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
    }

    @Test
    void testCreatingANewSubmissionIsSuccessful() throws ServiceException {
        when(overseasEntitiesService.createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH,
                REQUEST_ID,
                USER_ID,
                USER_DETAILS)).thenReturn(CREATED_SUCCESS_RESPONSE);
        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID,
                USER_DETAILS,
                mockHttpServletRequest);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals(CREATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH,
                REQUEST_ID,
                USER_ID,
                USER_DETAILS);
    }

    @Test
    void testValidationStatusResponseWhenTrue() throws SubmissionNotFoundException {
        ValidationStatusResponse validationStatus = new ValidationStatusResponse();
        validationStatus.setValid(true);
        when(overseasEntitiesService.isValid(SUBMISSION_ID)).thenReturn(validationStatus);

        var response = overseasEntitiesController.getValidationStatus(SUBMISSION_ID, TRANSACTION_ID, REQUEST_ID);
        assertEquals(ResponseEntity.ok().body(validationStatus), response);
    }

    @Test
    void testValidationStatusResponseWhenSubmissionNotFound() throws SubmissionNotFoundException {
        when(overseasEntitiesService.isValid(SUBMISSION_ID)).thenThrow(SubmissionNotFoundException.class);
        var response = overseasEntitiesController.getValidationStatus(SUBMISSION_ID, TRANSACTION_ID, REQUEST_ID);
        assertEquals(ResponseEntity.notFound().build(), response);
    }
}
