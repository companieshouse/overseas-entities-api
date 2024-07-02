package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ManagingOfficerIndividualDto {

    public static final String FIRST_NAME_FIELD = "first_name";
    public static final String LAST_NAME_FIELD = "last_name";
    public static final String HAS_FORMER_NAMES_FIELD = "has_former_names";
    public static final String FORMER_NAMES_FIELD = "former_names";
    public static final String DATE_OF_BIRTH_FIELD = "date_of_birth";
    public static final String HAVE_DAY_OF_BIRTH_FIELD = "have_day_of_birth";
    public static final String NATIONALITY_FIELD = "nationality";
    public static final String SECOND_NATIONALITY_FIELD = "second_nationality";
    public static final String USUAL_RESIDENTIAL_ADDRESS_FIELD = "usual_residential_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD = "is_service_address_same_as_usual_residential_address";
    public static final String OCCUPATION_FIELD = "occupation";
    public static final String ROLE_AND_RESPONSIBILITIES_FIELD = "role_and_responsibilities";
    public static final String START_DATE_FIELD = "start_date";
    public static final String RESIGNED_ON_DATE_FIELD = "resigned_on";
    public static final String CH_REFERENCE_FIELD = "ch_reference";
    public static final String ID_FIELD = "id";

    @JsonProperty(FIRST_NAME_FIELD)
    private String firstName;

    @JsonProperty(LAST_NAME_FIELD)
    private String lastName;

    @JsonProperty(HAS_FORMER_NAMES_FIELD)
    private Boolean hasFormerNames;

    @JsonProperty(FORMER_NAMES_FIELD)
    private String formerNames;

    @JsonProperty(DATE_OF_BIRTH_FIELD)
    private LocalDate dateOfBirth;

    @JsonInclude(NON_NULL)
    @JsonProperty(HAVE_DAY_OF_BIRTH_FIELD)
    private Boolean haveDayOfBirth;

    @JsonProperty(NATIONALITY_FIELD)
    private String nationality;

    @JsonProperty(SECOND_NATIONALITY_FIELD)
    private String secondNationality;

    @JsonProperty(USUAL_RESIDENTIAL_ADDRESS_FIELD)
    private AddressDto usualResidentialAddress;

    @JsonProperty(SERVICE_ADDRESS_FIELD)
    private AddressDto serviceAddress;

    @JsonProperty(IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD)
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @JsonProperty(OCCUPATION_FIELD)
    private String occupation;

    @JsonProperty(ROLE_AND_RESPONSIBILITIES_FIELD)
    private String roleAndResponsibilities;

    @JsonProperty(START_DATE_FIELD)
    private LocalDate startDate;

    @JsonProperty(RESIGNED_ON_DATE_FIELD)
    private LocalDate resignedOn;

    @JsonProperty(CH_REFERENCE_FIELD)
    private String chipsReference;

    @JsonProperty(ID_FIELD)
    private String id;

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

    public LocalDate getResignedOn() {
        return resignedOn;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setResignedOn(LocalDate resignedOn) {
        this.resignedOn = resignedOn;
    }

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
}
