package uk.gov.companieshouse.overseasentitiesapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.HashHelper;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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


    @GetMapping("/beneficial-owners")
    public ResponseEntity<PrivateBoDataListApi> getOverseasEntityBeneficialOwners(
            @PathVariable(TRANSACTION_ID_KEY) String transactionId,
            @PathVariable(OVERSEAS_ENTITY_ID_KEY) String overseasEntityId,
            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException, NoSuchAlgorithmException {

        System.out.println("=================LOCAL 617 BRANCH ===========================");

        final var logMap = new HashMap<String, Object>();
        logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
        logMap.put(TRANSACTION_ID_KEY, transactionId);
        ApiLogger.infoContext(requestId, "Calling service to retrieve private beneficial owner information", logMap);

        isRoeUpdateFlagEnabled();
        final Optional<OverseasEntitySubmissionDto> overseasEntitySubmissionDto = overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId);
        if (overseasEntitySubmissionDto.isPresent() && overseasEntitySubmissionDto.get().isForUpdate()) {
            String entityNumber = overseasEntitySubmissionDto.get().getEntityNumber();
            try {
                var objectMapper = new ObjectMapper();
                var boDataListApi = objectMapper.readValue(jsonBeneficialOwnerString, PrivateBoDataListApi.class );
                PrivateBoDataListApi privateBeneficialOwnersData = new PrivateBoDataListApi(boDataListApi.getBoPrivateData());
//                PrivateBoDataListApi privateBeneficialOwnersData = privateDataRetrievalService.getBeneficialOwnersData(entityNumber);
//                ArrayList boPrivateData = new ArrayList<>();
//                boPrivateData.add(privateBoDataListApi);
//                PrivateBoDataListApi privateBeneficialOwnersData = new PrivateBoDataListApi(boPrivateData);

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
                System.out.println("============Response entity log " + ResponseEntity.ok(privateBeneficialOwnersData).getBody().getBoPrivateData().get(0));
                return ResponseEntity.ok(privateBeneficialOwnersData);
            } catch (Exception e) {
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


    public static final String jsonBeneficialOwnerString = "["
            + "{"
            + "\"id\":\"9001808903\","
            + "\"date_became_registrable\":\"2023-04-13 00:00:00.0\","
            + "\"is_service_address_same_as_usual_address\":\"N\","
            + "\"date_of_birth\":\"2023-04-13 00:00:00.0\","
            + "\"usual_residential_address\":{"
            + "\"address_line_1\":\"491 SOUTH GREEN OLD BOULEVARD\","
            + "\"address_line_2\":\"SIT NON PRAESENTIUM\","
            + "\"care_of\":null,"
            + "\"country\":\"ITALY\","
            + "\"locality\":\"EXPEDITA IN OFFICIIS\","
            + "\"po_box\":null,"
            + "\"postal_code\":\"29765\","
            + "\"premises\":\"DUSTIN MONTOYA\","
            + "\"region\":\"ULLAM AUTEM IMPEDIT\""
            + "},"
            + "\"principal_address\":{"
            + "\"address_line_1\":null,"
            + "\"address_line_2\":null,"
            + "\"care_of\":null,"
            + "\"country\":null,"
            + "\"locality\":null,"
            + "\"po_box\":null,"
            + "\"postal_code\":null,"
            + "\"premises\":null,"
            + "\"region\":null"
            + "}"
            + "},"
            + "{"
            + "\"id\":\"9001808904\","
            + "\"date_became_registrable\":\"2023-04-13 00:00:00.0\","
            + "\"is_service_address_same_as_usual_address\":\"N\","
            + "\"date_of_birth\":null,"
            + "\"usual_residential_address\":{"
            + "\"address_line_1\":null,"
            + "\"address_line_2\":null,"
            + "\"care_of\":null,"
            + "\"country\":null,"
            + "\"locality\":null,"
            + "\"po_box\":null,"
            + "\"postal_code\":null,"
            + "\"premises\":null,"
            + "\"region\":null"
            + "},"
            + "\"principal_address\":{"
            + "\"address_line_1\":\"204 NOBEL DRIVE\","
            + "\"address_line_2\":\"ASPERIORES VOLUPTATE\","
            + "\"care_of\":null,"
            + "\"country\":\"MACEDONIA\","
            + "\"locality\":\"VELIT UT FACILIS VE\","
            + "\"po_box\":null,"
            + "\"postal_code\":\"41608\","
            + "\"premises\":\"MEGAN WILDER\","
            + "\"region\":\"ID AUT OFFICIA CUPI\""
            + "}"
            + "},"
            + "{"
            + "\"id\":\"9001808905\","
            + "\"date_became_registrable\":\"2023-04-13 00:00:00.0\","
            + "\"is_service_address_same_as_usual_address\":\"N\","
            + "\"date_of_birth\":null,"
            + "\"usual_residential_address\":{"
            + "\"address_line_1\":null,"
            + "\"address_line_2\":null,"
            + "\"care_of\":null,"
            + "\"country\":null,"
            + "\"locality\":null,"
            + "\"po_box\":null,"
            + "\"postal_code\":null,"
            + "\"premises\":null,"
            + "\"region\":null"
            + "},"
            + "\"principal_address\":{"
            + "\"address_line_1\":\"55 GREEN COWLEY PARKWAY\","
            + "\"address_line_2\":\"DISTINCTIO FACILIS\","
            + "\"care_of\":null,"
            + "\"country\":\"MAURITIUS\","
            + "\"locality\":\"CULPA EOS ENIM QUI\","
            + "\"po_box\":null,"
            + "\"postal_code\":\"90079\","
            + "\"premises\":\"CORA BENTON\","
            + "\"region\":\"IN VELIT ET EIUS MIN\""
            + "}"
            + "}"
            + "]";
}