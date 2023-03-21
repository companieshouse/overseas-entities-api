package uk.gov.companieshouse.overseasentitiesapi.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.overseasentitiesapi.converter.OverseasEntitySubmissionDaoConverter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableMongoRepositories(basePackages = "uk.gov.companieshouse.overseasentitiesapi.repository")
public class MongoConfig {

    private static final String MONGO_DB_URI_PROPERTY = "spring.data.mongodb.uri";
    private static final String MONGO_DB_NAME_PROPERTY = "overseas.entities.mongodb.dbname";

    @Bean
    public MongoClient mongoClient(@NotNull Environment environment) {
        final ConnectionString connectionString = new ConnectionString(
                Objects.requireNonNull(environment.getProperty(MONGO_DB_URI_PROPERTY)));
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate(@NotNull Environment environment) {
        MongoTemplate mongoTemplate = new MongoTemplate(
                mongoClient(environment),
                Objects.requireNonNull(environment.getProperty(MONGO_DB_NAME_PROPERTY)));
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        // tell mongodb to use the custom converters
        converter.setCustomConversions(mongoCustomConversions(mongoTemplate.getMongoDatabaseFactory()));
        converter.afterPropertiesSet();
        return mongoTemplate;
    }

    private MongoCustomConversions mongoCustomConversions(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoCustomConversions(
                List.of(
                        new OverseasEntitySubmissionDaoConverter(getDefaultMongoConverter(mongoDatabaseFactory))
                ));
    }

    private static MongoConverter getDefaultMongoConverter(MongoDatabaseFactory factory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.afterPropertiesSet();
        return converter;
    }
}