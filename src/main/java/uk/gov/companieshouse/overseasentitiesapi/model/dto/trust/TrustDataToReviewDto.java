package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class TrustDataToReviewDto extends TrustDataDto {

    public static final String REVIEW_STATUS_FIELD = "review_status";

    @JsonInclude(NON_NULL)
    @JsonProperty(REVIEW_STATUS_FIELD)
    private TrustReviewStatusDto reviewStatus;

    public TrustReviewStatusDto getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(TrustReviewStatusDto reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
