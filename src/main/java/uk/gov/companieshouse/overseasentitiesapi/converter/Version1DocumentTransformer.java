package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import static uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao.SCHEMA_VERSION_FIELD;

/**
 * A version 1.0 document has the entity name within a 'name' field in the 'entity' block. Transformation requires the
 * 'name' field (and its value) to be moved to a new, top-level 'entity_name' block and setting the correct version
 * number on the document.
 */
@Component
public class Version1DocumentTransformer implements DocumentTransformer {

    private static final String ENTITY_BLOCK_NAME = "entity";
    private static final String ENTITY_NAME_FIELD_NAME = "name";
    private static final String NEW_ENTITY_NAME_BLOCK_NAME = "entity_name";

    @Override
    public SchemaVersion forSchemaVersion() {
        return SchemaVersion.VERSION_1_0;
    }

    @Override
    public void transform(Document submissionDocument) {
        ApiLogger.info("Transforming a version 1.0 document...");

        var entityDocument = (Document) submissionDocument.get(ENTITY_BLOCK_NAME);
        var entityName = entityDocument.getString(ENTITY_NAME_FIELD_NAME);
        var entityNameDocument = new Document();
        entityNameDocument.put(ENTITY_NAME_FIELD_NAME, entityName);
        submissionDocument.put(NEW_ENTITY_NAME_BLOCK_NAME, entityNameDocument);
        entityDocument.remove(ENTITY_NAME_FIELD_NAME);

        submissionDocument.put(SCHEMA_VERSION_FIELD, SchemaVersion.VERSION_1_0.getVersion());
    }
}
