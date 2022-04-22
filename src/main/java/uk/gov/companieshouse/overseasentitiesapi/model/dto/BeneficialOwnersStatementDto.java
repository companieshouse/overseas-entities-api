package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

public class BeneficialOwnersStatementDto {

    @JsonProperty("beneficial_owners_statement")
    private BeneficialOwnersStatementType beneficialOwnersStatementType;

    public BeneficialOwnersStatementType getBeneficialOwnersStatementType() {
        return beneficialOwnersStatementType;
    }

    public void setBeneficialOwnersStatementType(BeneficialOwnersStatementType beneficialOwnersStatementType) {
        this.beneficialOwnersStatementType = beneficialOwnersStatementType;
    }
}
