package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.time.LocalDate;
import java.util.List;

public class BeneficialOwnerGovernmentOrPublicAuthorityDto {

    public static final String NAME_FIELD = "name";
    public static final String PRINCIPAL_ADDRESS_FIELD = "principal_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD = "is_service_address_same_as_principal_address";
    public static final String LEGAL_FORM_FIELD = "legal_form";
    public static final String LAW_GOVERNED_FIELD = "law_governed";
    public static final String START_DATE_FIELD = "start_date";
    public static final String BENEFICIAL_OWNER_NATURE_OF_CONTROL_TYPES_FIELD = "beneficial_owner_nature_of_control_types";
    public static final String NON_LEGAL_FIRM_MEMBERS_NATURE_OF_CONTROL_TYPES_FIELD = "non_legal_firm_members_nature_of_control_types";
    public static final String NON_LEGAL_FIRM_CONTROL_NATURE_OF_CONTROL_TYPES_FIELD = "non_legal_firm_control_nature_of_control_types";
    public static final String TRUST_NATURE_OF_CONTROL_TYPES_FIELD = "trust_control_nature_of_control_types";
    public static final String OWNER_OF_LAND_PERSON_NATURE_OF_CONTROL_JURISDICTIONS_FIELD = "owner_of_land_person_nature_of_control_jurisdictions";
    public static final String OWNER_OF_LAND_OTHER_ENTITY_NATURE_OF_CONTROL_JURISDICTIONS_FIELD = "owner_of_land_other_entity_nature_of_control_jurisdictions";
    public static final String IS_ON_SANCTIONS_LIST_FIELD = "is_on_sanctions_list";
    public static final String CEASED_DATE_FIELD = "ceased_date";
    public static final String CH_REFERENCE_FIELD = "ch_reference";
    public static final String ID_FIELD = "id";
    public static final String RELEVANT_PERIOD_FIELD ="relevant_period";

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(PRINCIPAL_ADDRESS_FIELD)
    private AddressDto principalAddress;

    @JsonProperty(SERVICE_ADDRESS_FIELD)
    private AddressDto serviceAddress;

    @JsonProperty(IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD)
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @JsonProperty(LEGAL_FORM_FIELD)
    private String legalForm;

    @JsonProperty(LAW_GOVERNED_FIELD)
    private String lawGoverned;

    @JsonProperty(START_DATE_FIELD)
    private LocalDate startDate;

    @JsonProperty(BENEFICIAL_OWNER_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @JsonProperty(NON_LEGAL_FIRM_MEMBERS_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

    @JsonProperty(NON_LEGAL_FIRM_CONTROL_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> nonLegalFirmControlNatureOfControlTypes;

    @JsonProperty(TRUST_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> trustNatureOfControlTypes;

    @JsonProperty(OWNER_OF_LAND_PERSON_NATURE_OF_CONTROL_JURISDICTIONS_FIELD)
    private List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions;

    @JsonProperty(OWNER_OF_LAND_OTHER_ENTITY_NATURE_OF_CONTROL_JURISDICTIONS_FIELD)
    private List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions;

    @JsonProperty(IS_ON_SANCTIONS_LIST_FIELD)
    private Boolean isOnSanctionsList;

    @JsonProperty(CEASED_DATE_FIELD)
    private LocalDate ceasedDate;

    @JsonProperty(CH_REFERENCE_FIELD)
    private String chipsReference;

    @JsonProperty(ID_FIELD)
    private String id;

    @JsonProperty(RELEVANT_PERIOD_FIELD)
    private boolean relevantPeriod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDto getPrincipalAddress() {
        return principalAddress;
    }

    public void setPrincipalAddress(AddressDto principalAddress) {
        this.principalAddress = principalAddress;
    }

    public AddressDto getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressDto serviceAddress) {
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

    public List<NatureOfControlType> getNonLegalFirmControlNatureOfControlTypes() {
        return nonLegalFirmControlNatureOfControlTypes;
    }

    public void setNonLegalFirmControlNatureOfControlTypes(List<NatureOfControlType> nonLegalFirmControlNatureOfControlTypes) {
        this.nonLegalFirmControlNatureOfControlTypes = nonLegalFirmControlNatureOfControlTypes;
    }

    public List<NatureOfControlType> getTrustNatureOfControlTypes() {
        return trustNatureOfControlTypes;
    }

    public void setTrustNatureOfControlTypes(List<NatureOfControlType> trustNatureOfControlTypes) {
        this.trustNatureOfControlTypes = trustNatureOfControlTypes;
    }

    public List<NatureOfControlJurisdictionType> getOwnerOfLandPersonNatureOfControlJurisdictions() {
        return ownerOfLandPersonNatureOfControlJurisdictions;
    }

    public void setOwnerOfLandPersonNatureOfControlJurisdictions(List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions) {
        this.ownerOfLandPersonNatureOfControlJurisdictions = ownerOfLandPersonNatureOfControlJurisdictions;
    }

    public List<NatureOfControlJurisdictionType> getOwnerOfLandOtherEntityNatureOfControlJurisdictions() {
        return ownerOfLandOtherEntityNatureOfControlJurisdictions;
    }

    public void setOwnerOfLandOtherEntityNatureOfControlJurisdictions(List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions) {
        this.ownerOfLandOtherEntityNatureOfControlJurisdictions = ownerOfLandOtherEntityNatureOfControlJurisdictions;
    }

    public Boolean getOnSanctionsList() {
        return isOnSanctionsList;
    }

    public void setOnSanctionsList(Boolean onSanctionsList) {
        isOnSanctionsList = onSanctionsList;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

    public String getChipsReference() { return chipsReference; }

    public void setChipsReference(String chipsReference) { this.chipsReference = chipsReference; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getRelevantPeriod() { return relevantPeriod; }

    public void setRelevantPeriod(boolean relevantPeriod) { this.relevantPeriod = relevantPeriod; }
}
