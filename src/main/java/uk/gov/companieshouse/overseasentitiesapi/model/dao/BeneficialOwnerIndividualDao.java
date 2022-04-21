package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.time.LocalDate;
import java.util.List;

public class BeneficialOwnerIndividualDao {

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Field("nationality")
    private String nationality;

    @Field("usual_residential_address")
    private AddressDao usualResidentialAddress;

    @Field("service_address")
    private AddressDao serviceAddress;

    @Field("is_service_address_same_as_usual_residential_address")
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @Field("start_date")
    private LocalDate startDate;

    @Field("beneficial_owner_nature_of_control_types")
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @Field("trustees_nature_of_control_types")
    private List<NatureOfControlType> trusteesNatureOfControlTypes;

    @Field("non_legal_firm_members_nature_of_control_types")
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

    @Field("is_on_sanctions_list")
    private Boolean isOnSanctionsList;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<NatureOfControlType> getBeneficialOwnerNatureOfControlTypes() {
        return beneficialOwnerNatureOfControlTypes;
    }

    public void setBeneficialOwnerNatureOfControlTypes(List<NatureOfControlType> beneficialOwnerNatureOfControlTypes) {
        this.beneficialOwnerNatureOfControlTypes = beneficialOwnerNatureOfControlTypes;
    }

    public List<NatureOfControlType> getTrusteesNatureOfControlTypes() {
        return trusteesNatureOfControlTypes;
    }

    public void setTrusteesNatureOfControlTypes(List<NatureOfControlType> trusteesNatureOfControlTypes) {
        this.trusteesNatureOfControlTypes = trusteesNatureOfControlTypes;
    }

    public List<NatureOfControlType> getNonLegalFirmMembersNatureOfControlTypes() {
        return nonLegalFirmMembersNatureOfControlTypes;
    }

    public void setNonLegalFirmMembersNatureOfControlTypes(List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes) {
        this.nonLegalFirmMembersNatureOfControlTypes = nonLegalFirmMembersNatureOfControlTypes;
    }

    public Boolean getOnSanctionsList() {
        return isOnSanctionsList;
    }

    public void setOnSanctionsList(Boolean onSanctionsList) {
        isOnSanctionsList = onSanctionsList;
    }
}
