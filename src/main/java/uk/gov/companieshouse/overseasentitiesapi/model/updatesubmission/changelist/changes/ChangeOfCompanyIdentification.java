package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

public class ChangeOfCompanyIdentification extends Change {
    private final String CHANGE_NAME = "changeOfCompanyIdentification";

    @JsonProperty("proposedLegalForm")
    private String proposedLegalForm;

    @JsonProperty("proposedGoverningLaw")
    private String proposedGoverningLaw;

    @JsonProperty("proposedRegisterLocation")
    private String proposedRegisterLocation;

    @JsonProperty("proposedCompanyIdentification")
    private String proposedCompanyIdentification;

    public ChangeOfCompanyIdentification(String proposedLegalForm,
                                         String proposedGoverningLaw,
                                         String proposedRegisterLocation,
                                         String proposedCompanyIdentification){
        super.setChangeName(CHANGE_NAME);
        this.proposedLegalForm = proposedLegalForm;
        this.proposedGoverningLaw = proposedGoverningLaw;
        this.proposedRegisterLocation = proposedRegisterLocation;
        this.proposedCompanyIdentification = proposedCompanyIdentification;
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

    public String getProposedCompanyIdentification() {
        return proposedCompanyIdentification;
    }

    public void setProposedCompanyIdentification(String proposedCompanyIdentification) {
        this.proposedCompanyIdentification = proposedCompanyIdentification;
    }
}
