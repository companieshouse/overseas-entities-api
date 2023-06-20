package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class OtherBeneficialOwnerPsc extends Psc {

  @JsonInclude(NON_NULL)
  @JsonProperty("corporateName")
  private String corporateName;

  @JsonProperty("companyIdentification")
  private CompanyIdentification companyIdentification;

  private static final String APPOINTMENT_TYPE = "OE GPA BO";

  public String getCorporateName() {
    return corporateName;
  }

  public void setCorporateName(String corporateName) {
    this.corporateName = corporateName;
  }

  public CompanyIdentification getCompanyIdentification() {
    return companyIdentification;
  }

  public void setCompanyIdentification(
      CompanyIdentification companyIdentification) {
    this.companyIdentification = companyIdentification;
  }
  @Override
  @JsonInclude(NON_NULL)
  @JsonProperty("registeredOffice")
  public Address getResidentialAddress() {
    return super.residentialAddress;
  }

  @Override
  @JsonInclude(NON_NULL)
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
    return Objects.equals(corporateName, that.corporateName)
        && Objects.equals(companyIdentification, that.companyIdentification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateName, companyIdentification, APPOINTMENT_TYPE,
        super.hashCode());
  }
}
