package uk.gov.companieshouse.overseasentitiesapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

@Component
@Mapper(componentModel = "spring")
public interface OverseasEntityDtoDaoMapper {

      OverseasEntitySubmissionDto daoToDto(OverseasEntitySubmissionDao overseasEntitySubmission);

      OverseasEntitySubmissionDao dtoToDao(OverseasEntitySubmissionDto overseasEntitySubmission);
}
