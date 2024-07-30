package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Field;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;

public class TrustIndividualDao {
    @Field("id")
    private String id;

    @Field("type")
    private BeneficialOwnerType type;

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

    @Field("second_nationality")
    private String secondNationality;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("is_service_address_same_as_usual_residential_address")
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @Field("usual_residential_address")
    private AddressDao usualResidentialAddress;

    @Field("date_became_interested_person")
    private LocalDate dateBecameInterestedPerson;

    @Field("ceased_date")
    private LocalDate ceasedDate;

    @Field("is_individual_still_involved_in_trust")
    private Boolean isIndividualStillInvolvedInTrust;

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

    public Boolean getServiceAddressSameAsUsualResidentialAddress() {
        return isServiceAddressSameAsUsualResidentialAddress;
    }

    public void setServiceAddressSameAsUsualResidentialAddress(Boolean serviceAddressSameAsUsualResidentialAddress) {
        isServiceAddressSameAsUsualResidentialAddress = serviceAddressSameAsUsualResidentialAddress;
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

    public String getSecondNationality() {
        return secondNationality;
    }

    public void setSecondNationality(String secondNationality) {
        this.secondNationality = secondNationality;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

    public Boolean getIndividualStillInvolvedInTrust() {
        return isIndividualStillInvolvedInTrust;
    }

    public void setIndividualStillInvolvedInTrust(Boolean individualStillInvolvedInTrust) {
        isIndividualStillInvolvedInTrust = individualStillInvolvedInTrust;
    }

    public boolean getRelevantPeriod() { return relevantPeriod; }

    public void setRelevantPeriod(boolean relevantPeriod) { this.relevantPeriod = relevantPeriod; }
}
