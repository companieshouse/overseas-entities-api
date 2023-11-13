package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class IndividualBeneficialOwnerCessation extends BeneficialOwnerCessation {

    @JsonProperty("personName")
    private PersonName personName;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    @JsonProperty("haveDayOfDate")
    private Boolean haveDayOfBirth;

    public IndividualBeneficialOwnerCessation(String appointmentId, LocalDate actionDate,
            LocalDate birthDate, Boolean haveDayOfBirth, PersonName personName) {
        super(appointmentId, actionDate);
        this.birthDate = birthDate;
        this.personName = personName;
        this.haveDayOfBirth = haveDayOfBirth;
        setAppointmentType("OE INDIVIDUAL BO");
    }

    public PersonName getPersonName() {
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

    public Boolean getHaveDayOfBirth() {
        return haveDayOfBirth;
    }

    public void setHaveDayOfBirth(Boolean haveDayOfBirth) {
        this.haveDayOfBirth = haveDayOfBirth;
    }
}
