package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.converter.Version_1_0_DocumentTransformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Version_1_0_DocumentTransformerTest {

    private static final String ENTITY_NAME = "Test Name";

    private Version_1_0_DocumentTransformer transformer = new Version_1_0_DocumentTransformer();

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
    }
}
