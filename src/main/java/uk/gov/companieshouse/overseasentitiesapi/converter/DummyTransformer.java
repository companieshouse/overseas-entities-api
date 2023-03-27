package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

/**
 * TODO Remove this transformer when one or more concrete transformers have been added to the API service. Only present
 *      to avoid a Spring bean wiring error on start-up, because no candidate transformers are found for the factory
 */
@Component
public class DummyTransformer implements Transformer {

    @Override
    public SchemaVersion forSchemaVersion() {
        return SchemaVersion.VERSION_3_1;
    }

    @Override
    public void transform(Document submissionDocument) {
        ApiLogger.info("\n\n !!! transforming !!! \n\n");
    }
}
