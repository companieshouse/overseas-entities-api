package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import uk.gov.companieshouse.overseasentitiesapi.converter.OverseasEntitySubmissionDaoConverter;

import java.util.List;

public class MongoConverters {

    private MongoConverters() { }

    public static MongoCustomConversions getMongoCustomConversions(MongoDatabaseFactory mongoDatabaseFactory) {
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
