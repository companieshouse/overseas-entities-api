package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.DueDiligence;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

@ExtendWith(MockitoExtension.class)
class PopulateUpdateSubmissionTest {

    @InjectMocks
    private PopulateUpdateSubmission populateUpdateSubmission;

    @Test
    void testUpdateSubmissionIsPopulatedWithOverseasEntitySubmissionDataFiledByAgent() {
        final OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);

        final UpdateSubmission updateSubmission = new UpdateSubmission();
        populateUpdateSubmission.populate(overseasEntitySubmissionDto, updateSubmission, false);

        assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());

        checkPresenterDetailsArePopulated(updateSubmission);
        checkStatementDetailsArePopulated(updateSubmission);
        checkFilingForDataDetailsArePopulated(updateSubmission);
        checkDueDiligenceDetailsArePopulated(updateSubmission);
    }

    @Test
    void testUpdateSubmissionIsPopulatedWithOverseasEntitySubmissionDataFiledByOverseasEntity() {
        final OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setDueDiligence(null);

        final UpdateSubmission updateSubmission = new UpdateSubmission();
        populateUpdateSubmission.populate(overseasEntitySubmissionDto, updateSubmission, false);

        assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());

        checkPresenterDetailsArePopulated(updateSubmission);
        checkStatementDetailsArePopulated(updateSubmission);
        checkFilingForDataDetailsArePopulated(updateSubmission);
        checkOverseasEntityDueDiligenceDetailsArePopulated(updateSubmission);
    }

    @Test
    void testUpdateSubmissionIsPopulatedWithOverseasEntitySubmissionDataWhenNoChanges() {
        final OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setUpdate(UpdateMock.getUpdateDto());
        overseasEntitySubmissionDto.setPresenter(PresenterMock.getPresenterDto());

        final UpdateSubmission updateSubmission = new UpdateSubmission();
        populateUpdateSubmission.populate(overseasEntitySubmissionDto, updateSubmission, false);

        assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());
        assertNotNull(updateSubmission.getPresenter());
        assertFalse(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
        assertNull(updateSubmission.getBeneficialOwnerStatement());
        assertNotNull(updateSubmission.getFilingForDate());
        assertNull(updateSubmission.getDueDiligence());
    }

    @Test
    void testUpdateSubmissionIsPopulatedWithOverseasEntitySubmissionDataWhenIsNoChangeScenario() {
        final OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setUpdate(UpdateMock.getUpdateDto());
        overseasEntitySubmissionDto.setPresenter(PresenterMock.getPresenterDto());
        overseasEntitySubmissionDto.setDueDiligence(DueDiligenceMock.getDueDiligenceDto());

        final UpdateSubmission updateSubmission = new UpdateSubmission();
        populateUpdateSubmission.populate(overseasEntitySubmissionDto, updateSubmission, true);

        assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());
        assertNotNull(updateSubmission.getPresenter());
        assertFalse(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
        assertNull(updateSubmission.getBeneficialOwnerStatement());
        assertNotNull(updateSubmission.getFilingForDate());
        assertNull(updateSubmission.getDueDiligence());
    }

    @Test
    void testUpdateSubmissionIsPopulatedWithNoChangesWhenIsNoChangeScenario() {
        final OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setUpdate(UpdateMock.getUpdateDto());
        overseasEntitySubmissionDto.setPresenter(PresenterMock.getPresenterDto());

        final UpdateSubmission updateSubmission = new UpdateSubmission();
        populateUpdateSubmission.populate(overseasEntitySubmissionDto, updateSubmission, true);

        assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());
        assertNotNull(updateSubmission.getPresenter());
        assertFalse(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
        assertNull(updateSubmission.getBeneficialOwnerStatement());
        assertNotNull(updateSubmission.getFilingForDate());
        assertNull(updateSubmission.getDueDiligence());


        assertTrue(updateSubmission.getAdditions().isEmpty());
        assertTrue(updateSubmission.getChanges().isEmpty());
        assertTrue(updateSubmission.getCessations().isEmpty());
    }

    private void checkPresenterDetailsArePopulated(UpdateSubmission updateSubmission) {
        assertNotNull(updateSubmission.getPresenter());
        assertEquals("Joe Bloggs", updateSubmission.getPresenter().getFullName());
        assertEquals("user@domain.roe", updateSubmission.getPresenter().getEmail());
    }

    private void checkStatementDetailsArePopulated(UpdateSubmission updateSubmission) {
        assertFalse(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, updateSubmission.getBeneficialOwnerStatement());
    }

    private void checkFilingForDataDetailsArePopulated(UpdateSubmission updateSubmission) {
        assertNotNull(updateSubmission.getFilingForDate());
        assertEquals("01", updateSubmission.getFilingForDate().getDay());
        assertEquals("02", updateSubmission.getFilingForDate().getMonth());
        assertEquals("2001",updateSubmission.getFilingForDate().getYear());
    }

    private void checkDueDiligenceDetailsArePopulated(UpdateSubmission updateSubmission) {
        final DueDiligence dueDiligence = updateSubmission.getDueDiligence();
        assertNotNull(dueDiligence);
        assertEquals("Mr Partner", dueDiligence.getPartnerName());
        assertEquals("2021-12-31", dueDiligence.getDateChecked());
        assertEquals("lorem@ipsum.com", dueDiligence.getEmail());
        assertEquals("ABC Checking limited", dueDiligence.getAgentName());
        assertEquals("Super supervisor", dueDiligence.getSupervisoryBody());
        assertEquals("agreed", dueDiligence.getDiligence());
        assertEquals("agent567", dueDiligence.getAgentAssuranceCode());

        final Address dueDiligenceCorrespondenceAddress = dueDiligence.getDueDiligenceCorrespondenceAddress();
        assertNotNull(dueDiligenceCorrespondenceAddress);
        assertEquals("100", dueDiligenceCorrespondenceAddress.getHouseNameNum());
        assertEquals("No Street", dueDiligenceCorrespondenceAddress.getStreet());
        assertEquals("", dueDiligenceCorrespondenceAddress.getArea());
        assertEquals("Notown", dueDiligenceCorrespondenceAddress.getPostTown());
        assertEquals("Noshire", dueDiligenceCorrespondenceAddress.getRegion());
        assertEquals("France", dueDiligenceCorrespondenceAddress.getCountry());
        assertEquals("NOW 3RE", dueDiligenceCorrespondenceAddress.getPostCode());
    }

    private void checkOverseasEntityDueDiligenceDetailsArePopulated(
            UpdateSubmission updateSubmission) {
        final DueDiligence dueDiligence = updateSubmission.getDueDiligence();
        assertNotNull(dueDiligence);
        assertEquals("John Smith", dueDiligence.getPartnerName());
        assertEquals("2022-01-01", dueDiligence.getDateChecked());
        assertEquals("user@domain.roe", dueDiligence.getEmail());
        assertEquals("ABC Checking Ltd", dueDiligence.getAgentName());
        assertEquals("Super Supervisor", dueDiligence.getSupervisoryBody());
        assertNull(dueDiligence.getDiligence());
        assertNull(dueDiligence.getAgentAssuranceCode());
        assertEquals("abc123", dueDiligence.getAmlRegistrationNumber());

        final Address dueDiligenceCorrespondenceAddress = dueDiligence.getDueDiligenceCorrespondenceAddress();
        assertNotNull(dueDiligenceCorrespondenceAddress);
        assertEquals("100", dueDiligenceCorrespondenceAddress.getHouseNameNum());
        assertEquals("No Street", dueDiligenceCorrespondenceAddress.getStreet());
        assertEquals("", dueDiligenceCorrespondenceAddress.getArea());
        assertEquals("Notown", dueDiligenceCorrespondenceAddress.getPostTown());
        assertEquals("Noshire", dueDiligenceCorrespondenceAddress.getRegion());
        assertEquals("France", dueDiligenceCorrespondenceAddress.getCountry());
        assertEquals("NOW 3RE", dueDiligenceCorrespondenceAddress.getPostCode());
    }
}
