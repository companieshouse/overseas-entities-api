package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RelevantStatementsType {
    CEASED_TO_BE_REGISTRABLE_BENEFICIAL_OWNER("ceased_to_be_registrable_beneficial_owner"),
    NO_CEASED_TO_BE_REGISTRABLE_BENEFICIAL_OWNER("no_ceased_to_be_registrable_beneficial_owner"),
    TRUST_INVOLVED_IN_THE_OE("trust_involved_in_the_oe"),
    NO_TRUST_INVOLVED_IN_THE_OE("no_trust_involved_in_the_oe"),
    BECOME_OR_CEASED_BENEFICIARY_OF_A_TRUST("become_or_ceased_beneficiary_of_a_trust"),
    NO_BECOME_OR_CEASED_BENEFICIARY_OF_A_TRUST("no_become_or_ceased_beneficiary_of_a_trust");

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

    /**
     *   getValue() returns the selected value (for e.g. in Update Submission) from this enum list
     * @return selected value of beneficialOwnersStatement
     */
    public String getValue() {
        return this.relevantStatements;
    }
}
