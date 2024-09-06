package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
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

    @Field("start_date")
    private LocalDate startDate;

    @Field("beneficial_owner_nature_of_control_types")
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @Field("trustees_nature_of_control_types")
    private List<NatureOfControlType> trusteesNatureOfControlTypes;

    @Field("non_legal_firm_members_nature_of_control_types")
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

    @Field("trust_control_nature_of_control_types")
    private List<NatureOfControlType> trustNatureOfControlTypes;

    @Field("owner_of_land_person_nature_of_control_jurisdictions")
    private List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions;

    @Field("owner_of_land_other_entity_nature_of_control_jurisdictions")
    private List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions;

    @Field("is_on_sanctions_list")
    private Boolean isOnSanctionsList;

    @Field("trust_ids")
    private List<String> trustIds;

    @Field("ceased_date")
    private LocalDate ceasedDate;

    @Field("ch_reference")
    private String chipsReference;

    @Field("id")
    private String id;

    @Field("relevant_period")
    private boolean relevantPeriod;

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

    public List<NatureOfControlType> getTrustNatureOfControlTypes() {
        return trustNatureOfControlTypes;
    }

    public void setTrustNatureOfControlTypes(List<NatureOfControlType> trustNatureOfControlTypes) {
        this.trustNatureOfControlTypes = trustNatureOfControlTypes;
    }

    public List<NatureOfControlJurisdictionType> getOwnerOfLandPersonNatureOfControlJurisdictions() {
        return ownerOfLandPersonNatureOfControlJurisdictions;
    }

    public void setOwnerOfLandPersonNatureOfControlJurisdictions(List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions) {
        this.ownerOfLandPersonNatureOfControlJurisdictions = ownerOfLandPersonNatureOfControlJurisdictions;
    }

    public List<NatureOfControlJurisdictionType> getOwnerOfLandOtherEntityNatureOfControlJurisdictions() {
        return ownerOfLandOtherEntityNatureOfControlJurisdictions;
    }

    public void setOwnerOfLandOtherEntityNatureOfControlJurisdictions(List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions) {
        this.ownerOfLandOtherEntityNatureOfControlJurisdictions = ownerOfLandOtherEntityNatureOfControlJurisdictions;
    }

    public Boolean getOnSanctionsList() {
        return isOnSanctionsList;
    }

    public void setOnSanctionsList(Boolean onSanctionsList) {
        isOnSanctionsList = onSanctionsList;
    }

    public List<String> getTrustIds() {
        return trustIds;
    }

    public void setTrustIds(List<String> trustIds) {
        this.trustIds = trustIds;
    }

    public void setCeasedDate(LocalDate ceasedDate) { this.ceasedDate = ceasedDate; }

    public LocalDate getCeasedDate() { return ceasedDate; }

    public String getChipsReference() {
        return chipsReference;
    }

    public void setChipsReference(String chipsReference) {
        this.chipsReference = chipsReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getRelevantPeriod() { return relevantPeriod; }

    public void setRelevantPeriod(boolean relevantPeriod) { this.relevantPeriod = relevantPeriod; }
}
