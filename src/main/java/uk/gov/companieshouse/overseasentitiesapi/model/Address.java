package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

public class Address {

    private static final String PROPERTY_NAME_NUMBER_FIELD = "property_name_number";
    private static final String LINE_1_FIELD = "line_1";
    private static final String LINE_2_FIELD = "line_2";
    private static final String TOWN_FIELD = "town";
    private static final String COUNTY_FIELD = "county";
    private static final String COUNTRY_FIELD = "country";
    private static final String POSTCODE_FIELD = "postcode";

    @JsonProperty(PROPERTY_NAME_NUMBER_FIELD)
    @Field(PROPERTY_NAME_NUMBER_FIELD)
    private String propertyNameNumber;

    @JsonProperty(LINE_1_FIELD)
    @Field(LINE_1_FIELD)
    private String line1;

    @JsonProperty(LINE_2_FIELD)
    @Field(LINE_2_FIELD)
    private String line2;

    @JsonProperty(TOWN_FIELD)
    @Field(TOWN_FIELD)
    private String town;

    @JsonProperty(COUNTY_FIELD)
    @Field(COUNTY_FIELD)
    private String county;

    @JsonProperty(COUNTRY_FIELD)
    @Field(COUNTRY_FIELD)
    private String country;

    @JsonProperty(POSTCODE_FIELD)
    @Field(POSTCODE_FIELD)
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
