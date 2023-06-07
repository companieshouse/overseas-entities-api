package uk.gov.companieshouse.overseasentitiesapi.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.CorporateManagingOfficerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualManagingOfficerCessation;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Service
public class ManagingOfficerCessationService {

  public static final String NO_PAIR_FOUND = "No matching MO was found in the database";
  public static final String NO_ID_FOUND_IN_PRIVATE_DATA = "No Managing Officer ID was found in Private Data";
  public static final String SERVICE = "ManagingOfficerCessationService";

  public List<Cessation> managingOfficerCessations(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto,
      Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData,
      Map<String, Object> logMap) {

    List<Cessation> cessations = new ArrayList<>();
    cessations.addAll(
        getIndividualManagingOfficers(overseasEntitySubmissionDto, combinedMoData, logMap).stream()
            .flatMap(Optional::stream)
            .collect(Collectors.toList()));
    cessations.addAll(
        getCorporateManagingOfficersCessations(overseasEntitySubmissionDto, combinedMoData, logMap)
            .stream()
            .flatMap(Optional::stream)
            .collect(Collectors.toList()));

    return cessations;
  }

  private List<Optional<IndividualManagingOfficerCessation>> getIndividualManagingOfficers(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto,
      Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData,
      Map<String, Object> logMap) {
    var managingOfficersIndividual = overseasEntitySubmissionDto.getManagingOfficersIndividual();
    return managingOfficersIndividual.stream()
        .filter(managingOfficer -> (managingOfficer.getResignedOn() != null))
        .filter(managingOfficer -> (managingOfficer.getChipsReference() != null))
        .map(
            managingOfficer ->
                getIndividualManagingOfficerCessation(managingOfficer, combinedMoData, logMap))
        .collect(Collectors.toList());
  }

  private List<Optional<CorporateManagingOfficerCessation>> getCorporateManagingOfficersCessations(
      OverseasEntitySubmissionDto overseasEntitySubmissionDto,
      Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData,
      Map<String, Object> logMap) {
    var managingOfficersCorporate = overseasEntitySubmissionDto.getManagingOfficersCorporate();
    return managingOfficersCorporate.stream()
        .filter(managingOfficer -> (managingOfficer.getResignedOn() != null))
        .filter(managingOfficer -> (managingOfficer.getChipsReference() != null))
        .map(
            managingOfficer ->
                getCorporateManagingOfficerCessation(managingOfficer, combinedMoData, logMap))
        .collect(Collectors.toList());
  }

  private Optional<IndividualManagingOfficerCessation> getIndividualManagingOfficerCessation(
      ManagingOfficerIndividualDto mo,
      Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData,
      Map<String, Object> logMap) {

    var publicPrivateMoPairOptional = getPublicPrivateMoPairOptional(mo.getChipsReference(), combinedMoData);

    if (publicPrivateMoPairOptional.isEmpty()) {
      ApiLogger.errorContext(SERVICE, NO_PAIR_FOUND, null, logMap);
      return Optional.empty();
    }

    var officerAppointmentId = getAppointmentId(publicPrivateMoPairOptional.get());
    if (officerAppointmentId == null) {
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = mo.getDateOfBirth().format(formatter);

    return Optional.of(
        new IndividualManagingOfficerCessation(
            officerAppointmentId,
            mo.getFirstName() + " " + mo.getLastName(),
            formattedDate,
            mo.getResignedOn()));
  }

  private Optional<CorporateManagingOfficerCessation> getCorporateManagingOfficerCessation(
      ManagingOfficerCorporateDto mo,
      Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData,
      Map<String, Object> logMap) {

    var publicPrivateMoPairOptional =
        getPublicPrivateMoPairOptional(mo.getChipsReference(), combinedMoData);

    if (publicPrivateMoPairOptional.isEmpty()) {
      ApiLogger.errorContext(SERVICE, NO_PAIR_FOUND, null, logMap);
      return Optional.empty();
    }

    var officerAppointmentId = getAppointmentId(publicPrivateMoPairOptional.get());
    if (officerAppointmentId == null) {
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    return Optional.of(
        new CorporateManagingOfficerCessation(
            officerAppointmentId, mo.getResignedOn(), mo.getName()));
  }

  private String getAppointmentId(
      Pair<CompanyOfficerApi, ManagingOfficerDataApi> publicPrivateMoPair) {
    return Optional.ofNullable(publicPrivateMoPair)
        .map(Pair::getRight)
        .map(ManagingOfficerDataApi::getManagingOfficerAppointmentId)
        .orElse(null);
  }

  private Optional<Pair<CompanyOfficerApi, ManagingOfficerDataApi>> getPublicPrivateMoPairOptional(
      String hashedId,
      Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData) {
    return Optional.ofNullable(combinedMoData.get(hashedId));
  }
}
