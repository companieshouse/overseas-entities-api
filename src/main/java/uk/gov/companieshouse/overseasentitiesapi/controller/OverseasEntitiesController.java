package uk.gov.companieshouse.overseasentitiesapi.controller;

import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusError;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntitySubmissionDtoValidator;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;
import uk.gov.companieshouse.service.rest.err.Errors;
import uk.gov.companieshouse.service.rest.response.ChResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_IDENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@RestController
@RequestMapping("/transactions/{" + TRANSACTION_ID_KEY + "}/overseas-entity")
public class OverseasEntitiesController {

    private static final String VALIDATION_ERRORS_MESSAGE = "Validation errors : %s";

    private final OverseasEntitiesService overseasEntitiesService;
    private final OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator;

    @Value("${FEATURE_FLAG_ENABLE_VALIDATION_25082022}")
    private boolean isValidationEnabled;

    @Autowired
    public OverseasEntitiesController(OverseasEntitiesService overseasEntitiesService,
                                      OverseasEntitySubmissionDtoValidator overseasEntitySubmissionDtoValidator) {
        this.overseasEntitiesService = overseasEntitiesService;
        this.overseasEntitySubmissionDtoValidator = overseasEntitySubmissionDtoValidator;
    }

    @PostMapping
    public ResponseEntity<Object> createNewSubmission(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @RequestBody OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            @RequestHeader(value = ERIC_IDENTITY) String userId,
            HttpServletRequest request) {

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        try {
            if (isValidationEnabled) {
                var validationErrors = overseasEntitySubmissionDtoValidator.validateFull(overseasEntitySubmissionDto, new Errors(), requestId);

                if (validationErrors.hasErrors()) {
                    ApiLogger.errorContext(requestId, String.format(VALIDATION_ERRORS_MESSAGE,
                            convertErrorsToJsonString(validationErrors)), null, logMap);
                    var responseBody = ChResponseBody.createErrorsBody(validationErrors);
                    return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                }
            }

            String passThroughTokenHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());

            ApiLogger.infoContext(requestId, "Calling service to create Overseas Entity Submission", logMap);

            return this.overseasEntitiesService.createOverseasEntity(
                    transaction,
                    overseasEntitySubmissionDto,
                    passThroughTokenHeader,
                    requestId,
                    userId);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId, "Error Creating Overseas Entity Submission", e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{overseas_entity_id}")
    public ResponseEntity<Object> updateSubmission(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String submissionId,
            @RequestBody OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            @RequestHeader(value = ERIC_IDENTITY) String userId) {

        var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, submissionId);
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        ApiLogger.infoContext(requestId, "Calling service to update the Overseas Entity Submission", logMap);

        try {
            // TODO The logic related to what specific validation checks should be run will need to be more
            //      sophisticated - this is covered by ROE-1415. For now though, it's only necessary to check if
            //      all validation checks should be run, i.e. if the user is calling the PUT end-point from the
            //      'check your answers' screen.
            if (isValidationEnabled && isValidationRequired(overseasEntitySubmissionDto)) {
                var validationErrors = overseasEntitySubmissionDtoValidator.validatePartial(
                        overseasEntitySubmissionDto, new Errors(), requestId);

                if (validationErrors.hasErrors()) {
                    ApiLogger.errorContext(requestId, String.format(VALIDATION_ERRORS_MESSAGE,
                            convertErrorsToJsonString(validationErrors)), null, logMap);
                    var responseBody = ChResponseBody.createErrorsBody(validationErrors);
                    return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                }
            }

            return overseasEntitiesService.updateOverseasEntity(
                    transaction,
                    submissionId,
                    overseasEntitySubmissionDto,
                    requestId,
                    userId);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId, "Error Updating Overseas Entity Submission", e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Temporary endpoint for creating an initial OE mongo record with partial data
     * and no validation (to be added). This is to prevent issues with existing POST endpoint
     * that will validate a whole submission.
     * @param transaction The transaction to be linked to OE submission
     * @param overseasEntitySubmissionDto The data to store
     * @param requestId Http request ID, used in logs
     * @param userId the ERIC user id
     * @param request the HttpServletRequest
     * @return ResponseEntity
     */
    @PostMapping("/start")
    public ResponseEntity<Object> createNewSubmissionForSaveAndResume(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @RequestBody OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            @RequestHeader(value = ERIC_IDENTITY) String userId,
            HttpServletRequest request) {

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        try {
            String passThroughTokenHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());

            ApiLogger.infoContext(requestId, "createNewSubmissionForSaveAndResume Calling service to create Overseas Entity Submission", logMap);

            return this.overseasEntitiesService.createOverseasEntity(
                    transaction,
                    overseasEntitySubmissionDto,
                    passThroughTokenHeader,
                    requestId,
                    userId);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId, "Error Creating Overseas Entity Submission", e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{overseas_entity_id}/validation-status")
    public ResponseEntity<Object> getValidationStatus(
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String submissionId,
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) {
        ApiLogger.debugContext(requestId, "Called getValidationStatus(...)");

        var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, submissionId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);

        ApiLogger.infoContext(requestId, "Calling service to get the overseas entity submission", logMap);
        var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(submissionId);
        if (submissionDtoOptional.isPresent()) {
            var validationStatus = new ValidationStatusResponse();
            validationStatus.setValid(true);

            if (isValidationEnabled) {
                var validationErrors = overseasEntitySubmissionDtoValidator.validateFull(
                        submissionDtoOptional.get(), new Errors(), requestId);

                if (validationErrors.hasErrors()) {
                    final var errorsAsJsonString = convertErrorsToJsonString(validationErrors);
                    ApiLogger.errorContext(requestId, String.format(VALIDATION_ERRORS_MESSAGE, errorsAsJsonString), null, logMap);

                    flagValidationStatusAsFailed(validationStatus, errorsAsJsonString);
                }
            }

            return ResponseEntity.ok().body(validationStatus);
        }

        final var message = String.format("Could not find submission data for submission %s", submissionId);
        ApiLogger.errorContext(requestId, message, null, logMap);
        return ResponseEntity.notFound().build();
    }

    private void flagValidationStatusAsFailed(ValidationStatusResponse validationStatus, String errorsAsJsonString) {
        validationStatus.setValid(false);

        // A simplified 'errors' object can be created as ROE registrations are not software filed. Note also
        // that at least one 'error' needs to be present for the validation check to register as failed by the
        // Transactions API
        var errors = new ValidationStatusError[1];
        var error = new ValidationStatusError();
        error.setError(errorsAsJsonString);
        errors[0] = error;

        validationStatus.setValidationStatusError(errors);
    }

    private boolean isValidationRequired(OverseasEntitySubmissionDto dto) {
        // The presence of one or more BOs or MOs in the submission indicates that all data should be present and that
        // the entire DTO can now be validated
        return (Objects.nonNull(dto.getBeneficialOwnersCorporate()) && !dto.getBeneficialOwnersCorporate().isEmpty())
                || (Objects.nonNull(dto.getBeneficialOwnersGovernmentOrPublicAuthority()) && !dto.getBeneficialOwnersGovernmentOrPublicAuthority().isEmpty())
                || (Objects.nonNull(dto.getBeneficialOwnersIndividual()) && !dto.getBeneficialOwnersIndividual().isEmpty())
                || (Objects.nonNull(dto.getManagingOfficersCorporate()) && !dto.getManagingOfficersCorporate().isEmpty())
                || (Objects.nonNull(dto.getManagingOfficersIndividual()) && !dto.getManagingOfficersIndividual().isEmpty());
    }

    private String convertErrorsToJsonString(Errors validationErrors) {
        var gson = new GsonBuilder().create();
        return gson.toJson(validationErrors);
    }
}
