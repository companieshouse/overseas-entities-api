package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

import java.util.Map;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_RESOURCE;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_SELF;

@Component
public class TransactionUtils {

    public boolean isTransactionLinkedToOverseasEntitySubmission(Transaction transaction, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        Map<String, String> links = overseasEntitySubmissionDto.getLinks();
        if (Objects.isNull(links)) {
            return false;
        }

        String overseasEntitySubmissionSelfLink = links.get(LINK_SELF);
        if (StringUtils.isBlank(overseasEntitySubmissionSelfLink)) {
            return false;
        }

        if (Objects.isNull(transaction.getResources())) {
            return false;
        }

        return transaction.getResources().entrySet().stream()
                .filter(resource -> FILING_KIND_OVERSEAS_ENTITY.equals(resource.getValue().getKind()))
                .anyMatch(resource -> overseasEntitySubmissionSelfLink.equals(resource.getValue().getLinks().get(LINK_RESOURCE)));
    }
}
