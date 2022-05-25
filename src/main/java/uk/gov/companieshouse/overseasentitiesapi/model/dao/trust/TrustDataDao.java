package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

public class TrustDataDao {

    @Field("name")
    private String name;

    @Field("creation_date")
    private LocalDate trustCreationDate;

    @Field("unable_to_obtain_all_trust_info")
    private boolean unableToObtainAllTrustInfo;

    @Field("historical_beneficial_owners")
    private List<BeneficialOwnerDao> historicalBeneficialOwners;

    @Field("beneficiaries")
    private List<BeneficialOwnerDao> beneficiaries;

    @Field("settlers")
    private List<BeneficialOwnerDao> settlers;

    @Field("grantors")
    private List<BeneficialOwnerDao> grantors;

    @Field("interested_persons")
    private List<BeneficialOwnerDao> interestedPersons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getTrustCreationDate() {
        return trustCreationDate;
    }

    public void setTrustCreationDate(LocalDate trustCreationDate) {
        this.trustCreationDate = trustCreationDate;
    }

    public boolean getUnableToObtainAllTrustInfo() {
        return unableToObtainAllTrustInfo;
    }

    public void setUnableToObtainAllTrustInfo(boolean unableToObtainAllTrustInfo) {
        this.unableToObtainAllTrustInfo = unableToObtainAllTrustInfo;
    }

    public List<BeneficialOwnerDao> getHistoricalBeneficialOwners() {
        return historicalBeneficialOwners;
    }

    public void setHistoricalBeneficialOwners(List<BeneficialOwnerDao> historicalBeneficialOwners) {
        this.historicalBeneficialOwners = historicalBeneficialOwners;
    }

    public List<BeneficialOwnerDao> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<BeneficialOwnerDao> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public List<BeneficialOwnerDao> getSettlers() {
        return settlers;
    }

    public void setSettlers(List<BeneficialOwnerDao> settlers) {
        this.settlers = settlers;
    }

    public List<BeneficialOwnerDao> getGrantors() {
        return grantors;
    }

    public void setGrantors(List<BeneficialOwnerDao> grantors) {
        this.grantors = grantors;
    }

    public List<BeneficialOwnerDao> getInterestedPersons() {
        return interestedPersons;
    }

    public void setInterestedPersons(List<BeneficialOwnerDao> interestedPersons) {
        this.interestedPersons = interestedPersons;
    }
}
