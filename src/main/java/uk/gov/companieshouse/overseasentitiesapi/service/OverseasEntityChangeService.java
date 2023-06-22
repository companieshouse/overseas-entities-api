package uk.gov.companieshouse.overseasentitiesapi.service;

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
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static uk.gov.companieshouse.overseasentitiesapi.utils.MissingPublicPrivateDataHandler.containsMissingOePublicPrivateData;

@Service
public class OverseasEntityChangeService {
    public static final String SERVICE = "OverseasEntityChangeService";
    private final OverseasEntityChangeComparator overseasEntityChangeComparator;

    @Autowired
    public OverseasEntityChangeService(OverseasEntityChangeComparator overseasEntityChangeComparator) {
        this.overseasEntityChangeComparator = overseasEntityChangeComparator;
    }

    public List<Change> collateOverseasEntityChanges(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission,
            Map<String, Object> logMap) {
        List<Change> changes = new ArrayList<>();

        if (!containsMissingOePublicPrivateData(existingRegistration, SERVICE, logMap) && updateSubmission != null) {
            addNonNullChangeToList(changes, retrieveEntityNameChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes, retrievePrincipalAddressChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes, retrieveCorrespondenceAddressChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes, retrieveCompanyIdentificationChange(existingRegistration, updateSubmission));
            addNonNullChangeToList(changes, retrieveEmailAddressChange(existingRegistration, updateSubmission));
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
        RegisteredOfficeAddressApi existingPrincipalAddress = Optional.ofNullable(existingRegistration.getLeft())
                .map(CompanyProfileApi::getRegisteredOfficeAddress)
                .orElse(null);
        var entityDto = Optional.ofNullable(updateSubmission)
                .map(OverseasEntitySubmissionDto::getEntity)
                .orElse(null);

        AddressDto updatedAddress;

        if (Optional.of(entityDto)
                .map(EntityDto::getServiceAddressSameAsPrincipalAddress)
                .orElse(false)) {
            updatedAddress = Optional.ofNullable(entityDto)
                    .map(EntityDto::getServiceAddress)
                    .orElse(null);
        } else {
            updatedAddress = Optional.ofNullable(entityDto)
                    .map(EntityDto::getPrincipalAddress)
                    .orElse(null);
        }
        return overseasEntityChangeComparator.comparePrincipalAddress(existingPrincipalAddress, updatedAddress);
    }

    private CorrespondenceAddressChange retrieveCorrespondenceAddressChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        return overseasEntityChangeComparator.compareCorrespondenceAddress(
                Optional.ofNullable(existingRegistration.getLeft())
                        .map(CompanyProfileApi::getServiceAddress)
                        .orElse(null),
                Optional.ofNullable(updateSubmission)
                        .map(OverseasEntitySubmissionDto::getEntity)
                        .map(EntityDto::getServiceAddress)
                        .orElse(null));
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
                        .map(EntityDto::getRegistrationNumber)
                        .orElse(null));
    }
}
