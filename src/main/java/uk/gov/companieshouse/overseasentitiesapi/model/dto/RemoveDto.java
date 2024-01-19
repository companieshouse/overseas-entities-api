package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveDto {

    public static final String IS_NOT_PROPRIETOR_OF_LAND_FIELD = "is_not_proprietor_of_land";

    @JsonProperty(IS_NOT_PROPRIETOR_OF_LAND_FIELD)
    private Boolean isNotProprietorOfLand;

    public Boolean getIsNotProprietorOfLand() {
        return isNotProprietorOfLand;
    }

    public void setIsNotProprietorOfLand(Boolean isNotProprietorOfLand) {
        this.isNotProprietorOfLand = isNotProprietorOfLand;
    }
}
