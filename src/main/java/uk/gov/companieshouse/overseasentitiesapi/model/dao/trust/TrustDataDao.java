package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

public class TrustDataDao {

    @Field("trust_name")
    private String trustName;

    @Field("trust_creation_date_day")
    private String trustCreateDateDay;

    @Field("trust_creation_date_month")
    private String trustCreateDateMonth;

    @Field("trust_creation_date_year")
    private String trustCreateDateYear;

    @Field("unable_to_obtain_all_trust_info")
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

    public String getTrustCreateDateMonth() {
        return trustCreateDateMonth;
    }

    public void setTrustCreateDateMonth(String trustCreateDateMonth) {
        this.trustCreateDateMonth = trustCreateDateMonth;
    }

    public String getTrustCreateDateYear() {
        return trustCreateDateYear;
    }

    public void setTrustCreateDateYear(String trustCreateDateYear) {
        this.trustCreateDateYear = trustCreateDateYear;
    }

    public String getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(String unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }
}
