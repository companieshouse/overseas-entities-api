package uk.gov.companieshouse.overseasentitiesapi.service.changelist;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.utils.OverseasEntityChangeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OverseasEntityChangeService {
    private final OverseasEntityChangeUtils overseasEntityChangeUtils;

    @Autowired
    public OverseasEntityChangeService(OverseasEntityChangeUtils overseasEntityChangeUtils) {
        this.overseasEntityChangeUtils = overseasEntityChangeUtils;
    }

    public List<Change> collateOverseasEntityChanges(Object existingSubmission, Object updatedSubmission){
        List<Change> changes = new ArrayList<>();

        changes.add(overseasEntityChangeUtils.verifyEntityNameChange("", ""));
        changes.add(overseasEntityChangeUtils.verifyPrincipalAddressChange(null, null));
        changes.add(overseasEntityChangeUtils.verifyCorrespondenceAddressChange(null, null));
        changes.add(overseasEntityChangeUtils.verifyCompanyIdentificationChange(
                "", "","", "",
                "", "","", ""));
        changes.add(overseasEntityChangeUtils.verifyEntityEmailAddressChange("", ""));

        changes.removeIf(Objects::isNull);

        return changes;
    }
}
