package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.HashingException;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.*;

@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}")
public class OverseasEntitiesDataController {

    PrivateDataRetrievalService privateDataRetrievalService;
    OverseasEntitiesService overseasEntitiesService;

    @Value("${FEATURE_FLAG_ENABLE_ROE_UPDATE_24112022:false}")
    private boolean isRoeUpdateEnabled;

    @Value("${PUBLIC_API_IDENTITY_HASH_SALT}")
    private String salt;

    @Autowired
    public OverseasEntitiesDataController(
            final PrivateDataRetrievalService privateDataRetrievalService,
            final OverseasEntitiesService overseasEntitiesService
    ){
        this.privateDataRetrievalService = privateDataRetrievalService;
        this.overseasEntitiesService = overseasEntitiesService;
    }

    @GetMapping("/details")
    public ResponseEntity<OverseasEntityDataApi> getOverseasEntityDetails (
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {

        HashMap<String, Object> logMap = createLogMap(transactionId, overseasEntityId);

        ApiLogger.infoContext(requestId, "Calling service to check the overseas entity submission", logMap);

        final var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);
        if (submissionDtoOptional.isPresent()) {
            OverseasEntitySubmissionDto submissionDto = submissionDtoOptional.get();
            if (!submissionDto.isForUpdate()) {
                throw new ServiceException("Submission for overseas entity details must be for update");
            }

            isRoeUpdateFlagEnabled();

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

    @GetMapping("/managing-officers")
    public ResponseEntity<ManagingOfficerListDataApi> getManagingOfficers(
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {

        HashMap<String, Object> logMap = createLogMap(transactionId, overseasEntityId);
        ApiLogger.infoContext(requestId, "Calling Overseas Entities Service to retrieve private MO data for overseas entity " + overseasEntityId, logMap);

        final var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        if (submissionDtoOptional.isPresent()) {
            final var submissionDto = submissionDtoOptional.get();

            if (!submissionDto.isForUpdate()) {
                throw new ServiceException("Submission for overseas entity details must be for update");
            }

            isRoeUpdateFlagEnabled();

            return retrieveAndEvaluateManagingOfficerData(submissionDto, overseasEntityId, requestId, logMap);
        } else {
            ApiLogger.errorContext(requestId, "Could not find overseas entity submission for overseas entity " + overseasEntityId, null, logMap);
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<ManagingOfficerListDataApi> retrieveAndEvaluateManagingOfficerData(OverseasEntitySubmissionDto submissionDto, String overseasEntityId, String requestId, HashMap<String, Object> logMap) {
        try {
            ManagingOfficerListDataApi managingOfficerDataList = privateDataRetrievalService.getManagingOfficerData(submissionDto.getEntityNumber());

            if (managingOfficerDataList == null || managingOfficerDataList.getManagingOfficerData() == null || managingOfficerDataList.getManagingOfficerData().isEmpty()) {
                ApiLogger.errorContext(requestId, "Could not find any managing officers data for overseas entity " + overseasEntityId, null, logMap);
                return ResponseEntity.notFound().build();
            }

            var hashHelper = new HashHelper(salt);

            managingOfficerDataList.getManagingOfficerData().forEach(managingOfficerData -> {
                try {
                    String hashedId = hashHelper.encode(managingOfficerData.getManagingOfficerAppointmentId());
                    managingOfficerData.setHashedId(hashedId);
                    managingOfficerData.setManagingOfficerAppointmentId(null);
                } catch (NoSuchAlgorithmException e) {
                    throw new HashingException("Cannot encode Managing Officer ID", e);
                }
            });

            ApiLogger.infoContext(requestId, "Successfully retrieved the managing officers data", logMap);
            return ResponseEntity.ok(managingOfficerDataList);

        } catch (ServiceException e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void isRoeUpdateFlagEnabled() throws ServiceException {
        if (!isRoeUpdateEnabled) {
            throw new ServiceException("ROE Update feature must be enabled for get overseas entity details");
        }
    }

    private HashMap<String, Object> createLogMap(String transactionId, String overseasEntityId) {
        HashMap<String, Object> logMap = new HashMap<>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        return logMap;
    }
}
