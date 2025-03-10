package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "overseas_entities_submissions")
public class OverseasEntitySubmissionDao {

    public static final String SCHEMA_VERSION_FIELD = "schema_version";

    @Id
    private String id;

    @Field("created_on")
    private LocalDateTime createdOn;

    @Field("created_by_user_id")
    private String createdByUserId;

    @Field("http_request_id")
    private String httpRequestId;

    @Field("entity_name")
    private EntityNameDao entityName;

    @Field("entity_number")
    private String entityNumber;

    @Field("presenter")
    private PresenterDao presenter;

    @Field("entity")
    private EntityDao entity;

    @Field("due_diligence")
    private DueDiligenceDao dueDiligence;

    @Field("overseas_entity_due_diligence")
    private OverseasEntityDueDiligenceDao overseasEntityDueDiligence;

    @Field("beneficial_owners_statement")
    private BeneficialOwnersStatementType beneficialOwnersStatement;

    @Field("beneficial_owners_individual")
    private List<BeneficialOwnerIndividualDao> beneficialOwnersIndividual;

    @Field("beneficial_owners_corporate")
    private List<BeneficialOwnerCorporateDao> beneficialOwnersCorporate;

    @Field("beneficial_owners_government_or_public_authority")
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDao> beneficialOwnersGovernmentOrPublicAuthority;

    @Field("managing_officers_individual")
    private List<ManagingOfficerIndividualDao> managingOfficersIndividual;

    @Field("managing_officers_corporate")
    private List<ManagingOfficerCorporateDao> managingOfficersCorporate;

    @Field("update")
    private UpdateDao update;

    @Field("remove")
    private RemoveDao remove;

    @Field("is_remove")
    private Boolean isRemove;

    @Field("has_sold_land")
    private Boolean hasSoldLand;

    @Field("is_secure_register")
    private Boolean isSecureRegister;

    @Field("who_is_registering")
    private String whoIsRegistering;

    @Field("payment")
    private PaymentDao payment;

    @Field("trusts")
    private List<TrustDataDao> trusts;

    @Field("links")
    private Map<String, String> links;

    @Field(SCHEMA_VERSION_FIELD)
    private String schemaVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EntityNameDao getEntityName() {
        return entityName;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityName(EntityNameDao entityName) {
        this.entityName = entityName;
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

    public DueDiligenceDao getDueDiligence() {
        return dueDiligence;
    }

    public void setDueDiligence(DueDiligenceDao dueDiligence) {
        this.dueDiligence = dueDiligence;
    }

    public OverseasEntityDueDiligenceDao getOverseasEntityDueDiligence() {
        return overseasEntityDueDiligence;
    }

    public void setOverseasEntityDueDiligence(OverseasEntityDueDiligenceDao overseasEntityDueDiligence) {
        this.overseasEntityDueDiligence = overseasEntityDueDiligence;
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

    public List<ManagingOfficerIndividualDao> getManagingOfficersIndividual() {
        return managingOfficersIndividual;
    }

    public void setManagingOfficersIndividual(List<ManagingOfficerIndividualDao> managingOfficersIndividual) {
        this.managingOfficersIndividual = managingOfficersIndividual;
    }

    public List<ManagingOfficerCorporateDao> getManagingOfficersCorporate() {
        return managingOfficersCorporate;
    }

    public void setManagingOfficersCorporate(List<ManagingOfficerCorporateDao> managingOfficersCorporate) {
        this.managingOfficersCorporate = managingOfficersCorporate;
    }

    public List<TrustDataDao> getTrusts() {
        return trusts;
    }

    public void setTrusts(List<TrustDataDao> trusts) {
        this.trusts = trusts;
    }

    public UpdateDao getUpdate() {
        return update;
    }

    public void setUpdate(UpdateDao update) {
        this.update = update;
    }

    public RemoveDao getRemove() {
        return remove;
    }

    public void setRemove(RemoveDao remove) {
        this.remove = remove;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public Boolean getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Boolean isRemove) {
        this.isRemove = isRemove;
    }

    public Boolean getHasSoldLand() {
        return hasSoldLand;
    }

    public void setHasSoldLand(Boolean hasSoldLand) {
        this.hasSoldLand = hasSoldLand;
    }

    public Boolean getIsSecureRegister() {
        return isSecureRegister;
    }

    public void setIsSecureRegister(Boolean isSecureRegister) {
        this.isSecureRegister = isSecureRegister;
    }

    public String getWhoIsRegistering() {
        return whoIsRegistering;
    }

    public void setWhoIsRegistering(String whoIsRegistering) {
        this.whoIsRegistering = whoIsRegistering;
    }

    public PaymentDao getPayment() {
        return payment;
    }

    public void setPayment(PaymentDao payment) {
        this.payment = payment;
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

    public String getHttpRequestId() {
        return httpRequestId;
    }

    public void setHttpRequestId(String httpRequestId) {
        this.httpRequestId = httpRequestId;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
