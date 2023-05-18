package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public class ServiceAddress {

  @JsonProperty("Address")
  private Address address;

  @JsonProperty("foreignAddressIndicator")
  private String foreignAddressIndicator;

  @JsonProperty("isServiceAddressROA")
  private Boolean isServiceAddressROA;

  public Address getAddress() {
    return address;
  }

  public void setAddress(
      Address address) {
    this.address = address;
  }

  public String getForeignAddressIndicator() {
    return foreignAddressIndicator;
  }

  public void setForeignAddressIndicator(String foreignAddressIndicator) {
    this.foreignAddressIndicator = foreignAddressIndicator;
  }

  public Boolean getServiceAddressROA() {
    return isServiceAddressROA;
  }

  public void setServiceAddressROA(Boolean serviceAddressROA) {
    isServiceAddressROA = serviceAddressROA;
  }
}
