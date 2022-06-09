package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class TrustDataDto {

    @JsonProperty("trust_id")
    private String trustId;

    @JsonProperty("trust_name")
    private String trustName;

    @JsonProperty("creation_date")
    private LocalDate creationDate;

    @JsonProperty("unable_to_obtain_all_trust_info")
    private Boolean unableToObtainAllTrustInfo;

    @JsonProperty("INDIVIDUAL")
    private List<TrustIndividualDto> individuals;

    @JsonProperty("HISTORICAL_BO")
    private List<HistoricalBeneficialOwnerDto> historicalBeneficialOwners;

    @JsonProperty("CORPORATE")
    private List<TrustCorporateDto> corporates;

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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(Boolean unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }

    public List<TrustIndividualDto> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<TrustIndividualDto> individuals) {
        this.individuals = individuals;
    }

    public List<HistoricalBeneficialOwnerDto> getHistoricalBeneficialOwners() {
        return historicalBeneficialOwners;
    }

    public void setHistoricalBeneficialOwners(List<HistoricalBeneficialOwnerDto> historicalBeneficialOwners) {
        this.historicalBeneficialOwners = historicalBeneficialOwners;
    }

    public List<TrustCorporateDto> getCorporates() {
        return corporates;
    }

    public void setCorporates(List<TrustCorporateDto> trustCorporateDtos) {
        this.corporates = trustCorporateDtos;
    }
}
