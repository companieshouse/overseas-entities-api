package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

public class Presenter {

    private static final String FULL_NAME_FIELD = "full_name";
    private static final String PHONE_NUMBER_FIELD = "phone_number";
    private static final String ROLE_FIELD = "role";
    private static final String ROLE_TITLE_FIELD = "role_title";
    private static final String ANTI_MONEY_LAUNDERING_REGISTRATION_NUMBER_FIELD = "anti_money_laundering_registration_number";

    @JsonProperty(FULL_NAME_FIELD)
    @Field(FULL_NAME_FIELD)
    private String fullName;

    @JsonProperty(PHONE_NUMBER_FIELD)
    @Field(PHONE_NUMBER_FIELD)
    private String phoneNumber;

    @JsonProperty(ROLE_FIELD)
    @Field(ROLE_FIELD)
    private String role;

    @JsonProperty(ROLE_TITLE_FIELD)
    @Field(ROLE_TITLE_FIELD)
    private String roleTitle;

    @JsonProperty(ANTI_MONEY_LAUNDERING_REGISTRATION_NUMBER_FIELD)
    @Field(ANTI_MONEY_LAUNDERING_REGISTRATION_NUMBER_FIELD)
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
