package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

import java.time.LocalDate;

public class CorporateManagingOfficerAddition extends ManagingOfficerAddition{
    @JsonProperty("name")
    private String name;

    @JsonProperty("contactName")
    private String contactName;

    @JsonProperty("contactEmail")
    private String contactEmail;

    @JsonProperty("identification")
    private CompanyIdentification identification;

    public CorporateManagingOfficerAddition(LocalDate actionDate,
                                            Address residentialAddress,
                                            Address serviceAddress,
                                            LocalDate resignedOn) {
        super(actionDate, residentialAddress, serviceAddress, resignedOn);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public CompanyIdentification getIdentification() {
        return identification;
    }

    public void setIdentification(CompanyIdentification identification) {
        this.identification = identification;
    }
}
