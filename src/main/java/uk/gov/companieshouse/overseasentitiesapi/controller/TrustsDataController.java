package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDataListApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.HashMap;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.*;

@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/trusts")
public class TrustsDataController {

    PrivateDataRetrievalService privateDataRetrievalService;
    OverseasEntitiesService overseasEntitiesService;

    @Value("${FEATURE_FLAG_ENABLE_ROE_UPDATE_24112022:false}")
    private boolean isRoeUpdateEnabled;

    @Autowired
    public TrustsDataController(
            final PrivateDataRetrievalService privateDataRetrievalService,
            final OverseasEntitiesService overseasEntitiesService
    ) {
        this.privateDataRetrievalService = privateDataRetrievalService;
        this.overseasEntitiesService = overseasEntitiesService;
    }

    @GetMapping("/details")
    public ResponseEntity<PrivateTrustDataListApi> getTrusts(
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {

        HashMap<String, Object> logMap = new HashMap<>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        ApiLogger.infoContext(requestId, "Calling Overseas Entities Service to retrieve private trust data for overseas entity " + overseasEntityId, logMap);

        final var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        if (submissionDtoOptional.isPresent()) {
            final var submissionDto = submissionDtoOptional.get();

            if (!submissionDto.isForUpdate()) {
                throw new ServiceException("Submission for overseas entity details must be for update");
            }

            if (!isRoeUpdateEnabled) {
                throw new ServiceException("ROE Update feature must be enabled for get overseas entity details");
            }

            return retrievePrivateTrustData(submissionDto, overseasEntityId, requestId, logMap);
        } else {
            ApiLogger.errorContext(requestId, "Could not find overseas entity submission for overseas entity " + overseasEntityId, null, logMap);
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<PrivateTrustDataListApi> retrievePrivateTrustData(OverseasEntitySubmissionDto submissionDto, String overseasEntityId, String requestId, HashMap<String, Object> logMap) {
        try {
            PrivateTrustDataListApi trusts = privateDataRetrievalService.getTrustsData(submissionDto.getEntityNumber());

            if (trusts == null || trusts.getTrustsData() == null || trusts.getTrustsData().isEmpty()) {
                ApiLogger.errorContext(requestId, "Could not find any trust data for overseas entity " + overseasEntityId, null, logMap);
                return ResponseEntity.notFound().build();
            }

            ApiLogger.infoContext(requestId, "Successfully retrieved trust data", logMap);
            return ResponseEntity.ok(trusts);
        } catch (ServiceException e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
