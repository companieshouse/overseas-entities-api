package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations;

import java.time.LocalDate;

public class CorporateManagingOfficerCessation extends ManagingOfficerCessation {

    public CorporateManagingOfficerCessation(String officerAppointmentId,
                                             LocalDate actionDate,
                                             String officerName) {
        super(officerAppointmentId, officerName, actionDate);
        setAppointmentType("Corporate Managing Officer");
    }
}