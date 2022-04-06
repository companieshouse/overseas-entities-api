package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class EntityDao {

    @Field("name")
    private String name;

    @Field("incorporation_country")
    private String incorporationCountry;

    @Field("principal_address")
    private AddressDao principalAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("is_service_address_same_as_principal_address")
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @Field("email")
    private String email;

    @Field("legal_form")
    private String legalForm;

    @Field("law_governed")
    private String lawGoverned;

    @Field("public_register_entity_registered_on")
    private LocalDate publicRegisterEntityRegisteredOn;

    @Field("registration_number")
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
