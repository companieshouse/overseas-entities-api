package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.Objects;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao.SCHEMA_VERSION_FIELD;

/**
 * Converts a Mongo (binary) JSON document into an instance of a <code>OverseasEntitySubmissionDao</code>.
 *
 * Achieved by using the default Mongo converter, but if the document is deemed to have been created using an
 * older version of the model structure it will first be transformed into a BSON document that represents the
 * current structure, ahead of conversion.
 */
@ReadingConverter
public class OverseasEntitySubmissionDaoConverter implements Converter<Document, OverseasEntitySubmissionDao> {

    private final MongoConverter defaultMongoConverter;

    private TransformerFactory transformerFactory;

    public OverseasEntitySubmissionDaoConverter(MongoConverter defaultMongoConverter, TransformerFactory transformerFactory) {
        this.defaultMongoConverter = defaultMongoConverter;
        this.transformerFactory = transformerFactory;
    }

    @Override
    public OverseasEntitySubmissionDao convert(final Document document) {

        SchemaVersion schemaVersion = getSchemaVersion(document);

        ApiLogger.info("Schema version is " + schemaVersion);

        Optional<SchemaVersionTransformer> transformer = transformerFactory.getTransformer(schemaVersion);

        // Note that a transformer may not be found - either because an older version of the model requires no
        // transformation or this document was saved with the latest version of the model structure
        if (transformer.isPresent()) {
            transformer.get().transform(document);
        }

        OverseasEntitySubmissionDao overseasEntitySubmissionDao = defaultMongoConverter.read(OverseasEntitySubmissionDao.class, document);

        return overseasEntitySubmissionDao;
    }

    private SchemaVersion getSchemaVersion(final Document document) {
        Object modelVersion = document.get(SCHEMA_VERSION_FIELD);

        if (Objects.nonNull(modelVersion)) {
            return SchemaVersion.fromString(modelVersion.toString());
        }

        /*
         * Infer model version as it is not present in older documents. This can be
         * achieved by examining where in the data model the entity name field is,
         * as this is the only field that has been changed between the versions <= 3.0
         */
        Object entityNameDocument = document.get("entity_name");

        if (Objects.isNull(entityNameDocument)) {             // Must be a version 1.0 model
            return SchemaVersion.VERSION_1_0;
        } else if (entityNameDocument instanceof String) {    // Must be a version 2.0 model
            return SchemaVersion.VERSION_2_0;
        } else {                                              // Assume it's a version 3.0 model
            return SchemaVersion.VERSION_3_0;
        }
    }
}
