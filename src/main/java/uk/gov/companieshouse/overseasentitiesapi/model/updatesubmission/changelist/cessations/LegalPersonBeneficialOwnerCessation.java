package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LegalPersonBeneficialOwnerCessation extends BeneficialOwnerCessation {

    @JsonProperty("corporateName")
    private String corporateName;

    public LegalPersonBeneficialOwnerCessation(String appointmentId,
            LocalDate actionDate,
            String corporateSoleName) {
        super(appointmentId, actionDate);
        this.corporateName = corporateSoleName;
        setAppointmentType("OE GPA BO");
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
}
