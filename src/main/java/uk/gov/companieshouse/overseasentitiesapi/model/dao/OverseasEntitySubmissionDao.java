package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

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

    @Field("beneficial_owners_statement")
    private BeneficialOwnersStatementType beneficialOwnersStatement;

    @Field("beneficial_owners_individual")
    private List<BeneficialOwnerIndividualDao> beneficialOwnersIndividual;

    @Field("beneficial_owners_corporate")
    private List<BeneficialOwnerCorporateDao> beneficialOwnersCorporate;

    @Field("beneficial_owners_government_or_public_authority")
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDao> beneficialOwnersGovernmentOrPublicAuthority;

    @Field("managing_officers_individual")
    private List<ManagingOfficerIndividualDao> managingOfficerIndividual;

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

    public BeneficialOwnersStatementType getBeneficialOwnersStatement() {
        return beneficialOwnersStatement;
    }

    public void setBeneficialOwnersStatement(BeneficialOwnersStatementType beneficialOwnersStatement) {
        this.beneficialOwnersStatement = beneficialOwnersStatement;
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

    public List<ManagingOfficerIndividualDao> getManagingOfficerIndividual() {
        return managingOfficerIndividual;
    }

    public void setManagingOfficerIndividual(List<ManagingOfficerIndividualDao> managingOfficerIndividual) {
        this.managingOfficerIndividual = managingOfficerIndividual;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
