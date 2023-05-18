package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class PersonName {
  @JsonProperty("forename")
  private String forename;
  @JsonProperty("surname")
  private String surname;

  public PersonName(String forename, String surname) {
    this.forename = forename;
    this.surname = surname;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(String forename) {
    this.forename = forename;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonName that = (PersonName) o;
    return Objects.equals(forename, that.forename) && Objects.equals(surname,
        that.surname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(forename, surname);
  }
}
