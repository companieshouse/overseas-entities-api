package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.cessations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.types.PersonName;

@Service
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
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
    return returned.stream()
        .filter((a) -> (a.getCeasedDate() != null))
        .filter((a) -> (a.getChipsReference() != null))
        .map(this::returnIndividualBeneficialOwnerCessation)
        .collect(Collectors.toList());
  }

  private List<LegalPersonBeneficialOwnerCessation> returnLegalPersonBeneficialOwners() {
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
    return returned.stream()
            .filter((a) -> (a.getCeasedDate() != null))
            .filter((a) -> (a.getChipsReference() != null))
            .map(this::returnLegalPersonBeneficialOwnerCessation)
            .collect(Collectors.toList());
  }

  private List<CorporateEntityBeneficialOwnerCessation>
      returnCorporateEntityBeneficialOwnerCessations() {
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
    return returned.stream()
            .filter((a) -> (a.getCeasedDate() != null))
            .filter((a) -> (a.getChipsReference() != null))
            .map(this::returnCorporateEntityBeneficialOwnerCessation)
            .collect(Collectors.toList());
  }

  public IndividualBeneficialOwnerCessation returnIndividualBeneficialOwnerCessation(
      BeneficialOwnerIndividualDto bo) {
        
    return new IndividualBeneficialOwnerCessation(
            bo.getChipsReference(),
            bo.getCeasedDate(),
            bo.getDateOfBirth(),
            new PersonName(bo.getFirstName(), bo.getLastName()));
  }

  public CorporateEntityBeneficialOwnerCessation returnCorporateEntityBeneficialOwnerCessation(
      BeneficialOwnerCorporateDto bo) {
    return new CorporateEntityBeneficialOwnerCessation(
            bo.getChipsReference(), bo.getCeasedDate(), bo.getName());
  }

  public LegalPersonBeneficialOwnerCessation returnLegalPersonBeneficialOwnerCessation(
      BeneficialOwnerGovernmentOrPublicAuthorityDto bo) {
    return new LegalPersonBeneficialOwnerCessation(
            bo.getChipsReference(), bo.getCeasedDate(), bo.getName());
  }
}
