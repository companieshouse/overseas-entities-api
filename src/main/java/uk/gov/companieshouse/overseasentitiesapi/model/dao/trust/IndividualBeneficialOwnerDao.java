package uk.gov.companieshouse.overseasentitiesapi.model.dao.trust;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;

import static uk.gov.companieshouse.overseasentitiesapi.utils.BeneficialOwnerTypes.beneficialOwnerTypes;

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
    private String dateOfBirth;

    @Field("nationality")
    private String nationality;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("usual_residential_address")
    private AddressDao usualResidentialAddress;

    @Field("date_became_interested_person")
    private String dateBecameInterestedPerson;

    public BeneficialOwnerType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = beneficialOwnerTypes.get(type);
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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

    public String getDateBecameInterestedPerson() {
        return dateBecameInterestedPerson;
    }

    public void setDateBecameInterestedPerson(String dateBecameInterestedPerson) {
        this.dateBecameInterestedPerson = dateBecameInterestedPerson;
    }
}
