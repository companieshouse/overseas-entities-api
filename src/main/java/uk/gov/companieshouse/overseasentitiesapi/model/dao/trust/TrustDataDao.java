package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

public class TrustDataDao {

    @Field("trust_name")
    private String trustName;

    @Field("trust_creation_date_day")
    private String trustCreationDateDay;

    @Field("trust_creation_date_month")
    private String trustCreationDateMonth;

    @Field("trust_creation_date_year")
    private String trustCreationDateYear;

    @Field("unable_to_obtain_all_trust_info")
    private String unableToObtainAllTrustInfo;

    public String getTrustName() {
        return trustName;
    }

    public void setTrustName(String trustName) {
        this.trustName = trustName;
    }

    public String getTrustCreationDateDay() {
        return trustCreationDateDay;
    }

    public void setTrustCreationDateDay(String trustCreationDateDay) {
        this.trustCreationDateDay = trustCreationDateDay;
    }

    public String getTrustCreationDateMonth() {
        return trustCreationDateMonth;
    }

    public void setTrustCreationDateMonth(String trustCreationDateMonth) {
        this.trustCreationDateMonth = trustCreationDateMonth;
    }

    public String getTrustCreationDateYear() {
        return trustCreationDateYear;
    }

    public void setTrustCreationDateYear(String trustCreationDateYear) {
        this.trustCreationDateYear = trustCreationDateYear;
    }

    public String getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(String unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }
}
