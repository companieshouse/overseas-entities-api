package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "overseas_entities_submissions")
public class OverseasEntitySubmissionSummaryDao {

    @Field("entity_number")
    private String entityNumber;

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }
}