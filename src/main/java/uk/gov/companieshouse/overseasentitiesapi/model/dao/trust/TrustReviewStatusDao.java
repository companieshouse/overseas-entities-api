package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

public class TrustReviewStatusDao {

    public static final String IN_REVIEW_FIELD = "in_review";

    public static final String REVIEWED_FORMER_BOS_FIELD = "reviewed_former_bos";

    public static final String REVIEWED_INDIVIDUALS_FIELD = "reviewed_individuals";

    public static final String REVIEWED_LEGAL_ENTITIES_FIELD = "reviewed_legal_entities";

    @Field(IN_REVIEW_FIELD)
    private Boolean inReview;

    @Field(REVIEWED_FORMER_BOS_FIELD)
    private Boolean reviewedFormerBOs;

    @Field(REVIEWED_INDIVIDUALS_FIELD)
    private Boolean reviewedIndividuals;

    @Field(REVIEWED_LEGAL_ENTITIES_FIELD)
    private Boolean reviewedLegalEntities;

    public Boolean getInReview() {
        return inReview;
    }

    public void setInReview(Boolean inReview) {
        this.inReview = inReview;
    }

    public Boolean getReviewedFormerBOs() {
        return reviewedFormerBOs;
    }

    public void setReviewedFormerBOs(Boolean reviewedFormerBOs) {
        this.reviewedFormerBOs = reviewedFormerBOs;
    }

    public Boolean getReviewedIndividuals() {
        return reviewedIndividuals;
    }

    public void setReviewedIndividuals(Boolean reviewedIndividuals) {
        this.reviewedIndividuals = reviewedIndividuals;
    }

    public Boolean getReviewedLegalEntities() {
        return reviewedLegalEntities;
    }

    public void setReviewedLegalEntities(Boolean reviewedLegalEntities) {
        this.reviewedLegalEntities = reviewedLegalEntities;
    }
}
