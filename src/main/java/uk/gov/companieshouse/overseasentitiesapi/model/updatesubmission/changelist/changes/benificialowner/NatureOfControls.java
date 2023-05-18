package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NatureOfControls {
  @JsonProperty("natureOfControlType")
  private String natureOfControlType;

  public String getNatureOfControlType() {
    return natureOfControlType;
  }

  public void setNatureOfControlType(String natureOfControlType) {
    this.natureOfControlType = natureOfControlType;
  }
}
