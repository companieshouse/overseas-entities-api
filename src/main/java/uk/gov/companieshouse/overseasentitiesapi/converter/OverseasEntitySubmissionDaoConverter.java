package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.lang.NonNull;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import java.util.Objects;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao.SCHEMA_VERSION_FIELD;

/**
 * Converts a Mongo (binary) JSON document into an instance of a <code>OverseasEntitySubmissionDao</code>.
 * Achieved by using the default Mongo converter, but if the document is deemed to have been created using an
 * older version of the model structure it will first be transformed into a BSON document that represents the
 * current structure, ahead of conversion.
 */
@ReadingConverter
public class OverseasEntitySubmissionDaoConverter implements Converter<Document, OverseasEntitySubmissionDao> {

    private final MongoConverter defaultMongoConverter;

    private final DocumentTransformerFactory transformerFactory;

    public OverseasEntitySubmissionDaoConverter(MongoConverter defaultMongoConverter, DocumentTransformerFactory transformerFactory) {
        this.defaultMongoConverter = defaultMongoConverter;
        this.transformerFactory = transformerFactory;
    }

    @Override
    public OverseasEntitySubmissionDao convert(@NonNull final Document document) {

        var schemaVersion = getSchemaVersion(document);

        ApiLogger.info("Schema version is " + schemaVersion);

        Optional<DocumentTransformer> transformer = transformerFactory.getTransformer(schemaVersion);

        // Note that a transformer may not be found - either because an older version of the model requires no
        // transformation or this document was saved with the latest version of the model structure
        transformer.ifPresent(documentTransformer -> documentTransformer.transform(document));

        return defaultMongoConverter.read(OverseasEntitySubmissionDao.class, document);
    }

    private SchemaVersion getSchemaVersion(final Document document) {
        Object modelVersion = document.get(SCHEMA_VERSION_FIELD);

        if (Objects.nonNull(modelVersion)) {
            return SchemaVersion.fromString(modelVersion.toString());
        }

        /*
         * Major version changes can be detected by examining where the 'entity name' was historically stored.
         * Its location in the document can be used to determine the schema version.
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
