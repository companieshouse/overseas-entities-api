package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.DueDiligence;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;

@ExtendWith(MockitoExtension.class)
class PopulateUpdateSubmissionServiceTest {

    private static final LocalDate LOCAL_DATE_TODAY = LocalDate.now();

    @InjectMocks
    private PopulateUpdateSubmissionService populateUpdateSubmissionService;

    @Test
    void testUpdateSubmissionIsPopulatedWithOverseasEntitySubmissionDataFiledByAgent() {
        final OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setOverseasEntityDueDiligence(null);

        final UpdateSubmission updateSubmission = new UpdateSubmission();
        populateUpdateSubmissionService.populate(overseasEntitySubmissionDto, updateSubmission);

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
        populateUpdateSubmissionService.populate(overseasEntitySubmissionDto, updateSubmission);

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
        populateUpdateSubmissionService.populate(overseasEntitySubmissionDto, updateSubmission);

        assertEquals(overseasEntitySubmissionDto, updateSubmission.getUserSubmission());
        assertNotNull(updateSubmission.getPresenter());
        assertFalse(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
        assertNull(updateSubmission.getBeneficialOwnerStatement());
        assertNotNull(updateSubmission.getFilingForDate());
        assertNull(updateSubmission.getDueDiligence());
    }

    private void checkPresenterDetailsArePopulated(UpdateSubmission updateSubmission) {
        assertNotNull(updateSubmission.getPresenter());
        assertEquals("Joe Bloggs", updateSubmission.getPresenter().getName());
        assertEquals("user@domain.roe", updateSubmission.getPresenter().getEmail());
    }

    private void checkStatementDetailsArePopulated(UpdateSubmission updateSubmission) {
        assertFalse(updateSubmission.getAnyBOsOrMOsAddedOrCeased());
        assertEquals("all_identified_all_details", updateSubmission.getBeneficialOwnerStatement());
    }

    private void checkFilingForDataDetailsArePopulated(UpdateSubmission updateSubmission) {
        assertNotNull(updateSubmission.getFilingForDate());
        assertEquals(String.valueOf(LOCAL_DATE_TODAY.getDayOfMonth()),
                updateSubmission.getFilingForDate().getDay());
        assertEquals(String.valueOf(LOCAL_DATE_TODAY.getMonth()),
                updateSubmission.getFilingForDate().getMonth());
        assertEquals(String.valueOf(LOCAL_DATE_TODAY.getYear()),
                updateSubmission.getFilingForDate().getYear());
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

        final AddressDto dueDiligenceCorrespondenceAddress = dueDiligence.getDueDiligenceCorrespondenceAddress();
        assertNotNull(dueDiligenceCorrespondenceAddress);
        assertEquals("100", dueDiligenceCorrespondenceAddress.getPropertyNameNumber());
        assertEquals("No Street", dueDiligenceCorrespondenceAddress.getLine1());
        assertEquals("", dueDiligenceCorrespondenceAddress.getLine2());
        assertEquals("Notown", dueDiligenceCorrespondenceAddress.getTown());
        assertEquals("Noshire", dueDiligenceCorrespondenceAddress.getCounty());
        assertEquals("France", dueDiligenceCorrespondenceAddress.getCountry());
        assertEquals("NOW 3RE", dueDiligenceCorrespondenceAddress.getPostcode());
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

        final AddressDto dueDiligenceCorrespondenceAddress = dueDiligence.getDueDiligenceCorrespondenceAddress();
        assertNotNull(dueDiligenceCorrespondenceAddress);
        assertEquals("100", dueDiligenceCorrespondenceAddress.getPropertyNameNumber());
        assertEquals("No Street", dueDiligenceCorrespondenceAddress.getLine1());
        assertEquals("", dueDiligenceCorrespondenceAddress.getLine2());
        assertEquals("Notown", dueDiligenceCorrespondenceAddress.getTown());
        assertEquals("Noshire", dueDiligenceCorrespondenceAddress.getCounty());
        assertEquals("France", dueDiligenceCorrespondenceAddress.getCountry());
        assertEquals("NOW 3RE", dueDiligenceCorrespondenceAddress.getPostcode());
    }

}
