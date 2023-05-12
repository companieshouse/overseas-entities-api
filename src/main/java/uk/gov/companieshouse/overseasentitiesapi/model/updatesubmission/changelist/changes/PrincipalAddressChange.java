package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class PrincipalAddressChange extends Change {
    private static final String CHANGE_NAME = "changeOfRoa";

    @JsonProperty("registeredOfficeAddress")
    private RegisteredOfficeAddressApi registeredOfficeAddress;

    @JsonProperty("proposedRegisteredOfficeAddress")
    private AddressDto proposedRegisteredOfficeAddress;

    public PrincipalAddressChange(RegisteredOfficeAddressApi registeredOfficeAddress, AddressDto proposedRegisteredOfficeAddress){
        super.setChangeName(CHANGE_NAME);
        this.registeredOfficeAddress = registeredOfficeAddress;
        this.proposedRegisteredOfficeAddress = proposedRegisteredOfficeAddress;
    }

    public RegisteredOfficeAddressApi getRegisteredOfficeAddress() {
        return registeredOfficeAddress;
    }

    public void setRegisteredOfficeAddress(RegisteredOfficeAddressApi registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
    }

    public AddressDto getProposedRegisteredOfficeAddress() {
        return proposedRegisteredOfficeAddress;
    }

    public void setProposedRegisteredOfficeAddress(AddressDto proposedRegisteredOfficeAddress) {
        this.proposedRegisteredOfficeAddress = proposedRegisteredOfficeAddress;
    }
}
