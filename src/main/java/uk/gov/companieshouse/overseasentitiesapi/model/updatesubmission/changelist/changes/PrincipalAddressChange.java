package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class PrincipalAddressChange extends Change {
    private static final String CHANGE_NAME = "changeOfRoa";

    @JsonProperty("proposedRegisteredOfficeAddress")
    private Address proposedRegisteredOfficeAddress;

    public PrincipalAddressChange(Address proposedRegisteredOfficeAddress){
        super.setChangeName(CHANGE_NAME);
        this.proposedRegisteredOfficeAddress = proposedRegisteredOfficeAddress;
    }

    public Address getProposedRegisteredOfficeAddress() {
        return proposedRegisteredOfficeAddress;
    }

    public void setProposedRegisteredOfficeAddress(Address proposedRegisteredOfficeAddress) {
        this.proposedRegisteredOfficeAddress = proposedRegisteredOfficeAddress;
    }
}
