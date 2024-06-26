package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class TrustDataDto {

    public static final String TRUST_ID_FIELD = "trust_id";
    public static final String TRUST_NAME_FIELD = "trust_name";
    public static final String CH_REFERENCE_FIELD = "ch_reference";
    public static final String TRUST_STILL_INVOLVED_IN_OVERSEAS_ENTITY_FIELD = "trust_still_involved_in_overseas_entity";
    public static final String CREATION_DATE_FIELD = "creation_date";
    public static final String CEASED_DATE_FIELD = "ceased_date";
    public static final String UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD = "unable_to_obtain_all_trust_info";
    public static final String INDIVIDUAL_FIELD = "INDIVIDUAL";
    public static final String HISTORICAL_BO_FIELD = "HISTORICAL_BO";
    public static final String CORPORATE_FIELD = "CORPORATE";

    @JsonProperty(TRUST_ID_FIELD)
    private String trustId;

    @JsonProperty(TRUST_NAME_FIELD)
    private String trustName;

    @JsonProperty(CH_REFERENCE_FIELD)
    private String chReference;

    @JsonProperty(TRUST_STILL_INVOLVED_IN_OVERSEAS_ENTITY_FIELD)
    private Boolean isTrustStillInvolvedInOverseasEntity;

    @JsonProperty(CREATION_DATE_FIELD)
    private LocalDate creationDate;

    @JsonProperty(CEASED_DATE_FIELD)
    private LocalDate ceasedDate;

    @JsonProperty(UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD)
    private Boolean unableToObtainAllTrustInfo;

    @JsonInclude(NON_NULL)
    @JsonProperty(INDIVIDUAL_FIELD)
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

    public String getChReference() { return chReference; }

    public void setChReference(String chReference) { this.chReference = chReference; }

    public Boolean isTrustStillInvolvedInOverseasEntity() {
        return isTrustStillInvolvedInOverseasEntity;
    }

    public void setTrustStillInvolvedInOverseasEntity(Boolean trustStillInvolvedInOverseasEntity) {
        isTrustStillInvolvedInOverseasEntity = trustStillInvolvedInOverseasEntity;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
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
