package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.DueDiligenceDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;

import java.time.LocalDate;

import static uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks.EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES;

public class DueDiligenceMock {

    public static DueDiligenceDao getDueDiligenceDao() {
        DueDiligenceDao dueDiligenceDao = new DueDiligenceDao();
        dueDiligenceDao.setIdentityDate(LocalDate.of(2022,1,1));
        dueDiligenceDao.setName("ABC Checking Ltd");
        dueDiligenceDao.setAddress(AddressMock.getAddressDao());
        dueDiligenceDao.setEmail(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES);
        dueDiligenceDao.setSupervisoryName("Super Supervisor");
        dueDiligenceDao.setAmlNumber("abc123");
        dueDiligenceDao.setAgentCode("c0de");
        dueDiligenceDao.setPartnerName("John Smith");
        dueDiligenceDao.setDiligence("Agreed");
        return dueDiligenceDao;
    }

    public static DueDiligenceDto getDueDiligenceDto() {
        DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
        dueDiligenceDto.setIdentityDate(LocalDate.of(2022,1,1));
        dueDiligenceDto.setName("ABC Checking Ltd");
        dueDiligenceDto.setAddress(AddressMock.getAddressDto());
        dueDiligenceDto.setEmail(" " + EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES + " ");
        dueDiligenceDto.setSupervisoryName("Super Supervisor");
        dueDiligenceDto.setAmlNumber("abc123");
        dueDiligenceDto.setAgentCode("c0de");
        dueDiligenceDto.setPartnerName("John Smith");
        dueDiligenceDto.setDiligence("Agreed");
        return dueDiligenceDto;
    }
}
