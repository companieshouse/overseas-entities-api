package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class AddressDto {

    public static final String PROPERTY_NAME_NUMBER_FIELD = "property_name_number";
    public static final String LINE_1_FIELD = "line_1";
    public static final String LINE_2_FIELD = "line_2";
    public static final String TOWN_FIELD = "town";
    public static final String COUNTY_FIELD = "county";
    public static final String LOCALITY_FIELD = "locality";
    public static final String COUNTRY_FIELD = "country";
    public static final String PO_BOX_FIELD = "po_box";
    public static final String CARE_OF_FIELD = "care_of";
    public static final String POSTCODE_FIELD = "postcode";

    @JsonInclude(NON_NULL)
    @JsonProperty(PROPERTY_NAME_NUMBER_FIELD)
    private String propertyNameNumber;

    @JsonInclude(NON_NULL)
    @JsonProperty(LINE_1_FIELD)
    private String line1;

    @JsonInclude(NON_NULL)
    @JsonProperty(LINE_2_FIELD)
    private String line2;

    @JsonInclude(NON_NULL)
    @JsonProperty(TOWN_FIELD)
    private String town;

    @JsonInclude(NON_NULL)
    @JsonProperty(COUNTY_FIELD)
    private String county;

    @JsonInclude(NON_NULL)
    @JsonProperty(LOCALITY_FIELD)
    private String locality;

    @JsonInclude(NON_NULL)
    @JsonProperty(COUNTRY_FIELD)
    private String country;

    @JsonInclude(NON_NULL)
    @JsonProperty(PO_BOX_FIELD)
    private String poBox;

    @JsonInclude(NON_NULL)
    @JsonProperty(CARE_OF_FIELD)
    private String careOf;

    @JsonInclude(NON_NULL)
    @JsonProperty(POSTCODE_FIELD)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressDto that = (AddressDto) o;

        return Objects.equals(propertyNameNumber, that.propertyNameNumber)
                && Objects.equals(line1, that.line1)
                && Objects.equals(line2, that.line2)
                && Objects.equals(town, that.town)
                && Objects.equals(county, that.county)
                && Objects.equals(locality, that.locality)
                && Objects.equals(country, that.country)
                && Objects.equals(poBox, that.poBox)
                && Objects.equals(careOf, that.careOf)
                && Objects.equals(postcode, that.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyNameNumber, line1, line2, town, county, locality, country, poBox, careOf, postcode);
    }
}
