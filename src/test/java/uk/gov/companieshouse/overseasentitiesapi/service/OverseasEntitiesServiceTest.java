package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionCreatedResponseDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.ERICHeaderParser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesServiceTest {
    private static final String REQUEST_ID = "fd4gld5h3jhh";
    private static final String PASSTHROUGH_TOKEN_HEADER = "13456";
    private static final String SUBMISSION_ID = "abc123";
    private static final String USER_ID = "22334455";
    private static final String USER_DETAILS = "demo@ch.gov.uk; forename=demoForename; surname=demoSurname";
    private static final String USER_EMAIL = "demo@ch.gov.uk";
    private static final LocalDateTime DUMMY_TIME_STAMP = LocalDateTime.of(2020, 2,2, 0, 0);

    @Mock
    private OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;

    @Mock
    private ERICHeaderParser ericHeaderParser;

    @Mock
    private Supplier<LocalDateTime> localDateTimeSupplier;

    @Captor
    private ArgumentCaptor<Transaction> transactionApiCaptor;

    @InjectMocks
    private OverseasEntitiesService overseasEntitiesService;

    @Test
    void testOverseasEntitySubmissionCreatedSuccessfully() throws ServiceException {
        final String txnId = "324234-123123-768685";
        final String submissionId = "434jhg43hj34534";

        Transaction transaction = new Transaction();
        transaction.setId(txnId);

        var overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var overseasEntitySubmissionDao = new OverseasEntitySubmissionDao();
        overseasEntitySubmissionDao.setId(submissionId);

        when(overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto)).thenReturn(overseasEntitySubmissionDao);
        when(overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao)).thenReturn(overseasEntitySubmissionDao);
        when(localDateTimeSupplier.get()).thenReturn(DUMMY_TIME_STAMP);
        when(ericHeaderParser.getEmailAddress(USER_DETAILS)).thenReturn(USER_EMAIL);


        // make the call to test
        var response = overseasEntitiesService.createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH_TOKEN_HEADER,
                REQUEST_ID,
                USER_ID,
                USER_DETAILS);

        verify(transactionService, times(1)).updateTransaction(transactionApiCaptor.capture(), any());
        verify(localDateTimeSupplier, times(1)).get();
        verify(ericHeaderParser, times(1)).getEmailAddress(USER_DETAILS);

        String submissionUri = String.format("/transactions/%s/overseas-entity/%s", transaction.getId(), overseasEntitySubmissionDao.getId());

        // assert 'self' link is set on dao object
        assertEquals(submissionUri, overseasEntitySubmissionDao.getLinks().get("self"));

        // assert transaction resources are updated to point to submission
        Transaction transactionSent = transactionApiCaptor.getValue();
        assertEquals(submissionUri, transactionSent.getResources().get(submissionUri).getLinks().get("resource"));
        assertEquals(submissionUri + "/validation-status", transactionSent.getResources().get(submissionUri).getLinks().get("validation_status"));

        // assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OverseasEntitySubmissionCreatedResponseDto responseDto = ((OverseasEntitySubmissionCreatedResponseDto) response.getBody());
        assertNotNull(responseDto);
        assertEquals(submissionId, responseDto.getId());
    }

    @Test
    void testOverseasEntitySubmissionCannotBeCreatedWhenExistingOverseasEntitySubmissionInTransaction() throws ServiceException {
        final String txnId = "324234-123123-768685";
        final String submissionId = "434jhg43hj34534";

        Transaction transaction = new Transaction();
        transaction.setId(txnId);
        Resource resource = new Resource();
        resource.setKind(FILING_KIND_OVERSEAS_ENTITY);
        Map<String, Resource> resourceMap = new HashMap<>();
        resourceMap.put(String.format("/transactions/%s/overseas-entity/%s", txnId, submissionId), resource);
        transaction.setResources(resourceMap);

        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();

        // make the call to test
        var response = overseasEntitiesService.createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH_TOKEN_HEADER,
                REQUEST_ID,
                USER_ID,
                USER_DETAILS);

        // assert response
        var responseBody = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(String.format("Transaction id: %s has an existing Overseas Entity submission", txnId), responseBody);
    }

    @Test
    void testValidationStatusWhenSubmissionIsPresent() throws SubmissionNotFoundException {
        OverseasEntitySubmissionDao submissionDao = new OverseasEntitySubmissionDao();
        when(overseasEntityDtoDaoMapper.daoToDto(submissionDao)).thenReturn(Mocks.buildSubmissionDto());
        when(overseasEntitySubmissionsRepository.findById(SUBMISSION_ID)).thenReturn(Optional.of(submissionDao));

        ValidationStatusResponse validationStatusResponse = overseasEntitiesService.isValid(SUBMISSION_ID);
        assertTrue(validationStatusResponse.isValid());
    }

    @Test
    void testValidationStatusWhenSubmissionIsNotPresent() {
        when(overseasEntitySubmissionsRepository.findById(SUBMISSION_ID)).thenReturn(Optional.empty());
        assertThrows(SubmissionNotFoundException.class, () -> overseasEntitiesService.isValid(SUBMISSION_ID));
    }
}
