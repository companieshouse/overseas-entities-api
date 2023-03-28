package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

/**
 * Interface to be implemented by model transformers.
 */
public interface Transformer {

    /**
     * @return The schema version that this transformer is intended for
     */
    SchemaVersion forSchemaVersion();

    /**
     * Transform the supplied document into the current version of the model
     *
     * @param submissionDocument
     */
    void transform(Document submissionDocument);
}
