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
import java.util.Optional;

import java.net.URI;
import java.util.function.Supplier;

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

    public ResponseEntity<Object> createOverseasEntity(Transaction transaction,
                                                       OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                       String passthroughTokenHeader,
                                                       String requestId,
                                                       String userId) throws ServiceException {
        ApiLogger.debugContext(requestId, "Called createOverseasEntity(...)");

        if (hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s has an existing Overseas Entity submission", transaction.getId()));
        }

        // add the overseas entity submission into MongoDB
        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);
        var insertedSubmission = overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao);

        String submissionUri = updateOverseasEntitySubmissionWithMetaData(insertedSubmission, transaction.getId(), requestId, userId);

        // create the Resource to be added to the Transaction (includes various links to the resource)
        var overseasEntityResource = createOverseasEntityTransactionResource(submissionUri);
        // add a link to our newly created Overseas Entity submission (aka resource) to the transaction
        addOverseasEntityResourceToTransaction(transaction, passthroughTokenHeader, submissionUri, overseasEntityResource, requestId);

        ApiLogger.infoContext(requestId, String.format("Overseas Entity Submission created for transaction id: %s with overseas-entity submission id: %s",  transaction.getId(), insertedSubmission.getId()));
        var overseasEntitySubmissionCreatedResponseDto = new OverseasEntitySubmissionCreatedResponseDto();
        overseasEntitySubmissionCreatedResponseDto.setId(insertedSubmission.getId());
        return ResponseEntity.created(URI.create(submissionUri)).body(overseasEntitySubmissionCreatedResponseDto);
    }

    public ResponseEntity<Object> updateOverseasEntity(Transaction transaction,
                                                       String submissionId,
                                                       OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                       String requestId,
                                                       String userId) {
        ApiLogger.debugContext(requestId, "Called updateOverseasEntity(...)");

        if (!hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format(
                    "Transaction id: %s does not have an existing Overseas Entity submission", transaction.getId()));
        }

        // TODO Check Mongo for existence of an Overseas Entity linked to the supplied transaction...

        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);

        overseasEntitySubmissionDao.setId(submissionId);
        overseasEntitySubmissionDao.setHttpRequestId(requestId);

        updateOverseasEntitySubmissionWithMetaData(overseasEntitySubmissionDao, transaction.getId(), requestId, userId);

        ApiLogger.infoContext(requestId, String.format(
                "Overseas Entity Submission updated for transaction id: %s and overseas-entity submission id: %s",
                transaction.getId(), submissionId));

        return ResponseEntity.ok().build();
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
                                                        Resource overseasEntityResource,
                                                        String loggingContext) throws ServiceException {
        transaction.setResources(Collections.singletonMap(submissionUri, overseasEntityResource));
        transactionService.updateTransaction(transaction, passthroughTokenHeader, loggingContext);
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

    private String updateOverseasEntitySubmissionWithMetaData(OverseasEntitySubmissionDao submission, String transactionId, String requestId, String userId) {
        var submissionUri = String.format(SUBMISSION_URI_PATTERN, transactionId, submission.getId());
        submission.setLinks(Collections.singletonMap("self", submissionUri));
        submission.setCreatedOn(dateTimeNowSupplier.get());
        submission.setHttpRequestId(requestId);
        submission.setCreatedByUserId(userId);

        overseasEntitySubmissionsRepository.save(submission);

        return submissionUri;
    }
}
