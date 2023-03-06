package uk.gov.companieshouse.overseasentitiesapi.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;

import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.converter.OverseasEntitySubmissionDaoConverter.ModelVersion.*;

@ReadingConverter
public class OverseasEntitySubmissionDaoConverter implements Converter<Document, OverseasEntitySubmissionDao> {

    @Override
    public OverseasEntitySubmissionDao convert(final Document document) {

        System.out.println("\n\n **************** Document: \n\n\n");
        System.out.println(document.toString());

        OverseasEntitySubmissionDao overseasEntitySubmissionDao;
        String entityName;

        final ModelVersion modelVersion = getModelVersionOfDocument(document);

        switch (modelVersion) {
            case v1_0:
                Document entityDocument = (Document) document.get("entity");
                entityName = entityDocument.getString("name");
                Document entityNameDocument = new Document();
                entityNameDocument.put("name", entityName);
                document.put("entity_name", entityNameDocument);
                break;
            case v2_0:
                entityName = (String) document.get("entity_name");
                entityNameDocument = new Document();
                entityNameDocument.put("name", entityName);
                document.put("entity_name", entityNameDocument);
                break;
            case v3_0:
                // Nothing to do - this is the most up-to-date version of the document
                break;
            default:
                // Can't really happen right now, but could if we start persisting a schema version field in the document
                throw new UnsupportedOperationException("Model version not recognised");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.registerModule(new JavaTimeModule());
        try {
            overseasEntitySubmissionDao = objectMapper.readValue(document.toJson(), OverseasEntitySubmissionDao.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Also tried using Gson and getting the vanilla converter but no joy...

        return overseasEntitySubmissionDao;
    }

    private ModelVersion getModelVersionOfDocument(Document document) {
        Object entityNameDocument = document.get("entity_name");

        // Infer model version as not present in older documents...
        if (Objects.isNull(entityNameDocument)) {             // Must be a version 1.0 model
            return v1_0;
        } else if (entityNameDocument instanceof String) {    // Must be a version 2.0 model
            return v2_0;
        } else {                                              // Assume it's a version 3.0 model
            return v3_0;
        }
    }

    enum ModelVersion {
        v1_0,
        v2_0,
        v3_0;
    }
}

