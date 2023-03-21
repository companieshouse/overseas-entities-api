package uk.gov.companieshouse.overseasentitiesapi.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;


@ReadingConverter
public class OverseasEntitySubmissionDaoConverter implements Converter<Document, OverseasEntitySubmissionDao> {

    private final MongoConverter defaultMongoConverter;

    public OverseasEntitySubmissionDaoConverter(MongoConverter defaultMongoConverter) {
        this.defaultMongoConverter = defaultMongoConverter;
    }

    @Override
    public OverseasEntitySubmissionDao convert(final Document document) {

        System.out.println("\n\n**************** Document: \n\n\n");
        System.out.println(document.toString());

        OverseasEntitySubmissionDao overseasEntitySubmissionDao;


        overseasEntitySubmissionDao = defaultMongoConverter.read(OverseasEntitySubmissionDao.class, document);

        return overseasEntitySubmissionDao;
    }
}
