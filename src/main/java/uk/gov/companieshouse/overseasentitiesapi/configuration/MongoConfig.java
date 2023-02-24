package uk.gov.companieshouse.overseasentitiesapi.configuration;

import com.google.api.client.util.Value;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.overseasentitiesapi.converter.OverseasEntitySubmissionDaoConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMongoRepositories(basePackages = "uk.gov.companieshouse.overseasentitiesapi.repository")
// public class MongoConfig extends AbstractMongoClientConfiguration {
public class MongoConfig {
    private final List<Converter<?, ?>> converters = new ArrayList<>();

    @Value("${spring.data.mongodb.uri}")
    private String mongoConnectionUrl;

//    @Override
//    protected String getDatabaseName() {
//        return "transactions_overseas_entities_submissions";
//    }
//
//    @Override
//    public MongoClient mongoClient() {
//        final ConnectionString connectionString = new ConnectionString(mongoConnectionUrl);
//        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//        return MongoClients.create(mongoClientSettings);
//    }
//
//    @Override
//    public MongoCustomConversions customConversions() {
//        converters.add(new OverseasEntitySubmissionDaoConverter());
//        return new MongoCustomConversions(converters);
//    }

    @Bean
    public MongoClient mongo() {
        System.out.println("***** " + mongoConnectionUrl);
        // TODO - get mongo connection from config
        final ConnectionString connectionString = new ConnectionString("mongodb://mongo/transactions_overseas_entities_submissions");
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        System.out.println(" ********** " + "IN MONGO TEMPLATE" );
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), "transactions_overseas_entities_submissions");
        MappingMongoConverter conv = (MappingMongoConverter) mongoTemplate.getConverter();
        // tell mongodb to use the custom converters
        conv.setCustomConversions(mongoCustomConversions());
        conv.afterPropertiesSet();
        return mongoTemplate;
    }


    private MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                Arrays.asList(
                        new OverseasEntitySubmissionDaoConverter()
                        ));
    }
}
