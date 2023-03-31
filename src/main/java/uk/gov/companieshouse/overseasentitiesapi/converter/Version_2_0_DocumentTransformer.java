package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

/**
 * A version 2.0 document has the entity name in an 'entity_name' field at the top level of the submission model and
 * not in any block. Transformation requires this field to be moved to a new, top-level 'entity_name' block.
 */
@Component
public class Version_2_0_DocumentTransformer implements DocumentTransformer {

    private static final String ENTITY_NAME_FIELD_AND_BLOCK_NAME = "entity_name";
    private static final String NEW_ENTITY_NAME_FIELD_NAME = "name";

    @Override
    public SchemaVersion forSchemaVersion() {
        return SchemaVersion.VERSION_2_0;
    }

    @Override
    public void transform(Document submissionDocument) {
        ApiLogger.info("Transforming a version 2.0 document...");
        String entityName = (String) submissionDocument.get(ENTITY_NAME_FIELD_AND_BLOCK_NAME);
        submissionDocument.remove(ENTITY_NAME_FIELD_AND_BLOCK_NAME);
        var entityNameDocument = new Document();
        entityNameDocument.put(NEW_ENTITY_NAME_FIELD_NAME, entityName);
        submissionDocument.put(ENTITY_NAME_FIELD_AND_BLOCK_NAME, entityNameDocument);
    }
}
