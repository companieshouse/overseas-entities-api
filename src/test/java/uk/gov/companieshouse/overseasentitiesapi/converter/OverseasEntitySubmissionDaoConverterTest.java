package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import uk.gov.companieshouse.overseasentitiesapi.model.SchemaVersion;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.utils.TestUtils;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverseasEntitySubmissionDaoConverterTest {

    @InjectMocks
    private OverseasEntitySubmissionDaoConverter overseasEntitySubmissionDaoConverter;

    @Mock
    private MongoConverter defaultMongoConverter;

    @Mock
    private DocumentTransformerFactory transformerFactory;

    @Test
    void testDocumentConversion() {
        Document document = getDocument("overseas_entity_v_3_0.json");
        overseasEntitySubmissionDaoConverter.convert(document);
        verify(transformerFactory, times(1)).getTransformer(SchemaVersion.VERSION_3_0);
        verify(defaultMongoConverter, times(1)).read(OverseasEntitySubmissionDao.class, document);
    }

    private Document getDocument(String filename) {
        TestUtils testUtils = new TestUtils();
        File file = testUtils.getFile(filename);
        String jsonData = testUtils.readString(file);
        return Document.parse(jsonData);
    }
}
