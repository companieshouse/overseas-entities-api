package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.companieshouse.overseasentitiesapi.converter.OverseasEntitySubmissionDaoConverter;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapperImpl;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.utils.TestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * If any of these tests start failing it's likely due to a DTO model structure change which is an indication of potential
 * issues for clients that still need to be able to work with Mongo records created using older versions of the data model.
 *
 * If the data model structure has been modified, consideration will need to be given to how the web - and other -
 * clients of the OE API end-points will deal with older versions of the model structure and ultimately how older
 * versions of the model structure can be loaded into the newly changed DAO and DTO models. Changes will probably
 * need to be made to the 'converters'.
 *
 * Once those things have been considered and addressed, existing tests should pass again. Then, a file with the new JSON
 * model structure should be added to the test resources folder and a new test added to successfully read and process
 * the new DTO structure.
 */
@DataMongoTest
@ExtendWith({SpringExtension.class})
class DtoModelChangeTest {

    private static final String TEST_COLLECTION_NAME = "TestCollection";

    private static final String ENTITY_NAME = "ARMS DIRECT";

    @Autowired
    private MongoTemplate mongoTemplate;

    // TODO Auto-wire this somehow...

//    @Autowired
    private OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper = new OverseasEntityDtoDaoMapperImpl();

    @BeforeEach
    void removeAllMongoRecords() {
        mongoTemplate.remove(new Query(), TEST_COLLECTION_NAME);
        mongoTemplate.dropCollection(TEST_COLLECTION_NAME);
        mongoTemplate.createCollection(TEST_COLLECTION_NAME);

        // TODO Comment these lines out for converter not to kick-in and (two) tests to fail:
        MappingMongoConverter conv = (MappingMongoConverter) mongoTemplate.getConverter();
        // tell mongodb to use the custom converters
        conv.setCustomConversions(mongoCustomConversions());
        conv.afterPropertiesSet();
    }

    private static final MongoConverter getDefaultMongoConverter(MongoDatabaseFactory factory) {
        System.out.println("\n\n**** 1");

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.afterPropertiesSet();
        System.out.println("\n\n**** 2");

        return converter;
    }

    private MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                Arrays.asList(
                        new OverseasEntitySubmissionDaoConverter(getDefaultMongoConverter(mongoTemplate.getMongoDatabaseFactory()))
                ));
    }

    @Test
    void testCanGenerateCorrectlyPopulatedDtoModelFromVersion1_0Json() {

        loadJsonDataIntoMongo("overseas_entity_v_1_0.json");

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        checkSubmissions(submissions);
    }

    @Test
    void testCanGenerateCorrectlyPopulatedDtoModelFromVersion2_0Json() {

        loadJsonDataIntoMongo("overseas_entity_v_2_0.json");

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        checkSubmissions(submissions);
    }

    @Test
    void testCanGenerateCorrectlyPopulatedDtoModelFromVersion3_0Json() throws Exception {

        loadJsonDataIntoMongo("overseas_entity_v_3_0.json");

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        checkSubmissions(submissions);
    }

    private void checkSubmissions(List<OverseasEntitySubmissionDao> submissions) {
        assertEquals(1, submissions.size());

        OverseasEntitySubmissionDao dao = submissions.get(0);
        assertNotNull(dao);
        assertNotNull(dao.getEntityName());
        assertEquals(ENTITY_NAME, dao.getEntityName().getName());

        assertEquals("Vlad Jones", dao.getPresenter().getFullName());

        var dto = overseasEntityDtoDaoMapper.daoToDto(dao);

        assertNotNull(dto);
        assertEquals(ENTITY_NAME, dto.getEntityName().getName());

        assertEquals("Vlad Jones", dto.getPresenter().getFullName());
    }

    private void loadJsonDataIntoMongo(String filename) {
        TestUtils testUtils = new TestUtils();
        File file = testUtils.getFile(filename);
        String jsonData = testUtils.readString(file);

        mongoTemplate.insert(Document.parse(jsonData), TEST_COLLECTION_NAME);
    }
}
