package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RelevantStatementsType {
    CHANGE_BO_RELEVANT_PERIOD("change_bo_relevant_period"),
    NO_CHANGE_BO_RELEVANT_PERIOD("no_change_bo_relevant_period"),
    TRUSTEE_INVOLVED_RELEVANT_PERIOD("trustee_involved_relevant_period"),
    NO_TRUSTEE_INVOLVED_RELEVANT_PERIOD("no_trustee_involved_relevant_period"),
    CHANGE_BENEFICIARY_RELEVANT_PERIOD("change_beneficiary_relevant_period"),
    NO_CHANGE_BENEFICIARY_RELEVANT_PERIOD("no_change_beneficiary_relevant_period");

    private final String relevantStatements;
    RelevantStatementsType(String relevantStatements) {
        this.relevantStatements = relevantStatements;
    }

    @JsonCreator
    public static RelevantStatementsType findByRelevantStatementTypeString (String relevantStatements) {
        for (RelevantStatementsType relevantStatement : values()) {
            if (relevantStatement.relevantStatements.equalsIgnoreCase(relevantStatements)) {
                return relevantStatement;

            }
        }
        return null;
    }

    public String getValue() {
        return this.relevantStatements;
    }
}
