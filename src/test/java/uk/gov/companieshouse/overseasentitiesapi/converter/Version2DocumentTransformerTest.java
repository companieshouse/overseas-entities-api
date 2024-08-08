package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Version2DocumentTransformerTest {

    private static final String ENTITY_NAME = "Test Name";

    private final Version2DocumentTransformer transformer = new Version2DocumentTransformer();

    @Test
    void testTransformation() {
        Document submissionDocument = new Document();

        submissionDocument.put("entity_name", ENTITY_NAME);

        transformer.transform(submissionDocument);

        Document nameDocument = (Document) submissionDocument.get("entity_name");

        assertNotNull(nameDocument);
        assertEquals(ENTITY_NAME, nameDocument.get("name"));

        assertEquals("2.0", submissionDocument.get("schema_version"));
    }
}
