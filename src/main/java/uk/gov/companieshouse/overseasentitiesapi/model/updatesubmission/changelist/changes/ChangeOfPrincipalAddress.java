package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

public class ChangeOfPrincipalAddress extends Change {
    private final String CHANGE_NAME = "changeOfRoa";

    @JsonProperty("address")
    private AddressDto address;

    public ChangeOfPrincipalAddress(AddressDto address){
        super.setChangeName(CHANGE_NAME);
        this.address = address;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
