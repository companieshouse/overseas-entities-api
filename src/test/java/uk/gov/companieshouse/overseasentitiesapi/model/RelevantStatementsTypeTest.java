package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class RelevantStatementsTypeTest {

    @ParameterizedTest
    @ValueSource(strings = { "change_bo_relevant_period", "CHANGE_BO_RELEVANT_PERIOD" })
    void checkFindByRelevantStatementsTypeStringExpectedEnumCases(String enumInput) {
        RelevantStatementsType type = RelevantStatementsType.findByRelevantStatementTypeString(enumInput);
        assertEquals(RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD, type);
    }
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { " ", "Minion" } )
    void checkFindByRelevantStatementsTypeStringNullCases(String enumInput) {
        RelevantStatementsType type = RelevantStatementsType.findByRelevantStatementTypeString(enumInput);
        assertNull(type);
    }

    @Test
    void testRelevantStatementsTypeGetValue() {
        var enumRelevantStatementsType =
                RelevantStatementsType.findByRelevantStatementTypeString(
                        "trustee_involved_relevant_period");
        assert enumRelevantStatementsType != null;
        assertEquals("trustee_involved_relevant_period", enumRelevantStatementsType.getValue());
    }

}
