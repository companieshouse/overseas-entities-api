package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.ConfirmationStatementApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.service.PublicDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class UpdateValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
    private static final String ENTITY_NUMBER = "OE112233";
    private static final String PASS_THROUGH_HEADER = "545345345";

    // Dates used for testing the filing date:
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate ONE_WEEK_AGO = LocalDate.now().minusDays(7);
    private static final LocalDate TWO_WEEKS_AGO = LocalDate.now().minusDays(14);
    private static final LocalDate NEXT_WEEK = LocalDate.now().plusDays(7);

    @InjectMocks
    private UpdateValidator updateValidator;

    @Mock
    private PublicDataRetrievalService publicDataRetrievalService;

    private UpdateDto updateDto;

    private CompanyProfileApi companyProfileApi;

    @BeforeEach
    public void init() throws ServiceException {
        updateDto = UpdateMock.getUpdateDto();

        companyProfileApi = new CompanyProfileApi();
        ConfirmationStatementApi confirmationStatementApi = new ConfirmationStatementApi();
        confirmationStatementApi.setNextMadeUpTo(LocalDate.now());
        companyProfileApi.setConfirmationStatement(confirmationStatementApi);
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsNull() throws ServiceException {
        updateDto.setFilingDate(null);
        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsNow() throws ServiceException {
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);

        updateDto.setFilingDate(LocalDate.now());
        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsInPast() throws ServiceException {
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);

        updateDto.setFilingDate(ONE_WEEK_AGO);
        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenFilingDateIsInFuture() throws ServiceException {
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);

        updateDto.setFilingDate(NEXT_WEEK);
        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE, qualifiedFieldName);

        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsBeforeNextMadeUpToDate() throws ServiceException {
        companyProfileApi.getConfirmationStatement().setNextMadeUpTo(ONE_WEEK_AGO);
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);
        updateDto.setFilingDate(TWO_WEEKS_AGO);

        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenFilingDateIsTheSameAsTheNextMadeUpToDate() throws ServiceException {
        companyProfileApi.getConfirmationStatement().setNextMadeUpTo(TODAY);
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);
        updateDto.setFilingDate(TODAY);

        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenFilingDateIsInThePastButAfterTheNextMadeUpToDate() throws ServiceException {
        final LocalDate nextMadeUpToDate = TWO_WEEKS_AGO;
        companyProfileApi.getConfirmationStatement().setNextMadeUpTo(nextMadeUpToDate);
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);

        updateDto.setFilingDate(ONE_WEEK_AGO);
        Errors errors = updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_ON_OR_BEFORE_MUD_ERROR_MESSAGE, qualifiedFieldName, nextMadeUpToDate);
        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    @Test
    void testServiceExceptionThrownWhenConfirmationStatementDetailsNotFound() throws ServiceException {
        companyProfileApi.setConfirmationStatement(null);
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);
        updateDto.setFilingDate(TWO_WEEKS_AGO);

        assertThrows(ServiceException.class, () -> updateValidator.validate(ENTITY_NUMBER, updateDto, new Errors(),
                LOGGING_CONTEXT, PASS_THROUGH_HEADER));
    }

    @Test
    void testNoFullValidationErrorReportedWhenFilingDateIsBeforeNextMadeUpToDate() throws ServiceException {
        companyProfileApi.getConfirmationStatement().setNextMadeUpTo(ONE_WEEK_AGO);
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);
        updateDto.setFilingDate(TWO_WEEKS_AGO);

        Errors errors = updateValidator.validateFull(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedWhenFilingDateIsAfterNextMadeUpToDate() throws ServiceException {
        final LocalDate nextMadeUpToDate = TWO_WEEKS_AGO;
        companyProfileApi.getConfirmationStatement().setNextMadeUpTo(nextMadeUpToDate);
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);
        updateDto.setFilingDate(ONE_WEEK_AGO);

        Errors errors = updateValidator.validateFull(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);

        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_ON_OR_BEFORE_MUD_ERROR_MESSAGE, qualifiedFieldName, nextMadeUpToDate);
        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    @Test
    void testNoFullValidationErrorReportedWhenFilingDateIsInPast() throws ServiceException {
        when(publicDataRetrievalService.getCompanyProfile(ENTITY_NUMBER, PASS_THROUGH_HEADER)).thenReturn(companyProfileApi);

        updateDto.setFilingDate(ONE_WEEK_AGO);
        Errors errors = updateValidator.validateFull(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testFullValidationErrorReportedWhenNoUpdate() throws ServiceException {
        Errors errors = updateValidator.validateFull(ENTITY_NUMBER, null, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    @Test
    void testFullValidationErrorReportedWhenNoUpdateFilingDate() throws ServiceException {
        updateDto.setFilingDate(null);
        Errors errors = updateValidator.validateFull(ENTITY_NUMBER, updateDto, new Errors(), LOGGING_CONTEXT, PASS_THROUGH_HEADER);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.UPDATE_FIELD,
                UpdateDto.FILING_DATE);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(UpdateDto.FILING_DATE, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = OverseasEntitySubmissionDto.UPDATE_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
