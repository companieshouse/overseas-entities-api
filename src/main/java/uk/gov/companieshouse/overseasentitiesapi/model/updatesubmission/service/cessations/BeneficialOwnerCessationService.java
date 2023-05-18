package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.cessations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.types.PersonName;

@Service
public class BeneficialOwnerCessationService {

  public static final String NO_PAIR_FOUND = "No pair found for the provided hashedId";

  public List<Cessation> beneficialOwnerCessations(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {

    List<Cessation> cessations = new ArrayList<>();
    cessations.addAll(returnIndividualBeneficialOwners(overseasEntitySubmissionDto, combinedBoData));
    cessations.addAll(returnCorporateEntityBeneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData));
    cessations.addAll(returnLegalPersonBeneficialOwners(overseasEntitySubmissionDto, combinedBoData));
    return cessations;
  }

  private List<IndividualBeneficialOwnerCessation> returnIndividualBeneficialOwners(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {
    var beneficialOwnersIndividual = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
    return beneficialOwnersIndividual.stream()
            .filter(beneficialOwner -> (beneficialOwner.getCeasedDate() != null))
            .filter(beneficialOwner -> (beneficialOwner.getChipsReference() != null))
            .map(beneficialOwner -> returnIndividualBeneficialOwnerCessation(beneficialOwner, combinedBoData))
            .collect(Collectors.toList());
  }

  private List<CorporateEntityBeneficialOwnerCessation> returnCorporateEntityBeneficialOwnerCessations(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
    return returned.stream()
            .filter(beneficialOwner -> (beneficialOwner.getCeasedDate() != null))
            .filter(beneficialOwner -> (beneficialOwner.getChipsReference() != null))
            .map(beneficialOwner -> returnCorporateEntityBeneficialOwnerCessation(beneficialOwner, combinedBoData))
            .collect(Collectors.toList());
  }

  private List<LegalPersonBeneficialOwnerCessation> returnLegalPersonBeneficialOwners(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
    return returned.stream()
            .filter(beneficialOwner -> (beneficialOwner.getCeasedDate() != null))
            .filter(beneficialOwner -> (beneficialOwner.getChipsReference() != null))
            .map(beneficialOwner -> returnLegalPersonBeneficialOwnerCessation(beneficialOwner, combinedBoData))
            .collect(Collectors.toList());
  }

  private IndividualBeneficialOwnerCessation returnIndividualBeneficialOwnerCessation(
      BeneficialOwnerIndividualDto bo, Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {

    var hashedId = bo.getChipsReference();
    var publicPrivateBoPair = Optional.ofNullable(combinedBoData.get(hashedId))
            .orElseThrow(() -> new IllegalArgumentException(NO_PAIR_FOUND));

    var privateBoData = publicPrivateBoPair.getRight();
    var appointmentId = Optional.ofNullable(privateBoData).map(PrivateBoDataApi::getPscId).orElse(null);

    return new IndividualBeneficialOwnerCessation(
        appointmentId,
        bo.getCeasedDate(),
        bo.getDateOfBirth(),
        new PersonName(bo.getFirstName(), bo.getLastName()));
  }

  private CorporateEntityBeneficialOwnerCessation returnCorporateEntityBeneficialOwnerCessation(
      BeneficialOwnerCorporateDto bo, Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {

    var hashedId = bo.getChipsReference();
    var publicPrivateBoPair = Optional.ofNullable(combinedBoData.get(hashedId))
            .orElseThrow(() -> new IllegalArgumentException(NO_PAIR_FOUND));

    var privateBoData = publicPrivateBoPair.getRight();
    var appointmentId = Optional.ofNullable(privateBoData).map(PrivateBoDataApi::getPscId).orElse(null);

    return new CorporateEntityBeneficialOwnerCessation(appointmentId, bo.getCeasedDate(), bo.getName());
  }

  private LegalPersonBeneficialOwnerCessation returnLegalPersonBeneficialOwnerCessation(
      BeneficialOwnerGovernmentOrPublicAuthorityDto bo,
      Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {

    var hashedId = bo.getChipsReference();
    var publicPrivateBoPair = Optional.ofNullable(combinedBoData.get(hashedId))
            .orElseThrow(() -> new IllegalArgumentException(NO_PAIR_FOUND));

    var privateBoData = publicPrivateBoPair.getRight();
    var appointmentId = Optional.ofNullable(privateBoData).map(PrivateBoDataApi::getPscId).orElse(null);

    return new LegalPersonBeneficialOwnerCessation(appointmentId, bo.getCeasedDate(), bo.getName());
  }
}
