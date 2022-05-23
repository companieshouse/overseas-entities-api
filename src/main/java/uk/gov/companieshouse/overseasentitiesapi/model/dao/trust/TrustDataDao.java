package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class TrustDataDao {

    @Field("name")
    private String trustName;

    @Field("trust_creation_date_day")
    private String trustCreationDateDay;

    @Field("trust_creation_date_month")
    private String trustCreationDateMonth;

    @Field("trust_creation_date_year")
    private String trustCreationDateYear;

    @Field("creation_date")
    private LocalDate trustCreationDate;

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

    public LocalDate getTrustCreationDate() {
        return trustCreationDate;
    }

    public void setTrustCreationDate(LocalDate trustCreationDate) {
        this.trustCreationDate = trustCreationDate;
    }

    public String getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(String unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }
}
