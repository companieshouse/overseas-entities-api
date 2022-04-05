package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class Entity {

    private static final String NAME_FIELD = "name";
    private static final String INCORPORATION_COUNTRY_FIELD = "incorporation_country";
    private static final String PRINCIPAL_ADDRESS_FIELD = "principal_address";
    private static final String SERVICE_ADDRESS_FIELD = "service_address";
    private static final String IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD = "is_service_address_same_as_principal_address";
    private static final String EMAIL_FIELD = "email";
    private static final String LEGAL_FORM_FIELD = "legal_form";
    private static final String LAW_GOVERNED_FIELD = "law_governed";
    private static final String PUBLIC_REGISTER_ENTITY_REGISTERED_ON_FIELD = "public_register_entity_registered_on";
    private static final String REGISTRATION_NUMBER_FIELD = "registration_number";

    @JsonProperty(NAME_FIELD)
    @Field(NAME_FIELD)
    private String name;

    @JsonProperty(INCORPORATION_COUNTRY_FIELD)
    @Field(INCORPORATION_COUNTRY_FIELD)
    private String incorporationCountry;

    @JsonProperty(PRINCIPAL_ADDRESS_FIELD)
    @Field(PRINCIPAL_ADDRESS_FIELD)
    private Address principalAddress;

    @JsonProperty(SERVICE_ADDRESS_FIELD)
    @Field(SERVICE_ADDRESS_FIELD)
    private Address serviceAddress;

    @JsonProperty(IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD)
    @Field(IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD)
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @JsonProperty(EMAIL_FIELD)
    @Field(EMAIL_FIELD)
    private String email;

    @JsonProperty(LEGAL_FORM_FIELD)
    @Field(LEGAL_FORM_FIELD)
    private String legalForm;

    @JsonProperty(LAW_GOVERNED_FIELD)
    @Field(LAW_GOVERNED_FIELD)
    private String lawGoverned;

    @JsonProperty(PUBLIC_REGISTER_ENTITY_REGISTERED_ON_FIELD)
    @JsonFormat(pattern="yyyy-MM-dd")
    @Field(PUBLIC_REGISTER_ENTITY_REGISTERED_ON_FIELD)
    private LocalDate publicRegisterEntityRegisteredOn;

    @JsonProperty(REGISTRATION_NUMBER_FIELD)
    @Field(REGISTRATION_NUMBER_FIELD)
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

    public Address getPrincipalAddress() {
        return principalAddress;
    }

    public void setPrincipalAddress(Address principalAddress) {
        this.principalAddress = principalAddress;
    }

    public Address getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(Address serviceAddress) {
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
