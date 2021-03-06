package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class AddressDto {

    @JsonInclude(NON_NULL)
    @JsonProperty("property_name_number")
    private String propertyNameNumber;

    @JsonInclude(NON_NULL)
    @JsonProperty("line_1")
    private String line1;

    @JsonInclude(NON_NULL)
    @JsonProperty("line_2")
    private String line2;

    @JsonInclude(NON_NULL)
    @JsonProperty("town")
    private String town;

    @JsonInclude(NON_NULL)
    @JsonProperty("county")
    private String county;

    @JsonInclude(NON_NULL)
    @JsonProperty("locality")
    private String locality;

    @JsonInclude(NON_NULL)
    @JsonProperty("country")
    private String country;

    @JsonInclude(NON_NULL)
    @JsonProperty("po_box")
    private String poBox;

    @JsonInclude(NON_NULL)
    @JsonProperty("care_of")
    private String careOf;

    @JsonInclude(NON_NULL)
    @JsonProperty("postcode")
    private String postcode;

    public String getPropertyNameNumber() {
        return propertyNameNumber;
    }

    public void setPropertyNameNumber(String propertyNameNumber) {
        this.propertyNameNumber = propertyNameNumber;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public String getCareOf() {
        return careOf;
    }

    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
