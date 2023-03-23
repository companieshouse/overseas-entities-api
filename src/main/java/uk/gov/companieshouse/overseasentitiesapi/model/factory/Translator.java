package uk.gov.companieshouse.overseasentitiesapi.model.factory;

import org.springframework.data.mongodb.core.mapping.Document;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

public interface Translator {
    SchemaVersion forSchemaVersion();
    void translate(Document submissionDocument);
}
