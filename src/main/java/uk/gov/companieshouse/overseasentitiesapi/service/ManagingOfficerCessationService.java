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
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;

@Service
public class ManagingOfficerCessationService {

  public static final String NO_PAIR_FOUND = "No matching MO was found in the database";
  public static final String NO_PUBLIC_MO_DATA_FOUND = "No public data found for managing officer - continuing with changes";
  public static final String NO_PRIVATE_MO_DATA_FOUND = "No private data found for managing officer - changes cannot be created";
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
            .toList());
    cessations.addAll(
        getCorporateManagingOfficersCessations(overseasEntitySubmissionDto, combinedMoData, logMap)
            .stream()
            .flatMap(Optional::stream)
            .toList());

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

    logMissingPublicPrivateData(publicPrivateMoPairOptional.get(), logMap);

    var officerAppointmentId = getAppointmentId(publicPrivateMoPairOptional.get());
    if (officerAppointmentId == null) {
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDateOfBirth = mo.getDateOfBirth().format(formatter);

    return Optional.of(
        new IndividualManagingOfficerCessation(
            officerAppointmentId,
            new PersonName(mo.getFirstName(), mo.getLastName()),
            formattedDateOfBirth,
            mo.getHaveDayOfBirth(),
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

    logMissingPublicPrivateData(publicPrivateMoPairOptional.get(), logMap);

    var officerAppointmentId = getAppointmentId(publicPrivateMoPairOptional.get());
    if (officerAppointmentId == null) {
      ApiLogger.errorContext(SERVICE, NO_ID_FOUND_IN_PRIVATE_DATA, null, logMap);
      return Optional.empty();
    }

    return Optional.of(
        new CorporateManagingOfficerCessation(
                // foreName is not used by CHIPS to display Corporate Managing Officer which is ceased
            officerAppointmentId, mo.getResignedOn(), new PersonName(null, mo.getName())));
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
    return Optional.ofNullable(combinedMoData).map(data -> data.get(hashedId));
  }

  private void logMissingPublicPrivateData(Pair<CompanyOfficerApi, ManagingOfficerDataApi> publicPrivateMoData, Map<String, Object> logMap) {
    if (publicPrivateMoData.getLeft() == null) {
      ApiLogger.errorContext(SERVICE, NO_PUBLIC_MO_DATA_FOUND, null, logMap);
    }

    if (publicPrivateMoData.getRight() == null) {
      ApiLogger.errorContext(SERVICE, NO_PRIVATE_MO_DATA_FOUND, null, logMap);
    }
  }
}
