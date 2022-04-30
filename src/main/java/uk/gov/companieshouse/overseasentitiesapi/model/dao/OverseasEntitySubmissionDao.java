package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "overseas_entities_submissions")
public class OverseasEntitySubmissionDao {

    @Id
    private String id;

    @Field("created_on")
    private LocalDateTime createdOn;

    @Field("created_by_user_id")
    private String createdByUserId;

    @Field("created_by_user_email")
    private String createdByUserEmail;

    @Field("http_request_id")
    private String httpRequestId;

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

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUserEmail() {
        return createdByUserEmail;
    }

    public void setCreatedByUserEmail(String createdByUserEmail) {
        this.createdByUserEmail = createdByUserEmail;
    }

    public String getHttpRequestId() {
        return httpRequestId;
    }

    public void setHttpRequestId(String httpRequestId) {
        this.httpRequestId = httpRequestId;
    }
}
