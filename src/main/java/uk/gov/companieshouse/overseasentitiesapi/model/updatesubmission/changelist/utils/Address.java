package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("id")
  private String id;

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

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("roAddressOverride")
  private String roAddressOverride;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("noAddressOverride")
  private String noAddressOverride;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getRoAddressOverride() {
    return roAddressOverride;
  }

  public void setRoAddressOverride(String roAddressOverride) {
    this.roAddressOverride = roAddressOverride;
  }

  public String getNoAddressOverride() {
    return noAddressOverride;
  }

  public void setNoAddressOverride(String noAddressOverride) {
    this.noAddressOverride = noAddressOverride;
  }

}

