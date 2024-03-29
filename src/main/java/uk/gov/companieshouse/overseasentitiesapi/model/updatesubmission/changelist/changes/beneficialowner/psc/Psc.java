package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public abstract class Psc {

  protected Address residentialAddress;

  @JsonInclude(NON_NULL)
  @JsonProperty("serviceAddress")
  private Address serviceAddress;

  @JsonInclude(NON_EMPTY)
  @JsonProperty("natureOfControls")
  private List<String> natureOfControls;

  @JsonInclude(NON_NULL)
  @JsonProperty("isOnSanctionsList")
  private Boolean isOnSanctionsList;

  public Address getServiceAddress() {
    return serviceAddress;
  }

  public void setServiceAddress(
      Address serviceAddress) {
    this.serviceAddress = serviceAddress;
  }

  @JsonProperty("appointmentType")
  protected abstract String getAppointmentType();

  public abstract Address getResidentialAddress();

  public abstract void setResidentialAddress(Address residentialAddress);

  public List<String> getNatureOfControls() {
    return natureOfControls;
  }


  public void setNatureOfControls(
      List<String> natureOfControls) {
    this.natureOfControls = natureOfControls;
  }

  public void addNatureOfControlTypes(List<String> natureOfControls) {
    if (this.natureOfControls == null) {
      this.natureOfControls = new ArrayList<>();
    }
    this.natureOfControls.addAll(natureOfControls);
  }

  public Boolean getIsOnSanctionsList() {
    return isOnSanctionsList;
  }

  public void setOnSanctionsList(Boolean isOnSanctionsList) {
    this.isOnSanctionsList = isOnSanctionsList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var psc = (Psc) o;
    return  Objects.equals(serviceAddress, psc.serviceAddress) &&
            Objects.equals(residentialAddress, psc.residentialAddress) &&
            Objects.equals(natureOfControls, psc.natureOfControls) &&
            isOnSanctionsList == psc.isOnSanctionsList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceAddress, residentialAddress, natureOfControls, isOnSanctionsList);
  }
}

