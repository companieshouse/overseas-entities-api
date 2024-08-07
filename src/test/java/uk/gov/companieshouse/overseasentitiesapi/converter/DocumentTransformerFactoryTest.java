package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentTransformerFactoryTest {

    private DocumentTransformerFactory transformerFactory;

    @BeforeEach
    void initialise() {
        transformerFactory = new DocumentTransformerFactory();
        List<DocumentTransformer> transformers = new ArrayList<>();
        transformers.add(new TestTransformer());
        ReflectionTestUtils.setField(transformerFactory, "transformers", transformers);
        transformerFactory.initialiseTransformerMap();
    }

    @Test
    void testTransformerFactoryWhenTransformerFound() {
        Optional<DocumentTransformer> transformer = transformerFactory.getTransformer(SchemaVersion.VERSION_3_1);
        assertTrue(transformer.isPresent());
        assertEquals(SchemaVersion.VERSION_3_1, transformer.get().forSchemaVersion());
    }

    @Test
    void testTransformerFactoryWhenTransformerNotFound() {
        Optional<DocumentTransformer> transformer = transformerFactory.getTransformer(SchemaVersion.VERSION_1_0);
        assertTrue(transformer.isEmpty());
    }

    public static class TestTransformer implements DocumentTransformer {

        @Override
        public SchemaVersion forSchemaVersion() {
            return SchemaVersion.VERSION_3_1;
        }

        @Override
        public void transform(Document submissionDocument) {
        }
    }
}
