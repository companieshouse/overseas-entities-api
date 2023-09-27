package uk.gov.companieshouse.overseasentitiesapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.api.model.utils.PrivateDataList;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.Constants;

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


    private <T> ResponseEntity<T> checkSubmissionDto(
            Function<Map, ResponseEntity<T>> retrievalFunction,
            String transactionId,
            String overseasEntityId,
            String requestId) throws ServiceException {

        Map<String, Object> logMap = new HashMap<>();
        logMap.put(Constants.OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(Constants.TRANSACTION_ID_KEY, transactionId);
        ApiLogger.infoContext(requestId,
                "Calling Overseas Entities Service to retrieve private trust data for overseas entity "
                        + overseasEntityId, logMap);
        final var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(
                overseasEntityId);
        if (submissionDtoOptional.isPresent()) {
            final var submissionDto = submissionDtoOptional.get();

            if (!submissionDto.isForUpdate()) {
                throw new ServiceException(
                        "Submission for overseas entity details must be for update");
            }
            if (!isRoeUpdateEnabled) {
                throw new ServiceException(
                        "ROE Update feature must be enabled for get overseas entity details");
            }
            return retrievalFunction.apply(logMap);
        } else {
            ApiLogger.errorContext(requestId,
                    "Could not find overseas entity submission for overseas entity "
                            + overseasEntityId, null, logMap);
            return ResponseEntity.notFound().build();
        }
    }
    private <T extends PrivateDataList> ResponseEntity<T> retrievePrivateTrustData(
            Callable<T> supplier,
            String overseasEntityId,
            String requestId,
            String logPart,
            Map<String, Object> logMap
    ) {
        try {
            T trusts = supplier.call();

            if (trusts == null || trusts.getData() == null || trusts.getData().isEmpty()) {
                ApiLogger.errorContext(requestId,
                        "Could not find any "+logPart+" for overseas entity " + overseasEntityId,
                        null, logMap);
                return ResponseEntity.notFound().build();
            }

            ApiLogger.infoContext(requestId, "Successfully retrieved " + logPart, logMap);
            return ResponseEntity.ok(trusts);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
