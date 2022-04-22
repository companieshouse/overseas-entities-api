package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

public class BeneficialOwnersStatementDto {

    private BeneficialOwnersStatementType beneficialOwnersStatementType;

    public BeneficialOwnersStatementType getBeneficialOwnersStatementType() {
        return beneficialOwnersStatementType;
    }

    public void setBeneficialOwnersStatementType(BeneficialOwnersStatementType beneficialOwnersStatementType) {
        this.beneficialOwnersStatementType = beneficialOwnersStatementType;
    }
}
