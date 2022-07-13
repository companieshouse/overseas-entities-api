package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class OverseasEntityDueDiligenceDao {

    @Field("identity_date")
    private LocalDate identityDate;
    @Field("name")
    private String name;
    @Field("identity_address")
    private AddressDao address;
    @Field("email")
    private String email;
    @Field("supervisory_name")
    private String supervisoryName;
    @Field("aml_number")
    private String amlNumber;
    @Field("partner_name")
    private String partnerName;

    public LocalDate getIdentityDate() {
        return identityDate;
    }

    public void setIdentityDate(LocalDate identityDate) {
        this.identityDate = identityDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDao getAddress() {
        return address;
    }

    public void setAddress(AddressDao address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupervisoryName() {
        return supervisoryName;
    }

    public void setSupervisoryName(String supervisoryName) {
        this.supervisoryName = supervisoryName;
    }

    public String getAmlNumber() {
        return amlNumber;
    }

    public void setAmlNumber(String amlNumber) {
        this.amlNumber = amlNumber;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
