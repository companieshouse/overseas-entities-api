package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public abstract class Psc {

  protected Address residentialAddress;
  @JsonProperty("actionDate")
  private String actionDate;
  @JsonProperty("serviceAddress")
  private Address serviceAddress;
  @JsonProperty("natureOfControls")
  private List<String> natureOfControls;

  @JsonProperty("isOnSanctionsList")
  private boolean isOnSanctionsList;

  public String getActionDate() {
    return actionDate;
  }

  public void setActionDate(String actionDate) {
    this.actionDate = actionDate;
  }

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

  public boolean isOnSanctionsList() {
    return isOnSanctionsList;
  }

  public void setOnSanctionsList(boolean isOnSanctionsList) {
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
    return Objects.equals(actionDate, psc.actionDate) &&
            Objects.equals(serviceAddress, psc.serviceAddress) &&
            Objects.equals(residentialAddress, psc.residentialAddress) &&
            Objects.equals(natureOfControls, psc.natureOfControls) &&
            isOnSanctionsList == psc.isOnSanctionsList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionDate, serviceAddress, residentialAddress, natureOfControls);
  }
}

