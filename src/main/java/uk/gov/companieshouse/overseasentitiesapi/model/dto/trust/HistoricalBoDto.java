package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricalBoDto {
    @JsonProperty("forename")
    private String forename;

    @JsonProperty("other_forenames")
    private String otherForenames;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("ceased_date_day")
    private String ceasedDateDay;

    @JsonProperty("ceased_date_month")
    private String ceasedDateMonth;

    @JsonProperty("ceased_date_year")
    private String ceasedDateYear;

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

    public String getCeasedDateDay() {
        return ceasedDateDay;
    }

    public void setCeasedDateDay(String ceasedDateDay) {
        this.ceasedDateDay = ceasedDateDay;
    }

    public String getCeasedDateMonth() {
        return ceasedDateMonth;
    }

    public void setCeasedDateMonth(String ceasedDateMonth) {
        this.ceasedDateMonth = ceasedDateMonth;
    }

    public String getCeasedDateYear() {
        return ceasedDateYear;
    }

    public void setCeasedDateYear(String ceasedDateYear) {
        this.ceasedDateYear = ceasedDateYear;
    }

    public String getCeasedDate() {
        String[] dateFields = {ceasedDateYear, ceasedDateMonth, ceasedDateDay};
        return String.join("-", dateFields);
    }
}
