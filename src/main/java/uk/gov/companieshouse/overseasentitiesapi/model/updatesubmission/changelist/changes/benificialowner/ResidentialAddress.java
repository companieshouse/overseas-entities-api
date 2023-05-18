package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public class ResidentialAddress {
  @JsonProperty("Address")
  private Address address;
  @JsonInclude(Include.NON_NULL)
  @JsonProperty("foreignAddressIndicator")
  private String foreignAddressIndicator;
  @JsonInclude(Include.NON_NULL)
  @JsonProperty("isServiceAddressSA")
  private Boolean isServiceAddressSA;
  @JsonInclude(Include.NON_NULL)
  @JsonProperty("uraBarcode")
  private String uraBarcode;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getForeignAddressIndicator() {
    return foreignAddressIndicator;
  }

  public void setForeignAddressIndicator(String foreignAddressIndicator) {
    this.foreignAddressIndicator = foreignAddressIndicator;
  }

  public Boolean getServiceAddressSA() {
    return isServiceAddressSA;
  }

  public void setServiceAddressSA(Boolean serviceAddressSA) {
    isServiceAddressSA = serviceAddressSA;
  }

  public String getUraBarcode() {
    return uraBarcode;
  }

  public void setUraBarcode(String uraBarcode) {
    this.uraBarcode = uraBarcode;
  }
}
