package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonName {
    @JsonProperty("forename")
    private String forename;
    @JsonProperty("surname")
    private String surname;

    public PersonName(String forename, String surname){
        this.forename = forename;
        this.surname = surname;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String setSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
