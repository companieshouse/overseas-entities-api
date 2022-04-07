package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionCreatedResponseDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.repository.OverseasEntitySubmissionsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverseasEntitiesServiceTest {
    private static final String PASSTHROUGH_TOKEN_HEADER = "13456";

    @Mock
    private OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private OverseasEntitySubmissionsRepository overseasEntitySubmissionsRepository;

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

        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        OverseasEntitySubmissionDao overseasEntitySubmissionDao = new OverseasEntitySubmissionDao();
        overseasEntitySubmissionDao.setId(submissionId);

        when(overseasEntityDtoDaoMapper.dtoToDao(overseasEntitySubmissionDto)).thenReturn(overseasEntitySubmissionDao);
        when(overseasEntitySubmissionsRepository.insert(overseasEntitySubmissionDao)).thenReturn(overseasEntitySubmissionDao);

        // make the call to test
        var response = overseasEntitiesService.createOverseasEntity(transaction, overseasEntitySubmissionDto, PASSTHROUGH_TOKEN_HEADER);

        verify(transactionService, times(1)).updateTransaction(transactionApiCaptor.capture(), any());

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
}
