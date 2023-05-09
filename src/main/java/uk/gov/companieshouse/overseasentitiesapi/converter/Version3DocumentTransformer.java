package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import static uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao.SCHEMA_VERSION_FIELD;

/**
 * A version 3.0 document already has the entity name in an 'entity_name' block. Transformation simply requires adding
 * and setting the correct version number on the document.
 */
@Component
public class Version3DocumentTransformer implements DocumentTransformer {

    @Override
    public SchemaVersion forSchemaVersion() {
        return SchemaVersion.VERSION_3_0;
    }

    @Override
    public void transform(Document submissionDocument) {
        ApiLogger.info("Transforming a version 3.0 document...");

        // Structure does not need changing, apart from having to set the version number

        submissionDocument.put(SCHEMA_VERSION_FIELD, SchemaVersion.VERSION_3_0.getVersion());
    }
}
