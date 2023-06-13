package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer;

import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.IndividualManagingOfficer;

public class IndividualManagingOfficerChange extends ManagingOfficerChange<IndividualManagingOfficer> {
    private static final String APPOINTMENT_TYPE = "Individual Managing Officer";

    public IndividualManagingOfficerChange() {
        super.appointmentType = APPOINTMENT_TYPE;
    }
}
