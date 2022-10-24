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
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionCreatedResponseDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;
import uk.gov.companieshouse.overseasentitiesapi.utils.TransactionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.LINK_SELF;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.SUBMISSION_URI_PATTERN;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesServiceTest {
    private static final String REQUEST_ID = "fd4gld5h3jhh";
    private static final String PASSTHROUGH_TOKEN_HEADER = "13456";
    private static final String SUBMISSION_ID = "abc123";
    private static final String USER_ID = "22334455";
    private static final LocalDateTime DUMMY_TIME_STAMP = LocalDateTime.of(2020, 2,2, 0, 0);
    private static final String TRANSACTION_ID = "324234-123123-768685";
    private static final String LOGGING_CONTEXT = "32hkjh";

    @Mock
    private OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;

    @Mock
    private Supplier<LocalDateTime> localDateTimeSupplier;

    @Mock
    private TransactionUtils transactionUtils;

    @Captor
    private ArgumentCaptor<Transaction> transactionApiCaptor;

    @Captor
    private ArgumentCaptor<OverseasEntitySubmissionDao> overseasEntitySubmissionsRepositoryCaptor;

    @InjectMocks
    private OverseasEntitiesService overseasEntitiesService;

    @Test
    void testOverseasEntitySubmissionCreatedSuccessfully() throws ServiceException {
        final String submissionId = "434jhg43hj34534";

        Transaction transaction = buildTransaction();

        var overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var overseasEntitySubmissionDao = new OverseasEntitySubmissionDao();
        overseasEntitySubmissionDao.setId(submissionId);

        var trustDataDto = new TrustDataDto();
        trustDataDto.setTrustName("dto");
        List<TrustDataDto> trustDataDtoList = new ArrayList<>();
        trustDataDtoList.add(trustDataDto);
        var mappedTrustDataDao = new TrustDataDao();
        mappedTrustDataDao.setTrustName("mapped dto to dao");
        List<TrustDataDao> trustDataDaoList = new ArrayList<>();
        trustDataDaoList.add(mappedTrustDataDao);

        overseasEntitySubmissionDto.setTrusts(trustDataDtoList);
        overseasEntitySubmissionDao.setTrusts(trustDataDaoList);

        when(overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto)).thenReturn(overseasEntitySubmissionDao);
        when(overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao)).thenReturn(overseasEntitySubmissionDao);
        when(localDateTimeSupplier.get()).thenReturn(DUMMY_TIME_STAMP);


        // make the call to test
        var response = overseasEntitiesService.createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH_TOKEN_HEADER,
                REQUEST_ID,
                USER_ID);

        verify(transactionService, times(1)).updateTransaction(transactionApiCaptor.capture(), any(), any());
        verify(localDateTimeSupplier, times(1)).get();

        String submissionUri = String.format("/transactions/%s/overseas-entity/%s", transaction.getId(), overseasEntitySubmissionDao.getId());

        // assert 'self' link is set on dao object
        assertEquals(submissionUri, overseasEntitySubmissionDao.getLinks().get("self"));

        // assert trust data is added to submission
        assertEquals(1, overseasEntitySubmissionDao.getTrusts().size());

        // assert transaction resources are updated to point to submission
        Transaction transactionSent = transactionApiCaptor.getValue();
        assertEquals(submissionUri, transactionSent.getResources().get(submissionUri).getLinks().get("resource"));
        assertEquals(submissionUri + "/validation-status", transactionSent.getResources().get(submissionUri).getLinks().get("validation_status"));
        assertEquals(submissionUri + "/costs", transactionSent.getResources().get(submissionUri).getLinks().get("costs"));

        // assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OverseasEntitySubmissionCreatedResponseDto responseDto = ((OverseasEntitySubmissionCreatedResponseDto) response.getBody());
        assertNotNull(responseDto);
        assertEquals(submissionId, responseDto.getId());
    }

    @Test
    void testOverseasEntitySubmissionCannotBeCreatedWhenExistingOverseasEntitySubmissionInTransaction() throws ServiceException {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        Resource resource = new Resource();
        resource.setKind(FILING_KIND_OVERSEAS_ENTITY);
        Map<String, Resource> resourceMap = new HashMap<>();
        resourceMap.put(String.format("/transactions/%s/overseas-entity/%s", TRANSACTION_ID, SUBMISSION_ID), resource);
        transaction.setResources(resourceMap);

        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();

        // make the call to test
        var response = overseasEntitiesService.createOverseasEntity(
                transaction,
                overseasEntitySubmissionDto,
                PASSTHROUGH_TOKEN_HEADER,
                REQUEST_ID,
                USER_ID);

        // assert response
        var responseBody = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(String.format("Transaction id: %s has an existing Overseas Entity submission", TRANSACTION_ID), responseBody);
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

    @Test
    void testOverseasEntitySubmissionUpdatedSuccessfully() {
        var transaction = buildTransaction();
        var overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        var overseasEntitySubmissionDao = new OverseasEntitySubmissionDao();

        when(transactionUtils.isTransactionLinkedToOverseasEntitySubmission(eq(transaction), any(String.class)))
                .thenReturn(true);
        when(overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto)).thenReturn(overseasEntitySubmissionDao);
        when(localDateTimeSupplier.get()).thenReturn(DUMMY_TIME_STAMP);

        // ensure DAO id is null before we use it in the test
        assertNull(overseasEntitySubmissionDao.getId());

        // make the call to test
        var response = overseasEntitiesService.updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        verify(overseasEntitySubmissionsRepository, times(1)).save(overseasEntitySubmissionsRepositoryCaptor.capture());
        var savedSubmission = overseasEntitySubmissionsRepositoryCaptor.getValue();

        assertEquals(SUBMISSION_ID, savedSubmission.getId());
        assertEquals(REQUEST_ID, savedSubmission.getHttpRequestId());

        var submissionUri = String.format(SUBMISSION_URI_PATTERN, TRANSACTION_ID, SUBMISSION_ID);
        assertEquals(submissionUri, savedSubmission.getLinks().get(LINK_SELF));

        assertEquals(DUMMY_TIME_STAMP, savedSubmission.getCreatedOn());
        assertEquals(USER_ID, savedSubmission.getCreatedByUserId());

        // assert response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateOverseasEntitySubmissionReturnsErrorIfTransactionANdSubmissionNotLinked() {
        var transaction = buildTransaction();
        var overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();

        when(transactionUtils.isTransactionLinkedToOverseasEntitySubmission(eq(transaction), any(String.class)))
                .thenReturn(false);

        // make the call to test
        var response = overseasEntitiesService.updateOverseasEntity(
                transaction,
                SUBMISSION_ID,
                overseasEntitySubmissionDto,
                REQUEST_ID,
                USER_ID);

        // assert response
        var responseBody = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var expectedMessage = String.format("Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s", TRANSACTION_ID, SUBMISSION_ID);
        assertEquals(expectedMessage, responseBody);
    }

    private Transaction buildTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        return transaction;
    }
}
