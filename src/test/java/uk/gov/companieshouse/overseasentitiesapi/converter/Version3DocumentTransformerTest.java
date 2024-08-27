package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Version3DocumentTransformerTest {

    private final Version3DocumentTransformer transformer = new Version3DocumentTransformer();

    @Test
    void testTransformation() {
        Document submissionDocument = new Document();

        transformer.transform(submissionDocument);

        assertEquals("3.0", submissionDocument.get("schema_version"));
    }
}