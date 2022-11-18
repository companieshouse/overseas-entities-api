package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.EntityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;


public class EntityMock {

    public static EntityDto getEntityDto() {
        EntityDto entityDto = new EntityDto();
        entityDto.setName("ABC Entity");
        entityDto.setIncorporationCountry("France");
        entityDto.setServiceAddressSameAsPrincipalAddress(Boolean.TRUE);
        entityDto.setEmail("jbloggs@jb.com");
        entityDto.setLegalForm("LF");
        entityDto.setLawGoverned("LG");
        return entityDto;
    }

    public static EntityDao getEntityDao() {
        EntityDao entityDao = new EntityDao();
        entityDao.setName("ABC Entity");
        entityDao.setIncorporationCountry("France");
        entityDao.setServiceAddressSameAsPrincipalAddress(Boolean.TRUE);
        entityDao.setEmail("jbloggs@jb.com");
        entityDao.setLegalForm("LF");
        entityDao.setLawGoverned("LG");
        return entityDao;
    }
}
