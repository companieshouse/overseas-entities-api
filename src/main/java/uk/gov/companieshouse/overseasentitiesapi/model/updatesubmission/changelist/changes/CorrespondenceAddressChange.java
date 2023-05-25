package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class CorrespondenceAddressChange extends Change {
    private static final String CHANGE_NAME = "changeOfServiceAddress";

    @JsonProperty("proposedServiceAddress")
    private Address proposedServiceAddress;

    public CorrespondenceAddressChange(Address proposedServiceAddress){
        super.setChangeName(CHANGE_NAME);
        this.proposedServiceAddress = proposedServiceAddress;
    }

    public Address getProposedServiceAddress() {
        return proposedServiceAddress;
    }

    public void setProposedServiceAddress(Address proposedServiceAddress) {
        this.proposedServiceAddress = proposedServiceAddress;
    }
}
