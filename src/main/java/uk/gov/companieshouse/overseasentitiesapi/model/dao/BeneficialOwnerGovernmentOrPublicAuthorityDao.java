package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.time.LocalDate;
import java.util.List;

public class BeneficialOwnerGovernmentOrPublicAuthorityDao {

    @Field("name")
    private String name;

    @Field("principal_address")
    private AddressDao principalAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("is_service_address_same_as_principal_address")
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @Field("legal_form")
    private String legalForm;

    @Field("law_governed")
    private String lawGoverned;

    @Field("start_date")
    private LocalDate startDate;

    @Field("beneficial_owner_nature_of_control_types")
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @Field("non_legal_firm_members_nature_of_control_types")
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

    @Field("is_on_sanctions_list")
    private Boolean isOnSanctionsList;

    @Field("trust_ids")
    private List<String> trustIds;

    @Field("ceased_date")
    private LocalDate ceasedDate;

    @Field("ch_reference")
    private String chipsReference;

    @Field("id")
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDao getPrincipalAddress() {
        return principalAddress;
    }

    public void setPrincipalAddress(AddressDao principalAddress) {
        this.principalAddress = principalAddress;
    }

    public AddressDao getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressDao serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Boolean getServiceAddressSameAsPrincipalAddress() {
        return isServiceAddressSameAsPrincipalAddress;
    }

    public void setServiceAddressSameAsPrincipalAddress(Boolean serviceAddressSameAsPrincipalAddress) {
        isServiceAddressSameAsPrincipalAddress = serviceAddressSameAsPrincipalAddress;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public String getLawGoverned() {
        return lawGoverned;
    }

    public void setLawGoverned(String lawGoverned) {
        this.lawGoverned = lawGoverned;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<NatureOfControlType> getBeneficialOwnerNatureOfControlTypes() {
        return beneficialOwnerNatureOfControlTypes;
    }

    public void setBeneficialOwnerNatureOfControlTypes(List<NatureOfControlType> beneficialOwnerNatureOfControlTypes) {
        this.beneficialOwnerNatureOfControlTypes = beneficialOwnerNatureOfControlTypes;
    }

    public List<NatureOfControlType> getNonLegalFirmMembersNatureOfControlTypes() {
        return nonLegalFirmMembersNatureOfControlTypes;
    }

    public void setNonLegalFirmMembersNatureOfControlTypes(List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes) {
        this.nonLegalFirmMembersNatureOfControlTypes = nonLegalFirmMembersNatureOfControlTypes;
    }

    public Boolean getOnSanctionsList() {
        return isOnSanctionsList;
    }

    public void setOnSanctionsList(Boolean onSanctionsList) {
        isOnSanctionsList = onSanctionsList;
    }

    public List<String> getTrustIds() {
        return trustIds;
    }

    public void setTrustIds(List<String> trustIds) {
        this.trustIds = trustIds;
    }

    public void setCeasedDate(LocalDate ceasedDate) { this.ceasedDate = ceasedDate; }

    public LocalDate getCeasedDate() { return ceasedDate; }

    public String getChipsReference() {
        return chipsReference;
    }

    public void setChipsReference(String chipsReference) {
        this.chipsReference = chipsReference;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }
}
