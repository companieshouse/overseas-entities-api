package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DateBecameRegistrableChange extends Change{
  private static final String CHANGE_NAME = "dateBecomeRegistrableChange";

  @JsonProperty("date_became_registrable")
  private String dateBecameRegistrable;

  public DateBecameRegistrableChange(String dateBecameRegistrable) {
    this.dateBecameRegistrable = dateBecameRegistrable;
  }
  public String getDateBecameRegistrable() {
    return dateBecameRegistrable;
  }
  public void setDateBecameRegistrable(String dateBecameRegistrable) {
    this.dateBecameRegistrable = dateBecameRegistrable;
  }
}
