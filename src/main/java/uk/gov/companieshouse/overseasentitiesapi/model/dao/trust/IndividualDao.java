package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.IndividualType;

public class IndividualDao {
    @Field("type")
    private IndividualType type;

    @Field("forename")
    private String forename;

    @Field("other_forenames")
    private String otherForenames;

}
