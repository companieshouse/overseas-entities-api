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
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntitySubmissionDtoValidator;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;
import uk.gov.companieshouse.service.rest.response.ChResponseBody;

import java.net.URI;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesControllerTest {

    private static final ResponseEntity<Object> CREATED_SUCCESS_RESPONSE = ResponseEntity.created(URI.create("URI")).body("Created");
    private static final ResponseEntity<Object> UPDATED_SUCCESS_RESPONSE = ResponseEntity.ok().build();
    private static final ResponseEntity<Object> UPDATED_FAILURE_RESPONSE = ResponseEntity.internalServerError().build();

    private static final String REQUEST_ID = "fd4gld5h3jhh";
    private static final String PASSTHROUGH = "13456";
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

    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void init() {
        setValidationEnabledFeatureFlag(false);

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
                USER_ID)).thenReturn(CREATED_SUCCESS_RESPONSE);
        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID,
                mockHttpServletRequest);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals(CREATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testCreatingANewSubmissionIsSuccessfulWithValidation() throws ServiceException {
        setValidationEnabledFeatureFlag(true);

        when(overseasEntitySubmissionDtoValidator.validate(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID))).thenReturn(new Errors());

        when(overseasEntitiesService.createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH,
                REQUEST_ID,
                USER_ID)).thenReturn(CREATED_SUCCESS_RESPONSE);
        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID,
                mockHttpServletRequest);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals(CREATED_SUCCESS_RESPONSE, response);

        verify(overseasEntitiesService).createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testCreatingANewSubmissionIsUnSuccessfulWithValidationError() throws ServiceException {
        setValidationEnabledFeatureFlag(true);
        Err err = Err.invalidBodyBuilderWithLocation("Any").withError("Any").build();

        when(overseasEntitySubmissionDtoValidator.validate(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors(err));

        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID,
                mockHttpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testResponseBodyContainsValidationErrorsWhenValidationEnabled() {
        setValidationEnabledFeatureFlag(true);
        Err errName = Err.invalidBodyBuilderWithLocation("name").withError("Name is too long").build();
        Err errAddress = Err.invalidBodyBuilderWithLocation("address").withError("Missing address").build();

        when(overseasEntitySubmissionDtoValidator.validate(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID)
        )).thenReturn(new Errors(errName, errAddress));

        var response = overseasEntitiesController.createNewSubmission(
                transaction,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID,
                mockHttpServletRequest);

        ChResponseBody<?> chResponseBody = (ChResponseBody<?>) response.getBody();
        assertNotNull(chResponseBody);
        Errors responseErrors = chResponseBody.getErrorBody();
        assertEquals(2, responseErrors.size());
        assertTrue(responseErrors.containsError(errName));
        assertTrue(responseErrors.containsError(errAddress));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
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

    @Test
    void testUpdatingAnExistingSubmissionIsSuccessful() throws ServiceException {
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
    void testUpdatingAnExistingSubmissionIsUnSuccessful() throws ServiceException {
        when(overseasEntitiesService.updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID)).thenThrow(new RuntimeException("UNEXPECTED ERROR"));

        var response = overseasEntitiesController.updateSubmission(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        assertEquals(UPDATED_FAILURE_RESPONSE, response);

        verify(overseasEntitiesService).updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testUpdatingAnExistingSubmissionIsSuccessfulWithValidationEnabledButValidationIsDeterminedToNotBeRequired() throws ServiceException {
        setValidationEnabledFeatureFlag(true);

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
        verify(overseasEntitySubmissionDtoValidator, never()).validate(any(), any(), any());
    }

    @Test
    void testUpdatingAnExistingSubmissionIsSuccessfulWithValidationEnabledAndValidationIsDeterminedToBeRequired() throws ServiceException {
        setValidationEnabledFeatureFlag(true);

        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(new ArrayList<>());
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().add(new BeneficialOwnerIndividualDto());

        when(overseasEntitySubmissionDtoValidator.validate(
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
        verify(overseasEntitySubmissionDtoValidator).validate(
                eq(overseasEntitySubmissionDto),
                any(Errors.class),
                eq(REQUEST_ID));
    }

    @Test
    void testUpdatingAnExistingSubmissionIsUnSuccessfulWhenValidationChecksFail() throws ServiceException {
        setValidationEnabledFeatureFlag(true);

        overseasEntitySubmissionDto.setManagingOfficersCorporate(new ArrayList<>());
        overseasEntitySubmissionDto.getManagingOfficersCorporate().add(new ManagingOfficerCorporateDto());

        Err err = Err.invalidBodyBuilderWithLocation("Any").withError("Any").build();

        when(overseasEntitySubmissionDtoValidator.validate(
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
    void testResponseBodyAfterUpdatingAnExistingSubmissionWhenValidationChecksFailContainsValidationErrors() {
        setValidationEnabledFeatureFlag(true);

        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(new ArrayList<>());
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().add(new BeneficialOwnerCorporateDto());

        Err errName = Err.invalidBodyBuilderWithLocation("name").withError("Name is too long").build();
        Err errAddress = Err.invalidBodyBuilderWithLocation("address").withError("Missing address").build();

        when(overseasEntitySubmissionDtoValidator.validate(
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

    private void setValidationEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(overseasEntitiesController, "isValidationEnabled", value);
    }
}
