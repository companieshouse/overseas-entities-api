package uk.gov.companieshouse.overseasentitiesapi.validation;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.ConfirmationStatementApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PaymentMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PaymentDto;

@ExtendWith(MockitoExtension.class)
class PaymentValidatorTest {

    @InjectMocks
    private PaymentValidator paymentValidator;

    @Mock
    private PaymentDto paymentDto;
    
    @BeforeEach
    public void init() throws ServiceException {
        paymentDto = PaymentMock.getPaymentDto();
    }
}
