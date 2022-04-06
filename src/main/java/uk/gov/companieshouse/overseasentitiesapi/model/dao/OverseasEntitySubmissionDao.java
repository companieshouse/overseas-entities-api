package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "overseas_entities_submissions")
public class OverseasEntitySubmissionDao {

    @Id
    private String id;

    @Field("presenter")
    private PresenterDao presenter;

    @Field("entity")
    private EntityDao entity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PresenterDao getPresenter() {
        return presenter;
    }

    public void setPresenter(PresenterDao presenter) {
        this.presenter = presenter;
    }

    public EntityDao getEntity() {
        return entity;
    }

    public void setEntity(EntityDao entity) {
        this.entity = entity;
    }
}
