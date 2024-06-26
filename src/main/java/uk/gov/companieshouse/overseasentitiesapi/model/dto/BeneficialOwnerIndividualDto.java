package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.time.LocalDate;
import java.util.List;

public class BeneficialOwnerIndividualDto {

    public static final String FIRST_NAME_FIELD = "first_name";
    public static final String LAST_NAME_FIELD = "last_name";
    public static final String DATE_OF_BIRTH_FIELD = "date_of_birth";
    public static final String HAVE_DAY_OF_BIRTH_FIELD = "have_day_of_birth";
    public static final String NATIONALITY_FIELD = "nationality";
    public static final String SECOND_NATIONALITY_FIELD = "second_nationality";
    public static final String USUAL_RESIDENTIAL_ADDRESS_FIELD = "usual_residential_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD = "is_service_address_same_as_usual_residential_address";
    public static final String START_DATE_FIELD = "start_date";
    public static final String BENEFICIAL_OWNER_NATURE_OF_CONTROL_TYPES_FIELD = "beneficial_owner_nature_of_control_types";
    public static final String TRUSTEES_NATURE_OF_CONTROL_TYPES_FIELD = "trustees_nature_of_control_types";
    public static final String NON_LEGAL_FIRM_MEMBERS_NATURE_OF_CONTROL_TYPES_FIELD = "non_legal_firm_members_nature_of_control_types";
    public static final String IS_ON_SANCTIONS_LIST_FIELD = "is_on_sanctions_list";
    public static final String TRUST_IDS_FIELD = "trust_ids";
    public static final String TRUST_DATA_FIELD = "trust_data";

    public static final String CEASED_DATE_FIELD = "ceased_date";

    public static final String CH_REFERENCE_FIELD = "ch_reference";

    @JsonProperty(FIRST_NAME_FIELD)
    private String firstName;

    @JsonProperty(LAST_NAME_FIELD)
    private String lastName;

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

    @JsonProperty(START_DATE_FIELD)
    private LocalDate startDate;

    @JsonProperty(BENEFICIAL_OWNER_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> beneficialOwnerNatureOfControlTypes;

    @JsonProperty(TRUSTEES_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> trusteesNatureOfControlTypes;

    @JsonProperty(NON_LEGAL_FIRM_MEMBERS_NATURE_OF_CONTROL_TYPES_FIELD)
    private List<NatureOfControlType> nonLegalFirmMembersNatureOfControlTypes;

    @JsonProperty(IS_ON_SANCTIONS_LIST_FIELD)
    private Boolean isOnSanctionsList;

    @JsonProperty(TRUST_IDS_FIELD)
    private List<String> trustIds;

    @JsonProperty(TRUST_DATA_FIELD)
    private String trustData;

    @JsonProperty(CEASED_DATE_FIELD)
    private LocalDate ceasedDate;

    @JsonProperty(CH_REFERENCE_FIELD)
    private String chipsReference;

    @JsonProperty("id")
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

    public List<String> getTrustIds() {
        return trustIds;
    }

    public void setTrustIds(List<String> trustIds) {
        this.trustIds = trustIds;
    }

    public String getTrustData() {
        return trustData;
    }

    public void setTrustData(String trustData) {
        this.trustData = trustData;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

    public String getChipsReference() { return chipsReference; }

    public void setChipsReference(String chipsReference) {
        this.chipsReference = chipsReference;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }
}
