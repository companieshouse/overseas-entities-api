package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.cessations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.types.PersonName;

public class BeneficialOwnerCessationService {
  private final OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  public BeneficialOwnerCessationService(OverseasEntitySubmissionDto updateSubmission) {
    this.overseasEntitySubmissionDto = updateSubmission;
  }

  public List<Cessation> beneficialOwnerCessations() {
    List<Cessation> cessations = new ArrayList<>();
    cessations.addAll(returnIndividualBeneficialOwners());
    cessations.addAll(returnCorporateEntityBeneficialOwnerCessations());
    cessations.addAll(returnLegalPersonBeneficialOwners());
    return cessations;
  }

  private List<IndividualBeneficialOwnerCessation> returnIndividualBeneficialOwners() {
    List<IndividualBeneficialOwnerCessation> cessations = new ArrayList<>();
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
    return returned.stream()
        .filter((a) -> (a.getCeasedDate() != null))
        .filter((a) -> (a.getChipsReference() != null))
        .map(this::returnIndividualBeneficialOwnerCessation)
        .collect(Collectors.toList());
  }

  private List<LegalPersonBeneficialOwnerCessation> returnLegalPersonBeneficialOwners() {
    List<LegalPersonBeneficialOwnerCessation> cessations = new ArrayList<>();
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
    return returned.stream()
            .filter((a) -> (a.getCeasedDate() != null))
            .filter((a) -> (a.getChipsReference() != null))
            .map(this::returnLegalPersonBeneficialOwnerCessation)
            .collect(Collectors.toList());
  }

  private List<CorporateEntityBeneficialOwnerCessation>
      returnCorporateEntityBeneficialOwnerCessations() {
    List<CorporateEntityBeneficialOwnerCessation> cessations = new ArrayList<>();
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
    return returned.stream()
            .filter((a) -> (a.getCeasedDate() != null))
            .filter((a) -> (a.getChipsReference() != null))
            .map(this::returnCorporateEntityBeneficialOwnerCessation)
            .collect(Collectors.toList());
  }

  public IndividualBeneficialOwnerCessation returnIndividualBeneficialOwnerCessation(
      BeneficialOwnerIndividualDto bo) {
    IndividualBeneficialOwnerCessation individualBeneficialOwnerCessation =
        new IndividualBeneficialOwnerCessation(
            bo.getChipsReference(),
            bo.getCeasedDate(),
            bo.getDateOfBirth(),
            new PersonName(bo.getFirstName(), bo.getLastName()));
    return individualBeneficialOwnerCessation;
  }

  public CorporateEntityBeneficialOwnerCessation returnCorporateEntityBeneficialOwnerCessation(
      BeneficialOwnerCorporateDto bo) {
    CorporateEntityBeneficialOwnerCessation corporateEntityBeneficialOwnerCessation =
        new CorporateEntityBeneficialOwnerCessation(
            bo.getChipsReference(), bo.getCeasedDate(), bo.getName());
    return corporateEntityBeneficialOwnerCessation;
  }

  public LegalPersonBeneficialOwnerCessation returnLegalPersonBeneficialOwnerCessation(
      BeneficialOwnerGovernmentOrPublicAuthorityDto bo) {
    LegalPersonBeneficialOwnerCessation legalPersonBeneficialOwnerCessation =
        new LegalPersonBeneficialOwnerCessation(
            bo.getChipsReference(), bo.getCeasedDate(), bo.getName());
    return legalPersonBeneficialOwnerCessation;
  }
}
