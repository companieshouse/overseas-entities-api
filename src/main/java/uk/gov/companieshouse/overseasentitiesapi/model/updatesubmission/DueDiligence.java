package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class DueDiligence {
    @JsonProperty("dateChecked")
    private String dateChecked;

    @JsonProperty("agentName")
    private String agentName;

    @JsonProperty("dueDiligenceCorrespondenceAddress")
    private Address dueDiligenceCorrespondenceAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("agentAssuranceCode")
    private String agentAssuranceCode;

    @JsonInclude(NON_NULL)
    @JsonProperty("amlRegistrationNumber")
    private String amlRegistrationNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("supervisoryBody")
    private String supervisoryBody;

    @JsonProperty("partnerName")
    private String partnerName;

    @JsonInclude(NON_NULL)
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

    public Address getDueDiligenceCorrespondenceAddress() {
        return dueDiligenceCorrespondenceAddress;
    }

    public void setDueDiligenceCorrespondenceAddress(Address dueDiligenceCorrespondenceAddress) {
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
