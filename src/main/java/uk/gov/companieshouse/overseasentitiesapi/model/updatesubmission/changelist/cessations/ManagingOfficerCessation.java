package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import java.time.LocalDate;

public abstract class ManagingOfficerCessation extends Cessation {

    private static final String CHANGE_NAME = "ceaseOfficerAppointment";

    @JsonProperty("officerAppointmentId")
    private String officerAppointmentId;

    @JsonProperty("officerName")
    private PersonName officerName;

    @JsonProperty("appointmentType")
    private String appointmentType;

    @JsonProperty("actionDate")
    private LocalDate actionDate;

    protected ManagingOfficerCessation(
            String officerAppointmentId,
            PersonName officerName,
            LocalDate actionDate) {
        super.setChangeName(CHANGE_NAME);
        this.officerAppointmentId = officerAppointmentId;
        this.officerName = officerName;
        this.actionDate = actionDate;
    }

    public String getOfficerAppointmentId(){
        return officerAppointmentId;
    }

    public void setOfficerAppointmentId(String officerAppointmentId){
        this.officerAppointmentId = officerAppointmentId;
    }

    public PersonName getOfficerName(){
        return officerName;
    }

    public void setOfficerName(PersonName officerName){
        this.officerName = officerName;
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
