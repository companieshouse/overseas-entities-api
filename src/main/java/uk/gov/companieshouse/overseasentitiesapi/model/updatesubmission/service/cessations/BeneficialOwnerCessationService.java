package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.cessations;

import java.util.ArrayList;
import java.util.List;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.BeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.CorporateEntityBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.LegalPersonBeneficialOwnerCessation;

public class BeneficialOwnerCessationService {
    public List<BeneficialOwnerCessation> beneficialOwnerCessations(OverseasEntitySubmissionDto updateSubmission) {
        List<BeneficialOwnerCessation> cessations = new ArrayList<>();
        cessations.addAll(returnIndividualBeneficialOwners(updateSubmission));
        cessations.addAll(returnCorporateEntityBeneficialOwnerCessations(updateSubmission));
        cessations.addAll(returnLegalPersonBeneficialOwners(updateSubmission));
        return cessations;
    }

    public List<IndividualBeneficialOwnerCessation> returnIndividualBeneficialOwners(
            OverseasEntitySubmissionDto updateSubmission) {
        List<IndividualBeneficialOwnerCessation> cessations = new ArrayList<>();
        var returned = updateSubmission.getBeneficialOwnersIndividual();
        return cessations;
    }

    public List<LegalPersonBeneficialOwnerCessation> returnLegalPersonBeneficialOwners(
            OverseasEntitySubmissionDto updateSubmission) {
        List<LegalPersonBeneficialOwnerCessation> cessations = new ArrayList<>();
        var returned = updateSubmission.getBeneficialOwnersGovernmentOrPublicAuthority();
        return cessations;
    }

    public List<CorporateEntityBeneficialOwnerCessation> returnCorporateEntityBeneficialOwnerCessations(
            OverseasEntitySubmissionDto updateSubmission) {
        List<CorporateEntityBeneficialOwnerCessation> cessations = new ArrayList<>();
        var returned = updateSubmission.getBeneficialOwnersCorporate();
        return cessations;
    }
}
