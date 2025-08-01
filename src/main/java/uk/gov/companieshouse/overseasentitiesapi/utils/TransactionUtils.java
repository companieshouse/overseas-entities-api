package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.transaction.Transaction;

import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_RESOURCE;

@Component
public class TransactionUtils {

    public boolean isTransactionLinkedToOverseasEntitySubmission(Transaction transaction, String overseasEntitySubmissionSelfLink) {
        if (StringUtils.isBlank(overseasEntitySubmissionSelfLink)) {
            return false;
        }

        if (Objects.isNull(transaction) || Objects.isNull(transaction.getResources())) {
            return false;
        }

        return transaction.getResources().entrySet().stream()
                .filter(resource -> FILING_KIND_OVERSEAS_ENTITY.equals(resource.getValue().getKind()))
                .anyMatch(resource -> overseasEntitySubmissionSelfLink.equals(resource.getValue().getLinks().get(LINK_RESOURCE)));
    }
}
