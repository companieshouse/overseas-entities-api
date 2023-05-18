package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class CompanyIdentificationChange extends Change {
    private static final String CHANGE_NAME = "changeOfCompanyIdentification";

    @JsonProperty("legalForm")
    private String legalForm;

    @JsonProperty("proposedLegalForm")
    private String proposedLegalForm;

    @JsonProperty("governingLaw")
    private String governingLaw;

    @JsonProperty("proposedGoverningLaw")
    private String proposedGoverningLaw;

    @JsonProperty("registerLocation")
    private String registerLocation;

    @JsonProperty("proposedRegisterLocation")
    private String proposedRegisterLocation;

    @JsonProperty("placeRegistered")
    private String placeRegistered;

    @JsonProperty("proposedPlaceRegistered")
    private String proposedPlaceRegistered;

    @JsonProperty("registrationNumber")
    private String registrationNumber;

    @JsonProperty("proposedRegistrationNumber")
    private String proposedRegistrationNumber;

    public CompanyIdentificationChange(CompanyIdentification companyIdentification,
                                       CompanyIdentification updatedCompanyIdentification){
        super.setChangeName(CHANGE_NAME);
        this.legalForm = companyIdentification.getLegalForm();
        this.proposedLegalForm = updatedCompanyIdentification.getLegalForm();
        this.governingLaw = companyIdentification.getGoverningLaw();
        this.proposedGoverningLaw = updatedCompanyIdentification.getGoverningLaw();
        this.registerLocation = companyIdentification.getRegisterLocation();
        this.proposedRegisterLocation = updatedCompanyIdentification.getRegisterLocation();
        this.placeRegistered = companyIdentification.getPlaceRegistered();
        this.proposedPlaceRegistered = updatedCompanyIdentification.getPlaceRegistered();
        this.registrationNumber = companyIdentification.getRegistrationNumber();
        this.proposedRegistrationNumber = updatedCompanyIdentification.getRegistrationNumber();
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public String getProposedLegalForm() {
        return proposedLegalForm;
    }

    public void setProposedLegalForm(String proposedLegalForm) {
        this.proposedLegalForm = proposedLegalForm;
    }

    public String getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(String governingLaw) {
        this.governingLaw = governingLaw;
    }

    public String getProposedGoverningLaw() {
        return proposedGoverningLaw;
    }

    public void setProposedGoverningLaw(String proposedGoverningLaw) {
        this.proposedGoverningLaw = proposedGoverningLaw;
    }

    public String getRegisterLocation() {
        return registerLocation;
    }

    public void setRegisterLocation(String registerLocation) {
        this.registerLocation = registerLocation;
    }

    public String getProposedRegisterLocation() {
        return proposedRegisterLocation;
    }

    public void setProposedRegisterLocation(String proposedRegisterLocation) {
        this.proposedRegisterLocation = proposedRegisterLocation;
    }

    public String getPlaceRegistered() {
        return placeRegistered;
    }

    public void setPlaceRegistered(String placeRegistered) {
        this.placeRegistered = placeRegistered;
    }

    public String getProposedPlaceRegistered() {
        return proposedPlaceRegistered;
    }

    public void setProposedPlaceRegistered(String proposedPlaceRegistered) {
        this.proposedPlaceRegistered = proposedPlaceRegistered;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getProposedRegistrationNumber() {
        return proposedRegistrationNumber;
    }

    public void setProposedRegistrationNumber(String proposedRegistrationNumber) {
        this.proposedRegistrationNumber = proposedRegistrationNumber;
    }
}

