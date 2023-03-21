package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionCreatedResponseDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.TransactionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import java.net.URI;
import java.util.function.Supplier;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.COSTS_URI_SUFFIX;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.CURRENT_VERSION;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_SELF;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.RESUME_JOURNEY_URI_PATTERN;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.SUBMISSION_URI_PATTERN;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.VALIDATION_STATUS_URI_SUFFIX;

@Service
public class OverseasEntitiesService {

    private final OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;
    private final TransactionService transactionService;
    private final OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;
    private final TransactionUtils transactionUtils;
    private final Supplier<LocalDateTime> dateTimeNowSupplier;

    @Autowired
    public OverseasEntitiesService(OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository,
                                   TransactionService transactionService,
                                   OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper,
                                   Supplier<LocalDateTime> dateTimeNowSupplier,
                                   TransactionUtils transactionUtils) {
        this.overseasEntitySubmissionsRepository = overseasEntitySubmissionsRepository;
        this.transactionService = transactionService;
        this.overseasEntityDtoDaoMapper = overseasEntityDtoDaoMapper;
        this.transactionUtils = transactionUtils;
        this.dateTimeNowSupplier = dateTimeNowSupplier;
    }

    public boolean isSubmissionAnUpdate(String requestId, String overseasEntityId) throws SubmissionNotFoundException {
        return getSubmissionType(requestId, overseasEntityId) == SubmissionType.UPDATE;
    }

    SubmissionType getSubmissionType(String requestId, String overseasEntityId) throws SubmissionNotFoundException {
        Optional<OverseasEntitySubmissionDto> submissionOpt = getOverseasEntitySubmission(
                overseasEntityId);
        if (submissionOpt.isEmpty()) {
            throw new SubmissionNotFoundException("Can not determine submission type");
        }

        String entityNumber = submissionOpt.get().getEntityNumber();
        if (submissionOpt.get().isForUpdate()) {
            ApiLogger.infoContext(requestId, String.format("Submission with overseas entity number %s found",
                    entityNumber));
            return SubmissionType.UPDATE;
        } else {
            ApiLogger.infoContext(requestId, "Submission without overseas entity number found");
            return SubmissionType.REGISTRATION;
        }
    }

    public ResponseEntity<Object> createOverseasEntityWithResumeLink(Transaction transaction,
                                                                     OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                                     String requestId,
                                                                     String userId) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called createOverseasEntityWithResumeLink(...)");

        return createOverseasEntity(transaction, overseasEntitySubmissionDto, requestId, userId, true);
    }

    public ResponseEntity<Object> createOverseasEntity(Transaction transaction,
                                                       OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                       String requestId,
                                                       String userId) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called createOverseasEntity(...)");

        return createOverseasEntity(transaction, overseasEntitySubmissionDto, requestId, userId, false);
    }

    private ResponseEntity<Object> createOverseasEntity(Transaction transaction,
                                                        OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                        String requestId,
                                                        String userId,
                                                        boolean addResumeLinkToTransaction) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called createOverseasEntity(...), with 'add resume link' = " + addResumeLinkToTransaction);

        if (hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s has an existing Overseas Entity submission", transaction.getId()));
        }

        // add the overseas entity submission into MongoDB
        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);
        overseasEntitySubmissionDao.setSchemaVersion(CURRENT_VERSION);
        var insertedSubmission = overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao);

        final String submissionId = insertedSubmission.getId();
        final String submissionUri = getSubmissionUri(transaction.getId(), submissionId);
        updateOverseasEntitySubmissionWithMetaData(insertedSubmission, submissionUri, requestId, userId);

        // create the Resource to be added to the Transaction (includes various links to the resource)
        var overseasEntityResource = createOverseasEntityTransactionResource(submissionUri);

        // Update company name set on the transaction and add a link to our newly created Overseas Entity
        // submission (aka resource) to the transaction (and potentially also a link for the 'resume' journey)
        updateTransactionWithLinksAndCompanyName(transaction, overseasEntitySubmissionDto.getEntityName(), submissionId,
                submissionUri, overseasEntityResource, requestId, addResumeLinkToTransaction);

        ApiLogger.infoContext(requestId, String.format("Overseas Entity Submission created for transaction id: %s with overseas-entity submission id: %s, schema version %s", transaction.getId(), insertedSubmission.getId(), CURRENT_VERSION));
        var overseasEntitySubmissionCreatedResponseDto = new OverseasEntitySubmissionCreatedResponseDto();
        overseasEntitySubmissionCreatedResponseDto.setId(insertedSubmission.getId());
        return ResponseEntity.created(URI.create(submissionUri)).body(overseasEntitySubmissionCreatedResponseDto);
    }

    public ResponseEntity<Object> updateOverseasEntity(Transaction transaction,
                                                       String submissionId,
                                                       OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                       String requestId,
                                                       String userId) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called updateOverseasEntity(...)");

        final String submissionUri = getSubmissionUri(transaction.getId(), submissionId);

        if (!transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, submissionUri)) {
            return ResponseEntity.badRequest().body(String.format(
                    "Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s", transaction.getId(), submissionId));
        }

        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);

        overseasEntitySubmissionDao.setId(submissionId);
        overseasEntitySubmissionDao.setSchemaVersion(CURRENT_VERSION);
        updateOverseasEntitySubmissionWithMetaData(overseasEntitySubmissionDao, submissionUri, requestId, userId);

        // Update company name set on the transaction, to ensure it matches the value received with this OE submission
        String entityName = null;
        if(Objects.nonNull(overseasEntitySubmissionDto.getEntityName())) {
            entityName = overseasEntitySubmissionDto.getEntityName().getName();
        }

        transaction.setCompanyName(entityName);
        transactionService.updateTransaction(transaction, requestId);

        ApiLogger.infoContext(requestId, String.format(
                "Overseas Entity Submission updated for transaction id: %s and overseas-entity submission id: %s, schema version %s",
                transaction.getId(), submissionId, CURRENT_VERSION));

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getSavedOverseasEntity(Transaction transaction,
                                                         String submissionId,
                                                         String requestId) throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {
        ApiLogger.debugContext(requestId, "Called getSavedOverseasEntity(...)");

        final String submissionUri = getSubmissionUri(transaction.getId(), submissionId);

        if (!transactionUtils.isTransactionLinkedToOverseasEntitySubmission(transaction, submissionUri)) {
            throw new SubmissionNotLinkedToTransactionException(String.format(
                    "Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s", transaction.getId(), submissionId));
        }

        Optional<OverseasEntitySubmissionDto> submissionOpt = getOverseasEntitySubmission(submissionId);
        OverseasEntitySubmissionDto submissionDto = submissionOpt
                .orElseThrow(() ->
                        new SubmissionNotFoundException(
                                String.format("Empty submission returned when generating filing for %s", submissionId)));

        return ResponseEntity.ok().body(submissionDto);
    }

    private boolean hasExistingOverseasEntitySubmission(Transaction transaction) {
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

    private void updateTransactionWithLinksAndCompanyName(Transaction transaction,
                                                          EntityNameDto entityNameDto,
                                                          String submissionId,
                                                          String submissionUri,
                                                          Resource overseasEntityResource,
                                                          String loggingContext,
                                                          boolean addResumeLinkToTransaction) throws ServiceException {
        String entityName = null;
        if (Objects.nonNull(entityNameDto)) {
            entityName = entityNameDto.getName();
        }
        transaction.setCompanyName(entityName);

        transaction.setResources(Collections.singletonMap(submissionUri, overseasEntityResource));

        if (addResumeLinkToTransaction) {
            final var resumeJourneyUri = String.format(RESUME_JOURNEY_URI_PATTERN, transaction.getId(), submissionId);
            transaction.setResumeJourneyUri(resumeJourneyUri);
        }

        transactionService.updateTransaction(transaction, loggingContext);
    }

    public Optional<OverseasEntitySubmissionDto> getOverseasEntitySubmission(String submissionId) {
        var submission = overseasEntitySubmissionsRepository.findById(submissionId);
        if (submission.isPresent()) {
            OverseasEntitySubmissionDao overseasEntitySubmissionDao = submission.get();
            ApiLogger.info(String.format("%s: Overseas Entities Submission found. About to return", overseasEntitySubmissionDao.getId()));
            if (overseasEntitySubmissionDao.getSchemaVersion() != null) {
                ApiLogger.info(String.format("Schema version %s", overseasEntitySubmissionDao.getSchemaVersion()));
            }

            var dto = overseasEntityDtoDaoMapper.daoToDto(overseasEntitySubmissionDao);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    private String getSubmissionUri(String transactionId, String submissionId) {
        return String.format(SUBMISSION_URI_PATTERN, transactionId, submissionId);
    }

    private void updateOverseasEntitySubmissionWithMetaData(OverseasEntitySubmissionDao submission,
                                                              String submissionUri,
                                                              String requestId,
                                                              String userId) {
        submission.setLinks(Collections.singletonMap(LINK_SELF, submissionUri));
        submission.setCreatedOn(dateTimeNowSupplier.get());
        submission.setHttpRequestId(requestId);
        submission.setCreatedByUserId(userId);

        overseasEntitySubmissionsRepository.save(submission);
    }
}
