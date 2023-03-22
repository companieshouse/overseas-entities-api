package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Version1_0TranslatorTest {

    private static final String ENTITY_NAME = "Test Name";

    private Version1_0Translator translator = new Version1_0Translator();

    @Test
    void testTranslation() {
        Document submissionDocument = new Document();
        Document entityDocument = new Document();

        entityDocument.put("name", ENTITY_NAME);
        submissionDocument.put("entity", entityDocument);

        translator.translate(submissionDocument);

        Document nameDocument = (Document) submissionDocument.get("entity_name");

        assertNotNull(nameDocument);
        assertEquals(ENTITY_NAME, nameDocument.get("name"));

        assertNull(((Document) submissionDocument.get("entity")).get("name"));
    }
}
