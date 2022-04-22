package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

public class BeneficialOwnersStatementDao {

    @Field("beneficial_owners_statement")
    private BeneficialOwnersStatementType beneficialOwnersStatementType;

    public BeneficialOwnersStatementType getBeneficialOwnersStatementType() {
        return beneficialOwnersStatementType;
    }

    public void setBeneficialOwnersStatementType(BeneficialOwnersStatementType beneficialOwnersStatementType) {
        this.beneficialOwnersStatementType = beneficialOwnersStatementType;
    }
}
