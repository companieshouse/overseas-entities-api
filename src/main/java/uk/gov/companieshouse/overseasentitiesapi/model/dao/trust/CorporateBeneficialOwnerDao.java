package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;

import static uk.gov.companieshouse.overseasentitiesapi.utils.BeneficialOwnerTypes.beneficialOwnerTypes;

public class CorporateBeneficialOwnerDao {
    @Field("type")
    private String type;

    @Field("name")
    private String name;

    @Field("registered_office_address")
    private AddressDao registeredOfficeAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("identification_country_registration")
    private String identificationCountryRegistration;

    @Field("identification_legal_authority")
    private String identificationLegalAuthority;

    @Field("identification_legal_form")
    private String identificationLegalForm;

    @Field("identification_place_registered")
    private String identificationPlaceRegistered;

    @Field("identification_registration_number")
    private String identificationRegistrationNumber;

    @Field("date_became_interested_person")
    private String dateBecameInterestedPerson;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = beneficialOwnerTypes.get(type);
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

    public AddressDao getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressDao serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getIdentificationCountryRegistration() {
        return identificationCountryRegistration;
    }

    public void setIdentificationCountryRegistration(String identificationCountryRegistration) {
        this.identificationCountryRegistration = identificationCountryRegistration;
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

    public String getDateBecameInterestedPerson() {
        return dateBecameInterestedPerson;
    }

    public void setDateBecameInterestedPerson(String dateBecameInterestedPerson) {
        this.dateBecameInterestedPerson = dateBecameInterestedPerson;
    }
}
