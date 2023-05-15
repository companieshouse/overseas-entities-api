package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CorporateEntityBeneficialOwnerCessation extends BeneficialOwnerCessation {

    @JsonProperty("corporateName")
    private String corporateName;

    public CorporateEntityBeneficialOwnerCessation(String appointmentId,
            LocalDate actionDate,
            String corporateName) {
        super(appointmentId, actionDate);
        this.corporateName = corporateName;
        setAppointmentType("5008");
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
}
