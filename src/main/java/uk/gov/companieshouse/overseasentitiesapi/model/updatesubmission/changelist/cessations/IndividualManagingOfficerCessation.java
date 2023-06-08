package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

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
    }

    public String getOfficerDateOfBirth() {
        return officerDateOfBirth;
    }

    public void setOfficerDateOfBirth(String officerDateOfBirth) {
        this.officerDateOfBirth = officerDateOfBirth;
    }
}
