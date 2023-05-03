package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

public class ManagingOfficerCorporateDao {

    @Field("name")
    private String name;

    @Field("principal_address")
    private AddressDao principalAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("is_service_address_same_as_principal_address")
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @Field("legal_form")
    private String legalForm;

    @Field("law_governed")
    private String lawGoverned;

    @Field("is_on_register_in_country_formed_in")
    private Boolean isOnRegisterInCountryFormedIn;

    @Field("public_register_name")
    private String publicRegisterName;

    @Field("registration_number")
    private String registrationNumber;

    @Field("role_and_responsibilities")
    private String roleAndResponsibilities;

    @Field("contact_full_name")
    private String contactFullName;

    @Field("contact_email")
    private String contactEmail;

    @Field("start_date")
    private LocalDate startDate;

    @Field("resigned_on")
    private LocalDate resignedOn;

    @Field("ch_reference")
    private String chipsReference;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getResignedOn(){
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
}
