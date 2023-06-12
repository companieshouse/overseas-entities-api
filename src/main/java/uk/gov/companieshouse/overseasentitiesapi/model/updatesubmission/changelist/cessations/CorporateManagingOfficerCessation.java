package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.CorporateManagingOfficerAddition.CORPORATE_MANAGING_OFFICER;

import java.time.LocalDate;

public class CorporateManagingOfficerCessation extends ManagingOfficerCessation {

    public CorporateManagingOfficerCessation(String officerAppointmentId,
                                             LocalDate actionDate,
                                             String officerName) {
        super(officerAppointmentId, officerName, actionDate);
        setAppointmentType(CORPORATE_MANAGING_OFFICER);
    }
}