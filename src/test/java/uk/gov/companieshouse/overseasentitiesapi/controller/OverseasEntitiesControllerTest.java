package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntitySubmissionDtoValidator;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;
import uk.gov.companieshouse.service.rest.response.ChResponseBody;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesControllerTest {

    private static final ResponseEntity<Object> CREATED_SUCCESS_RESPONSE = ResponseEntity.created(URI.create("URI")).body("Created");
    private static final ResponseEntity<Object> UPDATED_SUCCESS_RESPONSE = ResponseEntity.ok().build();

    private static final String REQUEST_ID = "fd4gld5h3jhh";
    private static final String SUBMISSION_ID = "abc123";
    private static final String TRANSACTION_ID = "test-1";
    private static final String USER_ID = "22334455";

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private Transaction transaction;

    @Mock
    private OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;

    @InjectMocks
    private OverseasEntitiesController overseasEntitiesController;

    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    @BeforeEach
    void init() {
        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
    }

    @Test
    void testCreatingANewSubmissionIsSuccessful() throws ServiceException {
        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());
        when(overseasEntitiesService.createOverseasEntityWithResumeLink(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenReturn(CREATED_SUCCESS_RESPONSE);
        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals(CREATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).createOverseasEntityWithResumeLink(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testCreatingANewUpdateSubmissionIsSuccessfulWithValidation() throws ServiceException {
        overseasEntitySubmissionDto.setEntityNumber("OE111129");
        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());
        when(overseasEntitiesService.createOverseasEntityWithResumeLink(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenReturn(CREATED_SUCCESS_RESPONSE);
        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals(CREATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).createOverseasEntityWithResumeLink(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testCreatingANewSubmissionIsSuccessfulWithValidation() throws ServiceException {
        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());

        when(overseasEntitiesService.createOverseasEntityWithResumeLink(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenReturn(CREATED_SUCCESS_RESPONSE);
        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals(CREATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).createOverseasEntityWithResumeLink(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testCreatingANewSubmissionIsUnSuccessfulWithValidationError() {
        try (MockedStatic<ApiLogger> mockApiLogger = mockStatic(ApiLogger.class)) {

            final String errorLocation = "EXAMPLE_ERROR_LOCATION";
            final String error = "EXAMPLE_ERROR";
            Err err = Err.invalidBodyBuilderWithLocation(errorLocation).withError(error).build();
            Errors errors = new Errors(err);
            when(overseasEntitySubmissionDtoValidator.validatePartial(
                    eq(overseasEntitySubmissionDto),
                    any(Errors.class),
                    eq(REQUEST_ID)
            )).thenReturn(errors);

            var response = overseasEntitiesController.createNewSubmission(
                    transaction,
                    overseasEntitySubmissionDto,
                    REQUEST_ID,
                    USER_ID);

            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

            mockApiLogger.verify(
                    () -> ApiLogger.errorContext(
                            eq(REQUEST_ID),
                            eq("Validation errors : {\"errs\":[{\"error\":\"" +
                                    error + "\",\"location\":\"" +
                                    errorLocation + "\",\"" +
                                    "locationType\":\"request-body\",\"type\":\"ch:validation\"}]}"),
                            eq(null),
                            any()),
                    times(1)
            );
        }
    }

    @Test
    void testResponseBodyContainsValidationErrorsWhenValidationEnabled() {
        Err errName = Err.invalidBodyBuilderWithLocation("name").withError("Name is too long").build();
        Err errAddress = Err.invalidBodyBuilderWithLocation("address").withError("Missing address").build();

        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors(errName, errAddress));

        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        ChResponseBody<?> chResponseBody = (ChResponseBody<?>) response.getBody();
        assertNotNull(chResponseBody);
        Errors responseErrors = chResponseBody.getErrorBody();
        assertEquals(2, responseErrors.size());
        assertTrue(responseErrors.containsError(errName));
        assertTrue(responseErrors.containsError(errAddress));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testValidationStatusResponseWhenSubmissionIsFound() {
        when(overseasEntitiesService.getOverseasEntitySubmission(SUBMISSION_ID)).thenReturn(Optional.of(overseasEntitySubmissionDto));
        when(overseasEntitySubmissionDtoValidator.validateFull(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors());

        var response = overseasEntitiesController.getValidationStatus(SUBMISSION_ID, TRANSACTION_ID, REQUEST_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ValidationStatusResponse);
        ValidationStatusResponse validationStatusResponse = (ValidationStatusResponse) response.getBody();
        assertTrue(validationStatusResponse.isValid());
        assertEquals(null, validationStatusResponse.getValidationStatusError());
    }

    @Test
    void testValidationStatusResponseWhenSubmissionIsFoundWithValidationEnabledAndAllChecksPass() {

        ValidationStatusResponse validationStatus = new ValidationStatusResponse();
        validationStatus.setValid(true);
        when(overseasEntitiesService.getOverseasEntitySubmission(SUBMISSION_ID)).thenReturn(Optional.of(overseasEntitySubmissionDto));

        when(overseasEntitySubmissionDtoValidator.validateFull(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());

        var response = overseasEntitiesController.getValidationStatus(SUBMISSION_ID, TRANSACTION_ID, REQUEST_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ValidationStatusResponse);
        ValidationStatusResponse validationStatusResponse = (ValidationStatusResponse) response.getBody();
        assertTrue(validationStatusResponse.isValid());
        assertNull(validationStatusResponse.getValidationStatusError());
    }

    @Test
    void testValidationStatusResponseWhenSubmissionIsFoundWithValidationEnabledAndCheckFails() {

        ValidationStatusResponse validationStatus = new ValidationStatusResponse();
        validationStatus.setValid(true);
        when(overseasEntitiesService.getOverseasEntitySubmission(SUBMISSION_ID)).thenReturn(Optional.of(overseasEntitySubmissionDto));

        final String errorLocation = "EXAMPLE_ERROR_LOCATION";
        final String error = "EXAMPLE_ERROR";
        Err err = Err.invalidBodyBuilderWithLocation(errorLocation).withError(error).build();
        when(overseasEntitySubmissionDtoValidator.validateFull(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors(err));

        var response = overseasEntitiesController.getValidationStatus(SUBMISSION_ID, TRANSACTION_ID, REQUEST_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ValidationStatusResponse);
        ValidationStatusResponse validationStatusResponse = (ValidationStatusResponse) response.getBody();
        assertFalse(validationStatusResponse.isValid());
        assertNotNull(validationStatusResponse.getValidationStatusError());
    }

    @Test
    void testValidationStatusResponseWhenSubmissionNotFound() {
        when(overseasEntitiesService.getOverseasEntitySubmission(SUBMISSION_ID)).thenReturn(Optional.empty());
        var response = overseasEntitiesController.getValidationStatus(SUBMISSION_ID, TRANSACTION_ID, REQUEST_ID);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testUpdatingAnExistingSubmissionIsSuccessful() throws ServiceException {

        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors());

        when(overseasEntitiesService.updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenReturn(UPDATED_SUCCESS_RESPONSE);

        var response = overseasEntitiesController.updateSubmission(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(UPDATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testUpdatingAnExistingSubmissionIsSuccessfulWithValidationEnabledButValidationIsDeterminedToNotBeRequired() throws ServiceException {

        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());

        when(overseasEntitiesService.updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenReturn(UPDATED_SUCCESS_RESPONSE);

        var response = overseasEntitiesController.updateSubmission(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(UPDATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
        verify(overseasEntitySubmissionDtoValidator, never()).validateFull(any(), any(), any());
    }

    @Test
    void testUpdatingAnExistingSubmissionIsSuccessfulWithValidationEnabledAndValidationIsDeterminedToBeRequired() throws ServiceException {

        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(new ArrayList<>());
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().add(new BeneficialOwnerIndividualDto());

        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());

        when(overseasEntitiesService.updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenReturn(UPDATED_SUCCESS_RESPONSE);

        var response = overseasEntitiesController.updateSubmission(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(UPDATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
        verify(overseasEntitySubmissionDtoValidator).validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID));
    }

    @Test
    void testUpdatingAnExistingSubmissionIsUnSuccessfulWhenValidationChecksFail() throws ServiceException {

        overseasEntitySubmissionDto.setManagingOfficersCorporate(new ArrayList<>());
        overseasEntitySubmissionDto.getManagingOfficersCorporate().add(new ManagingOfficerCorporateDto());

        Err err = Err.invalidBodyBuilderWithLocation("Any").withError("Any").build();

        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors(err));

        var response = overseasEntitiesController.updateSubmission(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

        verify(overseasEntitiesService, never()).updateOverseasEntity(any(), any(), any(), any(), any());
    }

    @Test
    void testUpdatingAnExistingSubmissionIsUnSuccessfulWithValidationError() {
        try (MockedStatic<ApiLogger> mockApiLogger = mockStatic(ApiLogger.class)) {

            overseasEntitySubmissionDto.setManagingOfficersCorporate(new ArrayList<>());
            overseasEntitySubmissionDto.getManagingOfficersCorporate().add(new ManagingOfficerCorporateDto());

            final String errorLocation = "EXAMPLE_ERROR_LOCATION";
            final String error = "EXAMPLE_ERROR";
            Err err = Err.invalidBodyBuilderWithLocation(errorLocation).withError(error).build();
            Errors errors = new Errors(err);
            when(overseasEntitySubmissionDtoValidator.validatePartial(
                    eq(overseasEntitySubmissionDto),
                    any(Errors.class),
                    eq(REQUEST_ID)
            )).thenReturn(errors);

            var response = overseasEntitiesController.updateSubmission(
                    transaction,
                    SUBMISSION_ID,
                    overseasEntitySubmissionDto,
                    REQUEST_ID,
                    USER_ID);

            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

            mockApiLogger.verify(
                    () -> ApiLogger.errorContext(
                            eq(REQUEST_ID),
                            eq("Validation errors : {\"errs\":[{\"error\":\"" +
                                    error + "\",\"location\":\"" +
                                    errorLocation + "\",\"" +
                                    "locationType\":\"request-body\",\"type\":\"ch:validation\"}]}"),
                            eq(null),
                            any()),
                    times(1)
            );
        }
    }

    @Test
    void testResponseBodyAfterUpdatingAnExistingSubmissionWhenValidationChecksFailContainsValidationErrors() {

        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(new ArrayList<>());
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().add(new BeneficialOwnerCorporateDto());

        Err errName = Err.invalidBodyBuilderWithLocation("name").withError("Name is too long").build();
        Err errAddress = Err.invalidBodyBuilderWithLocation("address").withError("Missing address").build();

        when(overseasEntitySubmissionDtoValidator.validatePartial(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors(errName, errAddress));

        var response = overseasEntitiesController.updateSubmission(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        ChResponseBody<?> chResponseBody = (ChResponseBody<?>) response.getBody();
        assertNotNull(chResponseBody);
        Errors responseErrors = chResponseBody.getErrorBody();
        assertEquals(2, responseErrors.size());
        assertTrue(responseErrors.containsError(errName));
        assertTrue(responseErrors.containsError(errAddress));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testGetSubmissionIsSuccessful() throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID
                )).thenReturn(ResponseEntity.ok().body(overseasEntitySubmissionDto));

        var response = overseasEntitiesController.getSubmission(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetSubmissionIsNotSuccessfulWhenSubmissionNotLinkedToTransaction() throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID
        )).thenThrow(new SubmissionNotLinkedToTransactionException(String.format("Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s", TRANSACTION_ID, SUBMISSION_ID)));

        var response = overseasEntitiesController.getSubmission(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetSubmissionIsNotSuccessfulWhenSubmissionNotFound() throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {
        when(overseasEntitiesService.getSavedOverseasEntity(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID
        )).thenThrow(new SubmissionNotFoundException(String.format("Empty submission returned when generating filing for %s", SUBMISSION_ID)));

        var response = overseasEntitiesController.getSubmission(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
