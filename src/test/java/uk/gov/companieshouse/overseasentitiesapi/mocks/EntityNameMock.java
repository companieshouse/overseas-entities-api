package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;

public class EntityNameMock {

    public static EntityNameDto getEntityNameDto() {
        EntityNameDto entityNameDto = new EntityNameDto();
        entityNameDto.setName("ABC Entity");
        return entityNameDto;
    }
}
