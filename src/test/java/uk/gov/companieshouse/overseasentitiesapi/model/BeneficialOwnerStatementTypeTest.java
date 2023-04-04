package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeneficialOwnerStatementTypeTest {

    @Test
    void checkFindByBeneficialOwnersStatementTypeStringLowerCase() {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString("all_identified_all_details");
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, type);
    }

    @Test
    void checkFindByBeneficialOwnersStatementTypeStringUpperCase() {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString("ALL_IDENTIFIED_ALL_DETAILS");
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, type);
    }

    @Test
    void checkFindByBeneficialOwnersStatementTypeStringNull() {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString(null);
        assertNull(type);
    }

    @Test
    void checkFindByBeneficialOwnersStatementTypeStringEmpty() {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString("");
        assertNull(type);
    }

    @Test
    void checkFindByBeneficialOwnersStatementTypeStringNonMatching() {
        BeneficialOwnersStatementType type = BeneficialOwnersStatementType.findByBeneficialOwnersStatementTypeString("Batman");
        assertNull(type);
    }
}
