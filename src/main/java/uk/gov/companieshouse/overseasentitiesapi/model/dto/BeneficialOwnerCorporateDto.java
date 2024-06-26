package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.time.LocalDate;
import java.util.List;

public class BeneficialOwnerCorporateDto {

    public static final String NAME_FIELD = "name";
    public static final String PRINCIPAL_ADDRESS_FIELD = "principal_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD = "is_service_address_same_as_principal_address";
    public static final String LEGAL_FORM_FIELD = "legal_form";
    public static final String LAW_GOVERNED_FIELD = "law_governed";
    public static final String IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD = "is_on_register_in_country_formed_in";
    public static final String PUBLIC_REGISTER_NAME_FIELD = "public_register_name";
    public static final String REGISTRATION_NUMBER_FIELD = "registration_number";
    public static final String START_DATE_FIELD = "start_date";
    public static final String BENEFICIAL_OWNER_NATURE_OF_CONTROL_TYPES_FIELD = "beneficial_owner_nature_of_control_types";
    public static final String TRUSTEES_NATURE_OF_CONTROL_TYPES_FIELD = "trustees_nature_of_control_types";
    public static final String NON_LEGAL_FIRM_MEMBERS_NATURE_OF_CONTROL_TYPES_FIELD = "non_legal_firm_members_nature_of_control_types";
    public static final String IS_ON_SANCTIONS_LIST_FIELD = "is_on_sanctions_list";
    public static final String TRUST_IDS_FIELD = "trust_ids";
    public static final String TRUST_DATA_FIELD = "trust_data";

    public static final String CEASED_DATE_FIELD = "ceased_date";

    public static final String CH_REFERENCE_FIELD = "ch_reference";

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

    @JsonProperty(IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD)
    private Boolean isOnRegisterInCountryFormedIn;

    @JsonProperty(PUBLIC_REGISTER_NAME_FIELD)
    private String publicRegisterName;

    @JsonProperty(REGISTRATION_NUMBER_FIELD)
    private String registrationNumber;

    @JsonProperty(START_DATE_FIELD)
    private LocalDate startDate;

    @JsonProperty(BENEFICIAL_OWNER_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @JsonProperty(TRUSTEES_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> trusteesNatureOfControlTypes;

    @JsonProperty(NON_LEGAL_FIRM_MEMBERS_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

    @JsonProperty(IS_ON_SANCTIONS_LIST_FIELD)
    private Boolean isOnSanctionsList;

    @JsonProperty(TRUST_IDS_FIELD)
    private List<String> trustIds;

    @JsonProperty(TRUST_DATA_FIELD)
    private String trustData;

    @JsonProperty(CEASED_DATE_FIELD)
    private LocalDate ceasedDate;

    @JsonProperty(CH_REFERENCE_FIELD)
    private String chipsReference;

    @JsonProperty("id")
    private String id;

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

    public Boolean getOnRegisterInCountryFormedIn() {
        return isOnRegisterInCountryFormedIn;
    }

    public void setOnRegisterInCountryFormedIn(Boolean onRegisterInCountryFormedIn) {
        isOnRegisterInCountryFormedIn = onRegisterInCountryFormedIn;
    }

    public String getPublicRegisterName() {
        return publicRegisterName;
    }

    public void setPublicRegisterName(String publicRegisterName) {
        this.publicRegisterName = publicRegisterName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public List<NatureOfControlType> getTrusteesNatureOfControlTypes() {
        return trusteesNatureOfControlTypes;
    }

    public void setTrusteesNatureOfControlTypes(List<NatureOfControlType> trusteesNatureOfControlTypes) {
        this.trusteesNatureOfControlTypes = trusteesNatureOfControlTypes;
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

    public String getTrustData() {
        return trustData;
    }

    public void setTrustData(String trustData) {
        this.trustData = trustData;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

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
