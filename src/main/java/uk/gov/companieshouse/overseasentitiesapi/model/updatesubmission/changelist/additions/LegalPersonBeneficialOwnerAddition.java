package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;

public class LegalPersonBeneficialOwnerAddition extends BeneficialOwnerAddition {
    private static final String APPOINTMENT_TYPE = "OE GPA BO";

    @JsonProperty("corporateSoleName")
    private String corporateSoleName;

    @JsonProperty("legalForm")
    private String legalForm;

    @JsonProperty("governingLaw")
    private String governingLaw;

    public LegalPersonBeneficialOwnerAddition(LocalDate actionDate, LocalDate ceasedDate, Address residentialAddress,
                                              Address serviceAddress, List<String> natureOfControls) {
        super(actionDate, ceasedDate, residentialAddress, serviceAddress, natureOfControls);
        setAppointmentType(APPOINTMENT_TYPE);
    }

    public String getCorporateSoleName() {
        return corporateSoleName;
    }

    public void setCorporateSoleName(String corporateSoleName) {
        this.corporateSoleName = corporateSoleName;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public String getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(String governingLaw) {
        this.governingLaw = governingLaw;
    }
}
