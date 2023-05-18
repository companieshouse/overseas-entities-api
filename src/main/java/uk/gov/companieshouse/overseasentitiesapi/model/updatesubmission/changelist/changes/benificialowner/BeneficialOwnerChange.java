package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

public class BeneficialOwnerChange extends Change {

  private static final String CHANGE_NAME = "pscAppointmentChange";
  private String appointmentId;
  private String name;

  @JsonProperty("psc")
  private Psc psc;
  @JsonProperty("pscPreviousDetails")
  private Psc pscPreviousDetails;

  public String getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Psc getPsc() {
    return psc;
  }

  public void setPsc(Psc psc) {
    this.psc = psc;
  }

  public Psc getPscPreviousDetails() {
    return pscPreviousDetails;
  }

  public void setPscPreviousDetails(Psc pscPreviousDetails) {
    this.pscPreviousDetails = pscPreviousDetails;
  }
}
