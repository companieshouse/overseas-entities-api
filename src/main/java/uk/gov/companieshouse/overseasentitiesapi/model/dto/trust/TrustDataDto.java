package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class TrustDataDto {

    public static final String TRUST_ID_FIELD = "trust_id";
    public static final String TRUST_NAME_FIELD = "trust_name";
    public static final String CREATION_DATE_FIELD = "creation_date";
    public static final String UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD = "unable_to_obtain_all_trust_info";
    public static final String HISTORICAL_BO_FIELD = "HISTORICAL_BO";
    public static final String CORPORATES_FIELD = "corporates";

    @JsonProperty(TRUST_ID_FIELD)
    private String trustId;

    @JsonProperty(TRUST_NAME_FIELD)
    private String trustName;

    @JsonProperty(CREATION_DATE_FIELD)
    private LocalDate creationDate;

    @JsonProperty(UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD)
    private Boolean unableToObtainAllTrustInfo;

    @JsonInclude(NON_NULL)
    @JsonProperty("INDIVIDUAL")
    private List<TrustIndividualDto> individuals;

    @JsonInclude(NON_NULL)
    @JsonProperty(HISTORICAL_BO_FIELD)
    private List<HistoricalBeneficialOwnerDto> historicalBeneficialOwners;

    @JsonInclude(NON_NULL)
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
