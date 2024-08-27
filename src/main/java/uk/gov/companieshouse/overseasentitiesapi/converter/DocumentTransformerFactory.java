package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DocumentTransformerFactory {

    @Autowired
    private List<DocumentTransformer> transformers;

    private final Map<SchemaVersion, DocumentTransformer> transformerMap = new EnumMap<>(SchemaVersion.class);


    @PostConstruct
    public void initialiseTransformerMap() {
        for (DocumentTransformer transformer : transformers) {
            transformerMap.put(transformer.forSchemaVersion(), transformer);
        }

        ApiLogger.info("Document transformer factory initialised with " + transformerMap.size() + " transformers");
    }

    public Optional<DocumentTransformer> getTransformer(SchemaVersion forSchemaVersion) {
        var transformer = transformerMap.get(forSchemaVersion);
        if (Objects.isNull(transformer)) {
            return Optional.empty();
        }
        return Optional.of(transformer);
    }
}
