package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

import uk.gov.companieshouse.overseasentitiesapi.model.RelevantStatementsType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataToReviewDto;

public class UpdateDto {

    public static final String DATE_OF_CREATION = "date_of_creation";
    public static final String FILING_DATE= "filing_date";
    public static final String BO_MO_DATA_FETCHED = "bo_mo_data_fetched";
    public static final String REGISTRABLE_BENEFICIAL_OWNER = "registrable_beneficial_owner";

    public static final String REVIEW_BENEFICIAL_OWNERS_INDIVIDUAL_FIELD = "review_beneficial_owners_individual";

    public static final String REVIEW_BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD = "review_beneficial_owners_government_or_public_authority";

    public static final String REVIEW_BENEFICIAL_OWNERS_CORPORATE_FIELD = "review_beneficial_owners_corporate";

    public static final String REVIEW_MANAGING_OFFICERS_INDIVIDUAL_FIELD = "review_managing_officers_individual";

    public static final String REVIEW_MANAGING_OFFICERS_CORPORATE_FIELD = "review_managing_officers_corporate";

    public static final String NO_CHANGE_SCENARIO = "no_change";

    public static final String TRUST_DATA_FETCHED = "trust_data_fetched";

    public static final String REVIEW_TRUSTS_FIELD = "review_trusts";
    private static final String RELEVANT_STATEMENT1 = "relevant_period_beneficial_owner";
    private static final String RELEVANT_STATEMENT2 = "relevant_period_trust_involved";
    private static final String RELEVANT_STATEMENT3 = "relevant_period_trust_beneficiary";

    @JsonProperty(DATE_OF_CREATION)
    private LocalDate dateOfCreation;

    @JsonProperty(FILING_DATE)
    private LocalDate filingDate;

    @JsonProperty(BO_MO_DATA_FETCHED)
    private boolean boMoDataFetched;

    @JsonProperty(REGISTRABLE_BENEFICIAL_OWNER)
    private boolean registrableBeneficialOwner;

    @JsonProperty(NO_CHANGE_SCENARIO)
    private boolean noChange;

    @JsonProperty(TRUST_DATA_FETCHED)
    private boolean trustDataFetched;

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

    @JsonProperty(REVIEW_TRUSTS_FIELD)
    private List<TrustDataToReviewDto> reviewTrusts;

    @JsonProperty(RELEVANT_STATEMENT1)
    private RelevantStatementsType relevantStatementsType;
    @JsonProperty(RELEVANT_STATEMENT3)
    private RelevantStatementsType relevantStatementsType3;
    @JsonProperty(RELEVANT_STATEMENT2)
    private RelevantStatementsType relevantStatementsType2;

    public RelevantStatementsType getRelevantStatementsType() {
        return relevantStatementsType;
    }

    public void setRelevantStatementsType(RelevantStatementsType relevantStatementsType) {
        this.relevantStatementsType = relevantStatementsType;
    }

    public RelevantStatementsType getRelevantStatementsType2() {
        return relevantStatementsType2;
    }

    public void setRelevantStatementsType2(RelevantStatementsType relevantStatementsType2) {
        this.relevantStatementsType2 = relevantStatementsType2;
    }


    public RelevantStatementsType getRelevantStatementsType3() {
        return relevantStatementsType3;
    }

    public void setRelevantStatementsType3(RelevantStatementsType relevantStatementsType3) {
        this.relevantStatementsType3 = relevantStatementsType3;
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


    public void setReviewManagingOfficersCorporate(List<ManagingOfficerCorporateDto> managingOfficersCorporate) {
        this.reviewManagingOfficersCorporate = managingOfficersCorporate;
    }

    public List<TrustDataToReviewDto> getReviewTrusts() {
        return reviewTrusts;
    }

    public void setReviewTrusts(List<TrustDataToReviewDto> reviewTrusts) {
        this.reviewTrusts = reviewTrusts;
    }
}
