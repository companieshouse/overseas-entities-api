package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "overseas_entities_submissions")
public class OverseasEntitySubmission {

    private static final String PRESENTER_FIELD = "presenter";
    private static final String ENTITY_FIELD = "entity";

    @Id
    private String id;

    @JsonProperty(PRESENTER_FIELD)
    @Field(PRESENTER_FIELD)
    private Presenter presenter;

    @JsonProperty(ENTITY_FIELD)
    @Field(ENTITY_FIELD)
    private Entity entity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Presenter presenter) {
        this.entity = entity;
    }
}
