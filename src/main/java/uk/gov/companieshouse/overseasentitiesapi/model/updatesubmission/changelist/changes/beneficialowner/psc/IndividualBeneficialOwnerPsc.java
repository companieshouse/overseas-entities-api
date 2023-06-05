package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class IndividualBeneficialOwnerPsc extends Psc {
  @JsonProperty("personName")
  private PersonName personName;

  @JsonProperty("nationalityOther")
  private String nationalityOther;

  @JsonProperty("birthDate")
  private String birthDate;

  private static final String APPOINTMENT_TYPE = "OE INDIVIDUAL BO";


  public PersonName getPersonName() {
    return personName;
  }

  public void setPersonName(
      PersonName personName) {
    this.personName = personName;
  }

  public String getNationalityOther() {
    return nationalityOther;
  }

  public void setNationalityOther(String nationalityOther) {
    this.nationalityOther = nationalityOther;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  @Override
  public String getAppointmentType() {
    return APPOINTMENT_TYPE;
  }

  @Override
  @JsonProperty("residentialAddress")
  public Address getResidentialAddress() {
    return super.residentialAddress;
  }

  @Override
  @JsonProperty("residentialAddress")
  public void setResidentialAddress(Address residentialAddress) {
    super.residentialAddress = residentialAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if(!super.equals(o)) {
      return false;
    }
    IndividualBeneficialOwnerPsc that = (IndividualBeneficialOwnerPsc) o;
    return Objects.equals(personName, that.personName) && Objects.equals(nationalityOther,
        that.nationalityOther) && Objects.equals(birthDate, that.birthDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personName, nationalityOther, birthDate, APPOINTMENT_TYPE, super.hashCode());
  }
}
