package uk.gov.companieshouse.overseasentitiesapi.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.*;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeComparator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.*;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getExistingRegistrationAllData;

@ExtendWith(MockitoExtension.class)
class OverseasEntityChangeServiceTest {
    OverseasEntityChangeComparator overseasEntityChangeComparator;

    OverseasEntityChangeService overseasEntityChangeService;

    Map<String, Object> logMap = new HashMap<>();

    ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void init() {
        overseasEntityChangeComparator = new OverseasEntityChangeComparator();
        overseasEntityChangeService = new OverseasEntityChangeService(overseasEntityChangeComparator);
        outputStreamCaptor = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
        outputStreamCaptor.reset();
        System.setOut(System.out);
    }

    @Test
    void testCollateOverseasEntityChangesAllFieldsChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionAllDataDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

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

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesBothInputsNullReturnsEmptyChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = null;
        OverseasEntitySubmissionDto updateSubmission = null;

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesExistingRegistrationInputNullReturnsEmptyChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = null;
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(0, result.size());
        assertTrue(outputStreamCaptor.toString().contains("No public and no private data found for overseas entity"));
    }

    @Test
    void testCollateOverseasEntityChangesUpdateSubmissionInputNullReturnsEmptyChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = null;

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyEntityNameChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionEntityNameDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyPrincipalAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionPrincipalAddressDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof PrincipalAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesUpdatedPrincipalAddressSameAsCorrespondenceChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionPrincipalAddressDifferent();
        updateSubmission.getEntity().setServiceAddressSameAsPrincipalAddress(true);

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof PrincipalAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyCorrespondenceAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCorrespondenceAddressDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof CorrespondenceAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyCompanyIdentificationChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCompanyIdentificationDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof CompanyIdentificationChange);
    }

    @Test
    void testCollateOverseasEntityChangesOnlyEmailAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionEntityEmailDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof EntityEmailAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesWithChangesPublicPrivateDataNullReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = new ImmutablePair<>(null, null);
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionAllDataDifferent();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertTrue(outputStreamCaptor.toString().contains("No public data found for overseas entity"));
        assertTrue(outputStreamCaptor.toString().contains("No private data found for overseas entity"));
        assertEquals(5, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
        assertTrue(result.get(1) instanceof PrincipalAddressChange);
        assertTrue(result.get(2) instanceof CorrespondenceAddressChange);
        assertTrue(result.get(3) instanceof CompanyIdentificationChange);
        assertTrue(result.get(4) instanceof EntityEmailAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesWithChangesPublicDataNullReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration
                = new ImmutablePair<>(null, getExistingRegistrationAllData().getRight());
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionAllDataDifferent();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertTrue(outputStreamCaptor.toString().contains("No public data found for overseas entity"));
        assertEquals(5, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
        assertTrue(result.get(1) instanceof PrincipalAddressChange);
        assertTrue(result.get(2) instanceof CorrespondenceAddressChange);
        assertTrue(result.get(3) instanceof CompanyIdentificationChange);
        assertTrue(result.get(4) instanceof EntityEmailAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesWithChangesPrivateDataNullReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration
                = new ImmutablePair<>(getExistingRegistrationAllData().getLeft(), null);
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionAllDataDifferent();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertTrue(outputStreamCaptor.toString().contains("No private data found for overseas entity"));
        assertEquals(5, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
        assertTrue(result.get(1) instanceof PrincipalAddressChange);
        assertTrue(result.get(2) instanceof CorrespondenceAddressChange);
        assertTrue(result.get(3) instanceof CompanyIdentificationChange);
        assertTrue(result.get(4) instanceof EntityEmailAddressChange);
    }

    @Test
    void testCollateOverseasEntityChangesPublicDataNullUpdateSubmissionContainsPublicDataChangesReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration
                = new ImmutablePair<>(null, getExistingRegistrationAllData().getRight());
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertTrue(outputStreamCaptor.toString().contains("No public data found for overseas entity"));
        // Only 'Public' data changes
        assertEquals(4, result.size());
        assertTrue(result.get(0) instanceof EntityNameChange);
        assertTrue(result.get(1) instanceof PrincipalAddressChange);
        assertTrue(result.get(2) instanceof CorrespondenceAddressChange);
        assertTrue(result.get(3) instanceof CompanyIdentificationChange);
    }

    @Test
    void testCollateOverseasEntityChangesPrivateDataNullUpdateSubmissionContainsPrivateDataChangesReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration
                = new ImmutablePair<>(getExistingRegistrationAllData().getLeft(), null);
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertTrue(outputStreamCaptor.toString().contains("No private data found for overseas entity"));
        // Only 'Private' data changes
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof EntityEmailAddressChange);
    }
}
