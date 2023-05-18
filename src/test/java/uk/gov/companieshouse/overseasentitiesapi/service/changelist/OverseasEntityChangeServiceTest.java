package uk.gov.companieshouse.overseasentitiesapi.service.changelist;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.*;

@ExtendWith(MockitoExtension.class)
public class OverseasEntityChangeServiceTest {
    OverseasEntityChangeValidator overseasEntityChangeValidator;

    OverseasEntityChangeService overseasEntityChangeService;

    @BeforeEach
    void init() {
        overseasEntityChangeValidator = new OverseasEntityChangeValidator();
        overseasEntityChangeService = new OverseasEntityChangeService(overseasEntityChangeValidator);
    }

    @Test
    void testCollateOverseasEntityChangesAllFieldsChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionAllDataDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(5, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
        assertTrue(result.get(1) instanceof PrincipalAddressChange);
        assertTrue(result.get(2) instanceof CorrespondenceAddressChange);
        assertTrue(result.get(3) instanceof CompanyIdentificationChange);
        assertTrue(result.get(4) instanceof EntityEmailAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesUpdateSubmissionNoChangeReturnsEmptyChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesNullInputReturnsEmptyChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = null;
        OverseasEntitySubmissionDto updateSubmission = null;

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyEntityNameChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionEntityNameDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyPrincipalAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionPrincipalAddressDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof PrincipalAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesUpdatedPrincipalAddressSameAsCorrespondenceChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionPrincipalAddressDifferent();
        updateSubmission.getEntity().setServiceAddressSameAsPrincipalAddress(true);

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof PrincipalAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyCorrespondenceAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCorrespondenceAddressDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof CorrespondenceAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyCompanyIdentificationChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCompanyIdentificationDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof CompanyIdentificationChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyEmailAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionEntityEmailDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof EntityEmailAddressChange);
    }

}
