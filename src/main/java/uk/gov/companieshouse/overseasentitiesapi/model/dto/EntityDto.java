package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class EntityDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("incorporation_country")
    private String incorporationCountry;

    @JsonProperty("principal_address")
    private AddressDto principalAddress;

    @JsonProperty("service_address")
    private AddressDto serviceAddress;

    @JsonProperty("is_service_address_same_as_principal_address")
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @JsonProperty("email")
    private String email;

    @JsonProperty("legal_form")
    private String legalForm;

    @JsonProperty("law_governed")
    private String lawGoverned;

    @JsonProperty("public_register_entity_registered_on")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publicRegisterEntityRegisteredOn;

    @JsonProperty("registration_number")
    private String registrationNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncorporationCountry() {
        return incorporationCountry;
    }

    public void setIncorporationCountry(String incorporationCountry) {
        this.incorporationCountry = incorporationCountry;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getPublicRegisterEntityRegisteredOn() {
        return publicRegisterEntityRegisteredOn;
    }

    public void setPublicRegisterEntityRegisteredOn(LocalDate publicRegisterEntityRegisteredOn) {
        this.publicRegisterEntityRegisteredOn = publicRegisterEntityRegisteredOn;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
