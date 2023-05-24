package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LegalPersonBeneficialOwnerCessation extends BeneficialOwnerCessation {

    @JsonProperty("corporateSoleName")
    private String corporateSoleName;

    public LegalPersonBeneficialOwnerCessation(String appointmentId,
            LocalDate actionDate,
            String corporateSoleName) {
        super(appointmentId, actionDate);
        this.corporateSoleName = corporateSoleName;
        setAppointmentType("OE GPA BO");
    }

    public String getCorporateSoleName() {
        return corporateSoleName;
    }

    public void setCorporateSoleName(String corporateSoleName) {
        this.corporateSoleName = corporateSoleName;
    }
}
