package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import java.util.HashMap;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;

@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/details")
public class OverseasEntitiesDataController {

    PrivateDataRetrievalService privateDataRetrievalService;
    OverseasEntitiesService overseasEntitiesService;

    @Value("${FEATURE_FLAG_ENABLE_ROE_UPDATE_24112022:false}")
    private boolean isRoeUpdateEnabled;

    @Autowired
    public OverseasEntitiesDataController(
            final PrivateDataRetrievalService privateDataRetrievalService,
            final OverseasEntitiesService overseasEntitiesService
    ){
        this.privateDataRetrievalService = privateDataRetrievalService;
        this.overseasEntitiesService = overseasEntitiesService;
    }

    @GetMapping
    public ResponseEntity<OverseasEntityDataApi> getOverseasEntityDetails (
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {

        final var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);

        ApiLogger.infoContext(requestId, "Calling service to check the overseas entity submission", logMap);

        final var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);
        if (submissionDtoOptional.isPresent()) {
            OverseasEntitySubmissionDto submissionDto = submissionDtoOptional.get();
            if (!submissionDto.isForUpdate()) {
                throw new ServiceException("Submission for overseas entity details must be for update");
            }
            if (!isRoeUpdateEnabled) {
                throw new ServiceException("ROE Update feature must be enabled for get overseas entity details");
            }

            return getOverseasEntityDataResponse(overseasEntityId, requestId, submissionDto, logMap);

        } else {
            final var message = String.format("Could not find overseas entity submission for overseas entity details %s", overseasEntityId);
            ApiLogger.errorContext(requestId, message, null, logMap);
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<OverseasEntityDataApi> getOverseasEntityDataResponse(
            String overseasEntityId,
            String requestId,
            OverseasEntitySubmissionDto submissionDto,
            HashMap<String, Object> logMap) {
        final String email = submissionDto.getEntity().getEmail();

        OverseasEntityDataApi overseasEntityDataApi = null;

        if (StringUtils.isBlank(email)) {
            String companyNumber = submissionDto.getEntityNumber();

            try {
                ApiLogger.infoContext(requestId, "Calling service to get overseas entity details",
                        logMap);
                overseasEntityDataApi = privateDataRetrievalService.getOverseasEntityData(companyNumber);
                if (overseasEntityDataApi == null || StringUtils.isBlank(overseasEntityDataApi.getEmail())) {
                    final var message = String.format("Could not find overseas entity details for overseas entity %s",
                            overseasEntityId);
                    ApiLogger.errorContext(requestId, message, null, logMap);
                    return ResponseEntity.notFound().build();
                }
            } catch (ServiceException e) {
                ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            overseasEntityDataApi = new OverseasEntityDataApi();
            overseasEntityDataApi.setEmail(email);
        }
        
        return ResponseEntity.ok(overseasEntityDataApi);
    }
}
