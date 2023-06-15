package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
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
public class PopulateUpdateSubmission {

    /**
     * Method populates values into UpdateSubmission for JSON output
     */
    public void populate(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            UpdateSubmission updateSubmission) {
        updateSubmission.setEntityNumber(overseasEntitySubmissionDto.getEntityNumber());
        updateSubmission.setUserSubmission(overseasEntitySubmissionDto);
        populateDueDiligence(overseasEntitySubmissionDto, updateSubmission);
        populatePresenter(overseasEntitySubmissionDto, updateSubmission);
        populateStatements(overseasEntitySubmissionDto, updateSubmission);
        populateFilingForDate(overseasEntitySubmissionDto, updateSubmission);
    }

    private void populateDueDiligence(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            UpdateSubmission updateSubmission) {
        var dueDiligenceDto = Optional.ofNullable(overseasEntitySubmissionDto.getDueDiligence());

        if (dueDiligenceDto.isPresent()) {
            populateDueDiligenceFiledByAgent(dueDiligenceDto.get(), updateSubmission);
        } else {
            Optional.ofNullable(overseasEntitySubmissionDto.getOverseasEntityDueDiligence())
                    .ifPresent(entityDueDiligenceDto -> populateDueDiligenceFiledByOverseasEntity(
                            entityDueDiligenceDto, updateSubmission));
        }
    }

    private void populateDueDiligenceFiledByOverseasEntity(
            OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto,
            UpdateSubmission updateSubmission) {
        var submissionDueDiligence = new DueDiligence();

        populateCommonDueDiligenceDetails(submissionDueDiligence,
                overseasEntityDueDiligenceDto.getIdentityDate(),
                overseasEntityDueDiligenceDto.getName(),
                overseasEntityDueDiligenceDto.getAddress(),
                overseasEntityDueDiligenceDto.getSupervisoryName(),
                overseasEntityDueDiligenceDto.getPartnerName(),
                overseasEntityDueDiligenceDto.getEmail());

        Optional.ofNullable(overseasEntityDueDiligenceDto.getAmlNumber())
                .ifPresent(submissionDueDiligence::setAmlRegistrationNumber);

        Optional.of(submissionDueDiligence).ifPresent(updateSubmission::setDueDiligence);
    }

    private void populateDueDiligenceFiledByAgent(DueDiligenceDto dueDiligenceDto,
            UpdateSubmission updateSubmission) {
        var submissionDueDiligence = new DueDiligence();

        populateCommonDueDiligenceDetails(submissionDueDiligence, dueDiligenceDto.getIdentityDate(),
                dueDiligenceDto.getName(), dueDiligenceDto.getAddress(),
                dueDiligenceDto.getSupervisoryName(), dueDiligenceDto.getPartnerName(),
                dueDiligenceDto.getEmail());

        Optional.ofNullable(dueDiligenceDto.getAgentCode())
                .ifPresent(submissionDueDiligence::setAgentAssuranceCode);
        Optional.ofNullable(dueDiligenceDto.getDiligence())
                .ifPresent(submissionDueDiligence::setDiligence);

        Optional.of(submissionDueDiligence).ifPresent(updateSubmission::setDueDiligence);
    }

    private void populateCommonDueDiligenceDetails(DueDiligence submissionDueDiligence,
            LocalDate identityDate, String name, AddressDto address, String supervisoryName,
            String partnerName, String email) {
        Optional.ofNullable(identityDate)
                .map(LocalDate::toString)
                .ifPresent(submissionDueDiligence::setDateChecked);
        Optional.ofNullable(name)
                .ifPresent(submissionDueDiligence::setAgentName);
        Optional.ofNullable(address)
                .ifPresent(addressDto -> submissionDueDiligence.setDueDiligenceCorrespondenceAddress(TypeConverter.addressDtoToAddress(addressDto)));
        Optional.ofNullable(supervisoryName)
                .ifPresent(submissionDueDiligence::setSupervisoryBody);
        Optional.ofNullable(partnerName)
                .ifPresent(submissionDueDiligence::setPartnerName);
        Optional.ofNullable(email)
                .ifPresent(submissionDueDiligence::setEmail);
    }

    private void populatePresenter(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            UpdateSubmission updateSubmission) {

        Optional.of(overseasEntitySubmissionDto.getPresenter())
                .ifPresent(presenterDto -> {
                    var presenter = new Presenter();
                    Optional.of(presenterDto).
                            map(PresenterDto::getEmail)
                            .ifPresent(presenter::setEmail);
                    Optional.of(presenterDto).
                            map(PresenterDto::getFullName)
                            .ifPresent(presenter::setName);
                    Optional.of(presenter)
                            .ifPresent(updateSubmission::setPresenter);
                });
    }

    private void populateStatements(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            UpdateSubmission updateSubmission) {

        Optional.ofNullable(overseasEntitySubmissionDto.getBeneficialOwnersStatement())
                .ifPresent(updateSubmission::setBeneficialOwnerStatement);

        Optional.of(overseasEntitySubmissionDto.getUpdate().isRegistrableBeneficialOwner())
                .ifPresent(updateSubmission::setAnyBOsOrMOsAddedOrCeased);
    }

    private void populateFilingForDate(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            UpdateSubmission updateSubmission) {
        Optional.ofNullable(overseasEntitySubmissionDto.getUpdate().getFilingDate())
                .ifPresent(filingDate -> {
                    var filingForDate = new FilingForDate();
                    Optional.of(filingDate).map(LocalDate::getDayOfMonth)
                            .map(Objects::toString)
                            .ifPresent(filingForDate::setDay);
                    Optional.of(filingDate).map(LocalDate::getMonth)
                            .map(Objects::toString)
                            .ifPresent(filingForDate::setMonth);
                    Optional.of(filingDate).map(LocalDate::getYear)
                            .map(Objects::toString)
                            .ifPresent(filingForDate::setYear);
                    Optional.of(filingForDate)
                            .ifPresent(updateSubmission::setFilingForDate);
                });
    }

}
