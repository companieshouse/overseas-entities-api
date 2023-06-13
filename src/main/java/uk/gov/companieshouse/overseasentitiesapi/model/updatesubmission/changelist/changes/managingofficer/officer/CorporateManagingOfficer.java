package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class CorporateManagingOfficer extends Officer{
    @JsonProperty("name")
    private String name;

    @JsonProperty("registeredOffice")
    private Address registeredOffice;

    @JsonProperty("contactName")
    private String contactName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("companyIdentification")
    private CompanyIdentification companyIdentification;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getRegisteredOffice() {
        return registeredOffice;
    }

    public void setRegisteredOffice(Address registeredOffice) {
        this.registeredOffice = registeredOffice;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompanyIdentification getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(CompanyIdentification companyIdentification) {
        this.companyIdentification = companyIdentification;
    }
}
