package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public abstract class ManagingOfficerAddition extends Addition {
    private static final String CHANGE_NAME = "addOfficerAppointment";

    @JsonProperty("actionDate")
    private LocalDate actionDate;

    @JsonInclude(NON_NULL)
    @JsonProperty("residentialAddress")
    private Address residentialAddress;

    @JsonProperty("serviceAddress")
    private Address serviceAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("resignedOn")
    private LocalDate resignedOn;

    @JsonProperty("role")
    private String role;

    protected ManagingOfficerAddition(
            LocalDate actionDate,
            Address residentialAddress,
            Address serviceAddress,
            LocalDate resignedOn,
            String appointmentType) {
        super.setChangeName(CHANGE_NAME);
        super.setAppointmentType(appointmentType);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
