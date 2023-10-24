package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualManagingOfficerAddition.INDIVIDUAL_MANAGING_OFFICER;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import java.time.LocalDate;

public class IndividualManagingOfficerCessation extends ManagingOfficerCessation {

    @JsonProperty("officerDateOfBirth")
    private String officerDateOfBirth;

    @JsonInclude(NON_NULL)
    @JsonProperty("haveDayOfBirth")
    private Boolean haveDayOfBirth;

    public IndividualManagingOfficerCessation(
            String officerAppointmentId,
            PersonName officerName,
            String officerDateOfBirth,
            Boolean haveDayOfBirth,
            LocalDate actionDate
            ) {
        super(officerAppointmentId, officerName, actionDate);
        this.officerDateOfBirth = officerDateOfBirth;
        this.haveDayOfBirth = haveDayOfBirth;
        setAppointmentType(INDIVIDUAL_MANAGING_OFFICER);
    }

    public String getOfficerDateOfBirth() {
        return officerDateOfBirth;
    }

    public void setOfficerDateOfBirth(String officerDateOfBirth) {
        this.officerDateOfBirth = officerDateOfBirth;
    }
}
