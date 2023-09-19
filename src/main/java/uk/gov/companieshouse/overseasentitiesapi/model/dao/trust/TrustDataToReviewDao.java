package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

public class TrustDataToReviewDao extends TrustDataDao {
    @Field("review_status")
    private TrustReviewStatusDao reviewStatus;

    public TrustReviewStatusDao getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(TrustReviewStatusDao reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
