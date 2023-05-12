package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class UpdateDao {

    @Field("date_of_creation")
    private LocalDate dateOfCreation;


    @Field("filing_date")
    private LocalDate filingDate;

    @Field("bo_mo_data_fetched")
    private boolean boMoDataFetched;

    @Field("registrable_beneficial_owner")
    private boolean registrableBeneficialOwner;

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

    public List<BeneficialOwnerIndividualDao> getBeneficialOwnersIndividual() {
        return reviewBeneficialOwnersIndividual;
    }

    public void setBeneficialOwnersIndividual(List<BeneficialOwnerIndividualDao> beneficialOwnersIndividual) {
        this.reviewBeneficialOwnersIndividual = beneficialOwnersIndividual;
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

}
