package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class BeneficialOwnerDao {
    @Field("type")
    private String type;

    @Field("forename")
    private String forename;

    @Field("other_forenames")
    private String otherForenames;

    @Field("surname")
    private String surname;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Field("nationality")
    private String nationality;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("usual_residential_address")
    private AddressDao usualResidentialAddress;

    @Field("date_became_interested_person")
    private LocalDate dateBecameInterestedPerson;

    // ===============================
    // Corporate Fields:

    @Field("name")
    private String name;

    @Field("registered_office_address")
    private AddressDao registeredOfficeAddress;

    @Field("identification_country_region")
    private String identificationCountryRegion;

    @Field("identification_legal_authority")
    private String identificationLegalAuthority;

    @Field("identification_legal_form")
    private String identificationLegalForm;

    @Field("identification_place_registered")
    private String identificationPlaceRegistered;

    @Field("identification_registration_number")
    private String identificationRegistrationNumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getOtherForenames() {
        return otherForenames;
    }

    public void setOtherForenames(String otherForenames) {
        this.otherForenames = otherForenames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public AddressDao getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressDao serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public AddressDao getUsualResidentialAddress() {
        return usualResidentialAddress;
    }

    public void setUsualResidentialAddress(AddressDao usualResidentialAddress) {
        this.usualResidentialAddress = usualResidentialAddress;
    }

    public LocalDate getDateBecameInterestedPerson() {
        return dateBecameInterestedPerson;
    }

    public void setDateBecameInterestedPerson(LocalDate dateBecameInterestedPerson) {
        this.dateBecameInterestedPerson = dateBecameInterestedPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDao getRegisteredOfficeAddress() {
        return registeredOfficeAddress;
    }

    public void setRegisteredOfficeAddress(AddressDao registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
    }

    public String getIdentificationCountryRegion() {
        return identificationCountryRegion;
    }

    public void setIdentificationCountryRegion(String identificationCountryRegion) {
        this.identificationCountryRegion = identificationCountryRegion;
    }

    public String getIdentificationLegalAuthority() {
        return identificationLegalAuthority;
    }

    public void setIdentificationLegalAuthority(String identificationLegalAuthority) {
        this.identificationLegalAuthority = identificationLegalAuthority;
    }

    public String getIdentificationLegalForm() {
        return identificationLegalForm;
    }

    public void setIdentificationLegalForm(String identificationLegalForm) {
        this.identificationLegalForm = identificationLegalForm;
    }

    public String getIdentificationPlaceRegistered() {
        return identificationPlaceRegistered;
    }

    public void setIdentificationPlaceRegistered(String identificationPlaceRegistered) {
        this.identificationPlaceRegistered = identificationPlaceRegistered;
    }

    public String getIdentificationRegistrationNumber() {
        return identificationRegistrationNumber;
    }

    public void setIdentificationRegistrationNumber(String identificationRegistrationNumber) {
        this.identificationRegistrationNumber = identificationRegistrationNumber;
    }
}
