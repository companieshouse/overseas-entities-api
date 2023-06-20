package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

import java.time.LocalDate;

public class CorporateManagingOfficerAddition extends ManagingOfficerAddition {

    public static final String CORPORATE_MANAGING_OFFICER = "Corporate Managing Officer";

    @JsonProperty("name")
    private String name;

    @JsonInclude(NON_NULL)
    @JsonProperty("registeredOffice")
    private Address registeredOffice;

    @JsonProperty("contactName")
    private String contactName;

    @JsonProperty("contactEmail")
    private String contactEmail;

    @JsonProperty("companyIdentification")
    private CompanyIdentification companyIdentification;

    public CorporateManagingOfficerAddition(LocalDate actionDate,
                                            Address residentialAddress,
                                            Address serviceAddress,
                                            LocalDate resignedOn) {
        super(actionDate, residentialAddress, serviceAddress, resignedOn, CORPORATE_MANAGING_OFFICER);
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

    public CompanyIdentification getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(CompanyIdentification companyIdentification) {
        this.companyIdentification = companyIdentification;
    }
}
