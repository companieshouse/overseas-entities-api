package uk.gov.companieshouse.overseasentitiesapi.service;

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
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.CorporateEntityBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.LegalPersonBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Service
public class BeneficialOwnerCessationService {

  public static final String NO_PAIR_FOUND = "No matching BO was found in the database";
  public static final String NO_PUBLIC_BO_DATA_FOUND = "No public data found for beneficial owner - continuing with changes";
  public static final String NO_PRIVATE_BO_DATA_FOUND = "No private data found for beneficial owner - changes cannot be created";
  public static final String NO_ID_FOUND_IN_PRIVATE_DATA = "No Beneficial Owner ID was found in Private Data";
  public static final String SERVICE = "BeneficialOwnerCessationService";

  public List<Cessation> beneficialOwnerCessations(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData,
          Map<String, Object> logMap) {

    List<Cessation> cessations = new ArrayList<>();
    cessations.addAll(getIndividualBeneficialOwners(overseasEntitySubmissionDto, combinedBoData, logMap)
            .stream()
            .flatMap(Optional::stream)
            .toList());
    cessations.addAll(getCorporateEntityBeneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData, logMap)
            .stream()
            .flatMap(Optional::stream)
            .toList());
    cessations.addAll(getLegalPersonBeneficialOwners(overseasEntitySubmissionDto, combinedBoData, logMap)
            .stream()
            .flatMap(Optional::stream)
            .toList());
    return cessations;
  }

  private List<Optional<IndividualBeneficialOwnerCessation>> getIndividualBeneficialOwners(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData, Map<String, Object> logMap) {
    var beneficialOwnersIndividual = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
    return beneficialOwnersIndividual.stream()
            .filter(beneficialOwner -> (beneficialOwner.getCeasedDate() != null))
            .filter(beneficialOwner -> (beneficialOwner.getChipsReference() != null))
            .map(beneficialOwner -> getIndividualBeneficialOwnerCessation(beneficialOwner, combinedBoData, logMap))
            .collect(Collectors.toList());
  }

  private List<Optional<CorporateEntityBeneficialOwnerCessation>> getCorporateEntityBeneficialOwnerCessations(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData, Map<String, Object> logMap) {
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
    return returned.stream()
            .filter(beneficialOwner -> (beneficialOwner.getCeasedDate() != null))
            .filter(beneficialOwner -> (beneficialOwner.getChipsReference() != null))
            .map(beneficialOwner -> getCorporateEntityBeneficialOwnerCessation(beneficialOwner, combinedBoData, logMap))
            .collect(Collectors.toList());
  }

  private List<Optional<LegalPersonBeneficialOwnerCessation>> getLegalPersonBeneficialOwners(
          OverseasEntitySubmissionDto overseasEntitySubmissionDto,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData, Map<String, Object> logMap) {
    var returned = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
    return returned.stream()
            .filter(beneficialOwner -> (beneficialOwner.getCeasedDate() != null))
            .filter(beneficialOwner -> (beneficialOwner.getChipsReference() != null))
            .map(beneficialOwner -> getLegalPersonBeneficialOwnerCessation(beneficialOwner, combinedBoData, logMap))
            .collect(Collectors.toList());
  }

  private Optional<IndividualBeneficialOwnerCessation> getIndividualBeneficialOwnerCessation(
          BeneficialOwnerIndividualDto bo,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData,
          Map<String, Object> logMap) {

    var publicPrivateBoPairOptional = getPublicPrivateBoPairOptional(bo.getChipsReference(), combinedBoData);

    if (publicPrivateBoPairOptional.isEmpty()) {
      ApiLogger.errorContext(SERVICE, NO_PAIR_FOUND,null, logMap);
      return Optional.empty();
    }

    logMissingPublicPrivateData(publicPrivateBoPairOptional.get(), logMap);

    var appointmentId = getAppointmentId(publicPrivateBoPairOptional.get());
    if(appointmentId == null){
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    return Optional.of(new IndividualBeneficialOwnerCessation(
            appointmentId,
            bo.getCeasedDate(),
            bo.getDateOfBirth(),
            bo.getHaveDayOfBirth(),
            new PersonName(bo.getFirstName(), bo.getLastName())
    ));
  }

  private Optional<CorporateEntityBeneficialOwnerCessation> getCorporateEntityBeneficialOwnerCessation(
          BeneficialOwnerCorporateDto bo,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData,
          Map<String, Object> logMap) {

    var publicPrivateBoPairOptional = getPublicPrivateBoPairOptional(bo.getChipsReference(), combinedBoData);

    if (publicPrivateBoPairOptional.isEmpty()) {
      ApiLogger.errorContext(SERVICE, NO_PAIR_FOUND,null, logMap);
      return Optional.empty();
    }

    logMissingPublicPrivateData(publicPrivateBoPairOptional.get(), logMap);

    var appointmentId = getAppointmentId(publicPrivateBoPairOptional.get());
    if(appointmentId == null){
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    return Optional.of(new CorporateEntityBeneficialOwnerCessation(appointmentId, bo.getCeasedDate(), bo.getName()));
  }

  private Optional<LegalPersonBeneficialOwnerCessation> getLegalPersonBeneficialOwnerCessation(
          BeneficialOwnerGovernmentOrPublicAuthorityDto bo,
          Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData,
          Map<String, Object> logMap) {

    var publicPrivateBoPairOptional = getPublicPrivateBoPairOptional(bo.getChipsReference(), combinedBoData);

    if (publicPrivateBoPairOptional.isEmpty()) {
      ApiLogger.errorContext(SERVICE, NO_PAIR_FOUND,null, logMap);
      return Optional.empty();
    }

    logMissingPublicPrivateData(publicPrivateBoPairOptional.get(), logMap);

    var appointmentId = getAppointmentId(publicPrivateBoPairOptional.get());
    if(appointmentId == null){
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    return Optional.of(new LegalPersonBeneficialOwnerCessation(appointmentId, bo.getCeasedDate(), bo.getName()));
  }

  private String getAppointmentId(Pair<PscApi, PrivateBoDataApi> publicPrivateBoPair) {
    return Optional.ofNullable(publicPrivateBoPair)
            .map(Pair::getRight)
            .map(PrivateBoDataApi::getPscId)
            .orElse(null);
  }

  private Optional<Pair<PscApi, PrivateBoDataApi>> getPublicPrivateBoPairOptional(
          String hashedId, Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData) {
    return Optional.ofNullable(combinedBoData).map(data -> data.get(hashedId));
  }

  private void logMissingPublicPrivateData(Pair<PscApi, PrivateBoDataApi> publicPrivateBoData, Map<String, Object> logMap) {
    if (publicPrivateBoData.getLeft() == null) {
      ApiLogger.errorContext(SERVICE, NO_PUBLIC_BO_DATA_FOUND, null, logMap);
    }

    if (publicPrivateBoData.getRight() == null) {
      ApiLogger.errorContext(SERVICE, NO_PRIVATE_BO_DATA_FOUND, null, logMap);
    }
  }
}
