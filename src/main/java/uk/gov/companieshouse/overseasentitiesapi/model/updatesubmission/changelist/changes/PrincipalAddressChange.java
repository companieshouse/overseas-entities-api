package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public class PrincipalAddressChange extends Change {
    private static final String CHANGE_NAME = "changeOfRoa";

    @JsonProperty("registeredOfficeAddress")
    private Address registeredOfficeAddress;

    @JsonProperty("proposedRegisteredOfficeAddress")
    private Address proposedRegisteredOfficeAddress;

    public PrincipalAddressChange(Address registeredOfficeAddress, Address proposedRegisteredOfficeAddress){
        super.setChangeName(CHANGE_NAME);
        this.registeredOfficeAddress = registeredOfficeAddress;
        this.proposedRegisteredOfficeAddress = proposedRegisteredOfficeAddress;
    }

    public Address getRegisteredOfficeAddress() {
        return registeredOfficeAddress;
    }

    public void setRegisteredOfficeAddress(Address registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
    }

    public Address getProposedRegisteredOfficeAddress() {
        return proposedRegisteredOfficeAddress;
    }

    public void setProposedRegisteredOfficeAddress(Address proposedRegisteredOfficeAddress) {
        this.proposedRegisteredOfficeAddress = proposedRegisteredOfficeAddress;
    }
}
