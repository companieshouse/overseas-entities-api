package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

public class CompanyIdentificationChange extends Change {
    private static final String CHANGE_NAME = "changeOfCompanyIdentification";

    @JsonInclude(NON_NULL)
    @JsonProperty("legalForm")
    private String legalForm;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedLegalForm")
    private String proposedLegalForm;

    @JsonInclude(NON_NULL)
    @JsonProperty("governingLaw")
    private String governingLaw;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedGoverningLaw")
    private String proposedGoverningLaw;

    @JsonInclude(NON_NULL)
    @JsonProperty("registerLocation")
    private String registerLocation;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedRegisterLocation")
    private String proposedRegisterLocation;

    @JsonInclude(NON_NULL)
    @JsonProperty("placeRegistered")
    private String placeRegistered;

    @JsonInclude(NON_NULL)
    @JsonProperty("proposedPlaceRegistered")
    private String proposedPlaceRegistered;

    @JsonInclude(NON_NULL)
    @JsonProperty("registrationNumber")
    private String registrationNumber;

    @JsonInclude(NON_NULL)
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

