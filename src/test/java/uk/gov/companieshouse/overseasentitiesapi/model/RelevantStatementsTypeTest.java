package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class RelevantStatementsTypeTest {

    @ParameterizedTest
    @ValueSource(strings = { "ceased_to_be_registrable_beneficial_owner", "CEASED_TO_BE_REGISTRABLE_BENEFICIAL_OWNER" })
    void checkFindByRelevantStatementsTypeStringExpectedEnumCases(String enumInput) {
        RelevantStatementsType type = RelevantStatementsType.findByRelevantStatementTypeString(enumInput);
        assertEquals(RelevantStatementsType.CEASED_TO_BE_REGISTRABLE_BENEFICIAL_OWNER, type);
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
                RelevantStatementsType.findByRelevantStatementTypeString("trust_involved_in_the_oe");
        assert enumRelevantStatementsType != null;
        assertEquals("trust_involved_in_the_oe", enumRelevantStatementsType.getValue());
    }

}
