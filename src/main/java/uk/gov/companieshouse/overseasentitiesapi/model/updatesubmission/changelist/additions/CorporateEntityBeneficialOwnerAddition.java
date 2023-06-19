package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class CorporateEntityBeneficialOwnerAddition extends BeneficialOwnerAddition {
    private static final String APPOINTMENT_TYPE = "OE OLE BO";

    @JsonProperty("corporateName")
    private String corporateName;

    @JsonInclude(NON_NULL)
    @JsonProperty("registeredOffice")
    private Address registeredOffice;

    @JsonProperty("companyIdentification")
    private CompanyIdentification companyIdentification;

    public CorporateEntityBeneficialOwnerAddition(LocalDate actionDate, LocalDate ceasedDate, Address residentialAddress,
                                                  Address serviceAddress, List<String> natureOfControls, boolean isOnSanctionsList) {
        super(actionDate, ceasedDate, residentialAddress, natureOfControls, isOnSanctionsList);
        setAppointmentType(APPOINTMENT_TYPE);
        this.registeredOffice = residentialAddress;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public Address getRegisteredOffice() {return this.registeredOffice; }

    public void setRegisteredOffice(Address registeredOffice) {
        this.registeredOffice = registeredOffice;
    }

    public CompanyIdentification getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(CompanyIdentification companyIdentification) {
        this.companyIdentification = companyIdentification;
    }
}
