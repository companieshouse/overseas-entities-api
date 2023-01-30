package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotLinkedToTransactionException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
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
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.RESUME_JOURNEY_URI_PATTERN;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.SUBMISSION_URI_PATTERN;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesServiceTest {
    private static final String REQUEST_ID = "fd4gld5h3jhh";
    private static final String SUBMISSION_ID = "abc123";
    private static final String USER_ID = "22334455";
    private static final LocalDateTime DUMMY_TIME_STAMP = LocalDateTime.of(2020, 2,2, 0, 0);
    private static final String TRANSACTION_ID = "324234-123123-768685";
    private static final String ENTITY_NAME = "MANSION HOLDINGS AG";

    private OverseasEntitySubmissionDao submissionDao = new OverseasEntitySubmissionDao();
    private OverseasEntitySubmissionDto submissionDto = new OverseasEntitySubmissionDto();

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
    private Object responseBody;

    @Test
    void testOverseasEntitySubmissionCreatedSuccessfullyWithResumeLink() throws ServiceException {
        testSubmissionCreation(true, ENTITY_NAME);
    }

    @Test
    void testOverseasEntitySubmissionCreatedSuccessfullyWithNoResumeLink() throws ServiceException {
        testSubmissionCreation(false, ENTITY_NAME);
    }

    @Test
    void testOverseasEntitySubmissionCanBeCreatedWhenEntityNameIsNull() throws ServiceException {
        testSubmissionCreation(false, null);
    }

    private void testSubmissionCreation(boolean isResumeLinkExpected, String entityName) throws ServiceException {
        final String submissionId = "434jhg43hj34534";

        Transaction transaction = buildTransaction();

        var overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        EntityNameDto entityNameDto = new EntityNameDto();
        entityNameDto.setName(entityName);
        overseasEntitySubmissionDto.setEntityName(entityNameDto);
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

        ResponseEntity<Object> response;

        // make the call to test
        if (isResumeLinkExpected) {
            response = overseasEntitiesService.createOverseasEntityWithResumeLink(
                    transaction,
                    overseasEntitySubmissionDto,
                    REQUEST_ID,
                    USER_ID);
        } else {
            response = overseasEntitiesService.createOverseasEntity(
                    transaction,
                    overseasEntitySubmissionDto,
                    REQUEST_ID,
                    USER_ID);
        }

        verify(transactionService, times(1)).updateTransaction(transactionApiCaptor.capture(), any());
        verify(localDateTimeSupplier, times(1)).get();

        String submissionUri = String.format("/transactions/%s/overseas-entity/%s", transaction.getId(), overseasEntitySubmissionDao.getId());

        // assert 'self' link is set on dao object
        assertEquals(submissionUri, overseasEntitySubmissionDao.getLinks().get("self"));

        // assert trust data is added to submission
        assertEquals(1, overseasEntitySubmissionDao.getTrusts().size());

        // assert transaction resources are updated to point to submission
        Transaction transactionSent = transactionApiCaptor.getValue();
        assertEquals(entityName, transactionSent.getCompanyName());
        assertEquals(submissionUri, transactionSent.getResources().get(submissionUri).getLinks().get("resource"));
        assertEquals(submissionUri + "/validation-status", transactionSent.getResources().get(submissionUri).getLinks().get("validation_status"));
        assertEquals(submissionUri + "/costs", transactionSent.getResources().get(submissionUri).getLinks().get("costs"));

        if (isResumeLinkExpected) {
            assertEquals(String.format(RESUME_JOURNEY_URI_PATTERN, transaction.getId(), overseasEntitySubmissionDao.getId()), transactionSent.getResumeJourneyUri());
        } else {
            assertNull(transactionSent.getResumeJourneyUri());
        }

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
                REQUEST_ID,
                USER_ID);

        // assert response
        var responseBody = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(String.format("Transaction id: %s has an existing Overseas Entity submission", TRANSACTION_ID), responseBody);
    }

    @Test
    void testOverseasEntitySubmissionUpdatedSuccessfully() throws ServiceException {
        var transaction = buildTransaction();
        var overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        EntityNameDto entityNameDto = new EntityNameDto();
        entityNameDto.setName(ENTITY_NAME);
        overseasEntitySubmissionDto.setEntityName(entityNameDto);
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

        verify(transactionService, times(1)).updateTransaction(transactionApiCaptor.capture(), any());
        // assert transaction resources are updated to point to submission
        Transaction transactionSent = transactionApiCaptor.getValue();
        assertEquals(ENTITY_NAME, transactionSent.getCompanyName());

        // assert response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateOverseasEntitySubmissionReturnsErrorIfTransactionAndSubmissionNotLinked() throws ServiceException {
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

    @Test
    void testGetSavedOverseasEntityWhenSubmissionFoundSuccessfully() throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException  {
        var overseasEntitySubmissionDao = new OverseasEntitySubmissionDao();
        var overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        var transaction = buildTransaction();
        when(transactionUtils.isTransactionLinkedToOverseasEntitySubmission(eq(transaction), any(String.class)))
                .thenReturn(true);
        when(overseasEntitySubmissionsRepository.findById(SUBMISSION_ID)
        ).thenReturn(Optional.of(overseasEntitySubmissionDao));
        when(overseasEntityDtoDaoMapper.daoToDto(overseasEntitySubmissionDao)
        ).thenReturn(overseasEntitySubmissionDto);

        var response = overseasEntitiesService.getSavedOverseasEntity(
                transaction,
                SUBMISSION_ID,
                REQUEST_ID);
        var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Joe Bloggs", ((OverseasEntitySubmissionDto)responseBody).getPresenter().getFullName());
        assertEquals("user@domain.roe", ((OverseasEntitySubmissionDto)responseBody).getPresenter().getEmail());
    }

    @Test
    void testGetSavedOverseasEntityWhenTransactionNotLinked() throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {
        var transaction = buildTransaction();
        when(transactionUtils.isTransactionLinkedToOverseasEntitySubmission(eq(transaction), any(String.class)))
                .thenReturn(false);
        Exception exception = assertThrows(SubmissionNotLinkedToTransactionException.class, () ->
                overseasEntitiesService.getSavedOverseasEntity(
                        transaction,
                        SUBMISSION_ID,
                        REQUEST_ID));

        String actualMessage = exception.getMessage();
        var expectedMessage = String.format("Transaction id: %s does not have a resource that matches Overseas Entity submission id: %s", TRANSACTION_ID, SUBMISSION_ID);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetSavedOverseasEntitySubmissionNotFoundSuccessfully() throws SubmissionNotFoundException, SubmissionNotLinkedToTransactionException {
        var transaction = buildTransaction();
        when(transactionUtils.isTransactionLinkedToOverseasEntitySubmission(eq(transaction), any(String.class)))
                .thenReturn(true);
        when(overseasEntitySubmissionsRepository.findById(SUBMISSION_ID)
        ).thenReturn(Optional.empty());

        Exception exception = assertThrows(SubmissionNotFoundException.class, () ->
                overseasEntitiesService.getSavedOverseasEntity(
                        transaction,
                        SUBMISSION_ID,
                        REQUEST_ID));
        String actualMessage = exception.getMessage();
        var expectedMessage = String.format("Empty submission returned when generating filing for %s", SUBMISSION_ID);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private Transaction buildTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        return transaction;
    }

    @Test
    void checkSubmissionTypeIsRegistrationIfNoOverseasEntityNumberInSubmission() throws SubmissionNotFoundException {
        submissionDto.setEntityNumber(null);
        when(overseasEntityDtoDaoMapper.daoToDto(submissionDao)).thenReturn(submissionDto);
        when(overseasEntitySubmissionsRepository.findById(any())).thenReturn(Optional.of(submissionDao));
        SubmissionType kind = overseasEntitiesService.getSubmissionType(REQUEST_ID, "testId1");
        assertEquals(SubmissionType.REGISTRATION, kind);
    }

    @Test
    void checkSubmissionTypeIsUpdateIfOverseasNumberInSubmission() throws SubmissionNotFoundException {
        submissionDto.setEntityNumber("OE111129");
        when(overseasEntityDtoDaoMapper.daoToDto(submissionDao)).thenReturn(submissionDto);
        when(overseasEntitySubmissionsRepository.findById(any())).thenReturn(Optional.of(submissionDao));
        SubmissionType kind = overseasEntitiesService.getSubmissionType(REQUEST_ID,"testId1");
        assertEquals(SubmissionType.UPDATE, kind);
    }

    @Test
    void checkIsUpdateSubmissionIfOverseasNumberInSubmission() throws SubmissionNotFoundException {
        submissionDto.setEntityNumber("OE111129");
        when(overseasEntityDtoDaoMapper.daoToDto(submissionDao)).thenReturn(submissionDto);
        when(overseasEntitySubmissionsRepository.findById(any())).thenReturn(Optional.of(submissionDao));
        boolean isUpdate = overseasEntitiesService.isSubmissionAnUpdate(REQUEST_ID,"testId1");
        assertEquals(true, isUpdate);
    }
    @Test
    void checkIsNotUpdateSubmissionIfNoOverseasNumberInSubmission() throws SubmissionNotFoundException {
        when(overseasEntityDtoDaoMapper.daoToDto(submissionDao)).thenReturn(submissionDto);
        when(overseasEntitySubmissionsRepository.findById(any())).thenReturn(Optional.of(submissionDao));
        boolean isUpdate = overseasEntitiesService.isSubmissionAnUpdate(REQUEST_ID,"testId1");
        assertEquals(false, isUpdate);
    }

    @Test
    void checkSubmissionTypeServiceThrowsExceptionWhenNoSuchSubmission() {
        when(overseasEntitySubmissionsRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(SubmissionNotFoundException.class, () -> {
            overseasEntitiesService.getSubmissionType(REQUEST_ID, "testId1");
        });
    }

    @Test
    void checkIsSubmissionAnUpdateThrowsServiceExceptionWhenNoSuchSubmission() {
        when(overseasEntitySubmissionsRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(SubmissionNotFoundException.class, () -> {
            overseasEntitiesService.isSubmissionAnUpdate(REQUEST_ID, "testId1");
        });
    }
}
