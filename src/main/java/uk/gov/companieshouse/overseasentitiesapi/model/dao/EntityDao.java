package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

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

    @Field("public_register_name")
    private String publicRegisterName;

    @Field("public_register_jurisdiction")
    private String publicRegisterJurisdiction;

    @Field("registration_number")
    private String registrationNumber;

    @Field("is_on_register_in_country_formed_in")
    private boolean isOnRegisterInCountryFormedIn;

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

    public String getPublicRegisterName() {
        return publicRegisterName;
    }

    public void setPublicRegisterName(String publicRegisterName) {
        this.publicRegisterName = publicRegisterName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getPublicRegisterJurisdiction() {
        return publicRegisterJurisdiction;
    }

    public void setPublicRegisterJurisdiction(String publicRegisterJurisdiction) {
        this.publicRegisterJurisdiction = publicRegisterJurisdiction;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public boolean isOnRegisterInCountryFormedIn() {
        return isOnRegisterInCountryFormedIn;
    }

    public void setOnRegisterInCountryFormedIn(boolean onRegisterInCountryFormedIn) {
        isOnRegisterInCountryFormedIn = onRegisterInCountryFormedIn;
    }
}
