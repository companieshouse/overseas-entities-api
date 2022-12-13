package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class EntityDto {

    public static final String INCORPORATION_COUNTRY_FIELD = "incorporation_country";
    public static final String PRINCIPAL_ADDRESS_FIELD = "principal_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD = "is_service_address_same_as_principal_address";
    public static final String EMAIL_PROPERTY_FIELD = "email";
    public static final String LEGAL_FORM_FIELD = "legal_form";
    public static final String LAW_GOVERNED_FIELD = "law_governed";
    public static final String PUBLIC_REGISTER_NAME_FIELD = "public_register_name";
    public static final String PUBLIC_REGISTER_JURISDICTION_FIELD = "public_register_jurisdiction";
    public static final String REGISTRATION_NUMBER_FIELD = "registration_number";
    public static final String IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD = "is_on_register_in_country_formed_in";

    @JsonProperty(INCORPORATION_COUNTRY_FIELD)
    private String incorporationCountry;

    @JsonProperty(PRINCIPAL_ADDRESS_FIELD)
    private AddressDto principalAddress;

    @JsonProperty(SERVICE_ADDRESS_FIELD)
    private AddressDto serviceAddress;

    @JsonProperty(IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD)
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @JsonProperty(EMAIL_PROPERTY_FIELD)
    private String email;

    @JsonProperty(LEGAL_FORM_FIELD)
    private String legalForm;

    @JsonProperty(LAW_GOVERNED_FIELD)
    private String lawGoverned;

    @JsonProperty(PUBLIC_REGISTER_NAME_FIELD)
    private String publicRegisterName;

    @JsonProperty(PUBLIC_REGISTER_JURISDICTION_FIELD)
    private String publicRegisterJurisdiction;

    @JsonProperty(REGISTRATION_NUMBER_FIELD)
    private String registrationNumber;

    @JsonProperty(IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD)
    private boolean isOnRegisterInCountryFormedIn;

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
        this.email = Objects.isNull(email) ? null : email.trim();
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

    public String getPublicRegisterJurisdiction() {
        return publicRegisterJurisdiction;
    }

    public void setPublicRegisterJurisdiction(String publicRegisterJurisdiction) {
        this.publicRegisterJurisdiction = publicRegisterJurisdiction;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
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
