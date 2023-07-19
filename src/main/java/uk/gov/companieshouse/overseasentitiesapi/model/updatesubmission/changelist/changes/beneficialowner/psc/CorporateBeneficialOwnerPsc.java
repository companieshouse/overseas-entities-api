package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class CorporateBeneficialOwnerPsc extends Psc {

  private static final String APPOINTMENT_TYPE = "OE OLE BO";

  @JsonInclude(NON_NULL)
  @JsonProperty("corporateName")
  private String corporateName;

  @JsonProperty("companyIdentification")
  private CompanyIdentification companyIdentification;

  @JsonInclude(NON_NULL)
  @JsonProperty("addedTrustIds")
  private List<String> addedTrustIds;

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

  public List<String> getAddedTrustIds() {
    return addedTrustIds;
  }

  public void setAddedTrustIds(List<String> addedTrustIds) {
    this.addedTrustIds = addedTrustIds;
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
    CorporateBeneficialOwnerPsc that = (CorporateBeneficialOwnerPsc) o;
    return Objects.equals(corporateName, that.corporateName) && Objects.equals(
        companyIdentification, that.companyIdentification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(corporateName, companyIdentification, APPOINTMENT_TYPE, super.hashCode());
  }
}
