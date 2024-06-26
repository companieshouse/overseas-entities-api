package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

import uk.gov.companieshouse.overseasentitiesapi.model.RelevantStatementsType;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataToReviewDao;

public class UpdateDao {

    @Field("date_of_creation")
    private LocalDate dateOfCreation;


    @Field("filing_date")
    private LocalDate filingDate;

    @Field("bo_mo_data_fetched")
    private boolean boMoDataFetched;

    @Field("registrable_beneficial_owner")
    private boolean registrableBeneficialOwner;

    @Field("no_change")
    private boolean noChange;

    @Field("trust_data_fetched")
    private boolean trustDataFetched;

    @Field("review_beneficial_owners_individual")
    private List<BeneficialOwnerIndividualDao> reviewBeneficialOwnersIndividual;

    @Field("review_beneficial_owners_corporate")
    private List<BeneficialOwnerCorporateDao> reviewBeneficialOwnersCorporate;

    @Field("review_beneficial_owners_government_or_public_authority")
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDao> reviewBeneficialOwnersGovernmentOrPublicAuthority;

    @Field("review_managing_officers_individual")
    private List<ManagingOfficerIndividualDao> reviewManagingOfficersIndividual;

    @Field("review_managing_officers_corporate")
    private List<ManagingOfficerCorporateDao> reviewManagingOfficersCorporate;

    @Field("review_trusts")
    private List<TrustDataToReviewDao> reviewTrusts;

    @Field("change_bo_relevant_period")
    private RelevantStatementsType changeBORelevantPeriod;

    @Field("trustee_involved_relevant_period")
    private RelevantStatementsType trusteeInvolvedRelevantPeriod;

    @Field("change_beneficiary_relevant_period")
    private RelevantStatementsType changeBeneficiaryRelevantPeriod;

    public RelevantStatementsType getChangeBORelevantPeriod() {
        return changeBORelevantPeriod;
    }

    public void setChangeBORelevantPeriod(RelevantStatementsType changeBORelevantPeriod) {
        this.changeBORelevantPeriod = changeBORelevantPeriod;
    }

    public RelevantStatementsType getTrusteeInvolvedRelevantPeriod() {
        return trusteeInvolvedRelevantPeriod;
    }

    public void setTrusteeInvolvedRelevantPeriod(RelevantStatementsType trusteeInvolvedRelevantPeriod) {
        this.trusteeInvolvedRelevantPeriod = trusteeInvolvedRelevantPeriod;
    }

    public RelevantStatementsType getChangeBeneficiaryRelevantPeriod() {
        return changeBeneficiaryRelevantPeriod;
    }

    public void setChangeBeneficiaryRelevantPeriod(RelevantStatementsType changeBeneficiaryRelevantPeriod) {
        this.changeBeneficiaryRelevantPeriod = changeBeneficiaryRelevantPeriod;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public boolean isBoMoDataFetched() {
        return boMoDataFetched;
    }

    public void setBoMoDataFetched(boolean boMoDataFetched) {
        this.boMoDataFetched = boMoDataFetched;
    }

    public boolean isRegistrableBeneficialOwner() {
        return registrableBeneficialOwner;
    }

    public void setRegistrableBeneficialOwner(boolean registrableBeneficialOwner) {
        this.registrableBeneficialOwner = registrableBeneficialOwner;
    }
    public boolean isNoChange() {
        return noChange;
    }

    public void setNoChange(boolean noChange) {
        this.noChange = noChange;
    }

    public boolean isTrustDataFetched() {
        return trustDataFetched;
    }

    public void setTrustDataFetched(boolean trustDataFetched) {
        this.trustDataFetched = trustDataFetched;
    }

    public List<BeneficialOwnerIndividualDao> getReviewBeneficialOwnersIndividual() {
        return reviewBeneficialOwnersIndividual;
    }

    public void setReviewBeneficialOwnersIndividual(List<BeneficialOwnerIndividualDao> reviewBeneficialOwnersIndividual) {
        this.reviewBeneficialOwnersIndividual = reviewBeneficialOwnersIndividual;
    }

    public List<BeneficialOwnerCorporateDao> getReviewBeneficialOwnersCorporate() {
        return reviewBeneficialOwnersCorporate;
    }

    public void setReviewBeneficialOwnersCorporate(List<BeneficialOwnerCorporateDao> reviewBeneficialOwnersCorporate) {
        this.reviewBeneficialOwnersCorporate = reviewBeneficialOwnersCorporate;
    }

    public List<BeneficialOwnerGovernmentOrPublicAuthorityDao> getReviewBeneficialOwnersGovernmentOrPublicAuthority() {
        return reviewBeneficialOwnersGovernmentOrPublicAuthority;
    }

    public void setReviewBeneficialOwnersGovernmentOrPublicAuthority(List<BeneficialOwnerGovernmentOrPublicAuthorityDao> reviewBeneficialOwnersGovernmentOrPublicAuthority) {
        this.reviewBeneficialOwnersGovernmentOrPublicAuthority = reviewBeneficialOwnersGovernmentOrPublicAuthority;
    }

    public List<ManagingOfficerIndividualDao> getReviewManagingOfficersIndividual() {
        return reviewManagingOfficersIndividual;
    }

    public void setReviewManagingOfficersIndividual(List<ManagingOfficerIndividualDao> reviewManagingOfficersIndividual) {
        this.reviewManagingOfficersIndividual = reviewManagingOfficersIndividual;
    }
    public List<ManagingOfficerCorporateDao> getReviewManagingOfficersCorporate() {
        return reviewManagingOfficersCorporate;
    }

    public void setReviewManagingOfficersCorporate(List<ManagingOfficerCorporateDao> reviewManagingOfficersCorporate) {
        this.reviewManagingOfficersCorporate = reviewManagingOfficersCorporate;
    }

    public List<TrustDataToReviewDao> getReviewTrusts() {
        return reviewTrusts;
    }

    public void setReviewTrusts(List<TrustDataToReviewDao> reviewTrusts) {
        this.reviewTrusts = reviewTrusts;
    }
}
