package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto.areAnyStringsNonNull;

public class TrustIndividualDto {
    @JsonProperty("type")
    private String type;

    @JsonInclude(NON_NULL)
    @JsonProperty("forename")
    private String forename;

    @JsonInclude(NON_NULL)
    @JsonProperty("other_forenames")
    private String otherForenames;

    @JsonInclude(NON_NULL)
    @JsonProperty("surname")
    private String surname;

    @JsonInclude(NON_NULL)
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonInclude(NON_NULL)
    @JsonProperty("nationality")
    private String nationality;

    @JsonInclude(NON_NULL)
    @JsonProperty("second_nationality")
    private String secondNationality;

    @JsonInclude(NON_NULL)
    @JsonProperty("service_address")
    private AddressDto serviceAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_line_1")
    private String saAddressLine1;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_line_2")
    private String saAddressLine2;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_care_of")
    private String saAddressCareOf;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_country")
    private String saAddressCountry;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_locality")
    private String saAddressLocality;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_po_box")
    private String saAddressPoBox;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_postal_code")
    private String saAddressPostalCode;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_premises")
    private String saAddressPremises;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_region")
    private String saAddressRegion;

    @JsonInclude(NON_NULL)
    @JsonProperty("is_service_address_same_as_usual_residential_address")
    private Boolean isServiceAddressSameAsUsualResidentialAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("usual_residential_address")
    private AddressDto usualResidentialAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_line_1")
    private String uraAddressLine1;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_line_2")
    private String uraAddressLine2;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_care_of")
    private String uraAddressCareOf;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_country")
    private String uraAddressCountry;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_locality")
    private String uraAddressLocality;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_po_box")
    private String uraAddressPoBox;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_postal_code")
    private String uraAddressPostalCode;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_premises")
    private String uraAddressPremises;

    @JsonInclude(NON_NULL)
    @JsonProperty("ura_address_region")
    private String uraAddressRegion;

    @JsonInclude(NON_NULL)
    @JsonProperty("date_became_interested_person")
    private LocalDate dateBecameInterestedPerson;

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
        // When converting from DTO to DAO the individual address fields will be present and need to be converted to an address object during the mapping process
        // When converting from DAO to DTO the individual fields will not be populated and so the address object just needs to be returned
        if (areAnyStringsNonNull(saAddressPremises, saAddressLine1, saAddressLine2, saAddressRegion, saAddressLocality, saAddressCountry, saAddressCareOf, saAddressPoBox, saAddressPostalCode)) {
            serviceAddress = new AddressDto();
            serviceAddress.setPropertyNameNumber(saAddressPremises);
            serviceAddress.setLine1(saAddressLine1);
            serviceAddress.setLine2(saAddressLine2);
            serviceAddress.setCounty(saAddressRegion);
            serviceAddress.setLocality(saAddressLocality);
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

    public String getSecondNationality() {
        return secondNationality;
    }

    public void setSecondNationality(String secondNationality) {
        this.secondNationality = secondNationality;
    }

}
