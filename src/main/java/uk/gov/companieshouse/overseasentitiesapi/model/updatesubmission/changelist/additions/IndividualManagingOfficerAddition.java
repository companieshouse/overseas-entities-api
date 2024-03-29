package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import java.time.LocalDate;

public class IndividualManagingOfficerAddition extends ManagingOfficerAddition {

    public static final String INDIVIDUAL_MANAGING_OFFICER = "Individual Managing Officer";
    @JsonProperty("personName")
    private PersonName personName;

    @JsonInclude(NON_EMPTY)
    @JsonProperty("formerNames")
    private String formerNames;

    @JsonProperty("nationalityOther")
    private String nationalityOther;

    @JsonProperty("occupation")
    private String occupation;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    public IndividualManagingOfficerAddition(LocalDate actionDate,
                                             Address residentialAddress,
                                             Address serviceAddress,
                                             LocalDate resignedOn) {
        super(actionDate, residentialAddress, serviceAddress, resignedOn, INDIVIDUAL_MANAGING_OFFICER);
    }

    public PersonName getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }

    public String getFormerNames() {
        return formerNames;
    }

    public void setFormerNames(String formerNames) {
        this.formerNames = formerNames;
    }

    public String getNationalityOther() {
        return nationalityOther;
    }

    public void setNationalityOther(String nationalityOther) {
        this.nationalityOther = nationalityOther;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
