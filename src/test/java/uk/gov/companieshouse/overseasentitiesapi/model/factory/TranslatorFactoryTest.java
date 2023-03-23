package uk.gov.companieshouse.overseasentitiesapi.model.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslatorFactoryTest {

    private TranslatorFactory translatorFactory;

    @BeforeEach
    void initialise() {
        translatorFactory = new TranslatorFactory();
        List<Translator> translators = new ArrayList<>();
        translators.add(new TestTranslator());
        ReflectionTestUtils.setField(translatorFactory, "translators", translators);
    }

    @Test
    void testTranslatorFactory() throws ServiceException {
        translatorFactory.initialiseTranslatorMap();
        Translator translator = TranslatorFactory.getTranslator(SchemaVersion.VERSION_3_1);
        translator.translate(null);
        assertEquals(SchemaVersion.VERSION_3_1, translator.forSchemaVersion());
        assertEquals("translation occurred", ((TestTranslator)translator).getTestValue());
    }
}
