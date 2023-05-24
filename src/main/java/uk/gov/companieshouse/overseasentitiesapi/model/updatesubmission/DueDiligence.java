package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class DueDiligence {
    @JsonProperty("dateChecked")
    private String dateChecked;

    @JsonProperty("agentName")
    private String agentName;

    @JsonProperty("dueDiligenceCorrespondenceAddress")
    private AddressDto dueDiligenceCorrespondenceAddress;

    @JsonProperty("agentAssuranceCode")
    private String agentAssuranceCode;

    @JsonProperty("amlRegistrationNumber")
    private String amlRegistrationNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("supervisoryBody")
    private String supervisoryBody;

    @JsonProperty("partnerName")
    private String partnerName;

    @JsonProperty("diligence")
    private String diligence;

    public String getDateChecked() {
        return dateChecked;
    }

    public void setDateChecked(String dateChecked) {
        this.dateChecked = dateChecked;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public AddressDto getDueDiligenceCorrespondenceAddress() {
        return dueDiligenceCorrespondenceAddress;
    }

    public void setDueDiligenceCorrespondenceAddress(AddressDto dueDiligenceCorrespondenceAddress) {
        this.dueDiligenceCorrespondenceAddress = dueDiligenceCorrespondenceAddress;
    }

    public String getAgentAssuranceCode() {
        return agentAssuranceCode;
    }

    public void setAgentAssuranceCode(String agentAssuranceCode) {
        this.agentAssuranceCode = agentAssuranceCode;
    }

    public String getAmlRegistrationNumber() {
        return amlRegistrationNumber;
    }

    public void setAmlRegistrationNumber(String amlRegistrationNumber) {
        this.amlRegistrationNumber = amlRegistrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupervisoryBody() {
        return supervisoryBody;
    }

    public void setSupervisoryBody(String supervisoryBody) {
        this.supervisoryBody = supervisoryBody;
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
