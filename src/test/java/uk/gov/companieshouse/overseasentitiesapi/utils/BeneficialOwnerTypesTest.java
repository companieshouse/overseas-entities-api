package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static uk.gov.companieshouse.overseasentitiesapi.utils.BeneficialOwnerTypes.beneficialOwnerTypes;

class BeneficialOwnerTypesTest {

    @Test
    void testBeneficialOwnerTypes() {
        assertEquals(
            BeneficialOwnerType.CORPORATE_INTERESTED_PERSON,
            beneficialOwnerTypes.get("Corporate Interested Person")
        );
    }

}
