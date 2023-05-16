package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class CorrespondenceAddressChange extends Change {
    private static final String CHANGE_NAME = "changeOfServiceAddress";

    @JsonProperty("serviceAddress")
    private AddressDto serviceAddress;

    @JsonProperty("proposedServiceAddress")
    private AddressDto proposedServiceAddress;

    public CorrespondenceAddressChange(AddressDto serviceAddress, AddressDto proposedServiceAddress){
        super.setChangeName(CHANGE_NAME);
        this.serviceAddress = serviceAddress;
        this.proposedServiceAddress = proposedServiceAddress;
    }

    public AddressDto getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressDto serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public AddressDto getProposedServiceAddress() {
        return proposedServiceAddress;
    }

    public void setProposedServiceAddress(AddressDto proposedServiceAddress) {
        this.proposedServiceAddress = proposedServiceAddress;
    }
}
