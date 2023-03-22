package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;

public class Version1_0Translator implements Translator {

    private static final String ENTITY_BLOCK_NAME = "entity";
    private static final String OLD_ENTITY_NAME_FIELD_NAME = "name";
    private static final String NEW_ENTITY_NAME_BLOCK_NAME = "name";
    private static final String NEW_ENTITY_NAME_FIELD_NAME = "entity_name";

    @Override
    public void translate(Document submissionDocument) {
        Document entityDocument = (Document) submissionDocument.get(ENTITY_BLOCK_NAME);
        String entityName = entityDocument.getString(OLD_ENTITY_NAME_FIELD_NAME);
        Document entityNameDocument = new Document();
        entityNameDocument.put(NEW_ENTITY_NAME_BLOCK_NAME, entityName);
        submissionDocument.put(NEW_ENTITY_NAME_FIELD_NAME, entityNameDocument);
        entityDocument.remove(OLD_ENTITY_NAME_FIELD_NAME);
    }
}
