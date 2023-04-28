package uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilingForDate {
    @JsonProperty("year")
    private String year;

    @JsonProperty("month")
    private String month;

    @JsonProperty("day")
    private String day;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
