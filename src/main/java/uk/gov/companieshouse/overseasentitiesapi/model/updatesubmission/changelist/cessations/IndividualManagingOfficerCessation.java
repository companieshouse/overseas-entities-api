package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualManagingOfficerAddition.INDIVIDUAL_MANAGING_OFFICER;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class IndividualManagingOfficerCessation extends ManagingOfficerCessation {

    @JsonProperty("officerDateOfBirth")
    private String officerDateOfBirth;

    public IndividualManagingOfficerCessation(
            String officerAppointmentId,
            String officerName,
            String officerDateOfBirth,
            LocalDate actionDate
            ) {
        super(officerAppointmentId, officerName, actionDate);
        this.officerDateOfBirth = officerDateOfBirth;
        setAppointmentType(INDIVIDUAL_MANAGING_OFFICER);
    }

    public String getOfficerDateOfBirth() {
        return officerDateOfBirth;
    }

    public void setOfficerDateOfBirth(String officerDateOfBirth) {
        this.officerDateOfBirth = officerDateOfBirth;
    }
}
