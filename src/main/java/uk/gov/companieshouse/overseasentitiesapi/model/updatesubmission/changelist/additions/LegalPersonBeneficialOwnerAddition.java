package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class LegalPersonBeneficialOwnerAddition extends BeneficialOwnerAddition {
    private static final String APPOINTMENT_TYPE = "OE GPA BO";

    @JsonProperty("corporateSoleName")
    private String corporateSoleName;

    @JsonProperty("companyIdentification")
    private CompanyIdentification companyIdentification;

    public LegalPersonBeneficialOwnerAddition(LocalDate actionDate, LocalDate ceasedDate, Address residentialAddress,
                                              Address serviceAddress, List<String> natureOfControls, boolean isOnSanctionsList) {
        super(actionDate, ceasedDate, residentialAddress, serviceAddress, natureOfControls, isOnSanctionsList);
        setAppointmentType(APPOINTMENT_TYPE);
    }

    public String getCorporateSoleName() {
        return corporateSoleName;
    }

    public void setCorporateSoleName(String corporateSoleName) {
        this.corporateSoleName = corporateSoleName;
    }

    public CompanyIdentification getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(CompanyIdentification companyIdentification) {
        this.companyIdentification = companyIdentification;
    }
}
