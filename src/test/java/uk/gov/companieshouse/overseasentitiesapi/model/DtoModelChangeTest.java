package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.companieshouse.overseasentitiesapi.converter.DocumentTransformerFactory;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.TestUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
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
 *
 * Note the use of Spring profiles, to ensure that the (default) <code>MongoConfig</code> configuration is not loaded
 * during this test (as the specific in-memory Mongo DB config needs to be picked up).
 */
@AutoConfigureDataMongo
@SpringBootTest
@ActiveProfiles("test")
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
                            "overseas_entity_v_3_0.json" })
    void testCanGenerateDtoModelFromAllHistoricalJsonVersions(String fileName) {

        loadJsonDataIntoMongo(fileName);

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        checkSubmissions(submissions);
    }

    @Test
    void testCanGenerateDtoModelFromCurrentJsonVersion() {

        loadJsonDataIntoMongo("overseas_entity_v_3_1.json");

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        OverseasEntitySubmissionDto dto = checkSubmissions(submissions);

        // Only do this for the most up-to-date JSON example, as others may well be missing fields that have subsequently
        // been added to the DTO model, which will result in null check failures
        checkNoDtoModelFieldsAreNull(dto);
    }

    private OverseasEntitySubmissionDto checkSubmissions(List<OverseasEntitySubmissionDao> submissions) {
        assertEquals(1, submissions.size());

        // Note that not all model fields are checked here. Assertions are targeted at fields which are known to have
        // changed due to 'breaking' model changes. At present, that is only the 'entity name' field, which has moved
        // location in the model structure twice (to date).
        //
        // Not null checks on other fields are run for the current (up-to-date) JSON example using the
        // checkNoDtoModelFieldsAreNull() method.

        OverseasEntitySubmissionDao dao = submissions.get(0);
        assertNotNull(dao);
        assertNotNull(dao.getEntityName());
        assertEquals(ENTITY_NAME, dao.getEntityName().getName());

        var dto = overseasEntityDtoDaoMapper.daoToDto(dao);

        assertNotNull(dto);
        assertNotNull(dto.getEntityName());
        assertEquals(ENTITY_NAME, dto.getEntityName().getName());

        return dto;
    }

    /**
     * This method is called recursively, in order to traverse the entire DTO model structure and check all fields.
     *
     * @param object The DTO object to check. If the DTO itself contains other DTOs, they will also be checked.
     */
    private void checkNoDtoModelFieldsAreNull(Object object) {
        // TODO Change all System.outs to logger calls
        String className = object.getClass().getName();
        System.out.println("\nCheck for nulls in " + className + ":\n");

        Field[] allFields = object.getClass().getDeclaredFields();

        Arrays.stream(allFields).forEach(field -> {
            field.setAccessible(true);

            try {
                if (!Modifier.isStatic(field.getModifiers())) {

                    // TODO The 'ignoreForNow' method will go, when current JSON file has been updated to have all fields present in it
                    String fieldName = field.getName();
                    if (!ignoreForNow(className, fieldName)) {
                        System.out.println("Checking:" + fieldName);

                        Object nestedDtoObject = field.get(object);

                        if (nestedDtoObject == null) {
                            fail("Unexpected null value for field '" + fieldName + "'");
                        } else if (field.getClass().isPrimitive() || nestedDtoObject instanceof String
                                // TODO In the future, other object types might need to be added here, e.g. Integer
                                //      (not sure this would occur, most JSON values are stored as strings...)
                                || nestedDtoObject instanceof Boolean || nestedDtoObject instanceof LocalDate
                                // Don't recursively inspect any lists or maps that just contain simple collections of strings
                                || isAListOrMapOfStrings(nestedDtoObject)) {
                            System.out.println("Value = " + nestedDtoObject);
                        } else {
                            if (nestedDtoObject instanceof Map) {
                                ((Map) nestedDtoObject).forEach((key, value) -> checkNoDtoModelFieldsAreNull(value));
                            } else if (nestedDtoObject instanceof List) {
                                ((List) nestedDtoObject).forEach((value) -> checkNoDtoModelFieldsAreNull(value));
                            } else {
                                checkNoDtoModelFieldsAreNull(nestedDtoObject);
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private boolean isAListOrMapOfStrings(Object dto) {
        if (dto instanceof List) {
            List list = (List) dto;
            return !list.isEmpty() && list.get(0) instanceof String;
        } else if (dto instanceof Map) {
            Map map = (Map) dto;
            return !map.isEmpty() && map.get(map.keySet().iterator().next()) instanceof String;
        }

        return false;
    }

    private boolean ignoreForNow(String className, String fieldName) {
        // TODO Set values for these fields in the 'overseas_entity_v_3_1.json'
        return
                (className.equalsIgnoreCase("uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDTO") && fieldName.startsWith("saAddress"))
                || (className.equalsIgnoreCase("uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto") && fieldName.startsWith("saAddress"))
                || (className.equalsIgnoreCase("uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDTO") && fieldName.startsWith("uraAddress"))
                || (className.equalsIgnoreCase("uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto") && fieldName.startsWith("roAddress"))

                // TODO Having looked into this a bit more, I now think this is a valid exclusion as there is no corresponding
                //      'trustData' field on the DAO. The DTO field is only populated by the FilingsService, so not related
                //      to data coming from Mongo/JSON (and if its datatype ever changed that would presumably be picked up
                //      by a compile-time failure
                || fieldName.equals("trustData");
    }

    private void loadJsonDataIntoMongo(String filename) {
        TestUtils testUtils = new TestUtils();
        File file = testUtils.getFile(filename);
        String jsonData = testUtils.readString(file);

        mongoTemplate.insert(Document.parse(jsonData), TEST_COLLECTION_NAME);
    }
}
