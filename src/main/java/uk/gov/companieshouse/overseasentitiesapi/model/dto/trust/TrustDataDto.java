package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TrustDataDto {

    @JsonProperty("trust_name")
    private String trustName;

    @JsonProperty("trust_creation_date_day")
    private String trustCreationDateDay;

    @JsonProperty("trust_creation_date_month")
    private String trustCreationDateMonth;

    @JsonProperty("trust_creation_date_year")
    private String trustCreationDateYear;

    @JsonProperty("unable_to_obtain_all_trust_info")
    private String unableToObtainAllTrustInfo;

    @JsonProperty("INDIVIDUAL")
    private List<IndividualDto> individualDtos;

    @JsonProperty("HISTORICAL_BO")
    private List<HistoricalBoDto> historicalBoDtos;

    @JsonProperty("CORPORATE")
    private List<CorporateDto> corporateDtos;

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

    public List<IndividualDto> getIndividualDtos() {
        return individualDtos;
    }

    public void setIndividualDtos(List<IndividualDto> individualDtos) {
        this.individualDtos = individualDtos;
    }

    public List<HistoricalBoDto> getHistoricalBoDtos() {
        return historicalBoDtos;
    }

    public void setHistoricalBoDtos(List<HistoricalBoDto> historicalBoDtos) {
        this.historicalBoDtos = historicalBoDtos;
    }

    public List<CorporateDto> getCorporateDtos() {
        return corporateDtos;
    }

    public void setCorporateDtos(List<CorporateDto> corporateDtos) {
        this.corporateDtos = corporateDtos;
    }
}
