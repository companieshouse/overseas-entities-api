package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class IndividualDto {
    @JsonProperty("type")
    private String type;

    @JsonProperty("forename")
    private String forename;

    @JsonProperty("other_forenames")
    private String otherForenames;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("dob_day")
    private String dobDay;

    @JsonProperty("dob_month")
    private String dobMonth;

    @JsonProperty("dob_year")
    private String dobYear;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("sa_address_line_1")
    private String saAddressLine1;

    @JsonProperty("sa_address_line_2")
    private String saAddressLine2;

    @JsonProperty("sa_address_care_of")
    private String saAddressCareOf;

    @JsonProperty("sa_address_country")
    private String saAddressCountry;

    @JsonProperty("sa_address_locality")
    private String saAddressLocality;

    @JsonProperty("sa_address_po_box")
    private String saAddressPoBox;

    @JsonProperty("sa_address_postal_code")
    private String saAddressPostalCode;

    @JsonProperty("sa_address_premises")
    private String saAddressPremises;

    @JsonProperty("sa_address_region")
    private String saAddressRegion;

    @JsonProperty("ura_address_line_1")
    private String uraAddressLine1;

    @JsonProperty("ura_address_line_2")
    private String uraAddressLine2;

    @JsonProperty("ura_address_care_of")
    private String uraAddressCareOf;

    @JsonProperty("ura_address_country")
    private String uraAddressCountry;

    @JsonProperty("ura_address_locality")
    private String uraAddressLocality;

    @JsonProperty("ura_address_po_box")
    private String uraAddressPoBox;

    @JsonProperty("ura_address_postal_code")
    private String uraAddressPostalCode;

    @JsonProperty("ura_address_premises")
    private String uraAddressPremises;

    @JsonProperty("ura_address_region")
    private String uraAddressRegion;

    @JsonProperty("date_became_interested_person_day")
    private String dateBecameInterestedPersonDay;

    @JsonProperty("date_became_interested_person_month")
    private String dateBecameInterestedPersonMonth;

    @JsonProperty("date_became_interested_person_year")
    private String dateBecameInterestedPersonYear;

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

    public String getDobDay() {
        return dobDay;
    }

    public void setDobDay(String dobDay) {
        this.dobDay = dobDay;
    }

    public String getDobMonth() {
        return dobMonth;
    }

    public void setDobMonth(String dobMonth) {
        this.dobMonth = dobMonth;
    }

    public String getDobYear() {
        return dobYear;
    }

    public void setDobYear(String dobYear) {
        this.dobYear = dobYear;
    }

    public String getDateOfBirth() {
        String[] dateFields = {dobYear, dobMonth, dobDay};
        return String.join("-", dateFields);
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

    public AddressDto getServiceAddress() {
        AddressDto serviceAddress = new AddressDto();
        serviceAddress.setPropertyNameNumber(saAddressPremises);
        serviceAddress.setLine1(saAddressLine1);
        serviceAddress.setLine2(saAddressLine2);
        serviceAddress.setCounty(saAddressRegion);
        serviceAddress.setLocality(saAddressLocality);
        serviceAddress.setCountry(saAddressCountry);
        serviceAddress.setCareOf(saAddressCareOf);
        serviceAddress.setPoBox(saAddressPoBox);
        serviceAddress.setPostcode(saAddressPostalCode);
        return serviceAddress;
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
        AddressDto address = new AddressDto();
        address.setPropertyNameNumber(uraAddressPremises);
        address.setLine1(uraAddressLine1);
        address.setLine2(uraAddressLine2);
        address.setCounty(uraAddressRegion);
        address.setLocality(uraAddressLocality);
        address.setCountry(uraAddressCountry);
        address.setCareOf(uraAddressCareOf);
        address.setPoBox(uraAddressPoBox);
        address.setPostcode(uraAddressPostalCode);
        return address;
    }

    public String getDateBecameInterestedPersonDay() {
        return dateBecameInterestedPersonDay;
    }

    public void setDateBecameInterestedPersonDay(String dateBecameInterestedPersonDay) {
        this.dateBecameInterestedPersonDay = dateBecameInterestedPersonDay;
    }

    public String getDateBecameInterestedPersonMonth() {
        return dateBecameInterestedPersonMonth;
    }

    public void setDateBecameInterestedPersonMonth(String dateBecameInterestedPersonMonth) {
        this.dateBecameInterestedPersonMonth = dateBecameInterestedPersonMonth;
    }

    public String getDateBecameInterestedPersonYear() {
        return dateBecameInterestedPersonYear;
    }

    public void setDateBecameInterestedPersonYear(String dateBecameInterestedPersonYear) {
        this.dateBecameInterestedPersonYear = dateBecameInterestedPersonYear;
    }

    public String getDateBecameInterestedPerson() {
        String[] dateFields = {dateBecameInterestedPersonYear, dateBecameInterestedPersonMonth, dateBecameInterestedPersonDay};
        return String.join("-", dateFields);
    }
}
