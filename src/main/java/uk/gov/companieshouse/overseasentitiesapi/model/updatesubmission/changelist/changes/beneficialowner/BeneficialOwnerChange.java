package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.Psc;

public abstract class BeneficialOwnerChange<T extends Psc> extends Change {
  @JsonProperty("change")
  private String change = "pscAppointmentChange";

  @JsonProperty("appointmentId")
  private String appointmentId;

  @JsonProperty("psc")
  private T psc;

  public String getChange() {
    return change;
  }

  public void setChange(String change) {
    this.change = change;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }

  public T getPsc() {
    return psc;
  }

  public void setPsc(T psc) {
    this.psc = psc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeneficialOwnerChange<?> that = (BeneficialOwnerChange<?>) o;
    return Objects.equals(change, that.change) && Objects.equals(appointmentId,
        that.appointmentId) && Objects.equals(psc, that.psc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(change, appointmentId, psc, getClass());
  }
}
