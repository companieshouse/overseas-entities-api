package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class CompanyIdentificationChange extends Change {
    private static final String CHANGE_NAME = "changeOfCompanyIdentification";

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedLegalForm")
    private String proposedLegalForm;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedGoverningLaw")
    private String proposedGoverningLaw;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedRegisterLocation")
    private String proposedRegisterLocation;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedPlaceRegistered")
    private String proposedPlaceRegistered;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedRegisterJurisdiction")
    private String proposedRegisterJurisdiction;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedRegistrationNumber")
    private String proposedRegistrationNumber;

    public CompanyIdentificationChange(String proposedLegalForm,
                                       String proposedGoverningLaw,
                                       String proposedRegisterLocation,
                                       String proposedPlaceRegistered,
                                       String proposedRegisterJurisdiction,
                                       String proposedRegistrationNumber) {
        super.setChangeName(CHANGE_NAME);
        this.proposedLegalForm = proposedLegalForm;
        this.proposedGoverningLaw = proposedGoverningLaw;
        this.proposedRegisterLocation = proposedRegisterLocation;
        this.proposedPlaceRegistered = proposedPlaceRegistered;
        this.proposedRegisterJurisdiction = proposedRegisterJurisdiction;
        this.proposedRegistrationNumber = proposedRegistrationNumber;
    }

    public String getProposedLegalForm() {
        return proposedLegalForm;
    }

    public void setProposedLegalForm(String proposedLegalForm) {
        this.proposedLegalForm = proposedLegalForm;
    }

    public String getProposedGoverningLaw() {
        return proposedGoverningLaw;
    }

    public void setProposedGoverningLaw(String proposedGoverningLaw) {
        this.proposedGoverningLaw = proposedGoverningLaw;
    }

    public String getProposedRegisterLocation() {
        return proposedRegisterLocation;
    }

    public void setProposedRegisterLocation(String proposedRegisterLocation) {
        this.proposedRegisterLocation = proposedRegisterLocation;
    }

    public String getProposedPlaceRegistered() {
        return proposedPlaceRegistered;
    }

    public void setProposedPlaceRegistered(String proposedPlaceRegistered) {
        this.proposedPlaceRegistered = proposedPlaceRegistered;
    }

    public String getProposedRegisterJurisdiction() {
        return proposedRegisterJurisdiction;
    }

    public void setProposedRegisterJurisdiction(String proposedRegisterJurisdiction) {
        this.proposedRegisterJurisdiction = proposedRegisterJurisdiction;
    }

    public String getProposedRegistrationNumber() {
        return proposedRegistrationNumber;
    }

    public void setProposedRegistrationNumber(String proposedRegistrationNumber) {
        this.proposedRegistrationNumber = proposedRegistrationNumber;
    }
}

