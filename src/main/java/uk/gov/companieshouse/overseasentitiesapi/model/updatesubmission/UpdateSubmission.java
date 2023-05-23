package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

import java.util.ArrayList;
import java.util.List;

public class UpdateSubmission {
    public static final String UPDATE_TYPE_FIELD = "type";
    public static final String UPDATE_USER_SUBMISSION_FIELD = "userSubmission";
    public static final String UPDATE_ENTITY_NUMBER_FIELD = "entityNumber";
    public static final String UPDATE_DUE_DILIGENCE_FIELD = "dueDiligence";
    public static final String UPDATE_PRESENTER_FIELD = "presenter";
    public static final String FILING_FOR_DATE_FIELD = "filingForDate";
    public static final String NO_CHANGES_IN_FILING_PERIOD_FIELD = "noChangesInFilingPeriodStatement";
    public static final String ANY_BOS_ADDED_CEASED_FIELD = "anyBOsOrMOsAddedOrCeased";
    public static final String BENEFICIAL_OWNERS_FIELD = "beneficialOwnerStatement";
    public static final String CHANGES_FIELD = "changes";
    public static final String ADDITIONS_FIELD = "additions";
    public static final String CESSATIONS_FIELD = "cessations";

    @JsonProperty(UPDATE_TYPE_FIELD)
    private String type = "OE02";

    @JsonProperty(UPDATE_USER_SUBMISSION_FIELD)
    private OverseasEntitySubmissionDto userSubmission;

    @JsonProperty(UPDATE_ENTITY_NUMBER_FIELD)
    private String entityNumber;

    @JsonProperty(UPDATE_DUE_DILIGENCE_FIELD)
    private DueDiligence dueDiligence;

    @JsonProperty(UPDATE_PRESENTER_FIELD)
    private Presenter presenter;

    @JsonProperty(FILING_FOR_DATE_FIELD)
    private FilingForDate filingForDate;

    @JsonProperty(NO_CHANGES_IN_FILING_PERIOD_FIELD)
    private Boolean noChangesInFilingPeriodStatement;

    @JsonProperty(ANY_BOS_ADDED_CEASED_FIELD)
    private Boolean anyBOsOrMOsAddedOrCeased;

    @JsonProperty(BENEFICIAL_OWNERS_FIELD)
    private String beneficialOwnerStatement;

    @JsonProperty(CHANGES_FIELD)
    private List<Change> changes;

    @JsonProperty(ADDITIONS_FIELD)
    private List<Addition> additions;

    @JsonProperty(CESSATIONS_FIELD)
    private List<Cessation> cessations;

    public UpdateSubmission() {
        this.changes = new ArrayList<>();
        this.additions = new ArrayList<>();
        this.cessations = new ArrayList<>();
    }

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

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
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

    public Boolean getNoChangesInFilingPeriodStatement() {
        return noChangesInFilingPeriodStatement;
    }

    public void setNoChangesInFilingPeriodStatement(Boolean noChangesInFilingPeriodStatement) {
        this.noChangesInFilingPeriodStatement = noChangesInFilingPeriodStatement;
    }

    public Boolean getAnyBOsOrMOsAddedOrCeased() {
        return anyBOsOrMOsAddedOrCeased;
    }

    public void setAnyBOsOrMOsAddedOrCeased(Boolean anyBOsOrMOsAddedOrCeased) {
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

    public List<Addition> getAdditions() {
        return additions;
    }

    public void setAdditions(List<Addition> additions) {
        this.additions = additions;
    }

    public List<Cessation> getCessations() {
        return cessations;
    }

    public void setCessations(List<Cessation> cessations) {
        this.cessations = cessations;
    }
}
