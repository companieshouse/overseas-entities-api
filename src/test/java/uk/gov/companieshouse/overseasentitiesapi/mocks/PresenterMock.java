package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.PresenterDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

import static uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks.EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES;

public class PresenterMock {
    public static PresenterDto getPresenterDto() {
        PresenterDto presenterDto = new PresenterDto();
        presenterDto.setFullName("Joe Bloggs");
        presenterDto.setEmail(" " + EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES + " ");
        return presenterDto;
    }

    public static PresenterDao getPresenterDao() {
        PresenterDao presenterDao = new PresenterDao();
        presenterDao.setFullName("Joe Bloggs");
        presenterDao.setEmail(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES);
        return presenterDao;
    }
}
