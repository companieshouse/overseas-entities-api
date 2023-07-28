package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;

public class Address {

    @JsonInclude(NON_NULL)
    @JsonProperty("careOf")
    private String careOf;

    @JsonInclude(NON_NULL)
    @JsonProperty("poBox")
    private String poBox;

    @JsonInclude(NON_NULL)
    @JsonProperty("careOfCompany")
    private String careOfCompany;

    @JsonInclude(NON_NULL)
    @JsonProperty("houseNameNum")
    private String houseNameNum;

    @JsonInclude(NON_NULL)
    @JsonProperty("street")
    private String street;

    @JsonInclude(NON_NULL)
    @JsonProperty("area")
    private String area;

    @JsonInclude(NON_NULL)
    @JsonProperty("postTown")
    private String postTown;

    @JsonInclude(NON_NULL)
    @JsonProperty("region")
    private String region;

    @JsonInclude(NON_NULL)
    @JsonProperty("postCode")
    private String postCode;

    @JsonInclude(NON_NULL)
    @JsonProperty("country")
    private String country;

    private static String normalise(String value) {
        var text = StringUtils.normalizeSpace(value);
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text;
    }


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

        var address = (Address) o;

        return StringUtils.equalsIgnoreCase(normalise(careOf), normalise(address.careOf))
                && StringUtils.equalsIgnoreCase(normalise(poBox), normalise(address.poBox))
                && StringUtils.equalsIgnoreCase(normalise(careOfCompany),
                normalise(address.careOfCompany))
                && StringUtils.equalsIgnoreCase(normalise(
                getPremisesAndStreet()), normalise(address.getPremisesAndStreet()))
                && StringUtils.equalsIgnoreCase(normalise(area), normalise(address.area))
                && StringUtils.equalsIgnoreCase(normalise(postTown), normalise(address.postTown))
                && StringUtils.equalsIgnoreCase(normalise(region), normalise(address.region))
                && StringUtils.equalsIgnoreCase(normalise(postCode), normalise(address.postCode))
                && StringUtils.equalsIgnoreCase(normalise(country), normalise(address.country));
    }

    private String getPremisesAndStreet() {
        if (StringUtils.isBlank(houseNameNum)) {
            return street;
        }
        if (StringUtils.isBlank(street)) {
            return houseNameNum;
        }
        return houseNameNum + " " + street;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Optional.ofNullable(normalise(careOf)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(poBox)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(careOfCompany)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(houseNameNum)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(street)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(area)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(postTown)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(region)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(postCode)).map(String::toLowerCase).orElse(null),
                Optional.ofNullable(normalise(country)).map(String::toLowerCase).orElse(null)
        );
    }


}
