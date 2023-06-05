package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class OtherBeneficialOwnerPsc extends Psc {

  @JsonProperty("corporateSoleName")
  private String corporateSoleName;

  @JsonProperty("companyIdentification")
  private CompanyIdentification companyIdentification;

  private static final String APPOINTMENT_TYPE = "OE GPA BO";

  public String getCorporateSoleName() {
    return corporateSoleName;
  }

  public void setCorporateSoleName(String corporateSoleName) {
    this.corporateSoleName = corporateSoleName;
  }

  public CompanyIdentification getCompanyIdentification() {
    return companyIdentification;
  }

  public void setCompanyIdentification(
      CompanyIdentification companyIdentification) {
    this.companyIdentification = companyIdentification;
  }
  @Override
  @JsonProperty("registeredOffice")
  public Address getResidentialAddress() {
    return super.residentialAddress;
  }

  @Override
  @JsonProperty("registeredOffice")
  public void setResidentialAddress(Address residentialAddress) {
    super.residentialAddress = residentialAddress;
  }

  @Override
  public String getAppointmentType() {
    return APPOINTMENT_TYPE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    OtherBeneficialOwnerPsc that = (OtherBeneficialOwnerPsc) o;
    return Objects.equals(corporateSoleName, that.corporateSoleName)
        && Objects.equals(companyIdentification, that.companyIdentification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateSoleName, companyIdentification, APPOINTMENT_TYPE,
        super.hashCode());
  }
}
