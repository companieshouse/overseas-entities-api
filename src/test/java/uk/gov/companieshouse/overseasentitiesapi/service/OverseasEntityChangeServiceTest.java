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
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.CompanyIdentificationChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.CorrespondenceAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityEmailAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityNameChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.PrincipalAddressChange;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeComparator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getExistingRegistrationAllData;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getNoChangeUpdateSubmission;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getUpdateSubmissionAllDataDifferent;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getUpdateSubmissionCompanyIdentificationDifferent;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getUpdateSubmissionCorrespondenceAddressDifferent;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getUpdateSubmissionEntityEmailDifferent;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.CollatedOverseasEntityDataMock.getUpdateSubmissionEntityNameDifferent;

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
        assertInstanceOf(EntityNameChange.class, result.get(0));
        assertInstanceOf(PrincipalAddressChange.class, result.get(1));
        assertInstanceOf(CorrespondenceAddressChange.class, result.get(2));
        assertInstanceOf(CompanyIdentificationChange.class, result.get(3));
        assertInstanceOf(EntityEmailAddressChange.class, result.get(4));
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
        var result = overseasEntityChangeService.collateOverseasEntityChanges(null, null, logMap);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesExistingRegistrationInputNullReturnsEmptyChangeList() {
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();

        System.setOut(new PrintStream(outputStreamCaptor));

        var result = overseasEntityChangeService.collateOverseasEntityChanges(null, updateSubmission, logMap);

        assertEquals(0, result.size());
        assertTrue(outputStreamCaptor.toString().contains("No public and no private data found for overseas entity"));
    }

    @Test
    void testCollateOverseasEntityChangesUpdateSubmissionInputNullReturnsEmptyChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, null, logMap);

        assertEquals(0, result.size());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyEntityNameChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionEntityNameDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertInstanceOf(EntityNameChange.class, result.getFirst());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyPrincipalAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCorrespondenceAddressDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertInstanceOf(CorrespondenceAddressChange.class, result.getFirst());
    }

    @Test
    void testCollateOverseasEntityChangesUpdatedPrincipalAddressSameAsCorrespondenceChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCorrespondenceAddressDifferent();
        updateSubmission.getEntity().setServiceAddressSameAsPrincipalAddress(true);

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertInstanceOf(CorrespondenceAddressChange.class, result.getFirst());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyCorrespondenceAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCorrespondenceAddressDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertInstanceOf(CorrespondenceAddressChange.class, result.getFirst());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyCompanyIdentificationChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionCompanyIdentificationDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertInstanceOf(CompanyIdentificationChange.class, result.getFirst());
    }

    @Test
    void testCollateOverseasEntityChangesOnlyEmailAddressChangedReturnsChangeList() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = getExistingRegistrationAllData();
        OverseasEntitySubmissionDto updateSubmission = getUpdateSubmissionEntityEmailDifferent();

        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission, logMap);

        assertEquals(1, result.size());
        assertInstanceOf(EntityEmailAddressChange.class, result.getFirst());
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
        assertInstanceOf(EntityNameChange.class, result.get(0));
        assertInstanceOf(PrincipalAddressChange.class, result.get(1));
        assertInstanceOf(CorrespondenceAddressChange.class, result.get(2));
        assertInstanceOf(CompanyIdentificationChange.class, result.get(3));
        assertInstanceOf(EntityEmailAddressChange.class, result.get(4));
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
        assertInstanceOf(EntityNameChange.class, result.get(0));
        assertInstanceOf(PrincipalAddressChange.class, result.get(1));
        assertInstanceOf(CorrespondenceAddressChange.class, result.get(2));
        assertInstanceOf(CompanyIdentificationChange.class, result.get(3));
        assertInstanceOf(EntityEmailAddressChange.class, result.get(4));
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
        assertInstanceOf(EntityNameChange.class, result.get(0));
        assertInstanceOf(PrincipalAddressChange.class, result.get(1));
        assertInstanceOf(CorrespondenceAddressChange.class, result.get(2));
        assertInstanceOf(CompanyIdentificationChange.class, result.get(3));
        assertInstanceOf(EntityEmailAddressChange.class, result.get(4));
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
        assertInstanceOf(EntityNameChange.class, result.get(0));
        assertInstanceOf(PrincipalAddressChange.class, result.get(1));
        assertInstanceOf(CorrespondenceAddressChange.class, result.get(2));
        assertInstanceOf(CompanyIdentificationChange.class, result.get(3));
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
        assertInstanceOf(EntityEmailAddressChange.class, result.getFirst());
    }
}
