package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto.areAnyStringsNonNull;

public class TrustIndividualDto {

    public static final String ID_FIELD = "id";
    public static final String FORENAME_FIELD = "forename";
    public static final String SURNAME_FIELD = "surname";
    public static final String DATE_OF_BIRTH_FIELD = "date_of_birth";
    public static final String TYPE_FIELD = "type";
    public static final String DATE_BECAME_INTERESTED_PERSON_FIELD = "date_became_interested_person";
    public static final String CEASED_DATE_FIELD = "ceased_date";
    public static final String NATIONALITY_FIELD = "nationality";
    public static final String SECOND_NATIONALITY_FIELD = "second_nationality";

    public static final String USUAL_RESIDENTIAL_ADDRESS_FIELD = "usual_residential_address";
    public static final String URA_ADDRESS_LINE_1_FIELD = "ura_address_line_1";
    public static final String URA_ADDRESS_LINE_2_FIELD = "ura_address_line_2";
    public static final String URA_ADDRESS_COUNTRY_FIELD = "ura_address_country";
    public static final String URA_ADDRESS_LOCALITY_FIELD = "ura_address_locality";
    public static final String URA_ADDRESS_POSTAL_CODE_FIELD = "ura_address_postal_code";
    public static final String URA_ADDRESS_PREMISES_FIELD = "ura_address_premises";
    public static final String URA_ADDRESS_REGION_FIELD = "ura_address_region";

    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String SA_ADDRESS_LINE_1_FIELD = "sa_address_line_1";
    public static final String SA_ADDRESS_LINE_2_FIELD = "sa_address_line_2";
    public static final String SA_ADDRESS_LOCALITY_FIELD = "sa_address_locality";
    public static final String SA_ADDRESS_PREMISES_FIELD = "sa_address_premises";
    public static final String SA_ADDRESS_REGION_FIELD = "sa_address_region";
    public static final String SA_ADDRESS_COUNTRY_FIELD = "sa_address_country";
    public static final String SA_ADDRESS_POSTAL_CODE_FIELD = "sa_address_postal_code";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD = "is_service_address_same_as_usual_residential_address";
    public static final String IS_INDIVIDUAL_STILL_INVOLVED_IN_TRUST_FIELD = "is_individual_still_involved_in_trust";
    public static final String RELEVANT_PERIOD_FIELD ="relevant_period";
    public static final String START_DATE_FIELD = "start_date";

    @JsonProperty(ID_FIELD)
    private String id;

    @JsonInclude(NON_NULL)
    @JsonProperty(FORENAME_FIELD)
    private String forename;

    @JsonInclude(NON_NULL)
    @JsonProperty("other_forenames")
    private String otherForenames;

    @JsonInclude(NON_NULL)
    @JsonProperty(SURNAME_FIELD)
    private String surname;

    @JsonInclude(NON_NULL)
    @JsonProperty(DATE_OF_BIRTH_FIELD)
    private LocalDate dateOfBirth;

    @JsonProperty(TYPE_FIELD)
    private String type;

    @JsonInclude(NON_NULL)
    @JsonProperty(DATE_BECAME_INTERESTED_PERSON_FIELD)
    private LocalDate dateBecameInterestedPerson;

    @JsonProperty(CEASED_DATE_FIELD)
    private LocalDate ceasedDate;

    @JsonInclude(NON_NULL)
    @JsonProperty(NATIONALITY_FIELD)
    private String nationality;

    @JsonInclude(NON_NULL)
    @JsonProperty(SECOND_NATIONALITY_FIELD)
    private String secondNationality;

    @JsonInclude(NON_NULL)
    @JsonProperty(SERVICE_ADDRESS_FIELD)
    private AddressDto serviceAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_LINE_1_FIELD)
    private String saAddressLine1;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_LINE_2_FIELD)
    private String saAddressLine2;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_care_of")
    private String saAddressCareOf;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_COUNTRY_FIELD)
    private String saAddressCountry;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_LOCALITY_FIELD)
    private String saAddressLocality;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_po_box")
    private String saAddressPoBox;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_POSTAL_CODE_FIELD)
    private String saAddressPostalCode;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_PREMISES_FIELD)
    private String saAddressPremises;

    @JsonInclude(NON_NULL)
    @JsonProperty(SA_ADDRESS_REGION_FIELD)
    private String saAddressRegion;

    @JsonProperty(START_DATE_FIELD)
    private LocalDate startDate;

    @JsonInclude(NON_NULL)
    @JsonProperty(IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD)
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty(USUAL_RESIDENTIAL_ADDRESS_FIELD)
    private AddressDto usualResidentialAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_LINE_1_FIELD)
    private String uraAddressLine1;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_LINE_2_FIELD)
    private String uraAddressLine2;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_care_of")
    private String uraAddressCareOf;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_COUNTRY_FIELD)
    private String uraAddressCountry;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_LOCALITY_FIELD)
    private String uraAddressLocality;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_po_box")
    private String uraAddressPoBox;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_POSTAL_CODE_FIELD)
    private String uraAddressPostalCode;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_PREMISES_FIELD)
    private String uraAddressPremises;

    @JsonInclude(NON_NULL)
    @JsonProperty(URA_ADDRESS_REGION_FIELD)
    private String uraAddressRegion;

    @JsonProperty(IS_INDIVIDUAL_STILL_INVOLVED_IN_TRUST_FIELD)
    private Boolean isIndividualStillInvolvedInTrust;

    @JsonProperty(RELEVANT_PERIOD_FIELD)
    private boolean relevantPeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSaAddressLine1() {
        return saAddressLine1;
    }

    public void setSaAddressLine1(String saAddressLine1) {
        this.saAddressLine1 = saAddressLine1;
    }

    public String getSaAddressLine2() {
        return saAddressLine2;
    }

    public void setSaAddressLine2(String saAddressLine2) {
        this.saAddressLine2 = saAddressLine2;
    }

    public String getSaAddressCareOf() {
        return saAddressCareOf;
    }

    public void setSaAddressCareOf(String saAddressCareOf) {
        this.saAddressCareOf = saAddressCareOf;
    }

    public String getSaAddressCountry() {
        return saAddressCountry;
    }

    public void setSaAddressCountry(String saAddressCountry) {
        this.saAddressCountry = saAddressCountry;
    }

    public String getSaAddressLocality() {
        return saAddressLocality;
    }

    public void setSaAddressLocality(String saAddressLocality) {
        this.saAddressLocality = saAddressLocality;
    }

    public String getSaAddressPoBox() {
        return saAddressPoBox;
    }

    public void setSaAddressPoBox(String saAddressPoBox) {
        this.saAddressPoBox = saAddressPoBox;
    }

    public String getSaAddressPostalCode() {
        return saAddressPostalCode;
    }

    public void setSaAddressPostalCode(String saAddressPostalCode) {
        this.saAddressPostalCode = saAddressPostalCode;
    }

    public String getSaAddressPremises() {
        return saAddressPremises;
    }

    public void setSaAddressPremises(String saAddressPremises) {
        this.saAddressPremises = saAddressPremises;
    }

    public String getSaAddressRegion() {
        return saAddressRegion;
    }

    public void setSaAddressRegion(String saAddressRegion) {
        this.saAddressRegion = saAddressRegion;
    }

    public Boolean getServiceAddressSameAsUsualResidentialAddress() {
        return isServiceAddressSameAsUsualResidentialAddress;
    }

    public void setServiceAddressSameAsUsualResidentialAddress(Boolean serviceAddressSameAsUsualResidentialAddress) {
        isServiceAddressSameAsUsualResidentialAddress = serviceAddressSameAsUsualResidentialAddress;
    }

    public AddressDto getServiceAddress() {
        if (Boolean.TRUE.equals(isServiceAddressSameAsUsualResidentialAddress)) {
            return getUsualResidentialAddress();
        }
        return getServiceAddressFromFields();
    }

    private AddressDto getServiceAddressFromFields() {
        // When converting from DTO to DAO the individual address fields will be present and need to be converted to an address object during the mapping process
        // When converting from DAO to DTO the individual fields will not be populated and so the address object just needs to be returned
        if (areAnyStringsNonNull(saAddressPremises, saAddressLine1, saAddressLine2, saAddressRegion, saAddressLocality, saAddressCountry, saAddressCareOf, saAddressPoBox, saAddressPostalCode)) {
            serviceAddress = new AddressDto();
            serviceAddress.setPropertyNameNumber(saAddressPremises);
            serviceAddress.setLine1(saAddressLine1);
            serviceAddress.setLine2(saAddressLine2);
            serviceAddress.setCounty(saAddressRegion);
            serviceAddress.setLocality(saAddressLocality);
            serviceAddress.setTown(saAddressLocality);
            serviceAddress.setCountry(saAddressCountry);
            serviceAddress.setCareOf(saAddressCareOf);
            serviceAddress.setPoBox(saAddressPoBox);
            serviceAddress.setPostcode(saAddressPostalCode);
        }
        return serviceAddress;
    }

    public void setServiceAddress(AddressDto serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getUraAddressLine1() {
        return uraAddressLine1;
    }

    public void setUraAddressLine1(String uraAddressLine1) {
        this.uraAddressLine1 = uraAddressLine1;
    }

    public String getUraAddressLine2() {
        return uraAddressLine2;
    }

    public void setUraAddressLine2(String uraAddressLine2) {
        this.uraAddressLine2 = uraAddressLine2;
    }

    public String getUraAddressCareOf() {
        return uraAddressCareOf;
    }

    public void setUraAddressCareOf(String uraAddressCareOf) {
        this.uraAddressCareOf = uraAddressCareOf;
    }

    public String getUraAddressCountry() {
        return uraAddressCountry;
    }

    public void setUraAddressCountry(String uraAddressCountry) {
        this.uraAddressCountry = uraAddressCountry;
    }

    public String getUraAddressLocality() {
        return uraAddressLocality;
    }

    public void setUraAddressLocality(String uraAddressLocality) {
        this.uraAddressLocality = uraAddressLocality;
    }

    public String getUraAddressPoBox() {
        return uraAddressPoBox;
    }

    public void setUraAddressPoBox(String uraAddressPoBox) {
        this.uraAddressPoBox = uraAddressPoBox;
    }

    public String getUraAddressPostalCode() {
        return uraAddressPostalCode;
    }

    public void setUraAddressPostalCode(String uraAddressPostalCode) {
        this.uraAddressPostalCode = uraAddressPostalCode;
    }

    public String getUraAddressPremises() {
        return uraAddressPremises;
    }

    public void setUraAddressPremises(String uraAddressPremises) {
        this.uraAddressPremises = uraAddressPremises;
    }

    public String getUraAddressRegion() {
        return uraAddressRegion;
    }

    public void setUraAddressRegion(String uraAddressRegion) {
        this.uraAddressRegion = uraAddressRegion;
    }

    public AddressDto getUsualResidentialAddress() {
        // When converting from DTO to DAO the individual address fields will be present and need to be converted to an address object during the mapping process
        // When converting from DAO to DTO the individual fields will not be populated and so the address object just needs to be returned
        if (areAnyStringsNonNull(uraAddressPremises, uraAddressLine1, uraAddressLine2, uraAddressRegion, uraAddressLocality, uraAddressCountry, uraAddressCareOf, uraAddressPoBox, uraAddressPostalCode)) {
            usualResidentialAddress = new AddressDto();
            usualResidentialAddress.setPropertyNameNumber(uraAddressPremises);
            usualResidentialAddress.setLine1(uraAddressLine1);
            usualResidentialAddress.setLine2(uraAddressLine2);
            usualResidentialAddress.setCounty(uraAddressRegion);
            usualResidentialAddress.setLocality(uraAddressLocality);
            usualResidentialAddress.setTown(uraAddressLocality);
            usualResidentialAddress.setCountry(uraAddressCountry);
            usualResidentialAddress.setCareOf(uraAddressCareOf);
            usualResidentialAddress.setPoBox(uraAddressPoBox);
            usualResidentialAddress.setPostcode(uraAddressPostalCode);
        }
        return usualResidentialAddress;
    }

    public void setUsualResidentialAddress(AddressDto usualResidentialAddress) {
        this.usualResidentialAddress = usualResidentialAddress;
    }

    public LocalDate getDateBecameInterestedPerson() {
        return dateBecameInterestedPerson;
    }

    public void setDateBecameInterestedPerson(LocalDate dateBecameInterestedPerson) {
        this.dateBecameInterestedPerson = dateBecameInterestedPerson;
    }

    public LocalDate getCeasedDate() {
        return ceasedDate;
    }

    public void setCeasedDate(LocalDate ceasedDate) {
        this.ceasedDate = ceasedDate;
    }

    public String getSecondNationality() {
        return secondNationality;
    }

    public void setSecondNationality(String secondNationality) {
        this.secondNationality = secondNationality;
    }

    public Boolean getIndividualStillInvolvedInTrust() {
        return isIndividualStillInvolvedInTrust;
    }

    public void setIndividualStillInvolvedInTrust(Boolean individualStillInvolvedInTrust) {
        isIndividualStillInvolvedInTrust = individualStillInvolvedInTrust;
    }

    public boolean getRelevantPeriod() { return relevantPeriod; }

    public void setRelevantPeriod(boolean relevantPeriod) { this.relevantPeriod = relevantPeriod; }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
