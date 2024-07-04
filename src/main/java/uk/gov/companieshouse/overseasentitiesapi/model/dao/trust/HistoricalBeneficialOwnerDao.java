package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class HistoricalBeneficialOwnerDao {
    @Field("id")
    private String id;

    @Field("forename")
    private String forename;

    @Field("other_forenames")
    private String otherForenames;

    @Field("surname")
    private String surname;

    @Field("ceased_date")
    private LocalDate ceasedDate;

    @Field("notified_date")
    private LocalDate notifiedDate;

    @Field("corporate_indicator")
    private boolean corporateIndicator;

    @Field("corporate_name")
    private String corporateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
