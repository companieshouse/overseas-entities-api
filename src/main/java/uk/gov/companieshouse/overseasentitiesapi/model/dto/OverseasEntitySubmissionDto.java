package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

public class OverseasEntitySubmissionDto {

    @JsonProperty("presenter")
    private PresenterDto presenter;

    @JsonProperty("entity")
    private EntityDto entity;

    @Field("beneficial_owners_statement")
    private BeneficialOwnersStatementDto beneficialOwnersStatement;

    @JsonProperty("beneficial_owners_individual")
    private List<BeneficialOwnerIndividualDto> beneficialOwnersIndividual;

    @JsonProperty("beneficial_owners_corporate")
    private List<BeneficialOwnerCorporateDto> beneficialOwnersCorporate;

    @JsonProperty("beneficial_owners_government_or_public_authority")
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthority;

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

    public BeneficialOwnersStatementDto getBeneficialOwnersStatement() {
        return beneficialOwnersStatement;
    }

    public void setBeneficialOwnersStatement(BeneficialOwnersStatementDto beneficialOwnersStatement) {
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

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
