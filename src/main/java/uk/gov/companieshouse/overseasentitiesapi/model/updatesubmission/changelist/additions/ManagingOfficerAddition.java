package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;

public abstract class ManagingOfficerAddition extends Addition {
    private static final String CHANGE_NAME = "addOfficerAppointment";
    private static final String APPOINTMENT_TYPE = "Managing Officer";

    @JsonProperty("actionDate")
    private LocalDate actionDate;

    @JsonProperty("residentialAddress")
    private Address residentialAddress;

    @JsonProperty("serviceAddress")
    private Address serviceAddress;

    @JsonProperty("resignedOn")
    private LocalDate resignedOn;

    protected ManagingOfficerAddition(LocalDate actionDate,
                                   Address residentialAddress,
                                   Address serviceAddress,
                                   LocalDate resignedOn) {
        super.setChangeName(CHANGE_NAME);
        super.setAppointmentType(APPOINTMENT_TYPE);
        this.actionDate = actionDate;
        this.residentialAddress = residentialAddress;
        this.serviceAddress = serviceAddress;
        this.resignedOn = resignedOn;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public Address getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(Address residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public Address getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(Address serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public LocalDate getResignedOn() {
        return resignedOn;
    }

    public void setResignedOn(LocalDate resignedOn) {
        this.resignedOn = resignedOn;
    }
}
