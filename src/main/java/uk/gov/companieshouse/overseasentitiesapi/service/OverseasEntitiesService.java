package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.Optional;

@Service
public class OverseasEntitiesService {

    private final OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;

    private final OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @Autowired
    public OverseasEntitiesService(OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository,
                                   OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper) {
        this.overseasEntitySubmissionsRepository = overseasEntitySubmissionsRepository;
        this.overseasEntityDtoDaoMapper = overseasEntityDtoDaoMapper;
    }

    public void createOverseasEntity(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        ApiLogger.debug("Called createOverseasEntity()");

        var overseasEntitySubmissionDao = overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto);
        var insertedSubmissionDao = overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao);

        ApiLogger.debug("Created a new overseas entity with id " + insertedSubmissionDao.getId());
    }

    public Optional<OverseasEntitySubmissionDto> getOverseasEntitySubmission(String submissionId) {
        var submission = overseasEntitySubmissionsRepository.findById(submissionId);
        if (submission.isPresent()) {
            ApiLogger.info(String.format("%s: Confirmation Statement Submission found. About to return", submission.get().getId()));

            var json = overseasEntityDtoDaoMapper.daoToDto(submission.get());
            return Optional.of(json);
        } else {
            return Optional.empty();
        }
    }
}
