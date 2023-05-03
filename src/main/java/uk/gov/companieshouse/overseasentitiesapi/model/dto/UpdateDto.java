package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class UpdateDto {

    public static final String DATE_OF_CREATION = "date_of_creation";
    public static final String BO_MO_DATA_FETCHED = "bo_mo_data_fetched";
    public static final String REGISTRABLE_BENEFICIAL_OWNER = "registrable_beneficial_owner";

    public static final String REVIEW_BENEFICIAL_OWNERS_INDIVIDUAL_FIELD = "review_beneficial_owners_individual";

    public static final String REVIEW_BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD = "review_beneficial_owners_government_or_public_authority";

    public static final String REVIEW_BENEFICIAL_OWNERS_CORPORATE_FIELD = "review_beneficial_owners_corporate";

    public static final String REVIEW_MANAGING_OFFICERS_INDIVIDUAL_FIELD = "review_managing_officers_individual";

    public static final String REVIEW_MANAGING_OFFICERS_CORPORATE_FIELD = "review_managing_officers_corporate";

    @JsonProperty(DATE_OF_CREATION)
    private LocalDate dateOfCreation;

    @JsonProperty(BO_MO_DATA_FETCHED)
    private boolean boMoDataFetched;

    @JsonProperty(REGISTRABLE_BENEFICIAL_OWNER)
    private boolean registrableBeneficialOwner;


    @JsonProperty(REVIEW_BENEFICIAL_OWNERS_INDIVIDUAL_FIELD)
    private List<BeneficialOwnerIndividualDto> reviewBeneficialOwnersIndividual;

    @JsonProperty(REVIEW_BENEFICIAL_OWNERS_CORPORATE_FIELD)
    private List<BeneficialOwnerCorporateDto> reviewBeneficialOwnersCorporate;

    @JsonProperty(REVIEW_BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD)
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> reviewBeneficialOwnersGovernmentOrPublicAuthority;

    @JsonProperty(REVIEW_MANAGING_OFFICERS_INDIVIDUAL_FIELD)
    private List<ManagingOfficerIndividualDto> reviewManagingOfficersIndividual;

    @JsonProperty(REVIEW_MANAGING_OFFICERS_CORPORATE_FIELD)
    private List<ManagingOfficerCorporateDto> reviewManagingOfficersCorporate;

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
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

    public List<BeneficialOwnerIndividualDto> getReviewBeneficialOwnersIndividual() {
        return reviewBeneficialOwnersIndividual;
    }

    public void setReviewBeneficialOwnersIndividual(List<BeneficialOwnerIndividualDto> beneficialOwnersIndividual) {
        this.reviewBeneficialOwnersIndividual = beneficialOwnersIndividual;
    }

    public List<BeneficialOwnerCorporateDto> getReviewBeneficialOwnersCorporate() {
        return reviewBeneficialOwnersCorporate;
    }

    public void setReviewBeneficialOwnersCorporate(List<BeneficialOwnerCorporateDto> beneficialOwnersCorporate) {
        this.reviewBeneficialOwnersCorporate = beneficialOwnersCorporate;
    }

    public List<BeneficialOwnerGovernmentOrPublicAuthorityDto> getReviewBeneficialOwnersGovernmentOrPublicAuthority() {
        return reviewBeneficialOwnersGovernmentOrPublicAuthority;
    }

    public void setReviewBeneficialOwnersGovernmentOrPublicAuthority(List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthority) {
        this.reviewBeneficialOwnersGovernmentOrPublicAuthority = beneficialOwnersGovernmentOrPublicAuthority;
    }

    public List<ManagingOfficerIndividualDto> getReviewManagingOfficersIndividual() {
        return reviewManagingOfficersIndividual;
    }

    public void setReviewManagingOfficersIndividual(List<ManagingOfficerIndividualDto> reviewManagingOfficersIndividual) {
        this.reviewManagingOfficersIndividual = reviewManagingOfficersIndividual;
    }

    public List<ManagingOfficerCorporateDto> getReviewManagingOfficersCorporate() {
        return reviewManagingOfficersCorporate;
    }

    public void setReviewManagingOfficersCorporate(List<ManagingOfficerCorporateDto> managingOfficersCorporate) {
        this.reviewManagingOfficersCorporate = managingOfficersCorporate;
    }

}
