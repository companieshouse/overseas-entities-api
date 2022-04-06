package uk.gov.companieshouse.overseasentitiesapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;

@Repository
public interface OverseasEntitySubmissionsRepository extends MongoRepository<OverseasEntitySubmissionDao, String> {
}
