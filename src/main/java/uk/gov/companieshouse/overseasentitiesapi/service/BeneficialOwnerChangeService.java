package uk.gov.companieshouse.overseasentitiesapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.psc.Identification;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.BeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.CorporateBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.IndividualBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.OtherBeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.CorporateBeneficialOwnerPsc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.IndividualBeneficialOwnerPsc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.OtherBeneficialOwnerPsc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.Psc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.ChangeManager;
import uk.gov.companieshouse.overseasentitiesapi.utils.ComparisonHelper;
import uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping;
import uk.gov.companieshouse.overseasentitiesapi.utils.NatureOfControlTypeMapping;
import uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter;

@Service
public class BeneficialOwnerChangeService {
  public static final String SERVICE = "BeneficialOwnerChangeService";
  public static final String NO_PUBLIC_AND_NO_PRIVATE_BO_DATA_FOUND = "No public and no private data found for beneficial owner";
  public static final String NO_PUBLIC_BO_DATA_FOUND = "No public data found for beneficial owner - continuing with changes";
  public static final String NO_PRIVATE_BO_DATA_FOUND = "No private data found for beneficial owner - changes cannot be created";
  private Map<String, Pair<PscApi, PrivateBoDataApi>> publicPrivateBo;
  private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  /**
   * Gathers all changes in beneficial ownership across different types of owners.
   *
   * @return a list of Change objects, encompassing changes in beneficial ownership for individual
   * owners, corporate entities, and others. This consolidated list provides a complete overview of
   * all beneficial ownership changes in the system.
   */
  public List<Change> collateBeneficialOwnerChanges(
      Map<String, Pair<PscApi, PrivateBoDataApi>> publicPrivateBo,
      OverseasEntitySubmissionDto overseasEntitySubmissionDto,
      Map<String, Object> logMap) {
    this.publicPrivateBo = publicPrivateBo;
    this.overseasEntitySubmissionDto = overseasEntitySubmissionDto;

    List<Change> changes = new ArrayList<>();
    changes.addAll(individualBeneficialOwnerChange(logMap));
    changes.addAll(beneficialOwnerOtherChange(logMap));
    changes.addAll(beneficialOwnerCorporateChange(logMap));
    return changes;
  }

  /**
   * Identifies and returns changes related to the 'Individual' type beneficial owners in the
   * submission.
   *
   * <p>This method is responsible for tracking the changes made to the beneficial owners of
   * 'Individual' type within the provided submission. Each change is encapsulated within a
   * {@code BeneficialOwnerChange<IndividualBeneficialOwnerPsc>} object.</p>
   *
   * @return A list of changes ({@code BeneficialOwnerChange<IndividualBeneficialOwnerPsc>} objects)
   * related to the beneficial owners of 'Individual' type. If no changes are detected, an empty
   * list is returned.
   */
  private List<Change> individualBeneficialOwnerChange(Map<String, Object> logMap) {
    var beneficialOwnersIndividual = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
    return beneficialOwnersIndividual
        .stream()
        .filter(bo -> bo.getChipsReference() != null)
        .map(individualBO -> convertBeneficialOwnerIndividualToChange(individualBO, logMap))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Identifies and returns changes related to the 'Other' type beneficial owners in the
   * submission.
   *
   * <p>This method is responsible for tracking the changes made to the beneficial owners of
   * 'Other' type within the provided submission. Each change is encapsulated within a
   * {@code BeneficialOwnerChange<OtherBeneficialOwnerPsc>} object.</p>
   *
   * @return A list of changes ({@code BeneficialOwnerChange<OtherBeneficialOwnerPsc>} objects)
   * related to the beneficial owners of 'Other' type. If no changes are detected, an empty list is
   * returned.
   */
  private List<Change> beneficialOwnerOtherChange(Map<String, Object> logMap) {
    var beneficialOwnersGovernmentOrPublicAuthority = overseasEntitySubmissionDto
        .getBeneficialOwnersGovernmentOrPublicAuthority();
    return beneficialOwnersGovernmentOrPublicAuthority
        .stream()
        .filter(bo -> bo.getChipsReference() != null)
        .map(otherBO -> convertBeneficialOwnerOtherChange(otherBO, logMap))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Identifies and returns changes related to the 'Corporate' type beneficial owners in the
   * submission.
   *
   * <p>This method is responsible for tracking the changes made to the beneficial owners of
   * 'Corporate' type within the provided submission. Each change is encapsulated within a
   * {@code BeneficialOwnerChange<CorporateBeneficialOwnerPsc>} object.</p>
   *
   * @return A list of changes ({@code BeneficialOwnerChange<CorporateBeneficialOwnerPsc>} objects)
   * related to the beneficial owners of 'Corporate' type. If no changes are detected, an empty list
   * is returned.
   */
  private List<Change> beneficialOwnerCorporateChange(Map<String, Object> logMap) {
    var beneficialOwnersGovernmentOrPublicAuthority = overseasEntitySubmissionDto
        .getBeneficialOwnersCorporate();
    return beneficialOwnersGovernmentOrPublicAuthority
        .stream()
        .filter(bo -> bo.getChipsReference() != null)
        .map(corporateBO -> convertBeneficialOwnerCorporateChange(corporateBO, logMap))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Converts a BeneficialOwnerCorporateDto into a BeneficialOwnerChange object.
   *
   * <p>The method detects changes between the provided BeneficialOwnerCorporateDto and its
   * corresponding information in the public and private data.</p>
   *
   * @param beneficialOwnerCorporateDto The BeneficialOwnerCorporateDto object to be converted.
   * @return A BeneficialOwnerChange object if changes are detected, otherwise null.
   */
  private BeneficialOwnerChange<CorporateBeneficialOwnerPsc> convertBeneficialOwnerCorporateChange(
      BeneficialOwnerCorporateDto beneficialOwnerCorporateDto,
      Map<String, Object> logMap) {

    var beneficialOwnerChange = new CorporateBeneficialOwnerChange();

    var psc = new CorporateBeneficialOwnerPsc();

    Pair<PscApi, PrivateBoDataApi> publicPrivateBoPair = publicPrivateBo.get(
        beneficialOwnerCorporateDto.getChipsReference());

    if (publicPrivateBoPair == null) {
      ApiLogger.errorContext(SERVICE, NO_PUBLIC_AND_NO_PRIVATE_BO_DATA_FOUND, null, logMap);
      return null;
    }
    else {
      if (publicPrivateBoPair.getLeft() == null) {
        ApiLogger.errorContext(SERVICE, NO_PUBLIC_BO_DATA_FOUND, null, logMap);
      }

      if (publicPrivateBoPair.getRight() == null) {
        ApiLogger.errorContext(SERVICE, NO_PRIVATE_BO_DATA_FOUND, null, logMap);
        return null;
      }
    }

    ChangeManager<CorporateBeneficialOwnerPsc, PscApi, PrivateBoDataApi> changeManager = new ChangeManager<>(
        psc, publicPrivateBoPair);

    beneficialOwnerChange.setAppointmentId(publicPrivateBoPair.getRight().getPscId());

    var beneficialOwnerNatureOfControlTypes = beneficialOwnerCorporateDto.getBeneficialOwnerNatureOfControlTypes();
    var trusteesNatureOfControlTypes = beneficialOwnerCorporateDto.getTrusteesNatureOfControlTypes();
    var nonLegalFirmMembersNatureOfControlTypes = beneficialOwnerCorporateDto.getNonLegalFirmMembersNatureOfControlTypes();

    var collectedNatureOfControl = NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList(
        beneficialOwnerNatureOfControlTypes,
        trusteesNatureOfControlTypes, nonLegalFirmMembersNatureOfControlTypes);

    boolean hasChange = setCommonAttributes(changeManager,
        beneficialOwnerCorporateDto.getServiceAddress(),
        beneficialOwnerCorporateDto.getPrincipalAddress(),
        PrivateBoDataApi::getPrincipalAddress,
        collectedNatureOfControl,
        beneficialOwnerCorporateDto.getOnSanctionsList(),
        beneficialOwnerCorporateDto.getServiceAddressSameAsPrincipalAddress()
    );

    hasChange |= changeManager.compareAndBuildLeftChange(
        beneficialOwnerCorporateDto.getName(),
        PscApi::getName,
        CorporateBeneficialOwnerPsc::setCorporateName);

    psc.setCompanyIdentification(new CompanyIdentification());

    Identification publicIdentification = publicPrivateBoPair.getLeft() == null ? null
        : publicPrivateBoPair.getLeft().getIdentification();

    ChangeManager<CompanyIdentification, PscApi, Identification> companyIdentificationChangeManager
        = new ChangeManager<>(psc.getCompanyIdentification(),
        new ImmutablePair<>(publicPrivateBoPair.getLeft(), publicIdentification));

    hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
        beneficialOwnerCorporateDto.getLegalForm(),
        Identification::getLegalForm,
        CompanyIdentification::setLegalForm);

    hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
        beneficialOwnerCorporateDto.getLawGoverned(),
        Identification::getLegalAuthority,
        CompanyIdentification::setGoverningLaw);

    hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
        beneficialOwnerCorporateDto.getPublicRegisterName(),
        Identification::getPlaceRegistered,
        CompanyIdentification::setRegisterLocation);

    hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
        beneficialOwnerCorporateDto.getRegistrationNumber(),
        Identification::getRegistrationNumber,
        CompanyIdentification::setRegistrationNumber);

    if (beneficialOwnerCorporateDto.getTrustIds() != null && !beneficialOwnerCorporateDto.getTrustIds().isEmpty()) {
        psc.setAddedTrustIds(beneficialOwnerCorporateDto.getTrustIds());
        hasChange = true;
    }

    beneficialOwnerChange.setPsc(psc);
    return hasChange ? beneficialOwnerChange : null;
  }

  /**
   * Converts a BeneficialOwnerGovernmentOrPublicAuthorityDto into a BeneficialOwnerChange object.
   *
   * <p>The method detects changes between the provided
   * BeneficialOwnerGovernmentOrPublicAuthorityDto and its corresponding information in the public
   * and private data.</p>
   *
   * @param beneficialOwnerGovernmentOrPublicAuthorityDto The
   *                                                      BeneficialOwnerGovernmentOrPublicAuthorityDto
   *                                                      object to be converted.
   * @return A BeneficialOwnerChange object if changes are detected, otherwise null.
   */
  private BeneficialOwnerChange<OtherBeneficialOwnerPsc> convertBeneficialOwnerOtherChange(
      BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto,
      Map<String, Object> logMap) {
    var beneficialOwnerChange = new OtherBeneficialOwnerChange();

    var psc = new OtherBeneficialOwnerPsc();

    Pair<PscApi, PrivateBoDataApi> publicPrivateBoPair = publicPrivateBo.get(
        beneficialOwnerGovernmentOrPublicAuthorityDto.getChipsReference());


    if (publicPrivateBoPair == null) {
      ApiLogger.errorContext(SERVICE, NO_PUBLIC_AND_NO_PRIVATE_BO_DATA_FOUND, null, logMap);
      return null;
    }
    else {
      if (publicPrivateBoPair.getLeft() == null) {
        ApiLogger.errorContext(SERVICE, NO_PUBLIC_BO_DATA_FOUND, null, logMap);
      }

      if (publicPrivateBoPair.getRight() == null) {
        ApiLogger.errorContext(SERVICE, NO_PRIVATE_BO_DATA_FOUND, null, logMap);
        return null;
      }
    }

    ChangeManager<OtherBeneficialOwnerPsc, PscApi, PrivateBoDataApi> changeManager = new ChangeManager<>(
        psc, publicPrivateBoPair);

    beneficialOwnerChange.setAppointmentId(publicPrivateBoPair.getRight().getPscId());

    var beneficialOwnerNatureOfControlTypes = beneficialOwnerGovernmentOrPublicAuthorityDto.getBeneficialOwnerNatureOfControlTypes();
    var nonLegalFirmMembersNatureOfControlTypes = beneficialOwnerGovernmentOrPublicAuthorityDto.getNonLegalFirmMembersNatureOfControlTypes();

    var collectedNatureOfControl = NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList(
        beneficialOwnerNatureOfControlTypes,
        null, nonLegalFirmMembersNatureOfControlTypes);

    boolean hasChange = setCommonAttributes(changeManager,
        beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddress(),
        beneficialOwnerGovernmentOrPublicAuthorityDto.getPrincipalAddress(),
        PrivateBoDataApi::getPrincipalAddress,
        collectedNatureOfControl,
        beneficialOwnerGovernmentOrPublicAuthorityDto.getOnSanctionsList(),
        beneficialOwnerGovernmentOrPublicAuthorityDto.getServiceAddressSameAsPrincipalAddress()
    );

    hasChange |= changeManager.compareAndBuildLeftChange(
        beneficialOwnerGovernmentOrPublicAuthorityDto.getName(),
        PscApi::getName,
        OtherBeneficialOwnerPsc::setCorporateName);

    psc.setCompanyIdentification(new CompanyIdentification());

    Identification publicIdentification = publicPrivateBoPair.getLeft() == null ? null
        : publicPrivateBoPair.getLeft().getIdentification();

    ChangeManager<CompanyIdentification, PscApi, Identification> companyIdentificationChangeManager
        = new ChangeManager<>(psc.getCompanyIdentification(),
        new ImmutablePair<>(publicPrivateBoPair.getLeft(), publicIdentification));

    hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
        beneficialOwnerGovernmentOrPublicAuthorityDto.getLegalForm(),
        Identification::getLegalForm,
        CompanyIdentification::setLegalForm);

    hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
        beneficialOwnerGovernmentOrPublicAuthorityDto.getLawGoverned(),
        Identification::getLegalAuthority,
        CompanyIdentification::setGoverningLaw);

    beneficialOwnerChange.setPsc(psc);
    return hasChange ? beneficialOwnerChange : null;
  }

  /**
   * Converts a BeneficialOwnerIndividualDto into a BeneficialOwnerChange object.
   *
   * <p>The method detects changes between the provided
   * BeneficialOwnerGovernmentOrPublicAuthorityDto and its corresponding information in the public
   * and private data.</p>
   *
   * @param beneficialOwnerIndividualDto The BeneficialOwnerIndividualDto object to be converted.
   * @return A BeneficialOwnerChange object if changes are detected, otherwise null.
   */
  private BeneficialOwnerChange<IndividualBeneficialOwnerPsc> convertBeneficialOwnerIndividualToChange(
      BeneficialOwnerIndividualDto beneficialOwnerIndividualDto,
      Map<String, Object> logMap) {

    var beneficialOwnerChange = new IndividualBeneficialOwnerChange();

    var psc = new IndividualBeneficialOwnerPsc();

    Pair<PscApi, PrivateBoDataApi> publicPrivateBoPair = publicPrivateBo.get(
        beneficialOwnerIndividualDto.getChipsReference());

    if (publicPrivateBoPair == null) {
      ApiLogger.errorContext(SERVICE, NO_PUBLIC_AND_NO_PRIVATE_BO_DATA_FOUND, null, logMap);
      return null;
    }
    else {
      if (publicPrivateBoPair.getLeft() == null) {
        ApiLogger.errorContext(SERVICE, NO_PUBLIC_BO_DATA_FOUND, null, logMap);
      }

      if (publicPrivateBoPair.getRight() == null) {
        ApiLogger.errorContext(SERVICE, NO_PRIVATE_BO_DATA_FOUND, null, logMap);
        return null;
      }
    }

    ChangeManager<IndividualBeneficialOwnerPsc, PscApi, PrivateBoDataApi> changeManager = new ChangeManager<>(
        psc, publicPrivateBoPair);

    beneficialOwnerChange.setAppointmentId(publicPrivateBoPair.getRight().getPscId());

    var beneficialOwnerNatureOfControlTypes = beneficialOwnerIndividualDto.getBeneficialOwnerNatureOfControlTypes();
    var trusteesNatureOfControlTypes = beneficialOwnerIndividualDto.getTrusteesNatureOfControlTypes();
    var nonLegalFirmMembersNatureOfControlTypes = beneficialOwnerIndividualDto.getNonLegalFirmMembersNatureOfControlTypes();

    var collectedNatureOfControl = NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList(
        beneficialOwnerNatureOfControlTypes,
        trusteesNatureOfControlTypes, nonLegalFirmMembersNatureOfControlTypes);

    boolean hasChange = setCommonAttributes(changeManager,
        beneficialOwnerIndividualDto.getServiceAddress(),
        beneficialOwnerIndividualDto.getUsualResidentialAddress(),
        PrivateBoDataApi::getUsualResidentialAddress,
        collectedNatureOfControl,
        beneficialOwnerIndividualDto.getOnSanctionsList(),
        beneficialOwnerIndividualDto.getServiceAddressSameAsUsualResidentialAddress());

    var submissionNationality = NationalityOtherMapping.generateNationalityOtherField(
            beneficialOwnerIndividualDto.getNationality(),
            beneficialOwnerIndividualDto.getSecondNationality()
    );

    hasChange |= changeManager.compareAndBuildLeftChange(
        submissionNationality,
        PscApi::getNationality,
        IndividualBeneficialOwnerPsc::setNationalityOther);

    PersonName personName = null;
    if(beneficialOwnerIndividualDto.getFirstName() != null && beneficialOwnerIndividualDto.getLastName() != null) {
      personName = new PersonName(beneficialOwnerIndividualDto.getFirstName(),
          beneficialOwnerIndividualDto.getLastName());
    }

    hasChange |= changeManager.compareAndBuildLeftChange(
        personName,
        PscApi::getName,
        Function.identity(),
        ComparisonHelper::equals,
        IndividualBeneficialOwnerPsc::setPersonName
    );

    if (beneficialOwnerIndividualDto.getTrustIds() != null && !beneficialOwnerIndividualDto.getTrustIds().isEmpty()) {
        psc.setAddedTrustIds(beneficialOwnerIndividualDto.getTrustIds());
        hasChange = true;
    }

    beneficialOwnerChange.setPsc(psc);
    return hasChange ? beneficialOwnerChange : null;
  }

  /**
   * Applies changes to the common attributes found in all instances of the abstract Person of
   * Significant Control (PSC) class.
   *
   * <p>The method leverages the {@code ChangeManager} to detect changes
   * between the provided data in DTOs and the current state of these attributes in the PSC object.
   * If discrepancies are detected, the changes are applied accordingly.</p>
   *
   * @param changeManager       The ChangeManager used to detect and apply changes to the PSC.
   * @param serviceAddress      The service address DTO to be compared and potentially applied.
   * @param residentialAddress  The residential address DTO to be compared and potentially applied.
   * @param natureOfControls    The list of nature of controls to be compared and potentially
   *                            applied.
   * @return A boolean indicating whether any changes were detected and applied (true if changes
   * were applied, false otherwise).
   */
  private <P extends Psc> boolean setCommonAttributes(
          ChangeManager<P, PscApi, PrivateBoDataApi> changeManager,
          AddressDto serviceAddress,
          AddressDto residentialAddress,
          Function<PrivateBoDataApi, AddressApi> residentialAddressGetter,
          List<String> natureOfControls,
          Boolean isOnSanctionsList,
          Boolean serviceAddressSameAsResidentialAddress
  ) {

    var hasChange = changeManager.compareAndBuildLeftChange(
        Boolean.TRUE.equals(serviceAddressSameAsResidentialAddress) ? residentialAddress : serviceAddress,
        PscApi::getAddress,
        TypeConverter::addressDtoToAddress,
        ComparisonHelper::equals,
        Psc::setServiceAddress
    );

    hasChange |= changeManager.compareAndBuildRightChange(
        residentialAddress,
        residentialAddressGetter,
        TypeConverter::addressDtoToAddress,
        ComparisonHelper::equals,
        Psc::setResidentialAddress
    );

    hasChange |= changeManager.compareAndBuildLeftChange(
        natureOfControls,
        PscApi::getNaturesOfControl,
        Function.identity(),
        ComparisonHelper::natureOfControlsEquals,
        Psc::addNatureOfControlTypes);

    hasChange |= changeManager.compareAndBuildLeftChange(
            isOnSanctionsList,
            PscApi::isSanctioned,
            Function.identity(),
            ComparisonHelper::equals,
            Psc::setOnSanctionsList);

    return hasChange;
  }

}
