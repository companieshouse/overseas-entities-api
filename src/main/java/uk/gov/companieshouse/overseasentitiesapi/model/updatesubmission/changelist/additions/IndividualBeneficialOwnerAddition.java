package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;

public class IndividualBeneficialOwnerAddition extends BeneficialOwnerAddition {
    private static final String APPOINTMENT_TYPE = "OE INDIVIDUAL BO";

    @JsonProperty("personName")
    private PersonName personName;

    @JsonProperty("nationalityOther")
    private String nationalityOther;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    public IndividualBeneficialOwnerAddition(LocalDate actionDate, LocalDate ceasedDate, Address residentialResidence,
                                             Address serviceAddress, List<String> natureOfControls) {
        super(actionDate, ceasedDate, residentialResidence, serviceAddress, natureOfControls);
        setAppointmentType(APPOINTMENT_TYPE);
    }

    public PersonName getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }

    public String getNationalityOther() {
        return nationalityOther;
    }

    public void setNationalityOther(String nationalityOther) {
        this.nationalityOther = nationalityOther;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
