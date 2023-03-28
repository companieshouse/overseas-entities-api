package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.converter.Version_2_0_Transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Version_2_0_TransformerTest {

    private static final String ENTITY_NAME = "Test Name";

    private Version_2_0_Transformer transformer = new Version_2_0_Transformer();

    @Test
    void testTransformation() {
        Document submissionDocument = new Document();

        submissionDocument.put("entity_name", ENTITY_NAME);

        transformer.transform(submissionDocument);

        Document nameDocument = (Document) submissionDocument.get("entity_name");

        assertNotNull(nameDocument);
        assertEquals(ENTITY_NAME, nameDocument.get("name"));
    }

}
