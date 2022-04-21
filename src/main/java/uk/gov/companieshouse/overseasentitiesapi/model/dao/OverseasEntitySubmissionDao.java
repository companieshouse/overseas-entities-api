package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = "overseas_entities_submissions")
public class OverseasEntitySubmissionDao {

    @Id
    private String id;

    @Field("presenter")
    private PresenterDao presenter;

    @Field("entity")
    private EntityDao entity;

    @Field("beneficial_owners_individual")
    private List<BeneficialOwnerIndividualDao> beneficialOwnersIndividual;

    @Field("beneficial_owners_corporate")
    private List<BeneficialOwnerCorporateDao> beneficialOwnersCorporate;

    @Field("beneficial_owners_government_or_public_authority")
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDao> beneficialOwnersGovernmentOrPublicAuthority;

    @Field("links")
    private Map<String, String> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PresenterDao getPresenter() {
        return presenter;
    }

    public void setPresenter(PresenterDao presenter) {
        this.presenter = presenter;
    }

    public EntityDao getEntity() {
        return entity;
    }

    public void setEntity(EntityDao entity) {
        this.entity = entity;
    }

    public List<BeneficialOwnerIndividualDao> getBeneficialOwnersIndividual() {
        return beneficialOwnersIndividual;
    }

    public void setBeneficialOwnersIndividual(List<BeneficialOwnerIndividualDao> beneficialOwnersIndividual) {
        this.beneficialOwnersIndividual = beneficialOwnersIndividual;
    }

    public List<BeneficialOwnerCorporateDao> getBeneficialOwnersCorporate() {
        return beneficialOwnersCorporate;
    }

    public void setBeneficialOwnersCorporate(List<BeneficialOwnerCorporateDao> beneficialOwnersCorporate) {
        this.beneficialOwnersCorporate = beneficialOwnersCorporate;
    }

    public List<BeneficialOwnerGovernmentOrPublicAuthorityDao> getBeneficialOwnersGovernmentOrPublicAuthority() {
        return beneficialOwnersGovernmentOrPublicAuthority;
    }

    public void setBeneficialOwnersGovernmentOrPublicAuthority(List<BeneficialOwnerGovernmentOrPublicAuthorityDao> beneficialOwnersGovernmentOrPublicAuthority) {
        this.beneficialOwnersGovernmentOrPublicAuthority = beneficialOwnersGovernmentOrPublicAuthority;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
