package uk.gov.companieshouse.overseasentitiesapi.model.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranslatorFactory {

    @Autowired
    private List<Translator> translators;

    private static final Map<SchemaVersion, Translator> translatorMap = new HashMap<>();

    @PostConstruct
    public void initialiseTranslatorMap() {
        for (Translator translator : translators) {
            translatorMap.put(translator.forSchemaVersion(), translator);
        }
    }

    public static Translator getTranslator(SchemaVersion forSchemaVersion) throws ServiceException {
        Translator translator = translatorMap.get(forSchemaVersion);
        if (translator == null) {
            throw new ServiceException("Submission model translator not found for version " + forSchemaVersion);
        }
        return translator;
    }
}
