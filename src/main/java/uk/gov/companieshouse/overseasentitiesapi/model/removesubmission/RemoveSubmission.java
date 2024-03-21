package uk.gov.companieshouse.overseasentitiesapi.model.removesubmission;

import java.util.ArrayList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.DueDiligence;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.FilingForDate;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.Presenter;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

import java.util.ArrayList;
import java.util.List;

public class RemoveSubmission {
    public static final String REMOVE_TYPE_FIELD = "type";
    public static final String REMOVE_USER_SUBMISSION_FIELD = "userSubmission";
    public static final String REMOVE_ENTITY_NUMBER_FIELD = "entityNumber";
    public static final String REMOVE_ENTITY_NAME_FIELD = "entityName";
    public static final String REMOVE_DUE_DILIGENCE_FIELD = "dueDiligence";
    public static final String REMOVE_PRESENTER_FIELD = "presenter";
    public static final String FILING_FOR_DATE_FIELD = "filingForDate";
    public static final String ANY_BOS_ADDED_CEASED_FIELD = "anyBOsOrMOsAddedOrCeased";
    public static final String BENEFICIAL_OWNERS_FIELD = "beneficialOwnerStatement";
    public static final String CHANGES_FIELD = "changes";
    public static final String ADDITIONS_FIELD = "additions";
    public static final String CESSATIONS_FIELD = "cessations";
    public static final String TRUSTS_ADDITIONS_FIELD = "trusts";

    @JsonProperty(REMOVE_TYPE_FIELD)
    private String type = "OE03";

    @JsonProperty(REMOVE_USER_SUBMISSION_FIELD)
    private OverseasEntitySubmissionDto userSubmission;

    @JsonProperty(REMOVE_ENTITY_NUMBER_FIELD)
    private String entityNumber;

    @JsonProperty(REMOVE_ENTITY_NAME_FIELD)
    private String entityName;

    @JsonProperty(REMOVE_DUE_DILIGENCE_FIELD)
    private DueDiligence dueDiligence;

    @JsonProperty(REMOVE_PRESENTER_FIELD)
    private Presenter presenter;

    @JsonProperty(FILING_FOR_DATE_FIELD)
    private FilingForDate filingForDate;

    @JsonProperty(ANY_BOS_ADDED_CEASED_FIELD)
    private Boolean anyBOsOrMOsAddedOrCeased;

    @JsonProperty(BENEFICIAL_OWNERS_FIELD)
    private BeneficialOwnersStatementType beneficialOwnerStatement;


    @JsonInclude(NON_NULL)
    @JsonProperty(CHANGES_FIELD)
    private List<Change> changes;

    @JsonInclude(NON_NULL)
    @JsonProperty(ADDITIONS_FIELD)
    private List<Addition> additions;

    @JsonInclude(NON_NULL)
    @JsonProperty(CESSATIONS_FIELD)
    private List<Cessation> cessations;

    @JsonInclude(NON_NULL)
    @JsonProperty(TRUSTS_ADDITIONS_FIELD)
    private List<TrustDataDto> trustAdditions;

    public RemoveSubmission() {
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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

    public Boolean getAnyBOsOrMOsAddedOrCeased() {
        return anyBOsOrMOsAddedOrCeased;
    }

    public void setAnyBOsOrMOsAddedOrCeased(Boolean anyBOsOrMOsAddedOrCeased) {
        this.anyBOsOrMOsAddedOrCeased = anyBOsOrMOsAddedOrCeased;
    }

    public BeneficialOwnersStatementType getBeneficialOwnerStatement() {
        return beneficialOwnerStatement;
    }

    public void setBeneficialOwnerStatement(BeneficialOwnersStatementType beneficialOwnerStatement) {
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

    public List<TrustDataDto> getTrustAdditions() {
        return trustAdditions;
    }

    public void setTrustAdditions(List<TrustDataDto> trustAdditions) {
        this.trustAdditions = trustAdditions;
    }
}
