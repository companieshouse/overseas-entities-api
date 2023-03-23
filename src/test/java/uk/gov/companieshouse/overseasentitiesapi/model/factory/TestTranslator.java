package uk.gov.companieshouse.overseasentitiesapi.model.factory;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

@Component
public class TestTranslator implements Translator {

    private String testValue;

    @Override
    public SchemaVersion forSchemaVersion() {
        return SchemaVersion.VERSION_3_1;
    }

    @Override
    public void translate(Document submissionDocument) {
        testValue = "translation occurred";
    }

    public String getTestValue() {
        return testValue;
    }
}
