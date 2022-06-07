package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;

import java.time.LocalDate;

public class IndividualBeneficialOwnerDao {
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

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("usual_residential_address")
    private AddressDao usualResidentialAddress;

    @Field("date_became_interested_person")
    private LocalDate dateBecameInterestedPerson;

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
}
