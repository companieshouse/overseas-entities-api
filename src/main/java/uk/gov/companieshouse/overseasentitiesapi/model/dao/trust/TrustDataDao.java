package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class TrustDataDao {

    @Field("_id")
    private String trustId;

    @Field("name")
    private String trustName;

    @Field("creation_date")
    private String trustCreationDate;

    @Field("unable_to_obtain_all_trust_info")
    private boolean unableToObtainAllTrustInfo;

    @Field("historical_beneficial_owners")
    private List<HistoricalBeneficialOwnerDao> historicalBeneficialOwners;

    @Field("individual_beneficial_owners")
    private List<IndividualBeneficialOwnerDao> individualBeneficialOwners;

    @Field("corporate_beneficial_owners")
    private List<CorporateBeneficialOwnerDao> corporateBeneficialOwners;

    public String getId() {
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

    public String getTrustCreationDate() {
        return trustCreationDate;
    }

    public void setTrustCreationDate(String trustCreationDate) {
        this.trustCreationDate = trustCreationDate;
    }

    public boolean getUnableToObtainAllTrustInfo() {
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

    public boolean isUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public List<IndividualBeneficialOwnerDao> getIndividualBeneficialOwners() {
        return individualBeneficialOwners;
    }

    public void setIndividualBeneficialOwners(List<IndividualBeneficialOwnerDao> individualBeneficialOwners) {
        this.individualBeneficialOwners = individualBeneficialOwners;
    }

    public List<CorporateBeneficialOwnerDao> getCorporateBeneficialOwners() {
        return corporateBeneficialOwners;
    }

    public void setCorporateBeneficialOwners(List<CorporateBeneficialOwnerDao> corporateBeneficialOwners) {
        this.corporateBeneficialOwners = corporateBeneficialOwners;
    }

}
