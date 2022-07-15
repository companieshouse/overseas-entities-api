package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DueDiligenceDto {

    @JsonProperty("identity_date")
    private LocalDate identityDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("identity_address")
    private AddressDto address;
    @JsonProperty("email")
    private String email;
    @JsonProperty("supervisory_name")
    private String supervisoryName;
    @JsonProperty("aml_number")
    private String amlNumber;
    @JsonProperty("agent_code")
    private String agentCode;
    @JsonProperty("partner_name")
    private String partnerName;
    @JsonProperty("diligence")
    private String diligence;

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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
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

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getDiligence() {
        return diligence;
    }

    public void setDiligence(String diligence) {
        this.diligence = diligence;
    }
}
