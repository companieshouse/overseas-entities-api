package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntitySubmissionDtoValidator;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;
import uk.gov.companieshouse.service.rest.err.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_IDENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@RestController
@RequestMapping("/transactions/{" + TRANSACTION_ID_KEY + "}/overseas-entity")
public class OverseasEntitiesController {

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

    @PatchMapping
    public ResponseEntity<Object> completeInProgressSubmission(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            @RequestHeader(value = ERIC_IDENTITY) String userId,
            HttpServletRequest request) {

        // Maybe do API validation here, at end of the journey, when completing?

        return this.overseasEntitiesService.completeInProgressOverseasEntity(
                transaction,
                requestId);
    }

    @PutMapping
    public ResponseEntity<Object> replaceInProgressSubmission(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @RequestBody OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            @RequestHeader(value = ERIC_IDENTITY) String userId,
            HttpServletRequest request) {

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());
        String passThroughTokenHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());

        ApiLogger.infoContext(requestId, "Calling service to replace the in progress Overseas Entity Submission", logMap);

        try {
            return this.overseasEntitiesService.replaceInProgressOverseasEntity(
                    transaction,
                    overseasEntitySubmissionDto,
                    passThroughTokenHeader,
                    requestId,
                    userId);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId,"Error Creating Overseas Entity Submission", e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createNewInProgressSubmission(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @RequestBody OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            @RequestHeader(value = ERIC_IDENTITY) String userId,
            HttpServletRequest request) {

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());

        try {
            if (isValidationEnabled) {

                // Comment out API validation for now as POST end-point will be used for the initial submission to create
                // a new OE from the first screen (Land Sold Filter page) and model will be incomplete at this stage

//                var validationErrors = overseasEntitySubmissionDtoValidator.validate(overseasEntitySubmissionDto, new Errors(), requestId);
//
//                if (validationErrors.hasErrors()) {
//                    ApiLogger.errorContext(requestId, "Validation errors : " + validationErrors, new Exception());
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
            }

            String passThroughTokenHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());

            ApiLogger.infoContext(requestId, "Calling service to create an in progress Overseas Entity Submission", logMap);

            return this.overseasEntitiesService.createInProgressOverseasEntity(
                    transaction,
                    overseasEntitySubmissionDto,
                    passThroughTokenHeader,
                    requestId,
                    userId);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId,"Error Creating Overseas Entity Submission", e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{overseas_entity_id}/validation-status")
    public ResponseEntity<Object> getValidationStatus(
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String submissionId,
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) {

        var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, submissionId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        ApiLogger.infoContext(requestId, "Calling service to get validation status", logMap);
        try {
            var validationStatusResponse = this.overseasEntitiesService.isValid(submissionId);
            return ResponseEntity.ok().body(validationStatusResponse);
        } catch (SubmissionNotFoundException e) {
            ApiLogger.errorContext(requestId,e.getMessage(), e, logMap);
            return ResponseEntity.notFound().build();
        }
    }
}
