package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class TrustDataDto {

    @JsonProperty("trust_id")
    private String trustId;

    @JsonProperty("trust_name")
    private String trustName;

    @JsonProperty("trust_creation_date_day")
    private String trustCreationDateDay;

    @JsonProperty("trust_creation_date_month")
    private String trustCreationDateMonth;

    @JsonProperty("trust_creation_date_year")
    private String trustCreationDateYear;

    @JsonProperty("unable_to_obtain_all_trust_info")
    private Boolean unableToObtainAllTrustInfo;

    @JsonProperty("INDIVIDUAL")
    private List<IndividualDto> individuals;

    @JsonProperty("HISTORICAL_BO")
    private List<HistoricalBoDto> historicalBeneficialOwners;

    @JsonProperty("CORPORATE")
    private List<CorporateDto> corporates;

    public String getTrustId() {
        return trustId;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

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
        var year = Integer.parseInt(trustCreationDateYear);
        var month = Integer.parseInt(trustCreationDateMonth);
        var day = Integer.parseInt(trustCreationDateDay);
        return LocalDate.of(year, month, day);
    }

    public Boolean getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(Boolean unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }

    public List<IndividualDto> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<IndividualDto> individuals) {
        this.individuals = individuals;
    }

    public List<HistoricalBoDto> getHistoricalBeneficialOwners() {
        return historicalBeneficialOwners;
    }

    public void setHistoricalBeneficialOwners(List<HistoricalBoDto> historicalBeneficialOwners) {
        this.historicalBeneficialOwners = historicalBeneficialOwners;
    }

    public List<CorporateDto> getCorporates() {
        return corporates;
    }

    public void setCorporates(List<CorporateDto> corporateDtos) {
        this.corporates = corporateDtos;
    }
}
