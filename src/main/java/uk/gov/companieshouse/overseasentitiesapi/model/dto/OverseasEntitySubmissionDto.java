package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

import java.util.List;
import java.util.Map;

public class OverseasEntitySubmissionDto {

    public static final String ENTITY_FIELD = "entity";
    public static final String BENEFICIAL_OWNERS_INDIVIDUAL_FIELD = "beneficial_owners_individual";
    public static final String BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD = "beneficial_owners_government_or_public_authority";
    public static final String BENEFICIAL_OWNERS_CORPORATE_FIELD = "beneficial_owners_corporate";

    @JsonProperty("presenter")
    private PresenterDto presenter;

    @JsonProperty(ENTITY_FIELD)
    private EntityDto entity;

    @JsonProperty("beneficial_owners_statement")
    private BeneficialOwnersStatementType beneficialOwnersStatement;

    @JsonProperty(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD)
    private List<BeneficialOwnerIndividualDto> beneficialOwnersIndividual;

    @JsonProperty(BENEFICIAL_OWNERS_CORPORATE_FIELD)
    private List<BeneficialOwnerCorporateDto> beneficialOwnersCorporate;

    @JsonProperty(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD)
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthority;

    @JsonProperty("managing_officers_individual")
    private List<ManagingOfficerIndividualDto> managingOfficersIndividual;

    @JsonProperty("links")
    private Map<String, String> links;

    public PresenterDto getPresenter() {
        return presenter;
    }

    public void setPresenter(PresenterDto presenter) {
        this.presenter = presenter;
    }

    public EntityDto getEntity() {
        return entity;
    }

    public void setEntity(EntityDto entity) {
        this.entity = entity;
    }

    public BeneficialOwnersStatementType getBeneficialOwnersStatement() {
        return beneficialOwnersStatement;
    }

    public void setBeneficialOwnersStatement(BeneficialOwnersStatementType beneficialOwnersStatement) {
        this.beneficialOwnersStatement = beneficialOwnersStatement;
    }

    public List<BeneficialOwnerIndividualDto> getBeneficialOwnersIndividual() {
        return beneficialOwnersIndividual;
    }

    public void setBeneficialOwnersIndividual(List<BeneficialOwnerIndividualDto> beneficialOwnersIndividual) {
        this.beneficialOwnersIndividual = beneficialOwnersIndividual;
    }

    public List<BeneficialOwnerCorporateDto> getBeneficialOwnersCorporate() {
        return beneficialOwnersCorporate;
    }

    public void setBeneficialOwnersCorporate(List<BeneficialOwnerCorporateDto> beneficialOwnersCorporate) {
        this.beneficialOwnersCorporate = beneficialOwnersCorporate;
    }

    public List<BeneficialOwnerGovernmentOrPublicAuthorityDto> getBeneficialOwnersGovernmentOrPublicAuthority() {
        return beneficialOwnersGovernmentOrPublicAuthority;
    }

    public void setBeneficialOwnersGovernmentOrPublicAuthority(List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthority) {
        this.beneficialOwnersGovernmentOrPublicAuthority = beneficialOwnersGovernmentOrPublicAuthority;
    }

    public List<ManagingOfficerIndividualDto> getManagingOfficersIndividual() {
        return managingOfficersIndividual;
    }

    public void setManagingOfficersIndividual(List<ManagingOfficerIndividualDto> managingOfficersIndividual) {
        this.managingOfficersIndividual = managingOfficersIndividual;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
