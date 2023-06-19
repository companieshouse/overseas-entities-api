package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;

public class IndividualBeneficialOwnerAddition extends BeneficialOwnerAddition {
    private static final String APPOINTMENT_TYPE = "OE INDIVIDUAL BO";

    @JsonProperty("personName")
    private PersonName personName;

    @JsonInclude(NON_NULL)
    @JsonProperty("residentialAddress")
    private Address residentialAddress;

    @JsonProperty("nationalityOther")
    private String nationalityOther;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    public IndividualBeneficialOwnerAddition(LocalDate actionDate, LocalDate ceasedDate, Address residentialAddress,
                                             Address serviceAddress, List<String> natureOfControls, boolean isOnSanctionsList) {
        super(actionDate, ceasedDate, serviceAddress, natureOfControls, isOnSanctionsList);
        setAppointmentType(APPOINTMENT_TYPE);
        this.residentialAddress = residentialAddress;
    }

    public PersonName getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }

    public Address getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(Address residentialAddress) {
        this.residentialAddress = residentialAddress;
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
