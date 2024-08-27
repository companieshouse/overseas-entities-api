package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class Version1DocumentTransformerTest {

    private static final String ENTITY_NAME = "Test Name";

    private final Version1DocumentTransformer transformer = new Version1DocumentTransformer();

    @Test
    void testTransformation() {
        Document submissionDocument = new Document();
        Document entityDocument = new Document();

        entityDocument.put("name", ENTITY_NAME);
        submissionDocument.put("entity", entityDocument);

        transformer.transform(submissionDocument);

        Document nameDocument = (Document) submissionDocument.get("entity_name");

        assertNotNull(nameDocument);
        assertEquals(ENTITY_NAME, nameDocument.get("name"));

        assertNull(((Document) submissionDocument.get("entity")).get("name"));

        assertEquals("1.0", submissionDocument.get("schema_version"));
    }
}
