package uk.gov.companieshouse.overseasentitiesapi.service.update;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.DueDiligence;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.FilingForDate;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.Presenter;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;

/**
 * This is a helper class for FilingsService when the filing is an update. The purpose of this class
 * is to populate change submission values into UpdateSubmission.java with the ultimate gaol to
 * produce a json output of change submissions. It uses OESubmissionDTO and UpdateDTO values to
 * populate UpdateSubmission model.
 */
public class PopulateUpdateSubmission {
  private final OverseasEntitySubmissionDto overseasEntitySubmissionDto;
  private final UpdateSubmission updateSubmission;

  public PopulateUpdateSubmission(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
    this.overseasEntitySubmissionDto = overseasEntitySubmissionDto;
    this.updateSubmission = updateSubmission;
  }

  /** Method populates values into UpdateSubmission for JSON output */
  public UpdateSubmission populate() {

    updateSubmission.setUserSubmission(this.overseasEntitySubmissionDto);
    populateDueDiligence(this.overseasEntitySubmissionDto, updateSubmission);
    populatePresenter(this.overseasEntitySubmissionDto, this.updateSubmission);
    updateSubmission.setBeneficialOwnerStatement(
        this.overseasEntitySubmissionDto.getBeneficialOwnersStatement().getValue());
    updateSubmission.setAnyBOsOrMOsAddedOrCeased(
        this.overseasEntitySubmissionDto.getUpdate().isRegistrableBeneficialOwner());
    populateFilingForDate(this.overseasEntitySubmissionDto, updateSubmission);

    return updateSubmission;
  }

  public void populateDueDiligence(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {

    DueDiligenceDto dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
    OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto =
        overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
    DueDiligence submissionDueDiligence = new DueDiligence();
    submissionDueDiligence =
        (overseasEntitySubmissionDto.getDueDiligence() != null)
            ? populateByDueDiligenceDto(submissionDueDiligence, dueDiligenceDto)
            : populateByOEDueDiligenceDto(submissionDueDiligence, overseasEntityDueDiligenceDto);
    updateSubmission.setDueDiligence(submissionDueDiligence);
  }

  public DueDiligence populateByDueDiligenceDto(
      DueDiligence dueDiligence, DueDiligenceDto dueDiligenceDto) {

    dueDiligence.setDateChecked(dueDiligenceDto.getIdentityDate().toString());
    dueDiligence.setAgentName(dueDiligenceDto.getName());
    dueDiligence.setDueDiligenceCorrespondenceAddress(dueDiligenceDto.getAddress());
    dueDiligence.setSupervisoryBody(dueDiligenceDto.getSupervisoryName());
    dueDiligence.setPartnerName(dueDiligenceDto.getPartnerName());
    dueDiligence.setEmail(dueDiligenceDto.getEmail());
    dueDiligence.setAgentAssuranceCode(dueDiligenceDto.getAgentCode());
    dueDiligence.setDiligence(dueDiligenceDto.getDiligence());

    return dueDiligence;
  }

  public DueDiligence populateByOEDueDiligenceDto(
      DueDiligence dueDiligence, OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto) {

    dueDiligence.setDateChecked(overseasEntityDueDiligenceDto.getIdentityDate().toString());
    dueDiligence.setAgentName(overseasEntityDueDiligenceDto.getName());
    dueDiligence.setDueDiligenceCorrespondenceAddress(overseasEntityDueDiligenceDto.getAddress());
    dueDiligence.setSupervisoryBody(overseasEntityDueDiligenceDto.getSupervisoryName());
    dueDiligence.setPartnerName(overseasEntityDueDiligenceDto.getPartnerName());
    dueDiligence.setEmail(overseasEntityDueDiligenceDto.getEmail());
    dueDiligence.setAmlRegistrationNumber(overseasEntityDueDiligenceDto.getAmlNumber());

    return dueDiligence;
  }

  public void populateFilingForDate(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
    FilingForDate filingForDate = new FilingForDate();
    var filingDate = overseasEntitySubmissionDto.getUpdate().getFilingDate();
    filingForDate.setDay((String.valueOf(filingDate.getDayOfMonth())));
    filingForDate.setMonth((String.valueOf(filingDate.getMonth())));
    filingForDate.setYear((String.valueOf(filingDate.getYear())));
    updateSubmission.setFilingForDate(filingForDate);
  }

  public void populatePresenter(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
    Presenter presenter = new Presenter();
    if (overseasEntitySubmissionDto.getPresenter() != null) {
      presenter.setEmail(overseasEntitySubmissionDto.getPresenter().getEmail());
      presenter.setName(overseasEntitySubmissionDto.getPresenter().getFullName());
    }
    updateSubmission.setPresenter(presenter);
  }
}
