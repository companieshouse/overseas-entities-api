package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import uk.gov.companieshouse.overseasentitiesapi.converter.DocumentTransformerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoConfigTest {

    @Mock
    private Environment environment;

    @Mock
    private DocumentTransformerFactory documentTransformerFactory;


    @Test
    void testGettingMongoTemplateBean() {
        when(environment.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://mongo/test");
        when(environment.getProperty("overseas.entities.mongodb.dbname")).thenReturn("db-name");

        MongoConfig mongoConfig = new MongoConfig();
        MongoTemplate mongoTemplate = mongoConfig.mongoTemplate(environment, documentTransformerFactory);

        assertNotNull(mongoTemplate);
    }
}
