package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Component
public class Version_2_0_Transformer implements DocumentTransformer {
    private static final String ENTITY_NAME = "entity_name";
    private static final String NEW_ENTITY_NAME_FIELD_NAME = "name";

    @Override
    public SchemaVersion forSchemaVersion() {
        return SchemaVersion.VERSION_2_0;
    }

    @Override
    public void transform(Document submissionDocument) {
        ApiLogger.info("Transforming a version 2.0 document...");
        String entityField = (String) submissionDocument.get(ENTITY_NAME);
        submissionDocument.remove(ENTITY_NAME);
        Document entityNameDocument = new Document();
        entityNameDocument.put(NEW_ENTITY_NAME_FIELD_NAME, entityField);
        submissionDocument.put(ENTITY_NAME, entityNameDocument);
    }
}
