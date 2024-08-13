package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;

import java.time.LocalDate;

public class TrustCorporateDao {
    @Field("id")
    private String id;

    @Field("type")
    private BeneficialOwnerType type;

    @Field("name")
    private String name;

    @Field("registered_office_address")
    private AddressDao registeredOfficeAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("start_date")
    private LocalDate startDate;

    @Field("is_service_address_same_as_principal_address")
    private Boolean isServiceAddressSameAsPrincipalAddress;

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
    private LocalDate dateBecameInterestedPerson;

    @Field("is_on_register_in_country_formed_in")
    private Boolean onRegisterInCountryFormedIn;

    @Field("is_corporate_still_involved_in_trust")
    private Boolean isCorporateStillInvolvedInTrust;

    @Field("ceased_date")
    private LocalDate ceasedDate;

    @Field("relevant_period")
    private boolean relevantPeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BeneficialOwnerType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = BeneficialOwnerType.findByBeneficialOwnerTypeString(type);
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

    public Boolean getServiceAddressSameAsPrincipalAddress() {
        return isServiceAddressSameAsPrincipalAddress;
    }

    public void setServiceAddressSameAsPrincipalAddress(Boolean serviceAddressSameAsPrincipalAddress) {
        isServiceAddressSameAsPrincipalAddress = serviceAddressSameAsPrincipalAddress;
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

    public LocalDate getDateBecameInterestedPerson() {
        return dateBecameInterestedPerson;
    }

    public void setDateBecameInterestedPerson(LocalDate dateBecameInterestedPerson) {
        this.dateBecameInterestedPerson = dateBecameInterestedPerson;
    }

    public Boolean getOnRegisterInCountryFormedIn() {
        return onRegisterInCountryFormedIn;
    }

    public void setOnRegisterInCountryFormedIn(Boolean onRegisterInCountryFormedIn) {
        this.onRegisterInCountryFormedIn = onRegisterInCountryFormedIn;
    }

    public Boolean isCorporateStillInvolvedInTrust() {
        return isCorporateStillInvolvedInTrust;
    }

    public void setCorporateStillInvolvedInTrust(Boolean corporateStillInvolvedInTrust) {
        isCorporateStillInvolvedInTrust = corporateStillInvolvedInTrust;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

    public boolean getRelevantPeriod() { return relevantPeriod; }

    public void setRelevantPeriod(boolean relevantPeriod) { this.relevantPeriod = relevantPeriod; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
