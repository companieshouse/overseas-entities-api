package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BeneficialOwnerCessation extends Cessation {

    private static final String CHANGE_NAME = "ceasePSCAppointment";

    @JsonProperty("appointmentId")
    private String appointmentId;

    @JsonProperty("appointmentType")
    private String appointmentType;

    @JsonProperty("actionDate")
    private LocalDate actionDate;

    protected BeneficialOwnerCessation(
            String appointmentId,
            LocalDate actionDate) {
        super.setChangeName(CHANGE_NAME);
        this.appointmentId = appointmentId;
        this.actionDate = actionDate;
    }

    public String getAppointmentId(){
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId){
        this.appointmentId = appointmentId;
    }

    public String getAppointmentType(){
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType){
        this.appointmentType = appointmentType;
    }

    public LocalDate getActionDate(){
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate){
        this.actionDate = actionDate;
    }
}
