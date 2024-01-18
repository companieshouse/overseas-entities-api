package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

public class RemoveDao {
    @Field("is_not_proprietor_of_land")
    private Boolean isNotProprietorOfLand;

    public Boolean getIsNotProprietorOfLand() {
        return isNotProprietorOfLand;
    }

    public void setIsNotProprietorOfLand(Boolean isNotProprietorOfLand) {
        this.isNotProprietorOfLand = isNotProprietorOfLand;
    }
}
