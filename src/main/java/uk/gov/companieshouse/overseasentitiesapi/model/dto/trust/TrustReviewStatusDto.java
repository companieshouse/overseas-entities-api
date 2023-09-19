package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrustReviewStatusDto {

    public static final String IN_REVIEW = "in_review";

    public static final String REVIEWED_FORMER_BOS = "reviewed_former_bos";

    public static final String REVIEWED_INDIVIDUALS = "reviewed_individuals";

    public static final String REVIEWED_LEGAL_ENTITIES = "reviewed_legal_entities";

    @JsonProperty(IN_REVIEW)
    private Boolean inReview;

    @JsonProperty(REVIEWED_FORMER_BOS)
    private Boolean reviewedFormerBOs;

    @JsonProperty(REVIEWED_INDIVIDUALS)
    private Boolean reviewedIndividuals;

    @JsonProperty(REVIEWED_LEGAL_ENTITIES)
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
