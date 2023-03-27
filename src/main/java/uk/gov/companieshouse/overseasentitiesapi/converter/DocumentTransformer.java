package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

/**
 * Implemented by model transformers identified by their version.
 */
public interface DocumentTransformer {

    /**
     * @return The schema version that this transformer is intended for
     */
    SchemaVersion forSchemaVersion();

    /**
     * Transform the supplied document into the current version of the model
     *
     * @param submissionDocument containing the bson data to be transformed
     */
    void transform(Document submissionDocument);
}
