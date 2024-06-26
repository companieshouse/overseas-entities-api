package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

public class TrustDataDao {

    @Field("_id")
    private String trustId;

    @Field("name")
    private String trustName;

    @Field("ch_reference")
    private String chReference;

    @Field("trust_still_involved_in_overseas_entity")
    private Boolean isTrustStillInvolvedInOverseasEntity;

    @Field("creation_date")
    private LocalDate creationDate;

    @Field("ceased_date")
    private LocalDate ceasedDate;

    @Field("unable_to_obtain_all_trust_info")
    private boolean unableToObtainAllTrustInfo;

    @Field("historical_beneficial_owners")
    private List<HistoricalBeneficialOwnerDao> historicalBeneficialOwners;

    @Field("individuals")
    private List<TrustIndividualDao> individuals;

    @Field("corporates")
    private List<TrustCorporateDao> corporates;

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

    public boolean isUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(boolean unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }

    public List<HistoricalBeneficialOwnerDao> getHistoricalBeneficialOwners() {
        return historicalBeneficialOwners;
    }

    public void setHistoricalBeneficialOwners(List<HistoricalBeneficialOwnerDao> historicalBeneficialOwners) {
        this.historicalBeneficialOwners = historicalBeneficialOwners;
    }

    public List<TrustIndividualDao> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<TrustIndividualDao> individuals) {
        this.individuals = individuals;
    }

    public List<TrustCorporateDao> getCorporates() {
        return corporates;
    }

    public void setCorporates(List<TrustCorporateDao> corporates) {
        this.corporates = corporates;
    }
}
