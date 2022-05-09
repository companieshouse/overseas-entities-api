package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.PresenterDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

public class PresenterMock {
    public static PresenterDto getPresenterDto() {
        PresenterDto presenterDto = new PresenterDto();
        presenterDto.setFullName("Joe Bloggs");
        presenterDto.setEmail("user@domain.roe");
        return presenterDto;
    }

    public static PresenterDao getPresenterDao() {
        PresenterDao presenterDao = new PresenterDao();
        presenterDao.setFullName("full name");
        presenterDao.setEmail("user@domain.roe");
        return presenterDao;
    }
}
