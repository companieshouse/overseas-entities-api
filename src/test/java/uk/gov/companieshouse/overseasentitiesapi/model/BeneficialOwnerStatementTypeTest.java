package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeneficialOwnerStatementTypeTest {

    @ParameterizedTest
    @ValueSource(strings = { "all_identified_all_details", "ALL_IDENTIFIED_ALL_DETAILS" } )
    void checkFindByBeneficialOwnersStatementTypeStringExpectedEnumCases(String enumInput) {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString(enumInput);
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, type);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { " ", "Batman" } )
    void checkFindByBeneficialOwnersStatementTypeStringNullCases(String enumInput) {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString(enumInput);
        assertNull(type);
    }

    @Test
    void testBeneficialOwnersStatementTypeGetValue() {
        var enumBeneficialOwnersStatementType =
                BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString(
                        "none_identified");
        assert enumBeneficialOwnersStatementType != null;
        assertEquals("none_identified", enumBeneficialOwnersStatementType.getValue());
    }

}
