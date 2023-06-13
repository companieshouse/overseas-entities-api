package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer;

import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.CorporateManagingOfficer;

public class CorporateManagingOfficerChange extends ManagingOfficerChange<CorporateManagingOfficer> {
    private static final String APPOINTMENT_TYPE = "Corporate Managing Officer";

    public CorporateManagingOfficerChange() {
        super.appointmentType = APPOINTMENT_TYPE;
    }
}
