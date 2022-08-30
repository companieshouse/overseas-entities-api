package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;


public class EntityMock {

    public static EntityDto getEntityDto() {
        EntityDto entityDto = new EntityDto();
        entityDto.setName("ABC Entity");
        entityDto.setIncorporationCountry("Noland");
        entityDto.setServiceAddressSameAsPrincipalAddress(Boolean.TRUE);
        entityDto.setEmail("jbloggs@jb.com");
        entityDto.setLegalForm("LF");
        entityDto.setLawGoverned("LG");
        return entityDto;
    }
}
