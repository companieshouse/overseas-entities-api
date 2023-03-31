package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import uk.gov.companieshouse.overseasentitiesapi.converter.DocumentTransformerFactory;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.utils.TestUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.companieshouse.overseasentitiesapi.configuration.MongoConverters.getMongoCustomConversions;

/**
 * If any of the tests in this class start failing it's likely due to a DTO model structure change which is an indication
 * of potential issues for clients that still need to be able to work with Mongo records created using older versions of
 * the data model.
 *
 * If the data model structure has been modified, consideration will need to be given to how the web - and other -
 * clients of the OE API end-points will deal with older versions of the model structure and ultimately how older
 * versions of the model structure can be loaded into the newly changed DAO and DTO models. Changes will probably
 * need to be made to the Mongo custom 'converters'.
 *
 * Once those things have been considered and addressed, existing tests should pass again. Then, a file with the new JSON
 * model structure should be added to the test resources folder and a new test added to successfully read and process
 * the new, up-to-date DAO/DTO structure.
 */

@AutoConfigureDataMongo
@SpringBootTest
class DtoModelChangeTest {

    private static final String TEST_COLLECTION_NAME = "TestCollection";

    private static final String ENTITY_NAME = "ARMS DIRECT";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @Autowired
    private DocumentTransformerFactory transformerFactory;

    @BeforeEach
    void removeAllMongoRecords() {
        mongoTemplate.remove(new Query(), TEST_COLLECTION_NAME);
        mongoTemplate.dropCollection(TEST_COLLECTION_NAME);
        mongoTemplate.createCollection(TEST_COLLECTION_NAME);

        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        MappingContext mappingContext = converter.getMappingContext();

        // transformer factory argument instructs mongodb to use the custom converters
        converter.setCustomConversions(getMongoCustomConversions(mongoTemplate.getMongoDatabaseFactory(), transformerFactory, mappingContext));
        converter.afterPropertiesSet();
    }

    @ParameterizedTest
    @ValueSource(strings = {"overseas_entity_v_1_0.json", "overseas_entity_v_2_0.json",
                            "overseas_entity_v_3_0.json",  "overseas_entity_v_3_1.json" })
    void testCanGenerateDtoModelFromJsonVersion_1_0(String fileName) {

        loadJsonDataIntoMongo(fileName);

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        checkSubmissions(submissions);
    }

    private void checkSubmissions(List<OverseasEntitySubmissionDao> submissions) {
        assertEquals(1, submissions.size());

        // Note that not all model fields are checked here. Assertions are targeted at fields which are known to have
        // changed due to 'breaking' model changes. At present, that is only the 'entity name' field, which has moved
        // location in the model structure twice (to date). Checks on other fields may need adding in the future, however.

        OverseasEntitySubmissionDao dao = submissions.get(0);
        assertNotNull(dao);
        assertNotNull(dao.getEntityName());
        assertEquals(ENTITY_NAME, dao.getEntityName().getName());

        var dto = overseasEntityDtoDaoMapper.daoToDto(dao);

        assertNotNull(dto);
        assertNotNull(dto.getEntityName());
        assertEquals(ENTITY_NAME, dto.getEntityName().getName());
    }

    private void loadJsonDataIntoMongo(String filename) {
        TestUtils testUtils = new TestUtils();
        File file = testUtils.getFile(filename);
        String jsonData = testUtils.readString(file);

        mongoTemplate.insert(Document.parse(jsonData), TEST_COLLECTION_NAME);
    }
}
