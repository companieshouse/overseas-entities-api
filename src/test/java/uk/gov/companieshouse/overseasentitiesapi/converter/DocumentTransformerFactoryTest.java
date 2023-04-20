package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentTransformerFactoryTest {

    @Test
    void testTransformerFactoryWhenTransformerFound() {
        DocumentTransformerFactory transformerFactory = getDocumentTransformerFactory();
        Optional<DocumentTransformer> transformer = transformerFactory.getTransformer(SchemaVersion.VERSION_3_1);
        assertTrue(transformer.isPresent());
        assertEquals(SchemaVersion.VERSION_3_1, transformer.get().forSchemaVersion());
    }

    @Test
    void testTransformerFactoryWhenTransformerNotFound() {
        DocumentTransformerFactory transformerFactory = getDocumentTransformerFactory();
        Optional<DocumentTransformer> transformer = transformerFactory.getTransformer(SchemaVersion.VERSION_1_0);
        assertTrue(transformer.isEmpty());
    }

    // This approach for initialising the test instance of the DocumentTransformerFactory has been used as pipeline test
    // failures were encountered when using the JUnit @BeforeEach annotation to create the factory
    private DocumentTransformerFactory getDocumentTransformerFactory() {
        DocumentTransformerFactory transformerFactory = new DocumentTransformerFactory();
        List<DocumentTransformer> transformers = new ArrayList<>();
        transformers.add(new TestTransformer());
        ReflectionTestUtils.setField(transformerFactory, "transformers", transformers);
        transformerFactory.initialiseTransformerMap();

        return transformerFactory;
    }

    public class TestTransformer implements DocumentTransformer {

        @Override
        public SchemaVersion forSchemaVersion() {
            return SchemaVersion.VERSION_3_1;
        }

        @Override
        public void transform(Document submissionDocument) {
        }
    }
}
