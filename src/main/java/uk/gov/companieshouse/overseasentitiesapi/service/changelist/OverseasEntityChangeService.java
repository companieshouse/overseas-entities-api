package uk.gov.companieshouse.overseasentitiesapi.service.changelist;

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
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OverseasEntityChangeService {
    private final OverseasEntityChangeValidator overseasEntityChangeValidator;

    @Autowired
    public OverseasEntityChangeService(OverseasEntityChangeValidator overseasEntityChangeValidator) {
        this.overseasEntityChangeValidator = overseasEntityChangeValidator;
    }

    public List<Change> collateOverseasEntityChanges(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        List<Change> changes = new ArrayList<>();

        if (existingRegistration != null && updateSubmission != null) {
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
        return overseasEntityChangeValidator.verifyEntityNameChange(
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
        AddressDto updatedAddress = null;

        if (Optional.ofNullable(updateSubmission)
                .map(OverseasEntitySubmissionDto::getEntity)
                .map(EntityDto::getServiceAddressSameAsPrincipalAddress)
                .orElse(false)) {
            updatedAddress = Optional.ofNullable(updateSubmission)
                    .map(OverseasEntitySubmissionDto::getEntity)
                    .map(EntityDto::getServiceAddress)
                    .orElse(null);
        } else {
            updatedAddress = Optional.ofNullable(updateSubmission)
                    .map(OverseasEntitySubmissionDto::getEntity)
                    .map(EntityDto::getPrincipalAddress)
                    .orElse(null);
        }
        return overseasEntityChangeValidator.verifyPrincipalAddressChange(existingPrincipalAddress, updatedAddress);
    }

    private CorrespondenceAddressChange retrieveCorrespondenceAddressChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        return overseasEntityChangeValidator.verifyCorrespondenceAddressChange(
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
        return overseasEntityChangeValidator.verifyCompanyIdentificationChange(
                new CompanyIdentification(
                        Optional.ofNullable(existingRegistration.getLeft())
                                .map(CompanyProfileApi::getForeignCompanyDetails)
                                .map(ForeignCompanyDetailsApi::getLegalForm)
                                .orElse(null),
                        Optional.ofNullable(existingRegistration.getLeft())
                                .map(CompanyProfileApi::getForeignCompanyDetails)
                                .map(ForeignCompanyDetailsApi::getGovernedBy)
                                .orElse(null),
                        Optional.ofNullable(existingRegistration.getLeft())
                                .map(CompanyProfileApi::getForeignCompanyDetails)
                                .map(ForeignCompanyDetailsApi::getOriginatingRegistry)
                                .map(OriginatingRegistryApi::getCountry)
                                .orElse(null),
                        Optional.ofNullable(existingRegistration.getLeft())
                                .map(CompanyProfileApi::getForeignCompanyDetails)
                                .map(ForeignCompanyDetailsApi::getOriginatingRegistry)
                                .map(OriginatingRegistryApi::getName)
                                .orElse(null),
                        Optional.ofNullable(existingRegistration.getLeft())
                                .map(CompanyProfileApi::getForeignCompanyDetails)
                                .map(ForeignCompanyDetailsApi::getRegistrationNumber)
                                .orElse(null)),
                new CompanyIdentification(
                        Optional.ofNullable(updateSubmission)
                                .map(OverseasEntitySubmissionDto::getEntity)
                                .map(EntityDto::getLegalForm)
                                .orElse(null),
                        Optional.ofNullable(updateSubmission.getEntity())
                                .map(EntityDto::getLawGoverned)
                                .orElse(null),
                        Optional.ofNullable(updateSubmission.getEntity())
                                .map(EntityDto::getIncorporationCountry)
                                .orElse(null),
                        Optional.ofNullable(updateSubmission.getEntity())
                                .map(EntityDto::getPublicRegisterName)
                                .orElse(null),
                        Optional.ofNullable(updateSubmission.getEntity())
                                .map(EntityDto::getRegistrationNumber)
                                .orElse(null)));
    }

    private EntityEmailAddressChange retrieveEmailAddressChange(
            Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration,
            OverseasEntitySubmissionDto updateSubmission) {
        return overseasEntityChangeValidator.verifyEntityEmailAddressChange(
                Optional.ofNullable(existingRegistration.getRight())
                        .map(OverseasEntityDataApi::getEmail)
                        .orElse(null),
                Optional.ofNullable(updateSubmission)
                        .map(OverseasEntitySubmissionDto::getEntity)
                        .map(EntityDto::getEmail)
                        .orElse(null));
    }
}
