package uk.gov.companieshouse.overseasentitiesapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.api.model.company.foreigncompany.ForeignCompanyDetailsApi;
import uk.gov.companieshouse.api.model.company.foreigncompany.OriginatingRegistryApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.CompanyIdentificationChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.CorrespondenceAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityEmailAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityNameChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.PrincipalAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeComparator;

@Service
public class OverseasEntityChangeService {

    public static final String SERVICE = "OverseasEntityChangeService";
    public static final String NO_PUBLIC_AND_NO_PRIVATE_OE_DATA_FOUND = "No public and no private data found for overseas entity";
    public static final String NO_PUBLIC_OE_DATA_FOUND = "No public data found for overseas entity";
    public static final String NO_PRIVATE_OE_DATA_FOUND = "No private data found for overseas entity";
    private final OverseasEntityChangeComparator overseasEntityChangeComparator;

    @Autowired
    public OverseasEntityChangeService(
            OverseasEntityChangeComparator overseasEntityChangeComparator) {
        this.overseasEntityChangeComparator = overseasEntityChangeComparator;
    }

    public List<Change> collateOverseasEntityChanges(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission,
            Map<String, Object> logMap) {
        List<Change> changes = new ArrayList<>();

        if (existingRegistration == null) {
            ApiLogger.errorContext(SERVICE, NO_PUBLIC_AND_NO_PRIVATE_OE_DATA_FOUND, null, logMap);
        }
        else {
            if (existingRegistration.getLeft() == null) {
                ApiLogger.errorContext(SERVICE, NO_PUBLIC_OE_DATA_FOUND, null, logMap);
            }

            if (existingRegistration.getRight() == null) {
                ApiLogger.errorContext(SERVICE, NO_PRIVATE_OE_DATA_FOUND, null, logMap);
            }
        }

        if (existingRegistration != null && updateSubmission != null) {
            addNonNullChangeToList(changes,
                    retrieveEntityNameChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes,
                    retrievePrincipalAddressChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes,
                    retrieveCorrespondenceAddressChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes,
                    retrieveCompanyIdentificationChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes,
                    retrieveEmailAddressChange(existingRegistration, updateSubmission));
        }

        return changes;
    }

    private void addNonNullChangeToList(List<Change> changes, Change change) {
        if (change != null) {
            changes.add(change);
        }
    }

    private EntityNameChange retrieveEntityNameChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        return overseasEntityChangeComparator.compareEntityName(
                Optional.ofNullable(existingRegistration.getLeft())
                        .map(CompanyProfileApi::getCompanyName)
                        .orElse(null),
                Optional.ofNullable(updateSubmission)
                        .map(OverseasEntitySubmissionDto::getEntityName)
                        .map(EntityNameDto::getName)
                        .orElse(null));
    }

    private PrincipalAddressChange retrievePrincipalAddressChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        RegisteredOfficeAddressApi existingPrincipalAddress = Optional.ofNullable(
                        existingRegistration.getLeft())
                .map(CompanyProfileApi::getRegisteredOfficeAddress)
                .orElse(null);

        AddressDto updatedAddress = Optional.ofNullable(updateSubmission)
                .map(OverseasEntitySubmissionDto::getEntity)
                .map(EntityDto::getPrincipalAddress)
                .orElse(null);

        return overseasEntityChangeComparator.comparePrincipalAddress(existingPrincipalAddress,
                updatedAddress);
    }

    private CorrespondenceAddressChange retrieveCorrespondenceAddressChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {

        var entityDto = Optional.ofNullable(updateSubmission)
                .map(OverseasEntitySubmissionDto::getEntity)
                .orElse(null);

        AddressDto updatedAddress;

        assert entityDto != null;
        if (Optional.of(entityDto)
                .map(EntityDto::getServiceAddressSameAsPrincipalAddress)
                .orElse(false)) {

            updatedAddress = Optional.of(entityDto)
                    .map(EntityDto::getPrincipalAddress)
                    .orElse(null);
        } else {
            updatedAddress = Optional.of(entityDto)
                    .map(EntityDto::getServiceAddress)
                    .orElse(null);
        }

        var existingCorrespondenceAddress = Optional.ofNullable(existingRegistration.getLeft())
                .map(CompanyProfileApi::getServiceAddress)
                .orElse(null);

        return overseasEntityChangeComparator.compareCorrespondenceAddress(
                existingCorrespondenceAddress, updatedAddress);
    }

    private CompanyIdentificationChange retrieveCompanyIdentificationChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        return overseasEntityChangeComparator.compareCompanyIdentification(
                getCompanyIdentificationFromExistingRegistration(existingRegistration),
                getCompanyIdentificationFromUpdateSubmission(updateSubmission));
    }

    private EntityEmailAddressChange retrieveEmailAddressChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        return overseasEntityChangeComparator.compareEntityEmailAddress(
                Optional.ofNullable(existingRegistration.getRight())
                        .map(OverseasEntityDataApi::getEmail)
                        .orElse(null),
                Optional.ofNullable(updateSubmission)
                        .map(OverseasEntitySubmissionDto::getEntity)
                        .map(EntityDto::getEmail)
                        .orElse(null));
    }

    private CompanyIdentification getCompanyIdentificationFromExistingRegistration(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration) {
        var companyDetails = Optional.ofNullable(existingRegistration.getLeft())
                .map(CompanyProfileApi::getForeignCompanyDetails)
                .orElse(null);

        return new CompanyIdentification(
                Optional.ofNullable(companyDetails)
                        .map(ForeignCompanyDetailsApi::getLegalForm)
                        .orElse(null),
                Optional.ofNullable(companyDetails)
                        .map(ForeignCompanyDetailsApi::getGovernedBy)
                        .orElse(null),
                Optional.ofNullable(companyDetails)
                        .map(ForeignCompanyDetailsApi::getOriginatingRegistry)
                        .map(OriginatingRegistryApi::getCountry)
                        .orElse(null),
                Optional.ofNullable(companyDetails)
                        .map(ForeignCompanyDetailsApi::getOriginatingRegistry)
                        .map(OriginatingRegistryApi::getName)
                        .orElse(null),
                null,
                Optional.ofNullable(companyDetails)
                        .map(ForeignCompanyDetailsApi::getRegistrationNumber)
                        .orElse(null));
    }

    private CompanyIdentification getCompanyIdentificationFromUpdateSubmission(
            OverseasEntitySubmissionDto updateSubmission) {
        var entityDto = Optional.ofNullable(updateSubmission)
                .map(OverseasEntitySubmissionDto::getEntity)
                .orElse(null);

        return new CompanyIdentification(
                Optional.ofNullable(entityDto)
                        .map(EntityDto::getLegalForm)
                        .orElse(null),
                Optional.ofNullable(entityDto)
                        .map(EntityDto::getLawGoverned)
                        .orElse(null),
                Optional.ofNullable(entityDto)
                        .map(EntityDto::getIncorporationCountry)
                        .orElse(null),
                Optional.ofNullable(entityDto)
                        .map(EntityDto::getPublicRegisterName)
                        .orElse(null),
                Optional.ofNullable(entityDto)
                        .map(EntityDto::getPublicRegisterJurisdiction)
                        .orElse(null),
                Optional.ofNullable(entityDto)
                        .map(EntityDto::getRegistrationNumber)
                        .orElse(null));
    }
}
