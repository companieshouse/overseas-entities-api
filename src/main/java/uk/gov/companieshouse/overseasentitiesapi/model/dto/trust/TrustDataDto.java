package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrustDataDto {

    @JsonProperty("trust_name")
    private String trustName;

    @JsonProperty("trust_creation_date_day")
    private String trustCreateDateDay;

    @JsonProperty("trust_creation_date_month")
    private String trustCreateDateMonth;

    @JsonProperty("trust_creation_date_year")
    private String trustCreateDateYear;

    @JsonProperty("unable_to_obtain_all_trust_info")
    private String unableToObtainAllTrustInfo;

    public String getTrustName() {
        return trustName;
    }

    public void setTrustName(String trustName) {
        this.trustName = trustName;
    }

    public String getTrustCreateDateDay() {
        return trustCreateDateDay;
    }

    public void setTrustCreateDateDay(String trustCreateDateDay) {
        this.trustCreateDateDay = trustCreateDateDay;
    }

    public String getTrustCreationDateMonth() {
        return trustCreateDateMonth;
    }

    public void setTrustCreationDateMonth(String trustCreationDateMonth) {
        this.trustCreateDateMonth = trustCreationDateMonth;
    }

    public String getTrustCreationDateYear() {
        return trustCreateDateYear;
    }

    public void setTrustCreationDateYear(String trustCreationDateYear) {
        this.trustCreateDateYear = trustCreationDateYear;
    }

    public String getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(String unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }


}
