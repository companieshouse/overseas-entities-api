package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;


public class EntityMock {

    public static EntityDto getEntityDto() {
        EntityDto entityDto = new EntityDto();
        entityDto.setName("ABC Entity");
        return entityDto;
    }
}
