package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntityDueDiligenceDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;

import java.time.LocalDate;

public class OverseasEntityDueDiligenceMock {
    public static OverseasEntityDueDiligenceDao getOverseasEntityDueDiligenceDao() {
        OverseasEntityDueDiligenceDao dueDiligenceDao = new OverseasEntityDueDiligenceDao();
        dueDiligenceDao.setIdentityDate(LocalDate.of(2022,1,1));
        dueDiligenceDao.setAddress(AddressMock.getAddressDao());
        dueDiligenceDao.setName("ABC Checking Ltd");
        dueDiligenceDao.setEmail("investigations@abc.com");
        dueDiligenceDao.setSupervisoryName("Super Supervisor");
        dueDiligenceDao.setAmlNumber("abc123");
        dueDiligenceDao.setPartnerName("John Smith");
        return dueDiligenceDao;
    }

    public static OverseasEntityDueDiligenceDto getOverseasEntityDueDiligenceDto() {
        OverseasEntityDueDiligenceDto dueDiligenceDto = new OverseasEntityDueDiligenceDto();
        dueDiligenceDto.setIdentityDate(LocalDate.of(2022,1,1));
        dueDiligenceDto.setAddress(AddressMock.getAddressDto());
        dueDiligenceDto.setName("ABC Checking Ltd");
        dueDiligenceDto.setEmail("investigations@abc.com");
        dueDiligenceDto.setSupervisoryName("Super Supervisor");
        dueDiligenceDto.setAmlNumber("abc123");
        dueDiligenceDto.setPartnerName("John Smith");
        return dueDiligenceDto;
    }
}
