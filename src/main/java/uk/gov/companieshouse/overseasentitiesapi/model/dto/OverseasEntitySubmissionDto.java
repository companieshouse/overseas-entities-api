package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OverseasEntitySubmissionDto {

    @JsonProperty("presenter")
    private PresenterDto presenter;

    @JsonProperty("entity")
    private EntityDto entity;

    public PresenterDto getPresenter() {
        return presenter;
    }

    public void setPresenter(PresenterDto presenter) {
        this.presenter = presenter;
    }

    public EntityDto getEntity() {
        return entity;
    }

    public void setEntity(EntityDto entity) {
        this.entity = entity;
    }
}
