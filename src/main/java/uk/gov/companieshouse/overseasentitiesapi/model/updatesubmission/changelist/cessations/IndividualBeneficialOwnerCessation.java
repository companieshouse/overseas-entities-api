package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.types.PersonName;

public class IndividualBeneficialOwnerCessation extends BeneficialOwnerCessation {

    @JsonProperty("personName")
    private PersonName personName;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    public IndividualBeneficialOwnerCessation(String appointmentId,
            LocalDate actionDate,
            LocalDate birthDate,
            PersonName personName) {
        super(appointmentId, actionDate);
        this.birthDate = birthDate;
        this.personName = personName;
        setAppointmentType("5007");
    }

    public Object getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
