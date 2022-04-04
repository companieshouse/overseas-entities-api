package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class OverseasEntitySubmission {

    @JsonProperty
    private Presenter presenter;

    @JsonProperty
    private Entity entity;

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
