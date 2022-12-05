package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntityDueDiligenceDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks.EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES;

public class OverseasEntityDueDiligenceMock {

    public static final LocalDate IDENTITY_DATE = LocalDate.of(2022,1,1);
    public static final String NAME = "ABC Checking Ltd";
    public static final String SUPERVISOR_NAME = "Super Supervisor";
    public static final String AML_NUMBER = "abc123";
    public static final String PARTNER_NAME = "John Smith";

    public static OverseasEntityDueDiligenceDao getOverseasEntityDueDiligenceDao() {
        OverseasEntityDueDiligenceDao overseasEntityDueDiligenceDao = new OverseasEntityDueDiligenceDao();
        overseasEntityDueDiligenceDao.setIdentityDate(IDENTITY_DATE);
        overseasEntityDueDiligenceDao.setAddress(AddressMock.getAddressDao());
        overseasEntityDueDiligenceDao.setName(NAME);
        overseasEntityDueDiligenceDao.setEmail(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES);
        overseasEntityDueDiligenceDao.setSupervisoryName(SUPERVISOR_NAME);
        overseasEntityDueDiligenceDao.setAmlNumber(AML_NUMBER);
        overseasEntityDueDiligenceDao.setPartnerName(PARTNER_NAME);
        return overseasEntityDueDiligenceDao;
    }

    public static OverseasEntityDueDiligenceDto getOverseasEntityDueDiligenceDto() {
        OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
        overseasEntityDueDiligenceDto.setIdentityDate(IDENTITY_DATE);
        overseasEntityDueDiligenceDto.setAddress(AddressMock.getAddressDto());
        overseasEntityDueDiligenceDto.setName(NAME);
        overseasEntityDueDiligenceDto.setEmail(" " + EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES + " ");
        overseasEntityDueDiligenceDto.setSupervisoryName(SUPERVISOR_NAME);
        overseasEntityDueDiligenceDto.setAmlNumber(AML_NUMBER);
        overseasEntityDueDiligenceDto.setPartnerName(PARTNER_NAME);
        return overseasEntityDueDiligenceDto;
    }
}
