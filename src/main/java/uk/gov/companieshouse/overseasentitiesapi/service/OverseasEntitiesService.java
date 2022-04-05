package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.OverseasEntitySubmission;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Service
public class OverseasEntitiesService {

    private final OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;

    @Autowired
    public OverseasEntitiesService(OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository) {
        this.overseasEntitySubmissionsRepository = overseasEntitySubmissionsRepository;
    }

    public void createOverseasEntity(OverseasEntitySubmission overseasEntitySubmission) {
        ApiLogger.debug("Called createOverseasEntity()");

        var insertedSubmission = overseasEntitySubmissionsRepository.insert(overseasEntitySubmission);

        ApiLogger.debug("Created a new overseas entity with id " + insertedSubmission.getId());
    }
}
