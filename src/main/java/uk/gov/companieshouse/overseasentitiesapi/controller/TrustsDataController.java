package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.api.model.trusts.PrivateTrustDetailsListApi;
import uk.gov.companieshouse.api.model.utils.Hashable;
import uk.gov.companieshouse.api.model.utils.PrivateDataList;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.Constants;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.Callable;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.*;

@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/trusts")
public class TrustsDataController {
    PrivateDataRetrievalService privateDataRetrievalService;
    OverseasEntitiesService overseasEntitiesService;

    @Value("${FEATURE_FLAG_ENABLE_ROE_UPDATE_24112022:false}")
    private boolean isRoeUpdateEnabled;

    @Value("${PUBLIC_API_IDENTITY_HASH_SALT}")
    private String salt;

    private HashHelper hashHelper = new HashHelper(salt);

    private Map<String, Object> logMap;

    @Autowired
    public TrustsDataController(
            final PrivateDataRetrievalService privateDataRetrievalService,
            final OverseasEntitiesService overseasEntitiesService) {
        this.privateDataRetrievalService = privateDataRetrievalService;
        this.overseasEntitiesService = overseasEntitiesService;
    }

    @GetMapping("/details")
    public ResponseEntity<PrivateTrustDetailsListApi> getTrustDetails(
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {
        logMap = makeLogMap(transactionId, overseasEntityId);
        String companyNumber = getCompanyNumber(overseasEntityId, requestId);
        if (companyNumber == null) {
            return ResponseEntity.notFound().build();
        }

        return retrievePrivateTrustData(
                () -> privateDataRetrievalService.getTrustDetails(companyNumber), requestId, "trust details");
    }

    private Map makeLogMap(String transactionId, String overseasEntityId) {
        return Map.of(
                Constants.OVERSEAS_ENTITY_ID_KEY, overseasEntityId,
                Constants.TRANSACTION_ID_KEY, transactionId
        );
    }

    private String getCompanyNumber(String overseasEntityId, String requestId) throws ServiceException {
        ApiLogger.infoContext(requestId,
                "Calling Overseas Entities Service to retrieve private trust data for overseas entity "
                        + overseasEntityId, logMap);
        final var submissionDtoOptional = overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);

        if (!submissionDtoOptional.isPresent()) {
            ApiLogger.errorContext(requestId,
                    "Could not find overseas entity submission for overseas entity "
                            + overseasEntityId, null, logMap);
            return null;
        }

        final var submissionDto = submissionDtoOptional.get();

        if (!submissionDto.isForUpdate()) {
            throw new ServiceException(
                    "Submission for overseas entity details must be for update");
        }
        if (!isRoeUpdateEnabled) {
            throw new ServiceException(
                    "ROE Update feature must be enabled for get overseas entity details");
        }

        return submissionDto.getEntityNumber();
    }

    private <U extends Hashable, T extends PrivateDataList<U>> ResponseEntity<T> retrievePrivateTrustData(
            Callable<T> supplier,
            String requestId,
            String logPart) {
        try {
            T dataList = supplier.call();

            if (dataList == null || dataList.getData() == null || dataList.getData().isEmpty()) {
                ApiLogger.errorContext(requestId,
                        "Could not find any " + logPart + " for overseas entity "
                                + logMap.get(Constants.OVERSEAS_ENTITY_ID_KEY),
                        null, logMap);
                return ResponseEntity.notFound().build();
            }
            for (var data : dataList) {
                hashId(data);
            }
            ApiLogger.infoContext(requestId, "Successfully retrieved " + logPart, logMap);
            return ResponseEntity.ok(dataList);
        } catch (Exception e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void hashId(Hashable hashableData) throws ServiceException {
        try {
            String hashedId = hashHelper.encode(hashableData.getId());
            hashableData.setHashedId(hashedId);
            hashableData.setId(null);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Cannot encode ID", e);
        }
    }
}
