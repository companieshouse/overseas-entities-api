package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class NatureOfControl {
  @JsonProperty("natureOfControlType")
  private List<String> natureOfControlTypes;

  public List<String> getNatureOfControlTypes() {
    return natureOfControlTypes;
  }

  public void setNatureOfControlTypes(List<String> natureOfControlTypes) {
    this.natureOfControlTypes = natureOfControlTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NatureOfControl that = (NatureOfControl) o;
    return Objects.equals(natureOfControlTypes, that.natureOfControlTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(natureOfControlTypes);
  }
}
