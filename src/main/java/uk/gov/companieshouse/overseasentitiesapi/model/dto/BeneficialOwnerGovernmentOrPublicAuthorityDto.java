package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.util.List;

public class BeneficialOwnerGovernmentOrPublicAuthorityDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("principal_address")
    private AddressDto principalAddress;

    @JsonProperty("service_address")
    private AddressDto serviceAddress;

    @JsonProperty("is_service_address_same_as_principal_address")
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @JsonProperty("legal_form")
    private String legalForm;

    @JsonProperty("law_governed")
    private String lawGoverned;

    @JsonProperty("is_on_register_in_country_formed_in")
    private Boolean isOnRegisterInCountryFormedIn;

    @JsonProperty("public_register_name")
    private String publicRegisterName;

    @JsonProperty("registration_number")
    private String registrationNumber;

    @JsonProperty("beneficial_owner_nature_of_control_types")
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @JsonProperty("non_legal_firm_members_nature_of_control_types")
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

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
}
