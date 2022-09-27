package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.DueDiligenceDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;

import java.time.LocalDate;

public class DueDiligenceMock {

    public static DueDiligenceDao getDueDiligenceDao() {
        DueDiligenceDao dueDiligenceDao = new DueDiligenceDao();
        dueDiligenceDao.setIdentityDate(LocalDate.of(2022,1,1));
        dueDiligenceDao.setName("ABC Checking Ltd");
        dueDiligenceDao.setAddress(AddressMock.getAddressDao());
        dueDiligenceDao.setEmail("investigations@abc.com");
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
        dueDiligenceDto.setEmail("investigations@abc.com");
        dueDiligenceDto.setSupervisoryName("Super Supervisor");
        dueDiligenceDto.setAmlNumber("abc123");
        dueDiligenceDto.setAgentCode("c0de");
        dueDiligenceDto.setPartnerName("John Smith");
        dueDiligenceDto.setDiligence("Agreed");
        return dueDiligenceDto;
    }
}
