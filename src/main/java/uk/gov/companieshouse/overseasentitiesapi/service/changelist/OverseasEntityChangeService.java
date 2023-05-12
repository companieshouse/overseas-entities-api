package uk.gov.companieshouse.overseasentitiesapi.service.changelist;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OverseasEntityChangeService {
    private final OverseasEntityChangeValidator overseasEntityChangeValidator;

    @Autowired
    public OverseasEntityChangeService(OverseasEntityChangeValidator overseasEntityChangeValidator) {
        this.overseasEntityChangeValidator = overseasEntityChangeValidator;
    }

    public List<Change> collateOverseasEntityChanges(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission){
        List<Change> changes = new ArrayList<>();

        changes.add(overseasEntityChangeValidator.verifyEntityNameChange(
                existingRegistration.getLeft().getCompanyName(),
                updateSubmission.getEntityName().getName()));
        changes.add(overseasEntityChangeValidator.verifyPrincipalAddressChange(
                existingRegistration.getLeft().getRegisteredOfficeAddress(),
                updateSubmission.getEntity().getPrincipalAddress()));
        changes.add(overseasEntityChangeValidator.verifyCorrespondenceAddressChange(
                existingRegistration.getLeft().getRegisteredOfficeAddress(),
                updateSubmission.getEntity().getServiceAddress()));
//        changes.add(overseasEntityChangeValidator.verifyCompanyIdentificationChange(
//                existingRegistration.getLeft().getForeignCompanyDetails().getLegalForm(),
//                existingRegistration.getLeft().getForeignCompanyDetails().getGovernedBy(),
//                existingRegistration.getLeft().,
//                existingRegistration.getLeft().,
//                updateSubmission.getEntity().getLegalForm(),
//                updateSubmission.getEntity().getLawGoverned(),
//                updateSubmission.getEntity().get ,
//                ""));
        changes.add(overseasEntityChangeValidator.verifyEntityEmailAddressChange(
                existingRegistration.getRight().getEmail(),
                updateSubmission.getEntity().getEmail()));

        changes.removeIf(Objects::isNull);

        return changes;
    }
}
