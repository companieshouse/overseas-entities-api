package uk.gov.companieshouse.overseasentitiesapi.converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;

@ReadingConverter
public class OverseasEntitySubmissionDaoConverter implements Converter<Document, OverseasEntitySubmissionDao> {

    @Override
    public OverseasEntitySubmissionDao convert(final Document document) {
//        final DBObject dbObject = new BasicDBObject();
//        dbObject.put("name", user.getName());
//        dbObject.put("age", user.getAge());
//        if (user.getEmailAddress() != null) {
//            final DBObject emailDbObject = new BasicDBObject();
//            emailDbObject.put("value", user.getEmailAddress().getValue());
//            dbObject.put("email", emailDbObject);
//        }
//        dbObject.removeField("_class");
//        return dbObject;

        System.out.println(" ********************* \n ********************** \n **************** ");
        System.out.println(document.toString());
        return new OverseasEntitySubmissionDao();
    }
}
