package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class HistoricalBeneficialOwnerDto {
    @JsonProperty("forename")
    private String forename;

    @JsonProperty("other_forenames")
    private String otherForenames;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("ceased_date")
    private LocalDate ceasedDate;

    @JsonProperty("notified_date")
    private LocalDate notifiedDate;

    @JsonProperty("corporate_indicator")
    private boolean corporateIndicator;

    @JsonProperty("corporate_name")
    private String corporateName;

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getOtherForenames() {
        return otherForenames;
    }

    public void setOtherForenames(String otherForenames) {
        this.otherForenames = otherForenames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

    public LocalDate getNotifiedDate() {
        return notifiedDate;
    }

    public void setNotifiedDate(LocalDate notifiedDate) {
        this.notifiedDate = notifiedDate;
    }

    public boolean isCorporateIndicator() {
        return corporateIndicator;
    }

    public void setCorporateIndicator(boolean corporateIndicator) {
        this.corporateIndicator = corporateIndicator;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
    
}
