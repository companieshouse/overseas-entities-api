package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionCreatedResponseDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import java.net.URI;
import java.util.function.Supplier;

import static uk.gov.companieshouse.overseasentitiesapi.model.dao.StatusType.COMPLETE;
import static uk.gov.companieshouse.overseasentitiesapi.model.dao.StatusType.IN_PROGRESS;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.COSTS_URI_SUFFIX;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.SUBMISSION_URI_PATTERN;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.VALIDATION_STATUS_URI_SUFFIX;

@Service
public class OverseasEntitiesService {

    private final OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;
    private final TransactionService transactionService;
    private final OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;
    private final Supplier<LocalDateTime> dateTimeNowSupplier;

    @Autowired
    public OverseasEntitiesService(OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository,
                                   TransactionService transactionService,
                                   OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper,
                                   Supplier<LocalDateTime> dateTimeNowSupplier) {
        this.overseasEntitySubmissionsRepository = overseasEntitySubmissionsRepository;
        this.transactionService = transactionService;
        this.overseasEntityDtoDaoMapper = overseasEntityDtoDaoMapper;
        this.dateTimeNowSupplier = dateTimeNowSupplier;
    }

    public ResponseEntity<Object> completeInProgressOverseasEntity(Transaction transaction,
                                                                  String requestId) {
        ApiLogger.debugContext(requestId, "Called completeInProgressOverseasEntity(...)");

        // Controller POST and PUT methods should have been called before getting here...
        if (!hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s does NOT have an existing Overseas Entity submission", transaction.getId()));
        }

        String overseasEntityId = getOverseasEntityIdFromTransaction(transaction);

        OverseasEntitySubmissionDao submissionDao = overseasEntitySubmissionsRepository.findById(overseasEntityId).get();

        submissionDao.setStatus(COMPLETE);

        overseasEntitySubmissionsRepository.save(submissionDao);

        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<Object> replaceInProgressOverseasEntity(Transaction transaction,
                                                                  OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                                  String passthroughTokenHeader,
                                                                  String requestId,
                                                                  String userId) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called replaceInProgressOverseasEntity(...)");

        // Controller POST method and 'createInProgressOverseasEntity()' method should have been called before getting here...
        if (!hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s does NOT have an existing Overseas Entity submission", transaction.getId()));
        }

        String overseasEntityId = getOverseasEntityIdFromTransaction(transaction);

        ApiLogger.debugContext(requestId, "Updating Overseas Entity with id " + overseasEntityId);

        return createUpdateOverseasEntityInMongo(transaction, overseasEntitySubmissionDto, passthroughTokenHeader,
                requestId, userId, overseasEntityId);
    }


    // *** THIS IS A TERRIBLE HACK!! ***
    //
    // TODO Does OE id come in within the OverseasEntitySubmissionDto or is it populated as a request attribute on the
    //      controller method in a more elegant fashion? If the former, then it will need to be added to the DTO model.
    //
    //      For now, plucking this out of one of the transaction resources in a VERY dubious manner...
    private String getOverseasEntityIdFromTransaction(Transaction transaction) {
        String overseasEntityResource = transaction.getResources().entrySet().stream().findFirst().get().getValue()
                .getLinks().get("resource");

        ApiLogger.debugContext("Overseas entity resource link from transaction = " + overseasEntityResource, null);

        int locationOfUnderscore = overseasEntityResource.lastIndexOf("/") + 1;

        return overseasEntityResource.substring(locationOfUnderscore);
    }

    public ResponseEntity<Object> createInProgressOverseasEntity(Transaction transaction,
                                                                 OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                                 String passthroughTokenHeader,
                                                                 String requestId,
                                                                 String userId) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called createInProgressOverseasEntity(...)");

        if (hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s has an existing Overseas Entity submission", transaction.getId()));
        }

        return createUpdateOverseasEntityInMongo(transaction, overseasEntitySubmissionDto, passthroughTokenHeader, requestId, userId, null);
    }

    // TODO This should really be two separate methods of course
    private ResponseEntity<Object> createUpdateOverseasEntityInMongo(Transaction transaction,
                                                                     OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                                     String passthroughTokenHeader,
                                                                     String requestId,
                                                                     String userId,
                                                                     String overseasEntityId) throws ServiceException {
        // Create or update the overseas entity submission in MongoDB
        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);

        overseasEntitySubmissionDao.setStatus(IN_PROGRESS);

        OverseasEntitySubmissionDao submissionDao;
        String submissionUri;
        if (Objects.isNull(overseasEntityId)) {
            submissionDao = overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao);

            submissionUri = String.format(SUBMISSION_URI_PATTERN, transaction.getId(), submissionDao.getId());
            submissionDao.setLinks(Collections.singletonMap("self", submissionUri));

            // Maybe a lot of this shouldn't be done every time submission is updated? Could move up into 'create' block...
            submissionDao.setCreatedOn(dateTimeNowSupplier.get());

            submissionDao.setHttpRequestId(requestId);
            submissionDao.setCreatedByUserId(userId);

            overseasEntitySubmissionsRepository.save(submissionDao);

            // create the Resource to be added to the Transaction (includes various links to the resource)
            var overseasEntityResource = createOverseasEntityTransactionResource(submissionUri);
            // add a link to our newly created Overseas Entity submission (aka resource) to the transaction
            addOverseasEntityResourceToTransaction(transaction, passthroughTokenHeader, submissionUri, overseasEntityResource);

            ApiLogger.infoContext(requestId, String.format("Overseas Entity Submission created for transaction id: %s with overseas-entity submission id: %s",  transaction.getId(), submissionDao.getId()));
        } else {
            overseasEntitySubmissionDao.setId(overseasEntityId);
            submissionDao = overseasEntitySubmissionsRepository.save(overseasEntitySubmissionDao);
            submissionUri = String.format(SUBMISSION_URI_PATTERN, transaction.getId(), submissionDao.getId());
            ApiLogger.infoContext(requestId, String.format("Overseas Entity Submission updated for transaction id: %s with overseas-entity submission id: %s",  transaction.getId(), submissionDao.getId()));
        }

        var overseasEntitySubmissionCreatedResponseDto = new OverseasEntitySubmissionCreatedResponseDto();
        overseasEntitySubmissionCreatedResponseDto.setId(submissionDao.getId());
        return ResponseEntity.created(URI.create(submissionUri)).body(overseasEntitySubmissionCreatedResponseDto);
    }

    private boolean hasExistingOverseasEntitySubmission (Transaction transaction) {
        if (transaction.getResources() != null) {
            return transaction.getResources().entrySet().stream().anyMatch(resourceEntry -> FILING_KIND_OVERSEAS_ENTITY.equals(resourceEntry.getValue().getKind()));
        }
        return false;
    }

    private Resource createOverseasEntityTransactionResource(String submissionUri) {
        var overseasEntityResource = new Resource();
        overseasEntityResource.setKind(FILING_KIND_OVERSEAS_ENTITY);

        Map<String, String> linksMap = new HashMap<>();
        linksMap.put("resource", submissionUri);
        linksMap.put("validation_status", submissionUri + VALIDATION_STATUS_URI_SUFFIX);
        linksMap.put("costs", submissionUri + COSTS_URI_SUFFIX);

        overseasEntityResource.setLinks(linksMap);
        return overseasEntityResource;
    }

    private void addOverseasEntityResourceToTransaction(Transaction transaction,
                                                        String passthroughTokenHeader,
                                                        String submissionUri,
                                                        Resource overseasEntityResource) throws ServiceException {
        transaction.setResources(Collections.singletonMap(submissionUri, overseasEntityResource));
        transactionService.updateTransaction(transaction, passthroughTokenHeader);
    }

    public Optional<OverseasEntitySubmissionDto> getOverseasEntitySubmission(String submissionId) {
        var submission = overseasEntitySubmissionsRepository.findById(submissionId);
        if (submission.isPresent()) {
            ApiLogger.info(String.format("%s: Overseas Entities Submission found. About to return", submission.get().getId()));

            var dto = overseasEntityDtoDaoMapper.daoToDto(submission.get());
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    public ValidationStatusResponse isValid(String submissionId) throws SubmissionNotFoundException {
        var submissionDtoOptional = getOverseasEntitySubmission(submissionId);
        if(submissionDtoOptional.isPresent()) {
            var validationStatus = new ValidationStatusResponse();
            validationStatus.setValid(true);
            return validationStatus;
        } else {
            throw new SubmissionNotFoundException(
                    String.format("Could not find submission data for submission %s", submissionId));
        }
    }
}
