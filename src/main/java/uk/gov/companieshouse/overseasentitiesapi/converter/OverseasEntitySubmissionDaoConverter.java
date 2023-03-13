package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;

import java.util.Objects;

@ReadingConverter
public class OverseasEntitySubmissionDaoConverter implements Converter<Document, OverseasEntitySubmissionDao> {

    private MongoConverter defaultMongoConverter;

    public OverseasEntitySubmissionDaoConverter(MongoConverter defaultMongoConverter) {
        this.defaultMongoConverter = defaultMongoConverter;
    }

    @Override
    public OverseasEntitySubmissionDao convert(final Document document) {

        System.out.println("\n\n**************** Document: \n\n\n");
        System.out.println(document.toString());

        OverseasEntitySubmissionDao overseasEntitySubmissionDao;
        String entityName;

        final String modelVersion = getModelVersionOfDocument(document);

        System.out.println("Model version is: " + modelVersion);

        switch (modelVersion) {
            case "1.0":
                Document entityDocument = (Document) document.get("entity");
                entityName = entityDocument.getString("name");
                Document entityNameDocument = new Document();
                entityNameDocument.put("name", entityName);
                document.put("entity_name", entityNameDocument);
                break;
            case "2.0":
                entityName = (String) document.get("entity_name");
                entityNameDocument = new Document();
                entityNameDocument.put("name", entityName);
                document.put("entity_name", entityNameDocument);
                break;
            case "3.0":
            case "3.1":     // Essentially the same as a '3.0' model - only change was to introduce the 'schema_version' field
                // Nothing to do - this is the most up-to-date version of the document
                break;
            default:
                throw new UnsupportedOperationException("Model version not recognised");
        }

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        objectMapper.registerModule(new JavaTimeModule());
//        try {
//            overseasEntitySubmissionDao = objectMapper.readValue(document.toJson(), OverseasEntitySubmissionDao.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        overseasEntitySubmissionDao = defaultMongoConverter.read(OverseasEntitySubmissionDao.class, document);

        return overseasEntitySubmissionDao;
    }


    private String getModelVersionOfDocument(Document document) {
        Object modelVersion = document.get("schema_version");

        if (Objects.nonNull(modelVersion)) {
            return (String) modelVersion;
        }

        // Infer model version as not present in older documents...

        Object entityNameDocument = document.get("entity_name");

        if (Objects.isNull(entityNameDocument)) {             // Must be a version 1.0 model
            return "1.0";
        } else if (entityNameDocument instanceof String) {    // Must be a version 2.0 model
            return "2.0";
        } else {                                              // Assume it's a version 3.0 model
            return "3.0";
        }
    }
}

