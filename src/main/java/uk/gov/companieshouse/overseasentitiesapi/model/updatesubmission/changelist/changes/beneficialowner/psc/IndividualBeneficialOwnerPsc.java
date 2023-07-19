package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class IndividualBeneficialOwnerPsc extends Psc {

  @JsonInclude(NON_NULL)
  @JsonProperty("personName")
  private PersonName personName;

  @JsonInclude(NON_NULL)
  @JsonProperty("nationalityOther")
  private String nationalityOther;

  @JsonProperty("addedTrustIds")
  private List<String> trustIds;

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

  public List<String> getTrustIds() {
    return this.trustIds;
  }

  public void setTrustIds(List<String> trustIds) {
    this.trustIds = trustIds;
  }

  @Override
  public String getAppointmentType() {
    return APPOINTMENT_TYPE;
  }

  @Override
  @JsonInclude(NON_NULL)
  @JsonProperty("residentialAddress")
  public Address getResidentialAddress() {
    return super.residentialAddress;
  }

  @Override
  @JsonInclude(NON_NULL)
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
    return Objects.equals(personName, that.personName) && Objects.equals(nationalityOther, that.nationalityOther);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personName, nationalityOther, APPOINTMENT_TYPE, super.hashCode());
  }
}
