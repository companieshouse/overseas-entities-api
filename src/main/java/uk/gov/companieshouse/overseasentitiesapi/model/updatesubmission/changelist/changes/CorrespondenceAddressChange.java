package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public class CorrespondenceAddressChange extends Change {
    private static final String CHANGE_NAME = "changeOfServiceAddress";

    @JsonProperty("serviceAddress")
    private Address serviceAddress;

    @JsonProperty("proposedServiceAddress")
    private Address proposedServiceAddress;

    public CorrespondenceAddressChange(Address serviceAddress, Address proposedServiceAddress){
        super.setChangeName(CHANGE_NAME);
        this.serviceAddress = serviceAddress;
        this.proposedServiceAddress = proposedServiceAddress;
    }

    public Address getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(Address serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Address getProposedServiceAddress() {
        return proposedServiceAddress;
    }

    public void setProposedServiceAddress(Address proposedServiceAddress) {
        this.proposedServiceAddress = proposedServiceAddress;
    }
}
