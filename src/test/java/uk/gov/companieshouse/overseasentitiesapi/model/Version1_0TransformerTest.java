package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.converter.Version1_0Transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Version1_0TransformerTest {

    private static final String ENTITY_NAME = "Test Name";

    private Version1_0Transformer transformer = new Version1_0Transformer();

    @Test
    void testTranslation() {
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
