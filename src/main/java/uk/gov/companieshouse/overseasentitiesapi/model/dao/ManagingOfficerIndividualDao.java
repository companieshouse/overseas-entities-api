package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class ManagingOfficerIndividualDao {

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("has_former_names")
    private Boolean hasFormerNames;

    @Field("former_names")
    private String formerNames;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Field("have_day_of_birth")
    private Boolean haveDayOfBirth;
    
    @Field("nationality")
    private String nationality;

    @Field("second_nationality")
    private String secondNationality;

    @Field("usual_residential_address")
    private AddressDao usualResidentialAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("is_service_address_same_as_usual_residential_address")
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @Field("occupation")
    private String occupation;

    @Field("role_and_responsibilities")
    private String roleAndResponsibilities;

    @Field("start_date")
    private LocalDate startDate;

    @Field("resigned_on")
    private LocalDate resignedOn;

    @Field("ch_reference")
    private String chipsReference;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getHasFormerNames() {
        return hasFormerNames;
    }

    public void setHasFormerNames(Boolean hasFormerNames) {
        this.hasFormerNames = hasFormerNames;
    }

    public String getFormerNames() {
        return formerNames;
    }

    public void setFormerNames(String formerNames) {
        this.formerNames = formerNames;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getHaveDayOfBirth() {
        return haveDayOfBirth;
    }

    public void setHaveDayOfBirth(Boolean haveDayOfBirth) {
        this.haveDayOfBirth = haveDayOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSecondNationality() {
        return secondNationality;
    }

    public void setSecondNationality(String secondNationality) {
        this.secondNationality = secondNationality;
    }

    public AddressDao getUsualResidentialAddress() {
        return usualResidentialAddress;
    }

    public void setUsualResidentialAddress(AddressDao usualResidentialAddress) {
        this.usualResidentialAddress = usualResidentialAddress;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getRoleAndResponsibilities() {
        return roleAndResponsibilities;
    }

    public void setRoleAndResponsibilities(String roleAndResponsibilities) {
        this.roleAndResponsibilities = roleAndResponsibilities;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getResignedOn() { return resignedOn; }

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
