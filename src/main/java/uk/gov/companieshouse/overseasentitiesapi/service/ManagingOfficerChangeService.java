package uk.gov.companieshouse.overseasentitiesapi.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.IdentificationApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.CorporateManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.IndividualManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.ManagingOfficerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.CorporateManagingOfficer;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.IndividualManagingOfficer;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.managingofficer.officer.Officer;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.ChangeManager;
import uk.gov.companieshouse.overseasentitiesapi.utils.ComparisonHelper;
import uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping.generateNationalityOtherField;

@Service
public class ManagingOfficerChangeService {
    public static final String NO_PUBLIC_AND_NO_PRIVATE_MO_DATA_FOUND = "No public and no private data found for managing officer";
    public static final String NO_PUBLIC_MO_DATA_FOUND = "No public data found for managing officer - continuing with changes";
    public static final String NO_PRIVATE_MO_DATA_FOUND = "No private data found for managing officer - changes cannot be created";
    public static final String SERVICE = "ManagingOfficerChangeService";
    private Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> publicPrivateMo;
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

    /**
     * Gathers all changes in managing officers
     *
     * @return a list of Change objects, encompassing changes in individual and corporate managing
     * officers. This consolidated list provides a complete overview of all managing officer
     * changes in the system.
     */
    public List<Change> collateManagingOfficerChanges(
            Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> publicPrivateMo,
            OverseasEntitySubmissionDto overseasEntitySubmissionDto,
            Map<String, Object> logMap) {
        this.publicPrivateMo = publicPrivateMo;
        this.overseasEntitySubmissionDto = overseasEntitySubmissionDto;

        List<Change> changes = new ArrayList<>();
        changes.addAll(getIndividualManagingOfficerChange(logMap));
        changes.addAll(getCorporateManagingOfficerChange(logMap));
        return changes;
    }


    /**
     * Identifies and returns changes related to the 'Individual' type managing officers in the
     * submission.
     *
     * <p>This method is responsible for tracking the changes made to the managing officers of
     * 'Individual' type within the provided submission. Each change is encapsulated within a
     * {@code ManagingOfficerChange<IndividualManagingOfficer>} object.</p>
     *
     * @return A list of changes ({@code ManagingOfficerChange<IndividualManagingOfficer>} objects)
     * related to the managing officers of 'Individual' type. If no changes are detected, an empty
     * list is returned.
     */
    private List<Change> getIndividualManagingOfficerChange(Map<String, Object> logMap) {
        var individualManagingOfficers = overseasEntitySubmissionDto.getManagingOfficersIndividual();
        return individualManagingOfficers
                .stream()
                .filter(mo -> mo.getChipsReference() != null)
                .map(individualMO -> convertManagingOfficerIndividualToChange(individualMO, logMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * Identifies and returns changes related to the 'Corporate' type managing officers in the
     * submission.
     *
     * <p>This method is responsible for tracking the changes made to the managing officers of
     * 'Corporate' type within the provided submission. Each change is encapsulated within a
     * {@code ManagingOfficerChange<CorporateManagingOfficer>} object.</p>
     *
     * @return A list of changes ({@code ManagingOfficerChange<CorporateManagingOfficer>} objects)
     * related to the managing officers of 'Corporate' type. If no changes are detected, an empty
     * list is returned.
     */
    private List<Change> getCorporateManagingOfficerChange(Map<String, Object> logMap) {
        var corporateManagingOfficers = overseasEntitySubmissionDto.getManagingOfficersCorporate();
        return corporateManagingOfficers
                .stream()
                .filter(mo -> mo.getChipsReference() != null)
                .map(corporateMO -> convertManagingOfficerCorporateToChange(corporateMO, logMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Converts a ManagingOfficerIndividualDto into a ManagingOfficerChange object.
     *
     * <p>The method detects changes between the provided managingOfficerIndividualDto and its
     * corresponding information in the public and private data.</p>
     *
     * @param managingOfficerIndividualDto The ManagingOfficerIndividualDto object to be converted.
     * @return A ManagingOfficerChange object if changes are detected, otherwise null.
     */
    private ManagingOfficerChange<IndividualManagingOfficer> convertManagingOfficerIndividualToChange(
            ManagingOfficerIndividualDto managingOfficerIndividualDto,
            Map<String, Object> logMap) {
        var managingOfficerChange = new IndividualManagingOfficerChange();

        var officer = new IndividualManagingOfficer();

        Pair<CompanyOfficerApi, ManagingOfficerDataApi> publicPrivateMoPair = publicPrivateMo.get(
                managingOfficerIndividualDto.getChipsReference());

        if (publicPrivateMoPair == null) {
            ApiLogger.errorContext(SERVICE, NO_PUBLIC_AND_NO_PRIVATE_MO_DATA_FOUND, null, logMap);
            return null;
        }
        else {
            if (publicPrivateMoPair.getLeft() == null) {
                ApiLogger.errorContext(SERVICE, NO_PUBLIC_MO_DATA_FOUND, null, logMap);
            }

            if (publicPrivateMoPair.getRight() == null) {
                ApiLogger.errorContext(SERVICE, NO_PRIVATE_MO_DATA_FOUND, null, logMap);
                return null;
            }
        }

        ChangeManager<IndividualManagingOfficer, CompanyOfficerApi, ManagingOfficerDataApi> changeManager = new ChangeManager<>(
                officer, publicPrivateMoPair);

        managingOfficerChange.setAppointmentId(publicPrivateMoPair.getRight().getManagingOfficerAppointmentId());

        if (publicPrivateMoPair.getLeft() != null && publicPrivateMoPair.getLeft().getAppointedOn() == null) {
            officer.setStartDate(managingOfficerIndividualDto.getStartDate());
        }

        boolean hasChange = setCommonAttributes(changeManager,
                managingOfficerIndividualDto.getServiceAddress(),
                managingOfficerIndividualDto.getUsualResidentialAddress(),
                managingOfficerIndividualDto.getRoleAndResponsibilities(),
                managingOfficerIndividualDto.getServiceAddressSameAsUsualResidentialAddress()
        );

        PersonName personName = null;
        if (managingOfficerIndividualDto.getFirstName() != null && managingOfficerIndividualDto.getLastName() != null) {
            personName = new PersonName(managingOfficerIndividualDto.getFirstName(),
                    managingOfficerIndividualDto.getLastName());
        }
        hasChange |= changeManager.compareAndBuildLeftChange(
                personName,
                CompanyOfficerApi::getName,
                Function.identity(),
                ComparisonHelper::equals,
                IndividualManagingOfficer::setPersonName
        );

        hasChange |= changeManager.compareAndBuildLeftChange(
                managingOfficerIndividualDto.getFormerNames(),
                CompanyOfficerApi::getFormerNames,
                Function.identity(),
                ComparisonHelper::equalsFormerName,
                IndividualManagingOfficer::setFormerNames
        );

        hasChange |= changeManager.compareAndBuildRightChange(
                managingOfficerIndividualDto.getUsualResidentialAddress(),
                ManagingOfficerDataApi::getResidentialAddress,
                TypeConverter::addressDtoToAddress,
                ComparisonHelper::equals,
                IndividualManagingOfficer::setResidentialAddress
        );

        hasChange |= changeManager.compareAndBuildLeftChange(
                managingOfficerIndividualDto.getOccupation(),
                CompanyOfficerApi::getOccupation,
                IndividualManagingOfficer::setOccupation
        );

        var nationalitySubmission = generateNationalityOtherField(managingOfficerIndividualDto.getNationality(),
                managingOfficerIndividualDto.getSecondNationality());
        hasChange |= changeManager.compareAndBuildLeftChange(
                nationalitySubmission,
                CompanyOfficerApi::getNationality,
                IndividualManagingOfficer::setNationalityOther
        );

        managingOfficerChange.setOfficer(officer);
        return hasChange ? managingOfficerChange : null;
    }

    /**
     * Converts a ManagingOfficerCorporateDto into a ManagingOfficerChange object.
     *
     * <p>The method detects changes between the provided managingOfficerCorporateDto and its
     * corresponding information in the public and private data.</p>
     *
     * @param managingOfficerCorporateDto The ManagingOfficerCorporateDto object to be converted.
     * @return A ManagingOfficerChange object if changes are detected, otherwise null.
     */
    private ManagingOfficerChange<CorporateManagingOfficer> convertManagingOfficerCorporateToChange(
            ManagingOfficerCorporateDto managingOfficerCorporateDto,
            Map<String, Object> logMap) {
        var managingOfficerChange = new CorporateManagingOfficerChange();

        var officer = new CorporateManagingOfficer();

        Pair<CompanyOfficerApi, ManagingOfficerDataApi> publicPrivateMoPair = publicPrivateMo.get(
                managingOfficerCorporateDto.getChipsReference());

        if (publicPrivateMoPair == null) {
            ApiLogger.errorContext(SERVICE, NO_PUBLIC_AND_NO_PRIVATE_MO_DATA_FOUND, null, logMap);
            return null;
        }
        else {
            if (publicPrivateMoPair.getLeft() == null) {
                ApiLogger.errorContext(SERVICE, NO_PUBLIC_MO_DATA_FOUND, null, logMap);
            }

            if (publicPrivateMoPair.getRight() == null) {
                ApiLogger.errorContext(SERVICE, NO_PRIVATE_MO_DATA_FOUND, null, logMap);
                return null;
            }
        }

        ChangeManager<CorporateManagingOfficer, CompanyOfficerApi, ManagingOfficerDataApi> changeManager
                = new ChangeManager<>(officer, publicPrivateMoPair);

        managingOfficerChange.setAppointmentId(publicPrivateMoPair.getRight().getManagingOfficerAppointmentId());

        if (publicPrivateMoPair.getLeft() != null && publicPrivateMoPair.getLeft().getAppointedOn() == null) {
            officer.setStartDate(managingOfficerCorporateDto.getStartDate());
        }

        boolean hasChange = setCommonAttributes(changeManager,
                managingOfficerCorporateDto.getServiceAddress(),
                managingOfficerCorporateDto.getPrincipalAddress(),
                managingOfficerCorporateDto.getRoleAndResponsibilities(),
                managingOfficerCorporateDto.getServiceAddressSameAsPrincipalAddress()
        );

        hasChange |= changeManager.compareAndBuildLeftChange(
                managingOfficerCorporateDto.getName(),
                CompanyOfficerApi::getName,
                CorporateManagingOfficer::setName
        );

        hasChange |= changeManager.compareAndBuildLeftChange(
                managingOfficerCorporateDto.getPrincipalAddress(),
                CompanyOfficerApi::getPrincipalOfficeAddress,
                TypeConverter::addressDtoToAddress,
                ComparisonHelper::equals,
                CorporateManagingOfficer::setRegisteredOffice
        );

        hasChange |= changeManager.compareAndBuildRightChange(
                managingOfficerCorporateDto.getContactFullName(),
                ManagingOfficerDataApi::getContactNameFull,
                CorporateManagingOfficer::setContactName
        );

        hasChange |= changeManager.compareAndBuildRightChange(
                managingOfficerCorporateDto.getContactEmail(),
                ManagingOfficerDataApi::getContactEmailAddress,
                CorporateManagingOfficer::setEmail
        );

        officer.setCompanyIdentification(new CompanyIdentification());

        IdentificationApi publicIdentification = publicPrivateMoPair.getLeft() == null ? null
                : publicPrivateMoPair.getLeft().getIdentification();

        ChangeManager<CompanyIdentification, CompanyOfficerApi, IdentificationApi> companyIdentificationChangeManager
                = new ChangeManager<>(officer.getCompanyIdentification(),
                new ImmutablePair<>(publicPrivateMoPair.getLeft(), publicIdentification));

        hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
                managingOfficerCorporateDto.getLegalForm(),
                IdentificationApi::getLegalForm,
                CompanyIdentification::setLegalForm);

        hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
                managingOfficerCorporateDto.getLawGoverned(),
                IdentificationApi::getLegalAuthority,
                CompanyIdentification::setGoverningLaw);

        hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
                managingOfficerCorporateDto.getPublicRegisterName(),
                IdentificationApi::getPlaceRegistered,
                CompanyIdentification::setRegisterLocation);

        hasChange |= companyIdentificationChangeManager.compareAndBuildRightChange(
                managingOfficerCorporateDto.getRegistrationNumber(),
                IdentificationApi::getRegistrationNumber,
                CompanyIdentification::setRegistrationNumber);

        managingOfficerChange.setOfficer(officer);
        return hasChange ? managingOfficerChange : null;
    }

    private <P extends Officer> boolean setCommonAttributes(
            ChangeManager<P, CompanyOfficerApi, ManagingOfficerDataApi> changeManager,
            AddressDto serviceAddress,
            AddressDto residentialAddress,
            String role,
            Boolean addressesAreSameFlag
    ) {

        var hasChange = changeManager.compareAndBuildLeftChange(
                Boolean.TRUE.equals(addressesAreSameFlag) ? residentialAddress : serviceAddress,
                CompanyOfficerApi::getAddress,
                TypeConverter::addressDtoToAddress,
                ComparisonHelper::equals,
                Officer::setServiceAddress
        );

        hasChange |= changeManager.compareAndBuildLeftChange(
                role,
                CompanyOfficerApi::getOfficerRole,
                Function.identity(),
                ComparisonHelper::equals,
                Officer::setRoleAndResponsibilities
        );

        return hasChange;
    }
}
