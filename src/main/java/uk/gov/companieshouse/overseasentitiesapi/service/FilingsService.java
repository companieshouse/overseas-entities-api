package uk.gov.companieshouse.overseasentitiesapi.service;

import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.PRESENTER_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.TRUST_DATA;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto.IS_NOT_PROPRIETOR_OF_LAND_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.ADDITIONS_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.ANY_BOS_ADDED_CEASED_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.BENEFICIAL_OWNERS_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.CESSATIONS_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.CHANGES_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.CHANGE_BENEFICIARY_RELEVANT_PERIOD_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.CHANGE_BO_RELEVANT_PERIOD_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.FILING_FOR_DATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.TRUSTEE_INVOLVED_RELEVANT_PERIOD_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.UPDATE_DUE_DILIGENCE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.UPDATE_ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.UPDATE_ENTITY_NUMBER_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.UPDATE_PRESENTER_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission.UPDATE_USER_SUBMISSION_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY_REMOVE;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY_UPDATE;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.OVERSEAS_ENTITY_ID_KEY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRANSACTION_ID_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.PopulateUpdateSubmission;
import uk.gov.companieshouse.overseasentitiesapi.utils.PublicPrivateDataCombiner;

@Service
public class FilingsService {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

  @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION_IDENTIFIER}")
  private String filingDescriptionIdentifier;

  @Value("${OVERSEAS_ENTITIES_FILING_DESCRIPTION}")
  private String registrationFilingDescription;

  @Value("${OVERSEAS_ENTITIES_UPDATE_FILING_DESCRIPTION}")
  private String updateFilingDescription;

  @Value("${OVERSEAS_ENTITIES_REMOVE_FILING_DESCRIPTION}")
  private String removeFilingDescription;

  @Value("${OE01_COST}")
  private String registerCostAmount;

  @Value("${OE02_COST}")
  private String updateCostAmount;

  @Value("${FEATURE_FLAG_ENABLE_TRUSTS_CHIPS_1502023}")
  private boolean isTrustsSubmissionThroughWebEnabled;

  @Value("${PUBLIC_API_IDENTITY_HASH_SALT}")
  private String salt;

  private final OverseasEntitiesService overseasEntitiesService;
  private final ApiClientService apiClientService;
  private final Supplier<LocalDate> dateNowSupplier;
  private final ObjectMapper objectMapper;
  private final PublicDataRetrievalService publicDataRetrievalService;
  private final PrivateDataRetrievalService privateDataRetrievalService;
  private final BeneficialOwnerChangeService beneficialOwnerChangeService;
  private final BeneficialOwnerAdditionService beneficialOwnerAdditionService;
  private final BeneficialOwnerCessationService beneficialOwnerCessationService;
  private final ManagingOfficerChangeService managingOfficerChangeService;
  private final ManagingOfficerAdditionService managingOfficerAdditionService;
  private final ManagingOfficerCessationService managingOfficerCessationService;
  private final OverseasEntityChangeService overseasEntityChangeService;

  @Autowired
  public FilingsService(
          OverseasEntitiesService overseasEntitiesService,
          ApiClientService apiClientService,
          Supplier<LocalDate> dateNowSupplier,
          ObjectMapper objectMapper,
          PrivateDataRetrievalService privateDataRetrievalService,
          PublicDataRetrievalService publicDataRetrievalService,
          BeneficialOwnerChangeService beneficialOwnerChangeService,
          BeneficialOwnerAdditionService beneficialOwnerAdditionService,
          BeneficialOwnerCessationService beneficialOwnerCessationService,
          ManagingOfficerChangeService managingOfficerChangeService,
          ManagingOfficerAdditionService managingOfficerAdditionService,
          ManagingOfficerCessationService managingOfficerCessationService,
          OverseasEntityChangeService overseasEntityChangeService
  ) {
    this.overseasEntitiesService = overseasEntitiesService;
    this.apiClientService = apiClientService;
    this.dateNowSupplier = dateNowSupplier;
    this.objectMapper = objectMapper;
    this.privateDataRetrievalService = privateDataRetrievalService;
    this.publicDataRetrievalService = publicDataRetrievalService;
    this.beneficialOwnerChangeService = beneficialOwnerChangeService;
    this.beneficialOwnerAdditionService = beneficialOwnerAdditionService;
    this.beneficialOwnerCessationService = beneficialOwnerCessationService;
    this.managingOfficerChangeService = managingOfficerChangeService;
    this.managingOfficerCessationService = managingOfficerCessationService;
    this.managingOfficerAdditionService = managingOfficerAdditionService;
    this.overseasEntityChangeService = overseasEntityChangeService;
  }

  public FilingApi generateOverseasEntityFiling(
          String overseasEntityId,
          Transaction transaction,
          String passThroughTokenHeader)
          throws SubmissionNotFoundException, ServiceException {
    var filing = new FilingApi();
    setFilingApiData(filing, overseasEntityId, transaction, passThroughTokenHeader);
    return filing;
  }

  private void setFilingApiData(
          FilingApi filing,
          String overseasEntityId,
          Transaction transaction,
          String passThroughTokenHeader)
          throws SubmissionNotFoundException, ServiceException {

    var logMap = new HashMap<String, Object>();
    logMap.put(OVERSEAS_ENTITY_ID_KEY, overseasEntityId);
    logMap.put(TRANSACTION_ID_KEY, transaction.getId());

    Map<String, Object> userSubmission = new HashMap<>();

    OverseasEntitySubmissionDto submissionDto =
            overseasEntitiesService.getOverseasEntitySubmission(overseasEntityId)
                    .orElseThrow(() -> new SubmissionNotFoundException(
                            String.format("Empty submission returned when generating filing for %s", overseasEntityId)
                    ));

    if (submissionDto.isForUpdate()) {
      boolean isNoChange = submissionDto.getUpdate().isNoChange() && submissionDto.getUpdate().isNoChangeRelevantPeriod();
      ApiLogger.debug("Value of 'isNoChange' flag for Update is :" + isNoChange, logMap);
      var updateSubmission = new UpdateSubmission();
      collectUpdateSubmissionData(updateSubmission, submissionDto, passThroughTokenHeader, isNoChange, logMap);
      setUpdateSubmissionData(userSubmission, updateSubmission, isNoChange, logMap);
      filing.setKind(FILING_KIND_OVERSEAS_ENTITY_UPDATE);
      filing.setCost(updateCostAmount);
    } else if (submissionDto.isForRemove()) {
      // Note that most of the remove details are saved under the 'UpdateDTO' and an 'UpdateSubmission' object is still
      // used to record the data changes, just before the filing data is returned.
      // Cost is not set on a remove submission to prevent automatic refunds.
      boolean isNoChange = submissionDto.getUpdate().isNoChange();
      ApiLogger.debug("Value of 'isNoChange' flag for Remove is :" + isNoChange, logMap);
      var updateSubmission = new UpdateSubmission();
      collectUpdateSubmissionData(updateSubmission, submissionDto, passThroughTokenHeader, isNoChange, logMap);
      setRemoveSubmissionData(userSubmission, updateSubmission, isNoChange, submissionDto.getRemove().getIsNotProprietorOfLand(), logMap);
      filing.setKind(FILING_KIND_OVERSEAS_ENTITY_REMOVE);
    } else {
      setSubmissionData(userSubmission, submissionDto, logMap);
      filing.setKind(FILING_KIND_OVERSEAS_ENTITY);
      filing.setCost(registerCostAmount);
    }

    filing.setData(userSubmission);
    setPaymentData(userSubmission, transaction, passThroughTokenHeader, logMap);
    setDescriptionFields(filing, submissionDto);
  }

  private void collectUpdateSubmissionData(UpdateSubmission updateSubmission,
                                       OverseasEntitySubmissionDto submissionDto,
                                       String passThroughTokenHeader,
                                       boolean isNoChange,
                                       Map<String, Object> logMap) throws ServiceException {

    var populateUpdateSubmission = new PopulateUpdateSubmission();
    populateUpdateSubmission.populate(submissionDto, updateSubmission, isNoChange);

    if (!isNoChange) {
      var companyNumber = submissionDto.getEntityNumber();

      var publicPrivateDataCombiner = new PublicPrivateDataCombiner(publicDataRetrievalService,
              privateDataRetrievalService, salt);
      var publicPrivateOeData = publicPrivateDataCombiner.buildMergedOverseasEntityDataPair(
              companyNumber, passThroughTokenHeader);
      var publicPrivateBoData = publicPrivateDataCombiner.buildMergedBeneficialOwnerDataMap(
              companyNumber, passThroughTokenHeader);
      var publicPrivateMoData = publicPrivateDataCombiner.buildMergedManagingOfficerDataMap(
              companyNumber, passThroughTokenHeader);

      ApiLogger.infoContext("PublicPrivateDataCombiner",
              publicPrivateDataCombiner.logCollatedData());

      updateSubmission.setEntityName(publicPrivateOeData.getLeft().getCompanyName());

      updateSubmission.getChanges()
              .addAll(overseasEntityChangeService.collateOverseasEntityChanges(publicPrivateOeData,
                      submissionDto, logMap));

      updateSubmission.getChanges()
              .addAll(beneficialOwnerChangeService.collateBeneficialOwnerChanges(
                      publicPrivateBoData, submissionDto, logMap));
      updateSubmission.getAdditions()
              .addAll(beneficialOwnerAdditionService.beneficialOwnerAdditions(submissionDto));
      updateSubmission.getCessations()
              .addAll(beneficialOwnerCessationService.beneficialOwnerCessations(submissionDto,
                      publicPrivateBoData, logMap));
      updateSubmission.getChanges()
              .addAll(managingOfficerChangeService.collateManagingOfficerChanges(
                      publicPrivateMoData, submissionDto, logMap));
      updateSubmission.getCessations()
              .addAll(managingOfficerCessationService.managingOfficerCessations(submissionDto,
                      publicPrivateMoData, logMap));
      updateSubmission.getAdditions()
              .addAll(managingOfficerAdditionService.managingOfficerAdditions(submissionDto));

      if (!submissionDto.getTrusts().isEmpty() && submissionDto.getTrusts() != null) {
        updateSubmission.setTrustAdditions(submissionDto.getTrusts());
      }

      ApiLogger.debug("Value of 'changes' is :" + updateSubmission.getChanges(), logMap);

      ApiLogger.debug("Updates have been collected", logMap);
    } else {
      updateSubmission.setEntityName(submissionDto.getEntityName().getName());
    }
  }

  private void setUpdateSubmissionData(Map<String, Object> data,
                                       UpdateSubmission updateSubmission,
                                       boolean isNoChange,
                                       Map<String, Object> logMap) {
    setUpdateAndRemoveSubmissionData(data, updateSubmission, isNoChange, logMap);

    data.put(FILING_FOR_DATE_FIELD, updateSubmission.getFilingForDate());
    data.put(CHANGE_BO_RELEVANT_PERIOD_FIELD, updateSubmission.getChangeBORelevantPeriodStatement());
    data.put(TRUSTEE_INVOLVED_RELEVANT_PERIOD_FIELD, updateSubmission.getTrusteeInvolvedRelevantPeriodStatement());
    data.put(CHANGE_BENEFICIARY_RELEVANT_PERIOD_FIELD, updateSubmission.getChangeBeneficiaryRelevantPeriodStatement());

    ApiLogger.debug("Update specific submission data has been set on filing: " + data, logMap);
  }

  private void setRemoveSubmissionData(Map<String, Object> data,
                                       UpdateSubmission updateSubmission,
                                       boolean isNoChange,
                                       boolean isNotProprietorOfLand,
                                       Map<String, Object> logMap) {
    setUpdateAndRemoveSubmissionData(data, updateSubmission, isNoChange, logMap);

    data.put(IS_NOT_PROPRIETOR_OF_LAND_FIELD, isNotProprietorOfLand);

    ApiLogger.debug("Remove specific submission data has been set on filing: " + data, logMap);
  }

  private void setUpdateAndRemoveSubmissionData(Map<String, Object> data,
                                                UpdateSubmission updateSubmission,
                                                boolean isNoChange,
                                                Map<String, Object> logMap) {
    data.put(UPDATE_ENTITY_NUMBER_FIELD, updateSubmission.getEntityNumber());
    data.put(UPDATE_ENTITY_NAME_FIELD, updateSubmission.getEntityName());
    data.put(UPDATE_USER_SUBMISSION_FIELD, updateSubmission.getUserSubmission());
    if (!isNoChange) {
      ApiLogger.debug("Adding Agent due diligence data to filing", logMap);
      data.put(UPDATE_DUE_DILIGENCE_FIELD, updateSubmission.getDueDiligence());
    }
    data.put(UPDATE_PRESENTER_FIELD, updateSubmission.getPresenter());
    data.put(ANY_BOS_ADDED_CEASED_FIELD, updateSubmission.getAnyBOsOrMOsAddedOrCeased());
    data.put(BENEFICIAL_OWNERS_FIELD, updateSubmission.getBeneficialOwnerStatement());
    if (!isNoChange) {
      ApiLogger.debug("Adding 'changes' data to filing", logMap);
      data.put(CHANGES_FIELD, updateSubmission.getChanges());
      data.put(ADDITIONS_FIELD, updateSubmission.getAdditions());
      data.put(CESSATIONS_FIELD, updateSubmission.getCessations());
      data.put(TRUST_DATA, updateSubmission.getTrustAdditions());
    }

    ApiLogger.debug("Update/Remove submission data has been set on filing: " + data, logMap);
  }

  private void setSubmissionData(
          Map<String, Object> data, OverseasEntitySubmissionDto submissionDto, Map<String, Object> logMap)
          throws ServiceException {

    if (Objects.isNull(submissionDto.getEntityName())) {
      data.put(ENTITY_NAME_FIELD, null);
    } else {
      data.put(ENTITY_NAME_FIELD, submissionDto.getEntityName().getName());
    }

    data.put(PRESENTER_FIELD, submissionDto.getPresenter());
    data.put(ENTITY_FIELD, submissionDto.getEntity());
    data.put(DUE_DILIGENCE_FIELD, submissionDto.getDueDiligence());
    data.put(OVERSEAS_ENTITY_DUE_DILIGENCE, submissionDto.getOverseasEntityDueDiligence());
    data.put(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD, getBeneficialOwnersIndividualSubmissionData(submissionDto));
    data.put(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD, submissionDto.getBeneficialOwnersGovernmentOrPublicAuthority());
    data.put(BENEFICIAL_OWNERS_CORPORATE_FIELD, getBeneficialOwnersCorporateSubmissionData(submissionDto));
    data.put(MANAGING_OFFICERS_INDIVIDUAL_FIELD, submissionDto.getManagingOfficersIndividual());
    data.put(MANAGING_OFFICERS_CORPORATE_FIELD, submissionDto.getManagingOfficersCorporate());
    data.put(BENEFICIAL_OWNERS_STATEMENT, submissionDto.getBeneficialOwnersStatement());

    if (isTrustsSubmissionThroughWebEnabled) {
      data.put(TRUST_DATA, submissionDto.getTrusts());
    }

    ApiLogger.debug("Submission data has been set on filing", logMap);
  }

  private List<BeneficialOwnerIndividualDto> getBeneficialOwnersIndividualSubmissionData(
          OverseasEntitySubmissionDto submissionDto) throws ServiceException {

    List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualSubmissionData = new ArrayList<>();

    if (Objects.isNull(submissionDto.getBeneficialOwnersIndividual())) {
      return beneficialOwnersIndividualSubmissionData;
    }

    for (BeneficialOwnerIndividualDto beneficialOwner : submissionDto.getBeneficialOwnersIndividual()) {
      if (!isTrustsSubmissionThroughWebEnabled) {
        String noTrustsMessage =
                "No trusts exist for this filing but a trust id is provided for BO Individual "
                        + beneficialOwner.getFirstName()
                        + " "
                        + beneficialOwner.getLastName();

        List<TrustDataDto> trustData = getTrustData(submissionDto, beneficialOwner.getTrustIds(), noTrustsMessage);
        beneficialOwner.setTrustData(convertTrustDataToString(trustData));
      }
      beneficialOwnersIndividualSubmissionData.add(beneficialOwner);
    }

    return beneficialOwnersIndividualSubmissionData;
  }

  private List<BeneficialOwnerCorporateDto> getBeneficialOwnersCorporateSubmissionData(
          OverseasEntitySubmissionDto submissionDto) throws ServiceException {

    List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateSubmissionData = new ArrayList<>();

    if (Objects.isNull(submissionDto.getBeneficialOwnersCorporate())) {
      return beneficialOwnersCorporateSubmissionData;
    }

    for (BeneficialOwnerCorporateDto beneficialOwner :
            submissionDto.getBeneficialOwnersCorporate()) {
      if (!isTrustsSubmissionThroughWebEnabled) {
        String noTrustsMessage =
                "No trusts exist for this filing but a trust id is provided for BO Corporate "
                        + beneficialOwner.getPublicRegisterName();

        List<TrustDataDto> trustData =
                getTrustData(submissionDto, beneficialOwner.getTrustIds(), noTrustsMessage);
        beneficialOwner.setTrustData(convertTrustDataToString(trustData));
      }
      beneficialOwnersCorporateSubmissionData.add(beneficialOwner);
    }

    return beneficialOwnersCorporateSubmissionData;
  }

  private List<TrustDataDto> getTrustData(
          OverseasEntitySubmissionDto submissionDto,
          List<String> trustIds,
          String noTrustsExceptionMessage)
          throws ServiceException {
    List<TrustDataDto> trustsDataForBO = new ArrayList<>();
    // Loop through each trustId(s) and lookup the information for that trust
    if (Objects.isNull(trustIds)) {
      return trustsDataForBO;
    }

    for (String trustId : trustIds) {
      List<TrustDataDto> trustData = new ArrayList<>();

      if (Objects.isNull(submissionDto.getTrusts())) {
        throw new ServiceException(noTrustsExceptionMessage);
      }
      for (TrustDataDto trust : submissionDto.getTrusts()) {
        if (trust.getTrustId().equals(trustId)) {
          trustData.add(trust);
        }
      }

      // If there is more than one trust with the same ID throw an error
      if (trustData.size() > 1) {
        throw new ServiceException("There is more than one trust with the ID: " + trustId);
      }
      // If there are is no trust with that ID throw an error
      if (trustData.isEmpty()) {
        throw new ServiceException("There are no trusts for the ID: " + trustId);
      }

      trustsDataForBO.add(trustData.getFirst());
    }

    return trustsDataForBO;
  }

  private String convertTrustDataToString(List<TrustDataDto> trustsDataForBO)
          throws ServiceException {
    var trustData = "";
    if (!trustsDataForBO.isEmpty()) {
      // Convert trust data to JSON string if it exists on transaction else it's to an empty string
      try {
        trustData = objectMapper.writeValueAsString(trustsDataForBO);
      } catch (JsonProcessingException e) {
        throw new ServiceException("Error converting trust data to JSON " + e.getMessage(), e);
      }
    }
    return trustData;
  }

  private void setPaymentData(
          Map<String, Object> data,
          Transaction transaction,
          String passThroughTokenHeader,
          Map<String, Object> logMap)
          throws ServiceException {
    var paymentLink = transaction.getLinks().getPayment();
    var paymentReference = getPaymentReferenceFromTransaction(paymentLink, passThroughTokenHeader);
    var payment = getPayment(paymentReference, passThroughTokenHeader);

    data.put("payment_reference", paymentReference);
    data.put("payment_method", payment.getPaymentMethod());
    ApiLogger.debug("Payment data has been set on filing", logMap);
  }

  private PaymentApi getPayment(String paymentReference, String passThroughTokenHeader)
          throws ServiceException {
    try {
      return apiClientService
              .getOauthAuthenticatedClient(passThroughTokenHeader)
              .payment()
              .get("/payments/" + paymentReference)
              .execute()
              .getData();
    } catch (URIValidationException | IOException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  private String getPaymentReferenceFromTransaction(String uri, String passThroughTokenHeader)
          throws ServiceException {
    try {
      var transactionPaymentInfo =
              apiClientService
                      .getOauthAuthenticatedClient(passThroughTokenHeader)
                      .transactions()
                      .getPayment(uri)
                      .execute();

      return transactionPaymentInfo.getData().getPaymentReference();
    } catch (URIValidationException | IOException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  private void setDescriptionFields(FilingApi filing, OverseasEntitySubmissionDto submissionDto) {
    filing.setDescriptionIdentifier(filingDescriptionIdentifier);

    String filingDescription;
    if (submissionDto.isForUpdate()) {
      filingDescription = updateFilingDescription;
    } else if (submissionDto.isForRemove()) {
      filingDescription = removeFilingDescription;
    } else {
      // Must be a registration
      filingDescription = registrationFilingDescription;
    }
    String formattedDate = dateNowSupplier.get().format(formatter);
    filing.setDescription(filingDescription.replace("{date}", formattedDate));

    Map<String, String> values = new HashMap<>();
    filing.setDescriptionValues(values);
  }
}
