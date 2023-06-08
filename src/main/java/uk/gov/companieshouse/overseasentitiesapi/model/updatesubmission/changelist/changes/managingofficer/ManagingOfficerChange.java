package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.Officer;

public abstract class ManagingOfficerChange<T extends Officer> extends Change {
    private final String CHANGE_NAME = "officerAppointmentChange";
    private final String APPOINTMENT_TYPE = "Managing Officer";

    @JsonProperty("change")
    private String change = CHANGE_NAME;
    @JsonProperty("appointmentId")
    private String appointmentId;
    @JsonProperty("appointmentType")
    private String appointmentType = APPOINTMENT_TYPE;
    @JsonProperty("officer")
    private T officer;

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

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public T getOfficer() {
        return officer;
    }

    public void setOfficer(T officer) {
        this.officer = officer;
    }
}
