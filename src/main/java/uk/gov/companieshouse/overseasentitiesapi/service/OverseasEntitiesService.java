package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.OverseasEntitySubmission;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;

@Service
public class OverseasEntitiesService {

    private final OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;
    private final TransactionService transactionService;

    @Autowired
    public OverseasEntitiesService(OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository,
                                   TransactionService transactionService) {
        this.overseasEntitySubmissionsRepository = overseasEntitySubmissionsRepository;
        this.transactionService = transactionService;
    }

    public ResponseEntity<String> createOverseasEntity(Transaction transaction,
                                                       OverseasEntitySubmission overseasEntitySubmission,
                                                       String passthroughHeader) throws ServiceException {
        ApiLogger.debug("Called createOverseasEntity(...)");

        if (hasExistingOverseasEntitySubmission(transaction)) {
            return ResponseEntity.badRequest().body(String.format("Transaction id: %s has an existing Overseas Entity submission", transaction.getId()));
        }

        // add the overseas entity submission into MongoDB
        var insertedSubmission = overseasEntitySubmissionsRepository.insert(overseasEntitySubmission);
        var submissionUri = String.format("/transactions/%s/overseas-entity/%s", transaction.getId(), insertedSubmission.getId());
        insertedSubmission.setLinks(Collections.singletonMap("self", submissionUri));
        overseasEntitySubmissionsRepository.save(insertedSubmission);

        // add a link to our newly created Overseas Entity submission (aka resource) to the transaction
        var overseasEntityResource = createOverseasEntityTransactionResource(submissionUri);
        addOverseasEntityResourceToTransaction(transaction, passthroughHeader, submissionUri, overseasEntityResource);

        ApiLogger.info(String.format("Overseas Entity Submission created for transaction id: %s with overseas-entity id: %s",  transaction.getId(), insertedSubmission.getId()));
        return ResponseEntity.created(URI.create(submissionUri)).body("insertedSubmission.getId()");
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
        linksMap.put("validation_status", submissionUri + "/validation-status");
        // TODO - to enable payment uncomment line below and implement /costs endpoint
        // linksMap.put("costs", submissionUri + "/costs");

        overseasEntityResource.setLinks(linksMap);
        return overseasEntityResource;
    }

    private void addOverseasEntityResourceToTransaction(Transaction transaction,
                                                        String passthroughHeader,
                                                        String submissionUri,
                                                        Resource overseasEntityResource) throws ServiceException {
        transaction.setResources(Collections.singletonMap(submissionUri, overseasEntityResource));
        transactionService.updateTransaction(transaction, passthroughHeader);
    }
}
