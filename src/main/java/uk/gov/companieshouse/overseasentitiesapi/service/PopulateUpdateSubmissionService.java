package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Component;
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
@Component
public class PopulateUpdateSubmissionService {

    /** Method populates values into UpdateSubmission for JSON output */
    public void populate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {

        updateSubmission.setUserSubmission(overseasEntitySubmissionDto);
        populateDueDiligence(overseasEntitySubmissionDto, updateSubmission);
        populatePresenter(overseasEntitySubmissionDto, updateSubmission);
        updateSubmission.setBeneficialOwnerStatement(
                overseasEntitySubmissionDto.getBeneficialOwnersStatement().getValue());
        updateSubmission.setAnyBOsOrMOsAddedOrCeased(
                overseasEntitySubmissionDto.getUpdate().isRegistrableBeneficialOwner());
        populateFilingForDate(overseasEntitySubmissionDto, updateSubmission);
    }

    void populateDueDiligence(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {

        var dueDiligenceDto = overseasEntitySubmissionDto.getDueDiligence();
        var overseasEntityDueDiligenceDto = overseasEntitySubmissionDto.getOverseasEntityDueDiligence();
        var submissionDueDiligence = new DueDiligence();

        if (overseasEntitySubmissionDto.getDueDiligence() != null) {
            populateByDueDiligenceDto(submissionDueDiligence, dueDiligenceDto);
        } else {
            populateByOEDueDiligenceDto(submissionDueDiligence, overseasEntityDueDiligenceDto);
        }
        updateSubmission.setDueDiligence(submissionDueDiligence);
    }

    void populateByDueDiligenceDto(
            DueDiligence dueDiligence, DueDiligenceDto dueDiligenceDto) {

        dueDiligence.setDateChecked(dueDiligenceDto.getIdentityDate().toString());
        dueDiligence.setAgentName(dueDiligenceDto.getName());
        dueDiligence.setDueDiligenceCorrespondenceAddress(dueDiligenceDto.getAddress());
        dueDiligence.setSupervisoryBody(dueDiligenceDto.getSupervisoryName());
        dueDiligence.setPartnerName(dueDiligenceDto.getPartnerName());
        dueDiligence.setEmail(dueDiligenceDto.getEmail());
        dueDiligence.setAgentAssuranceCode(dueDiligenceDto.getAgentCode());
        dueDiligence.setDiligence(dueDiligenceDto.getDiligence());
    }

    void populateByOEDueDiligenceDto(
            DueDiligence dueDiligence, OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto) {

        dueDiligence.setDateChecked(overseasEntityDueDiligenceDto.getIdentityDate().toString());
        dueDiligence.setAgentName(overseasEntityDueDiligenceDto.getName());
        dueDiligence.setDueDiligenceCorrespondenceAddress(overseasEntityDueDiligenceDto.getAddress());
        dueDiligence.setSupervisoryBody(overseasEntityDueDiligenceDto.getSupervisoryName());
        dueDiligence.setPartnerName(overseasEntityDueDiligenceDto.getPartnerName());
        dueDiligence.setEmail(overseasEntityDueDiligenceDto.getEmail());
        dueDiligence.setAmlRegistrationNumber(overseasEntityDueDiligenceDto.getAmlNumber());
    }

    void populateFilingForDate(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
        var filingForDate = new FilingForDate();
        if (overseasEntitySubmissionDto.getUpdate() != null &&
                overseasEntitySubmissionDto.getUpdate().getFilingDate() != null) {
            var filingDate = overseasEntitySubmissionDto.getUpdate().getFilingDate();
            filingForDate.setDay((String.valueOf(filingDate.getDayOfMonth())));
            filingForDate.setMonth((String.valueOf(filingDate.getMonth())));
            filingForDate.setYear((String.valueOf(filingDate.getYear())));
        }
        updateSubmission.setFilingForDate(filingForDate);
    }

    void populatePresenter(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
        var presenter = new Presenter();
        if (overseasEntitySubmissionDto.getPresenter() != null) {
            presenter.setEmail(overseasEntitySubmissionDto.getPresenter().getEmail());
            presenter.setName(overseasEntitySubmissionDto.getPresenter().getFullName());
        }
        updateSubmission.setPresenter(presenter);
    }
}
