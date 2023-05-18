package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class CompanyIdentification {

  @JsonProperty("legalForm")
  private String legalForm;

  @JsonProperty("governingLaw")
  private String governingLaw;

  @JsonProperty("registerLocation")
  private String registerLocation;

  @JsonProperty("registrationNumber")
  private String registrationNumber;

  @JsonProperty("country")
  private String country;

  public String getLegalForm() {
    return legalForm;
  }

  public void setLegalForm(String legalForm) {
    this.legalForm = legalForm;
  }

  public String getGoverningLaw() {
    return governingLaw;
  }

  public void setGoverningLaw(String governingLaw) {
    this.governingLaw = governingLaw;
  }

  public String getRegisterLocation() {
    return registerLocation;
  }

  public void setRegisterLocation(String registerLocation) {
    this.registerLocation = registerLocation;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanyIdentification that = (CompanyIdentification) o;
    return Objects.equals(legalForm, that.legalForm) && Objects.equals(
        governingLaw, that.governingLaw) && Objects.equals(registerLocation,
        that.registerLocation) && Objects.equals(registrationNumber,
        that.registrationNumber) && Objects.equals(country, that.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(legalForm, governingLaw, registerLocation, registrationNumber, country);
  }

  @Override
  public String toString() {
    return "CompanyIdentification{" +
        "legalForm='" + legalForm + '\'' +
        ", governingLaw='" + governingLaw + '\'' +
        ", registerLocation='" + registerLocation + '\'' +
        ", registrationNumber='" + registrationNumber + '\'' +
        ", country='" + country + '\'' +
        '}';
  }
}

