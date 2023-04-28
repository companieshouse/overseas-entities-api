package uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission.changes.Change;

import java.util.List;

public class UpdateSubmission {
    @JsonProperty("type")
    private String type = "OE02";

    @JsonProperty("userSubmission")
    private OverseasEntitySubmissionDto userSubmission;

    @JsonProperty("dueDiligence")
    private DueDiligence dueDiligence;

    @JsonProperty("presenter")
    private Presenter presenter;

    @JsonProperty("filingForDate")
    private FilingForDate filingForDate;

    @JsonProperty("noChangesInFilingPeriodStatement")
    private String noChangesInFilingPeriodStatement;

    @JsonProperty("anyBOsOrMOsAddedOrCeased")
    private String anyBOsOrMOsAddedOrCeased;

    @JsonProperty("beneficialOwnerStatement")
    private String beneficialOwnerStatement;

    @JsonProperty("changes")
    private List<Change> changes;

    @JsonProperty("additions")
    private List<Change> additions;

    @JsonProperty("cessations")
    private List<Change> cessations;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OverseasEntitySubmissionDto getUserSubmission() {
        return userSubmission;
    }

    public void setUserSubmission(OverseasEntitySubmissionDto userSubmission) {
        this.userSubmission = userSubmission;
    }

    public DueDiligence getDueDiligence() {
        return dueDiligence;
    }

    public void setDueDiligence(DueDiligence dueDiligence) {
        this.dueDiligence = dueDiligence;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public FilingForDate getFilingForDate() {
        return filingForDate;
    }

    public void setFilingForDate(FilingForDate filingForDate) {
        this.filingForDate = filingForDate;
    }

    public String getNoChangesInFilingPeriodStatement() {
        return noChangesInFilingPeriodStatement;
    }

    public void setNoChangesInFilingPeriodStatement(String noChangesInFilingPeriodStatement) {
        this.noChangesInFilingPeriodStatement = noChangesInFilingPeriodStatement;
    }

    public String getAnyBOsOrMOsAddedOrCeased() {
        return anyBOsOrMOsAddedOrCeased;
    }

    public void setAnyBOsOrMOsAddedOrCeased(String anyBOsOrMOsAddedOrCeased) {
        this.anyBOsOrMOsAddedOrCeased = anyBOsOrMOsAddedOrCeased;
    }

    public String getBeneficialOwnerStatement() {
        return beneficialOwnerStatement;
    }

    public void setBeneficialOwnerStatement(String beneficialOwnerStatement) {
        this.beneficialOwnerStatement = beneficialOwnerStatement;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    public List<Change> getAdditions() {
        return additions;
    }

    public void setAdditions(List<Change> additions) {
        this.additions = additions;
    }

    public List<Change> getCessations() {
        return cessations;
    }

    public void setCessations(List<Change> cessations) {
        this.cessations = cessations;
    }
}
