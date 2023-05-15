package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.types.PersonName;

public class IndividualBeneficialOwnerCessation extends BeneficialOwnerCessation {

    @JsonProperty("personName")
    private PersonName personName;

    @JsonProperty("birthDate")
    private String birthDate;

    public IndividualBeneficialOwnerCessation(String appointmentId,
            LocalDate actionDate,
            String birthDate,
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}
