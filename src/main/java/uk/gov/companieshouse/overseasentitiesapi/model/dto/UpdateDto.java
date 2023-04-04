package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UpdateDto {

    public static final String DATE_OF_CREATION = "date_of_creation";
    public static final String BO_MO_DATA_FETCHED = "bo_mo_data_fetched";
    public static final String REGISTRABLE_BENEFICIAL_OWNER = "registrable_beneficial_owner";

    @JsonProperty(DATE_OF_CREATION)
    private LocalDate dateOfCreation;

    @JsonProperty(BO_MO_DATA_FETCHED)
    private boolean boMoDataFetched;

    @JsonProperty(REGISTRABLE_BENEFICIAL_OWNER)
    private boolean registrableBeneficialOwner;

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public boolean isBoMoDataFetched() {
        return boMoDataFetched;
    }

    public void setBoMoDataFetched(boolean boMoDataFetched) {
        this.boMoDataFetched = boMoDataFetched;
    }

    public boolean isRegistrableBeneficialOwner() {
        return registrableBeneficialOwner;
    }

    public void setRegistrableBeneficialOwner(boolean registrableBeneficialOwner) {
        this.registrableBeneficialOwner = registrableBeneficialOwner;
    }
}
