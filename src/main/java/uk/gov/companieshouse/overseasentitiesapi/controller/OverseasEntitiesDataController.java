package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_KEY;


@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}")
public class OverseasEntitiesDataController {

    private static final String TRANSACTION_NOT_LINKED_ERROR_MESSAGE = "Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s";
    PrivateDataRetrievalService privateDataRetrievalService;
    OverseasEntitiesService overseasEntitiesService;

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
    public ResponseEntity<Object> getOverseasEntityDetails (
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {

        String transactionId = transaction.getId();
        HashMap<String, Object> logMap = createLogMap(transactionId, overseasEntityId);

        ApiLogger.infoContext(requestId, "Calling service to check the overseas entity submission", logMap);

        final Optional<OverseasEntitySubmissionDto> submissionDtoOptional;
        try {
            submissionDtoOptional = overseasEntitiesService.getSavedOverseasEntity(transaction, overseasEntityId, requestId);
        } catch (SubmissionNotLinkedToTransactionException e) {
            ApiLogger.errorContext(requestId, e);
            return ResponseEntity.badRequest().body(String.format(
                    TRANSACTION_NOT_LINKED_ERROR_MESSAGE, transaction.getId(), overseasEntityId));
        }

        if (submissionDtoOptional.isPresent()) {
            OverseasEntitySubmissionDto submissionDto = submissionDtoOptional.get();
            if (!submissionDto.isForUpdateOrRemove()) {
                throw new ServiceException("Submission for overseas entity details must be for update or remove");
            }

            return getOverseasEntityDataResponse(overseasEntityId, requestId, submissionDto, logMap);

        } else {
            final var message = String.format("Could not find overseas entity submission for overseas entity details %s", overseasEntityId);
            ApiLogger.errorContext(requestId, message, null, logMap);
            return ResponseEntity.notFound().build();
        }

    }

    private ResponseEntity<Object> getOverseasEntityDataResponse(
            String overseasEntityId,
            String requestId,
            OverseasEntitySubmissionDto submissionDto,
            HashMap<String, Object> logMap) {

        final String email = submissionDto.getEntity().getEmail();

        OverseasEntityDataApi overseasEntityDataApi;

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

    @GetMapping("/beneficial-owners")
    public ResponseEntity<Object> getOverseasEntityBeneficialOwners(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws NoSuchAlgorithmException {

        String transactionId = transaction.getId();

        final var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        ApiLogger.infoContext(requestId, "Calling service to retrieve private beneficial owner information", logMap);

        final Optional<OverseasEntitySubmissionDto> overseasEntitySubmissionDto;
        try {
            overseasEntitySubmissionDto = overseasEntitiesService.getSavedOverseasEntity(transaction, overseasEntityId, requestId);
        } catch (SubmissionNotLinkedToTransactionException e) {
            ApiLogger.errorContext(requestId, e);
            return ResponseEntity.badRequest().body(String.format(
                    TRANSACTION_NOT_LINKED_ERROR_MESSAGE, transaction.getId(), overseasEntityId));
        }

        if (overseasEntitySubmissionDto.isPresent() && overseasEntitySubmissionDto.get().isForUpdateOrRemove()) {
            String entityNumber = overseasEntitySubmissionDto.get().getEntityNumber();
            try {
                PrivateBoDataListApi privateBeneficialOwnersData = privateDataRetrievalService.getBeneficialOwnersData(entityNumber);

                if (privateBeneficialOwnersData == null || privateBeneficialOwnersData.getBoPrivateData().isEmpty()) {
                    final var message = String.format("Beneficial owner private data not found for overseas entity %s",
                            overseasEntityId);
                    ApiLogger.errorContext(requestId, message, null, logMap);
                    return ResponseEntity.notFound().build();
                }

                var hashHelper = new HashHelper(salt);
                for (PrivateBoDataApi privateBoData : privateBeneficialOwnersData) {
                    var hashedId = hashHelper.encode(privateBoData.getPscId());
                    privateBoData.setHashedId(hashedId);
                    privateBoData.setPscId(null);
                }

                return ResponseEntity.ok(privateBeneficialOwnersData);
            } catch (ServiceException e) {
                ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            final var message = String.format("Could not retrieve private beneficial owner data without overseas entity submission for overseas entity %s", overseasEntityId);
            ApiLogger.errorContext(requestId, message, null, logMap);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/managing-officers")
    public ResponseEntity<Object> getManagingOfficers(
            @RequestAttribute(TRANSACTION_KEY) Transaction transaction,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {

        String transactionId = transaction.getId();
        HashMap<String, Object> logMap = createLogMap(transactionId, overseasEntityId);
        ApiLogger.infoContext(requestId, "Calling Overseas Entities Service to retrieve private MO data for overseas entity " + overseasEntityId, logMap);

        final Optional<OverseasEntitySubmissionDto> submissionDtoOptional;
        try {
            submissionDtoOptional = overseasEntitiesService.getSavedOverseasEntity(transaction, overseasEntityId, requestId);
        } catch (SubmissionNotLinkedToTransactionException e) {
            ApiLogger.errorContext(requestId, e);
            return ResponseEntity.badRequest().body(String.format(
                    TRANSACTION_NOT_LINKED_ERROR_MESSAGE, transaction.getId(), overseasEntityId));
        }

        if (submissionDtoOptional.isPresent()) {
            final var submissionDto = submissionDtoOptional.get();

            if (!submissionDto.isForUpdateOrRemove()) {
                throw new ServiceException("Submission for overseas entity details must be for update or remove");
            }

            return retrieveAndEvaluateManagingOfficerData(submissionDto, overseasEntityId, requestId, logMap);
        } else {
            ApiLogger.errorContext(requestId, "Could not find overseas entity submission for overseas entity " + overseasEntityId, null, logMap);
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<Object> retrieveAndEvaluateManagingOfficerData(OverseasEntitySubmissionDto submissionDto, String overseasEntityId, String requestId, HashMap<String, Object> logMap) {
        try {
            ManagingOfficerListDataApi managingOfficerDataList = privateDataRetrievalService.getManagingOfficerData(submissionDto.getEntityNumber());

            if (managingOfficerDataList == null || managingOfficerDataList.getManagingOfficerData() == null || managingOfficerDataList.getManagingOfficerData().isEmpty()) {
                ApiLogger.infoContext(requestId, "Could not find any managing officers data for overseas entity " + overseasEntityId, logMap);
                return ResponseEntity.notFound().build();
            }

            var hashHelper = new HashHelper(salt);

            for (ManagingOfficerDataApi managingOfficerData : managingOfficerDataList.getManagingOfficerData()) {
                processSingleManagingOfficer(managingOfficerData, hashHelper);
            }

            ApiLogger.infoContext(requestId, "Successfully retrieved the managing officers data", logMap);
            return ResponseEntity.ok(managingOfficerDataList);

        } catch (ServiceException e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void processSingleManagingOfficer(ManagingOfficerDataApi managingOfficerData, HashHelper hashHelper) throws ServiceException {
        try {
            String hashedId = hashHelper.encode(managingOfficerData.getManagingOfficerAppointmentId());
            managingOfficerData.setHashedId(hashedId);
            managingOfficerData.setManagingOfficerAppointmentId(null);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Cannot encode Managing Officer ID", e);
        }
    }

    private HashMap<String, Object> createLogMap(String transactionId, String overseasEntityId) {
        HashMap<String, Object> logMap = new HashMap<>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        return logMap;
    }
}
