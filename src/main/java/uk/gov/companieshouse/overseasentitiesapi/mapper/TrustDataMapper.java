package uk.gov.companieshouse.overseasentitiesapi.mapper;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

@Component
public interface TrustDataMapper {

    TrustDataDao dtoToDao(TrustDataDto trustDataDto) throws ServiceException;
}
