package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@RestController
@RequestMapping("/transactions/{" + TRANSACTION_ID_KEY + "}/overseas-entity")
public class OverseasEntitiesController {

    private final OverseasEntitiesService overseasEntitiesService;

    @Autowired
    public OverseasEntitiesController(OverseasEntitiesService overseasEntitiesService) {
        this.overseasEntitiesService = overseasEntitiesService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("\n\n*** VALIATION ERROR! ***\n\n");

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        System.out.println("\n\n*** Errors = >" + errors + "< ***\n\n");

        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping
    public ResponseEntity<Object> createNewSubmission(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @Valid @RequestBody OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId,
            HttpServletRequest request) {

        System.out.println("\n\n*** NEW SUBMISSION RECEIVED ***\n\n");

        System.out.println("\n\n*** Test field value = >" + overseasEntitySubmissionDto.getTest() + "< ***\n\n");

        String passthroughTokenHeader = request.getHeader(ApiSdkManager.getEricPassthroughTokenHeader());

        var logMap = new HashMap<String, Object>();
        logMap.put(TRANSACTION_ID_KEY, transaction.getId());
        ApiLogger.infoContext(requestId, "Calling service to create Overseas Entity Submission", logMap);

        try {
            return this.overseasEntitiesService.createOverseasEntity(transaction, overseasEntitySubmissionDto, passthroughTokenHeader);
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
