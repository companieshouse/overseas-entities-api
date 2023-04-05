package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class HistoricalBeneficialOwnerDto {

    public static final String FORENAME_FIELD = "forename";
    public static final String SURNAME_FIELD = "surname";
    public static final String CEASED_DATE_FIELD = "ceased_date";
    public static final String NOTIFIED_DATE_FIELD = "notified_date";
    public static final String CORPORATE_NAME_FIELD = "corporate_name";

    @JsonProperty(FORENAME_FIELD)
    private String forename;

    @JsonProperty("other_forenames")
    private String otherForenames;

    @JsonProperty(SURNAME_FIELD)
    private String surname;

    @JsonProperty(CEASED_DATE_FIELD)
    private LocalDate ceasedDate;

    @JsonProperty(NOTIFIED_DATE_FIELD)
    private LocalDate notifiedDate;

    @JsonProperty("corporate_indicator")
    private boolean corporateIndicator;

    @JsonProperty(CORPORATE_NAME_FIELD)
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
