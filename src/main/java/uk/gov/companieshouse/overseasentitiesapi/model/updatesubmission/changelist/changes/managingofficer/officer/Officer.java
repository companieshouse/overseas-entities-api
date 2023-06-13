package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;

public abstract class Officer {
    @JsonProperty("serviceAddress")
    private Address serviceAddress;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @JsonProperty("role")
    private String role;

    public Address getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(Address serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
