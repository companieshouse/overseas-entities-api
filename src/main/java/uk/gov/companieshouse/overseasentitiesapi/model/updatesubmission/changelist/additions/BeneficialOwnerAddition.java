package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public abstract class BeneficialOwnerAddition extends Addition {
    private static final String CHANGE_NAME = "pscAppointment";

    @JsonProperty("actionDate")
    private LocalDate actionDate;

    @JsonInclude(NON_NULL)
    @JsonProperty("ceasedDate")
    private LocalDate ceasedDate;

    @JsonProperty("residentialAddress")
    private Address residentialAddress;

    @JsonProperty("serviceAddress")
    private Address serviceAddress;

    @JsonProperty("natureOfControls")
    private List<String> natureOfControls;

    protected BeneficialOwnerAddition(LocalDate actionDate,
                                      LocalDate ceasedDate,
                                      Address residentialAddress,
                                      Address serviceAddress,
                                      List<String> natureOfControls) {
        super.setChangeName(CHANGE_NAME);
        this.actionDate = actionDate;
        this.ceasedDate = ceasedDate;
        this.residentialAddress = residentialAddress;
        this.serviceAddress = serviceAddress;
        this.natureOfControls = natureOfControls;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
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

    public List<String> getNatureOfControls() {
        return natureOfControls;
    }

    public void setNatureOfControls(List<String> natureOfControls) {
        this.natureOfControls = natureOfControls;
    }
}
