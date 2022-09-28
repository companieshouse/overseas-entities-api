package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DueDiligenceDto {

    public static final String IDENTITY_DATE_FIELD = "identity_date";
    public static final String NAME_FIELD = "name";
    public static final String IDENTITY_ADDRESS_FIELD = "identity_address";
    public static final String EMAIL_FIELD = "email";
    public static final String SUPERVISORY_NAME_FIELD = "supervisory_name";
    public static final String AML_NUMBER_FIELD = "aml_number";
    public static final String AGENT_CODE_FIELD = "agent_code";
    public static final String PARTNER_NAME_FIELD = "partner_name";
    public static final String DILIGENCE_FIELD = "diligence";

    @JsonProperty(IDENTITY_DATE_FIELD)
    private LocalDate identityDate;

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(IDENTITY_ADDRESS_FIELD)
    private AddressDto address;

    @JsonProperty(EMAIL_FIELD)
    private String email;

    @JsonProperty(SUPERVISORY_NAME_FIELD)
    private String supervisoryName;

    @JsonProperty(AML_NUMBER_FIELD)
    private String amlNumber;

    @JsonProperty(AGENT_CODE_FIELD)
    private String agentCode;

    @JsonProperty(PARTNER_NAME_FIELD)
    private String partnerName;

    @JsonProperty(DILIGENCE_FIELD)
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
