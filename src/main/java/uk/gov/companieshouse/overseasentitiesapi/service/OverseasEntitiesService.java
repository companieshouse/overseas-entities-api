package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionCreatedResponseDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.Optional;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.SUBMISSION_URI_PATTERN;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.VALIDATION_STATUS_URI_SUFFIX;

@Service
public class OverseasEntitiesService {

    private final OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;
    private final TransactionService transactionService;

    private final OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @Autowired
    public OverseasEntitiesService(OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository,
                                   TransactionService transactionService,
                                   OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper) {
        this.overseasEntitySubmissionsRepository = overseasEntitySubmissionsRepository;
        this.transactionService = transactionService;
        this.overseasEntityDtoDaoMapper = overseasEntityDtoDaoMapper;
    }

    public ResponseEntity<Object> createOverseasEntity(Transaction transaction,
                                                       OverseasEntitySubmissionDto overseasEntitySubmissionDto,
                                                       String passthroughTokenHeader) throws ServiceException {
        ApiLogger.debug("Called createOverseasEntity(...)");

        if (hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s has an existing Overseas Entity submission", transaction.getId()));
        }

        // add the overseas entity submission into MongoDB
        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);
        var insertedSubmission = overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao);
        var submissionUri = String.format(SUBMISSION_URI_PATTERN, transaction.getId(), insertedSubmission.getId());
        insertedSubmission.setLinks(Collections.singletonMap("self", submissionUri));
        overseasEntitySubmissionsRepository.save(insertedSubmission);

        // add a link to our newly created Overseas Entity submission (aka resource) to the transaction
        var overseasEntityResource = createOverseasEntityTransactionResource(submissionUri);
        addOverseasEntityResourceToTransaction(transaction, passthroughTokenHeader, submissionUri, overseasEntityResource);

        ApiLogger.info(String.format("Overseas Entity Submission created for transaction id: %s with overseas-entity id: %s",  transaction.getId(), insertedSubmission.getId()));
        var overseasEntitySubmissionCreatedResponseDto = new OverseasEntitySubmissionCreatedResponseDto();
        overseasEntitySubmissionCreatedResponseDto.setId(insertedSubmission.getId());
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
            ApiLogger.info(String.format("%s: Confirmation Statement Submission found. About to return", submission.get().getId()));

            var json = overseasEntityDtoDaoMapper.daoToDto(submission.get());
            return Optional.of(json);
        } else {
            return Optional.empty();
        }
    }
}
