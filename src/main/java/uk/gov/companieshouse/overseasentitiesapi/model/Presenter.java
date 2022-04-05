package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Presenter {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("role")
    private String role;

    @JsonProperty("role_title")
    private String roleTitle;

    @JsonProperty("anti_money_laundering_registration_number")
    private String antiMoneyLaunderingRegistrationNumber;


    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getAntiMoneyLaunderingRegistrationNumber() {
        return antiMoneyLaunderingRegistrationNumber;
    }

    public void setAntiMoneyLaunderingRegistrationNumber(String antiMoneyLaunderingRegistrationNumber) {
        this.antiMoneyLaunderingRegistrationNumber = antiMoneyLaunderingRegistrationNumber;
    }
}
