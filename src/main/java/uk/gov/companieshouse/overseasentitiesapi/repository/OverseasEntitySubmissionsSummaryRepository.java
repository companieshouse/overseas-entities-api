package uk.gov.companieshouse.overseasentitiesapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionSummaryDao;

@Repository
public interface OverseasEntitySubmissionsSummaryRepository extends MongoRepository<OverseasEntitySubmissionSummaryDao, String> {
}
