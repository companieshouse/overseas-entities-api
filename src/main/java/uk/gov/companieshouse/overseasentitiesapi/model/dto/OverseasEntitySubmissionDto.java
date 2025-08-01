package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class OverseasEntitySubmissionDto {

    public static final String ENTITY_NAME_FIELD = "entity_name";
    public static final String ENTITY_NUMBER_FIELD = "entity_number";
    public static final String PRESENTER_FIELD = "presenter";
    public static final String ENTITY_FIELD = "entity";
    public static final String DUE_DILIGENCE_FIELD = "due_diligence";
    public static final String OVERSEAS_ENTITY_DUE_DILIGENCE = "overseas_entity_due_diligence";
    public static final String BENEFICIAL_OWNERS_STATEMENT = "beneficial_owners_statement";
    public static final String BENEFICIAL_OWNERS_INDIVIDUAL_FIELD = "beneficial_owners_individual";
    public static final String BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD = "beneficial_owners_government_or_public_authority";
    public static final String BENEFICIAL_OWNERS_CORPORATE_FIELD = "beneficial_owners_corporate";
    public static final String MANAGING_OFFICERS_INDIVIDUAL_FIELD = "managing_officers_individual";
    public static final String MANAGING_OFFICERS_CORPORATE_FIELD = "managing_officers_corporate";
    public static final String TRUST_DATA = "trusts";
    public static final String IS_REMOVE_FIELD = "is_remove";
    public static final String UPDATE_FIELD = "update";
    public static final String REMOVE_FIELD = "remove";
    public static final String HAS_SOLD_LAND_FIELD = "has_sold_land";
    public static final String IS_SECURE_REGISTER_FIELD = "is_secure_register";
    public static final String WHO_IS_REGISTERING = "who_is_registering";
    public static final String PAYMENT_FIELD = "payment";

    @JsonProperty(ENTITY_NAME_FIELD)
    private EntityNameDto entityName;

    @JsonProperty(ENTITY_NUMBER_FIELD)
    private String entityNumber;

    @JsonProperty(PRESENTER_FIELD)
    private PresenterDto presenter;

    @JsonProperty(ENTITY_FIELD)
    private EntityDto entity;

    @JsonProperty(DUE_DILIGENCE_FIELD)
    private DueDiligenceDto dueDiligence;

    @JsonProperty(OVERSEAS_ENTITY_DUE_DILIGENCE)
    private OverseasEntityDueDiligenceDto overseasEntityDueDiligence;

    @JsonProperty(BENEFICIAL_OWNERS_STATEMENT)
    private BeneficialOwnersStatementType beneficialOwnersStatement;

    @JsonProperty(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD)
    private List<BeneficialOwnerIndividualDto> beneficialOwnersIndividual;

    @JsonProperty(BENEFICIAL_OWNERS_CORPORATE_FIELD)
    private List<BeneficialOwnerCorporateDto> beneficialOwnersCorporate;

    @JsonProperty(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD)
    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthority;

    @JsonProperty(MANAGING_OFFICERS_INDIVIDUAL_FIELD)
    private List<ManagingOfficerIndividualDto> managingOfficersIndividual;

    @JsonProperty(MANAGING_OFFICERS_CORPORATE_FIELD)
    private List<ManagingOfficerCorporateDto> managingOfficersCorporate;

    @JsonInclude(NON_NULL)
    @JsonProperty(TRUST_DATA)
    private List<TrustDataDto> trusts;

    @JsonProperty(UPDATE_FIELD)
    private UpdateDto update;

    @JsonProperty(REMOVE_FIELD)
    private RemoveDto remove;

    @JsonProperty(IS_REMOVE_FIELD)
    private Boolean isRemove;

    @JsonProperty(HAS_SOLD_LAND_FIELD)
    private Boolean hasSoldLand;

    @JsonProperty(IS_SECURE_REGISTER_FIELD)
    private Boolean isSecureRegister;

    @JsonProperty(WHO_IS_REGISTERING)
    private String whoIsRegistering;

    @JsonProperty(PAYMENT_FIELD)
    private PaymentDto payment;

    @JsonProperty("links")
    private Map<String, String> links;

    @JsonIgnore
    public boolean isForUpdate() {
        return StringUtils.isNotBlank(entityNumber) && !isForRemove();
    }

    @JsonIgnore
    public boolean isForRemove() {
        return Boolean.TRUE.equals(isRemove);
    }

    @JsonIgnore
    public boolean isForUpdateOrRemove() {
        return isForUpdate() || isForRemove();
    }

    public EntityNameDto getEntityName() {
        return entityName;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityName(EntityNameDto entityName) {
        this.entityName = entityName;
    }

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

    public DueDiligenceDto getDueDiligence() {
        return dueDiligence;
    }

    public void setDueDiligence(DueDiligenceDto dueDiligence) {
        this.dueDiligence = dueDiligence;
    }

    public OverseasEntityDueDiligenceDto getOverseasEntityDueDiligence() {
        return overseasEntityDueDiligence;
    }

    public void setOverseasEntityDueDiligence(OverseasEntityDueDiligenceDto overseasEntityDueDiligence) {
        this.overseasEntityDueDiligence = overseasEntityDueDiligence;
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

    public List<ManagingOfficerCorporateDto> getManagingOfficersCorporate() {
        return managingOfficersCorporate;
    }

    public void setManagingOfficersCorporate(List<ManagingOfficerCorporateDto> managingOfficersCorporate) {
        this.managingOfficersCorporate = managingOfficersCorporate;
    }

    public List<TrustDataDto> getTrusts() {
        return trusts;
    }

    public void setTrusts(List<TrustDataDto> trusts) {
        this.trusts = trusts;
    }

    public UpdateDto getUpdate() {
        return update;
    }

    public void setUpdate(UpdateDto update) {
        this.update = update;
    }

    public RemoveDto getRemove() {
        return remove;
    }

    public void setRemove(RemoveDto remove) {
        this.remove = remove;
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

    public void setWhoIsRegistering(String whoIsRegistering) { this.whoIsRegistering = whoIsRegistering; }

    public PaymentDto getPayment() {
        return payment;
    }

    public void setPayment(PaymentDto payment) {
        this.payment = payment;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
