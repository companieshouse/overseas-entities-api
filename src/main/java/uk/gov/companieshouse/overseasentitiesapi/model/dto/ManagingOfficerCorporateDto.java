package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class ManagingOfficerCorporateDto {

    public static final String NAME_FIELD = "name";
    public static final String PRINCIPAL_ADDRESS_FIELD = "principal_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD = "is_service_address_same_as_principal_address";
    public static final String LEGAL_FORM_FIELD = "legal_form";
    public static final String LAW_GOVERNED_FIELD = "law_governed";
    public static final String IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD = "is_on_register_in_country_formed_in";
    public static final String PUBLIC_REGISTER_NAME_FIELD = "public_register_name";
    public static final String REGISTRATION_NUMBER_FIELD = "registration_number";
    public static final String ROLE_AND_RESPONSIBILITIES_FIELD = "role_and_responsibilities";
    public static final String CONTACT_FULL_NAME_FIELD = "contact_full_name";
    public static final String CONTACT_EMAIL_FIELD = "contact_email";
    public static final String START_DATE_FIELD = "start_date";
    public static final String RESIGNED_ON_DATE_FIELD = "resigned_on";
    public static final String CH_REFERENCE_FIELD = "ch_reference";
    public static final String ID_FIELD = "id";

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

    @JsonProperty(ROLE_AND_RESPONSIBILITIES_FIELD)
    private String roleAndResponsibilities;

    @JsonProperty(CONTACT_FULL_NAME_FIELD)
    private String contactFullName;

    @JsonProperty(CONTACT_EMAIL_FIELD)
    private String contactEmail;

    @JsonProperty(START_DATE_FIELD)
    private LocalDate startDate;

    @JsonProperty(RESIGNED_ON_DATE_FIELD)
    private LocalDate resignedOn;

    @JsonProperty(CH_REFERENCE_FIELD)
    private String chipsReference;

    @JsonProperty(ID_FIELD)
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
        this.contactEmail = Objects.isNull(contactEmail) ? null : contactEmail.trim();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getResignedOn() {
        return resignedOn;
    }

    public void setResignedOn(LocalDate resignedOn) {
        this.resignedOn = resignedOn;
    }

    public String getChipsReference() {
        return chipsReference;
    }

    public void setChipsReference(String chipsReference) {
        this.chipsReference = chipsReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
