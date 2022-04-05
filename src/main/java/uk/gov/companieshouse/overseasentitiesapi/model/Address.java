package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    @JsonProperty("property_name_number")
    private String propertyNameNumber;

    @JsonProperty("line_1")
    private String line1;

    @JsonProperty("line_2")
    private String line2;

    @JsonProperty("town")
    private String town;

    @JsonProperty("county")
    private String county;

    @JsonProperty("country")
    private String country;

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
