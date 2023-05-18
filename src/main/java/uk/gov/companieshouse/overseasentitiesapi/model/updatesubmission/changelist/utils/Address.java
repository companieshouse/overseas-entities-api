package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Address {
    @JsonProperty("careOf")
    private String careOf;

    @JsonProperty("poBox")
    private String poBox;

    @JsonProperty("careOfCompany")
    private String careOfCompany;

    @JsonProperty("houseNameNum")
    private String houseNameNum;

    @JsonProperty("street")
    private String street;

    @JsonProperty("area")
    private String area;

    @JsonProperty("postTown")
    private String postTown;

    @JsonProperty("region")
    private String region;

    @JsonProperty("postCode")
    private String postCode;

    @JsonProperty("country")
    private String country;

    public String getCareOf() {
        return careOf;
    }

    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public String getCareOfCompany() {
        return careOfCompany;
    }

    public void setCareOfCompany(String careOfCompany) {
        this.careOfCompany = careOfCompany;
    }

    public String getHouseNameNum() {
        return houseNameNum;
    }

    public void setHouseNameNum(String houseNameNum) {
        this.houseNameNum = houseNameNum;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPostTown() {
        return postTown;
    }

    public void setPostTown(String postTown) {
        this.postTown = postTown;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;
        return Objects.equals(careOf, address.careOf)
                && Objects.equals(poBox, address.poBox)
                && Objects.equals(careOfCompany, address.careOfCompany)
                && Objects.equals(houseNameNum, address.houseNameNum)
                && Objects.equals(street, address.street)
                && Objects.equals(area, address.area)
                && Objects.equals(postTown, address.postTown)
                && Objects.equals(region, address.region)
                && Objects.equals(postCode, address.postCode)
                && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(careOf, poBox, careOfCompany, houseNameNum, street,
                area, postTown, region, postCode, country);
    }
}
