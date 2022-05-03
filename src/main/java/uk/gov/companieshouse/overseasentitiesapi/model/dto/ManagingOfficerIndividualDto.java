package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ManagingOfficerIndividualDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("has_former_names")
    private Boolean hasFormerNames;

    @JsonProperty("former_names")
    private String formerNames;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("usual_residential_address")
    private AddressDto usualResidentialAddress;

    @JsonProperty("service_address")
    private AddressDto serviceAddress;

    @JsonProperty("is_service_address_same_as_usual_residential_address")
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @JsonProperty("occupation")
    private String occupation;

    @JsonProperty("role_and_responsibilities")
    private String roleAndResponsibilities;

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public AddressDto getUsualResidentialAddress() {
        return usualResidentialAddress;
    }

    public void setUsualResidentialAddress(AddressDto usualResidentialAddress) {
        this.usualResidentialAddress = usualResidentialAddress;
    }

    public AddressDto getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressDto serviceAddress) {
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
}
