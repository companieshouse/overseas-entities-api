package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("companyIdentification")
    private String companyIdentification;

    @JsonProperty("proposedCompanyIdentification")
    private String proposedCompanyIdentification;

    public CompanyIdentificationChange(String legalForm, String proposedLegalForm,
                                       String governingLaw, String proposedGoverningLaw,
                                       String registerLocation, String proposedRegisterLocation,
                                       String companyIdentification, String proposedCompanyIdentification){
        super.setChangeName(CHANGE_NAME);
        this.legalForm = legalForm;
        this.proposedLegalForm = proposedLegalForm;
        this.governingLaw = governingLaw;
        this.proposedGoverningLaw = proposedGoverningLaw;
        this.registerLocation = registerLocation;
        this.proposedRegisterLocation = proposedRegisterLocation;
        this.companyIdentification = companyIdentification;
        this.proposedCompanyIdentification = proposedCompanyIdentification;
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

    public String getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(String companyIdentification) {
        this.companyIdentification = companyIdentification;
    }

    public String getProposedCompanyIdentification() {
        return proposedCompanyIdentification;
    }

    public void setProposedCompanyIdentification(String proposedCompanyIdentification) {
        this.proposedCompanyIdentification = proposedCompanyIdentification;
    }
}
