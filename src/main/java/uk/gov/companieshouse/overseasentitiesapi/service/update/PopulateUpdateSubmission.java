package uk.gov.companieshouse.overseasentitiesapi.service.update;

import org.apache.commons.lang.StringUtils;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.DueDiligence;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.FilingForDate;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.Presenter;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This is a helper class for FilingsService when the filing is an update.
 * The purpose of this class is to populate change submission values into
 * UpdateSubmission.java with the ultimate gaol to produce a json output of
 * change submissions. It uses  OESubmissionDTO and UpdateDTO values to populate
 * JSON template.
 */
public class PopulateUpdateSubmission {
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private UpdateSubmission updateSubmission;

    /* No arg constructor*/
    public PopulateUpdateSubmission() {

    }

    public PopulateUpdateSubmission(OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
        this.overseasEntitySubmissionDto    = overseasEntitySubmissionDto;
        this.updateSubmission               = updateSubmission;
    }

    /* method populates values into UpdateSubmission for JSON output */
public UpdateSubmission populate() {
    //updateSubmission.type(); Type is already set in UserSubmission, has a default value of "OE02";
    updateSubmission.setUserSubmission(this.overseasEntitySubmissionDto);
    populateDueDiligence(this.overseasEntitySubmissionDto, updateSubmission);
    populatePresenter(this.overseasEntitySubmissionDto, this.updateSubmission);
    // BOStatement TBD, already contacted Matthew Culpin to over-ride toString below//
    updateSubmission.setBeneficialOwnerStatement(
            this.overseasEntitySubmissionDto.getBeneficialOwnersStatement().toString());

    updateSubmission.setAnyBOsOrMOsAddedOrCeased(
            this.overseasEntitySubmissionDto.getUpdate()
                    .isRegistrableBeneficialOwner()
    );
    //contact details, --> overseasEntitySubmissionDto.getPresenter().getEmail();


    // TBD: NoChangesInFilingPeriodStatement will be done in https://companieshouse.atlassian.net/browse/UAR-461//
    // TBD updateSubmission.setNoChangesInFilingPeriodStatement(); //

    return updateSubmission;
}

public void populateDueDiligence(OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
    DueDiligenceDto         dueDiligenceDto     = overseasEntitySubmissionDto.getDueDiligence(); //TBD .. which DueDiligence to use??//
    DueDiligence            dueDiligence        = new DueDiligence();
    dueDiligence.setDueDiligenceCorrespondenceAddress(dueDiligenceDto.getAddress());
    dueDiligence.setAgentName(dueDiligenceDto.getName());
    dueDiligence.setEmail(dueDiligenceDto.getEmail());
    dueDiligence.setDateChecked(dueDiligenceDto.getIdentityDate().toString()); //TBD .. check date format//
    dueDiligence.setPartnerName(dueDiligenceDto.getPartnerName());
    dueDiligence.setAgentAssuranceCode(dueDiligenceDto.getAgentCode());
    dueDiligence.setSupervisoryBody(dueDiligenceDto.getSupervisoryName());
    dueDiligence.setAmlRegistrationNumber(dueDiligenceDto.getAmlNumber());
    updateSubmission.setDueDiligence(dueDiligence);
}

    public void populateFilingForDate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
        FilingForDate filingForDate = new FilingForDate();
        var filingDate              = overseasEntitySubmissionDto.getUpdate().getFilingDate();
        filingForDate.setDay((
                String.valueOf(
                        filingDate.getDayOfMonth())));
        filingForDate.setMonth((
                String.valueOf(
                        filingDate.getMonth())));
        filingForDate.setYear((
                String.valueOf(
                        filingDate.getYear())));
        updateSubmission.setFilingForDate(filingForDate);

    }
    public void populatePresenter(OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
        Presenter           presenter           = new Presenter();
        presenter.setEmail(overseasEntitySubmissionDto.getPresenter().getEmail());
        presenter.setName(overseasEntitySubmissionDto.getPresenter().getFullName());
        updateSubmission.setPresenter(presenter);
    }





    public UpdateSubmission populate(OverseasEntitySubmissionDto overseasEntitySubmissionDto, UpdateSubmission updateSubmission) {
        this.overseasEntitySubmissionDto    = overseasEntitySubmissionDto;
        this.updateSubmission               = updateSubmission;
        return   populate();
    }


    public OverseasEntitySubmissionDto getOverseasEntitySubmissionDto() {
        return overseasEntitySubmissionDto;
    }

    public void setOverseasEntitySubmissionDto(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        this.overseasEntitySubmissionDto = overseasEntitySubmissionDto;

    }

    @Override
    public String toString() {
        return "PopulateUpdateSubmission{}";
    }
}
