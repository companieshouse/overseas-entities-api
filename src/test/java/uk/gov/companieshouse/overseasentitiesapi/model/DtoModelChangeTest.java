package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.TestUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.companieshouse.overseasentitiesapi.configuration.MongoConverters.getMongoCustomConversions;

/**
 * If any of the tests in this class start failing it's likely due to a DTO model structure change which is an indication
 * of potential - current or future - issues for clients that still need to be able to work with Mongo records created
 * using older versions of the data model.
 *
 * If the data model structure has been modified, consideration needs to be given to how the web - and other -
 * clients of the OE API end-points will deal with older versions of the model structure and ultimately how older
 * versions of the model structure can be loaded into the newly changed DAO and DTO models. Among other things, a new,
 * Mongo, custom 'transformer' class may need to be written.
 *
 * If a non-breaking change to the model has been made - e.g. a new data field has simply been added to a DTO and DAO -
 * the 'current' test JSON file needs updating to avoid a null check failure (this essentially future-proofs the test,
 * which will potentially fail if the datatype of the newly added field ever changes).
 *
 * If a breaking change to the model has been made - e.g. a data field has changed its type from String to a custom POJO -
 * after a new 'transformer' has been implemented and a new <code>SchemaVersion</> enum value added, a file with the new
 * JSON model structure should be added to the test resources folder and the <code>OLD_JSON_MODEL_FILENAMES</code> and
 * <code>CURRENT_JSON_MODEL_FILENAME</code> constants updated, in order that the new, up-to-date DAO/DTO structure is
 * fully tested.
 *
 * Finally, note the use of Spring profiles, to ensure that the (default) <code>MongoConfig</code> configuration is not
 * loaded during this test (as the specific in-memory Mongo DB config needs to be picked up).
 */
@AutoConfigureDataMongo
@SpringBootTest
@ActiveProfiles("test")
class DtoModelChangeTest {

    private static final Logger LOGGER = Logger.getLogger(DtoModelChangeTest.class.getName());

    private static final String[] HISTORICAL_JSON_MODEL_FILENAMES = {
            "overseas_entity_v_1_0.json",
            "overseas_entity_v_2_0.json",
            "overseas_entity_v_3_0.json",
            "overseas_entity_v_3_1.json" };

    /**
     * If the DTO model changes and it is a 'breaking change', the current filename must be added to the
     * <code>HISTORICAL_JSON_MODEL_FILENAMES</code> list and a new test JSON file will need to be created and then
     * referenced here.
     */
    private static final String CURRENT_JSON_MODEL_FILENAME = "overseas_entity_v_3_2.json";

    /**
     * Allows the list to be used as input to a JUnit parameterised test (see below).
     */
    private static List<String> getHistoricalJsonModelFilenames() {
        return Arrays.asList(HISTORICAL_JSON_MODEL_FILENAMES);
    }

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

        // The transformer factory argument instructs mongodb to use the custom converters
        converter.setCustomConversions(getMongoCustomConversions(mongoTemplate.getMongoDatabaseFactory(), transformerFactory, mappingContext));
        converter.afterPropertiesSet();
    }

    @ParameterizedTest
    @MethodSource("getHistoricalJsonModelFilenames")
    void testCanGenerateDtoModelFromAllHistoricalJsonVersions(String fileName) {

        loadJsonDataIntoMongo(fileName);

        List<OverseasEntitySubmissionDao> submissions = mongoTemplate.findAll(OverseasEntitySubmissionDao.class, TEST_COLLECTION_NAME);

        checkSubmissions(submissions);
    }

    @Test
    void testCanGenerateDtoModelFromCurrentJsonVersion() {

        loadJsonDataIntoMongo(CURRENT_JSON_MODEL_FILENAME);

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
        String className = object.getClass().getName();
        LOGGER.info("Check for nulls in " + className + ":");

        Field[] allFields = object.getClass().getDeclaredFields();

        Arrays.stream(allFields).forEach(field -> {
            field.setAccessible(true);

            try {
                if (!Modifier.isStatic(field.getModifiers())) {
                    String fieldName = field.getName();

                    // If a field is present in the DTO model but not the DAO model, then it won't be saved
                    // to - or present in - Mongo and therefore doesn't need to be checked
                    if (isDtoFieldPresentInDaoModelOrMongo(className, fieldName)) {
                        LOGGER.info("Checking: " + fieldName);

                        Object nestedDtoObject = field.get(object);

                        if (nestedDtoObject == null) {
                            fail("Unexpected NULL value for field '" + fieldName + "'. This indicates that the DTO/DAO structure "
                                    + "has been updated without the new fields being added to the latest version of the test JSON file");
                        } else if (field.getClass().isPrimitive()
                                // Unlikely, but in the future, other object types might need to be added here, e.g. Integer
                                || nestedDtoObject instanceof String
                                || nestedDtoObject instanceof Boolean
                                || nestedDtoObject instanceof LocalDate
                                // Don't recursively inspect any lists or maps that just contain simple collections of strings
                                || isAListOrMapOfStrings(nestedDtoObject)) {
                            LOGGER.info("Value = " + nestedDtoObject);
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

    private boolean isDtoFieldPresentInDaoModelOrMongo(String className, String fieldName) {
        // These fields are valid exclusions from the null checks as there are no corresponding fields in the DAOs. The
        // DTO fields are only populated when the FilingsService is building a data structure to send to CHIPS, so not
        // related to JSON data being read from Mongo
        return !((className.equalsIgnoreCase(BeneficialOwnerIndividualDto.class.getName()) && fieldName.equals("trustData"))
                || (className.equalsIgnoreCase(BeneficialOwnerCorporateDto.class.getName()) && fieldName.startsWith("trustData"))
                || (className.equalsIgnoreCase(TrustIndividualDto.class.getName()) && fieldName.startsWith("saAddress"))
                || (className.equalsIgnoreCase(TrustCorporateDto.class.getName()) && fieldName.startsWith("saAddress"))
                || (className.equalsIgnoreCase(TrustIndividualDto.class.getName()) && fieldName.startsWith("uraAddress"))
                || (className.equalsIgnoreCase(TrustCorporateDto.class.getName()) && fieldName.startsWith("roAddress")));
    }

    private void loadJsonDataIntoMongo(String filename) {
        TestUtils testUtils = new TestUtils();
        File file = testUtils.getFile(filename);
        String jsonData = testUtils.readString(file);

        mongoTemplate.insert(Document.parse(jsonData), TEST_COLLECTION_NAME);
    }
}
