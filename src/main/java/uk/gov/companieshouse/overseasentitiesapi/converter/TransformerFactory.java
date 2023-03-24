package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransformerFactory {

    @Autowired
    private List<Transformer> transformers;

    private static final Map<SchemaVersion, Transformer> transformerMap = new EnumMap<>(SchemaVersion.class);

    @PostConstruct
    public void initialiseTransformerMap() {
        for (Transformer transformer : transformers) {
            transformerMap.put(transformer.forSchemaVersion(), transformer);
        }
    }

    public Optional<Transformer> getTranformer(SchemaVersion forSchemaVersion) {
        var transformer = transformerMap.get(forSchemaVersion);
        if (Objects.isNull(transformer)) {
            return Optional.empty();
        }
        return Optional.of(transformer);
    }
}
