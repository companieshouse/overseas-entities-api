package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

public class BeneficialOwnersStatementDao {

    private BeneficialOwnersStatementType beneficialOwnersStatementType;

    public BeneficialOwnersStatementType getBeneficialOwnersStatementType() {
        return beneficialOwnersStatementType;
    }

    public void setBeneficialOwnersStatementType(BeneficialOwnersStatementType beneficialOwnersStatementType) {
        this.beneficialOwnersStatementType = beneficialOwnersStatementType;
    }
}
