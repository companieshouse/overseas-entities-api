package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManagingOfficerCorporateDto {
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

    @JsonProperty("role_and_responsibilities")
    private String roleAndResponsibilities;

    @JsonProperty("contact_full_name")
    private String contactFullName;

    @JsonProperty("contact_email")
    private String contactEmail;

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

    public String getRoleAndResponsibilities() {
        return roleAndResponsibilities;
    }

    public void setRoleAndResponsibilities(String roleAndResponsibilities) {
        this.roleAndResponsibilities = roleAndResponsibilities;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
