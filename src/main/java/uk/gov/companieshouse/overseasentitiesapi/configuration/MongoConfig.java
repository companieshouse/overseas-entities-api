package uk.gov.companieshouse.overseasentitiesapi.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.overseasentitiesapi.converter.DocumentTransformerFactory;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.configuration.MongoConverters.getMongoCustomConversions;

@Configuration
@EnableMongoRepositories(basePackages = "uk.gov.companieshouse.overseasentitiesapi.repository")
@Profile("default")
public class MongoConfig {

    private static final String MONGO_DB_URI_PROPERTY = "spring.data.mongodb.uri";
    private static final String MONGO_DB_NAME_PROPERTY = "overseas.entities.mongodb.dbname";

    @Bean
    public MongoTemplate mongoTemplate(@NotNull Environment environment, DocumentTransformerFactory transformerFactory) {
        String mongoDbName = environment.getProperty(MONGO_DB_NAME_PROPERTY);
        ApiLogger.info(String.format("Configuring mongoTemplate bean with mongo db name %s,", mongoDbName));
        var mongoTemplate = new MongoTemplate(mongoClient(environment), Objects.requireNonNull(mongoDbName));
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        // tell mongodb to use the custom converters
        var mongoCustomConversions = getMongoCustomConversions(mongoTemplate.getMongoDatabaseFactory(), transformerFactory, converter.getMappingContext());
        converter.setCustomConversions(mongoCustomConversions);
        converter.afterPropertiesSet();
        return mongoTemplate;
    }

    private MongoClient mongoClient(@NotNull Environment environment) {
        String mongoDbUri = environment.getProperty(MONGO_DB_URI_PROPERTY);
        ApiLogger.info(String.format("Configuring mongoClient bean with connection string %s", mongoDbUri));
        final var connectionString = new ConnectionString(Objects.requireNonNull(mongoDbUri));
        final var mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }
}
